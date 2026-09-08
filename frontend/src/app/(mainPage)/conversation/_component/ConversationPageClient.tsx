"use client";

import { AuthContext } from "@/app/_context/AuthContext";
import { Client, type IMessage as StompMessage, type StompSubscription } from "@stomp/stompjs";
import Link from "next/link";
import moment from "moment";
import "../../../../../node_modules/moment/locale/vi";
import { use, useEffect, useRef, useState, type UIEvent } from "react";
import { Button, Form, Image, Spinner } from "react-bootstrap";
import { toast } from "react-toastify";

interface IProps {
    initialConversations: IConversation[],
    initialConversationId: number | null,
    loadConversations: (page?: number) => Promise<IPageResponse<IConversation>>,
    loadMessages: (conversationId: number, page?: number) => Promise<IPageResponse<IMessage>>
}

type SocketState = "connecting" | "connected" | "offline";

interface IMessageCache {
    messages: IMessage[],
    page: number,
    totalPages: number
}

const websocketUrl = process.env.NEXT_PUBLIC_WS_URL || "ws://localhost:8080/ws";

const formatTime = (value?: Date) => {
    if (!value) return "";
    return moment(value).format("HH:mm");
};

const toChatOrder = (messages: IMessage[]) => [...messages].reverse();

const getConversationName = (conversation: IConversation, username?: string) => {
    const otherUser = [conversation.sender, conversation.receiver]
        .filter(Boolean)
        .find((participant) => participant?.username !== username);

    return otherUser?.name || otherUser?.username || "Hội thoại";
};

const getConversationPeer = (conversation: IConversation, username?: string) => [conversation.sender, conversation.receiver]
    .filter(Boolean)
    .find((participant) => participant?.username !== username);

const isAIUsername = (username?: string) => ["aiagent", "ai_agent", "ai", "ROLE_AI"].includes((username || "").toLowerCase());

const formatBookPrice = (price: number) => new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND"
}).format(price);

const ConversationPageClient = (props: IProps) => {
    const { initialConversations, initialConversationId, loadConversations, loadMessages } = props;
    const { token, user } = use(AuthContext);
    const [conversations, setConversations] = useState(initialConversations);
    const [selectedConversationId, setSelectedConversationId] = useState<number | null>(initialConversationId);
    const [messages, setMessages] = useState<IMessage[]>([]);
    const [messageContent, setMessageContent] = useState("");
    const [messagesLoading, setMessagesLoading] = useState(false);
    const [messagePage, setMessagePage] = useState(0);
    const [messageTotalPages, setMessageTotalPages] = useState(0);
    const [messageLoadingMore, setMessageLoadingMore] = useState(false);
    const [sending, setSending] = useState(false);
    const [socketState, setSocketState] = useState<SocketState>("offline");
    const stompClientRef = useRef<Client | null>(null);
    const subscriptionRef = useRef<StompSubscription | null>(null);
    const userSubscriptionRef = useRef<StompSubscription | null>(null);
    const selectedConversationRef = useRef<number | null>(initialConversationId);
    const messageLoadingRef = useRef(false);
    const messageCacheRef = useRef<Map<number, IMessageCache>>(new Map());
    const scrollToBottomRef = useRef(true);
    const messageContainerRef = useRef<HTMLDivElement>(null);
    const messageEndRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (!token) return;

        const client = new Client({
            brokerURL: websocketUrl,
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            reconnectDelay: 5000,
            onConnect: () => {
                setSocketState("connected");
                subscriptionRef.current = client.subscribe("/user/queue/ai", (frame: StompMessage) => {
                    const response = JSON.parse(frame.body) as IMessage;
                    selectedConversationRef.current = response.conversationId ?? null;
                    setSelectedConversationId(response.conversationId ?? null);
                    setSending(false);
                    scrollToBottomRef.current = true;
                    if (response.conversationId) {
                        void loadMessages(response.conversationId, 0)
                            .then((result) => {
                                scrollToBottomRef.current = true;
                                const orderedMessages = toChatOrder(result.content);
                                setMessages(orderedMessages);
                                setMessagePage(result.page);
                                setMessageTotalPages(result.totalPages);
                                messageCacheRef.current.set(response.conversationId!, {
                                    messages: orderedMessages,
                                    page: result.page,
                                    totalPages: result.totalPages
                                });
                            })
                            .catch(() => setMessages((currentMessages) => [...currentMessages, response]));
                    } else {
                        setMessages((currentMessages) => [...currentMessages, response]);
                    }
                    void loadConversations(0)
                        .then((result) => setConversations(result.content))
                        .catch(() => undefined);
                });
                userSubscriptionRef.current = client.subscribe("/user/queue/messages", (frame: StompMessage) => {
                    const response = JSON.parse(frame.body) as IMessage;
                    if (response.conversationId === selectedConversationRef.current) {
                        scrollToBottomRef.current = true;
                        setMessages((currentMessages) => {
                            const withoutOptimisticMessage = currentMessages.filter((message) => {
                                const isOptimistic = typeof message.id === "string" && message.id.startsWith("local-");
                                return !(isOptimistic && message.content === response.content);
                            });
                            const updatedMessages = [...withoutOptimisticMessage, response];
                            const cachedMessages = messageCacheRef.current.get(response.conversationId!);
                            if (cachedMessages) {
                                messageCacheRef.current.set(response.conversationId!, {
                                    ...cachedMessages,
                                    messages: updatedMessages
                                });
                            }
                            return updatedMessages;
                        });
                    }
                    setSending(false);
                    void loadConversations(0)
                        .then((result) => setConversations(result.content))
                        .catch(() => undefined);
                });
            },
            onDisconnect: () => setSocketState("offline"),
            onStompError: () => {
                setSocketState("offline");
                setSending(false);
                toast.error("Không thể kết nối chatbot");
            },
            onWebSocketError: () => {
                setSocketState("offline");
                setSending(false);
            }
        });

        stompClientRef.current = client;
        client.activate();

        return () => {
            subscriptionRef.current?.unsubscribe();
            subscriptionRef.current = null;
            userSubscriptionRef.current?.unsubscribe();
            userSubscriptionRef.current = null;
            stompClientRef.current = null;
            void client.deactivate();
        };
    }, [loadConversations, loadMessages, token]);

    useEffect(() => {
        if (scrollToBottomRef.current) {
            messageEndRef.current?.scrollIntoView({ behavior: "smooth" });
            scrollToBottomRef.current = false;
        }
    }, [messages]);

    const handleSelectConversation = async (conversationId: number) => {
        selectedConversationRef.current = conversationId;
        setSelectedConversationId(conversationId);

        const cachedMessages = messageCacheRef.current.get(conversationId);
        if (cachedMessages) {
            scrollToBottomRef.current = true;
            setMessages(cachedMessages.messages);
            setMessagePage(cachedMessages.page);
            setMessageTotalPages(cachedMessages.totalPages);
            return;
        }

        setMessages([]);
        setMessagePage(0);
        setMessageTotalPages(0);
        setMessagesLoading(true);

        try {
            const result = await loadMessages(conversationId, 0);
            scrollToBottomRef.current = true;
            setMessages(toChatOrder(result.content));
            setMessagePage(result.page);
            setMessageTotalPages(result.totalPages);
            messageCacheRef.current.set(conversationId, {
                messages: toChatOrder(result.content),
                page: result.page,
                totalPages: result.totalPages
            });
        } catch {
            toast.error("Không thể tải tin nhắn");
        } finally {
            setMessagesLoading(false);
        }
    };

    useEffect(() => {
        if (initialConversationId === null) return;

        const cachedMessages = messageCacheRef.current.get(initialConversationId);
        if (cachedMessages) {
            setMessages(cachedMessages.messages);
            setMessagePage(cachedMessages.page);
            setMessageTotalPages(cachedMessages.totalPages);
            return;
        }

        setMessagesLoading(true);
        void loadMessages(initialConversationId, 0)
            .then((result) => {
                const orderedMessages = toChatOrder(result.content);
                scrollToBottomRef.current = true;
                setMessages(orderedMessages);
                setMessagePage(result.page);
                setMessageTotalPages(result.totalPages);
                messageCacheRef.current.set(initialConversationId, {
                    messages: orderedMessages,
                    page: result.page,
                    totalPages: result.totalPages
                });
            })
            .catch(() => toast.error("Không thể tải tin nhắn"))
            .finally(() => setMessagesLoading(false));
    }, [initialConversationId, loadMessages]);

    const handleNewChat = () => {
        selectedConversationRef.current = null;
        setSelectedConversationId(null);
        setMessages([]);
        setMessagePage(0);
        setMessageTotalPages(0);
        setMessageContent("");
    };

    const handleMessageScroll = async (event: UIEvent<HTMLDivElement>) => {
        const container = event.currentTarget;
        if (
            container.scrollTop > 32 ||
            selectedConversationId === null ||
            messageLoadingRef.current ||
            messagePage >= messageTotalPages - 1
        ) return;

        messageLoadingRef.current = true;
        setMessageLoadingMore(true);
        const previousScrollHeight = container.scrollHeight;

        try {
            const nextPage = await loadMessages(selectedConversationId, messagePage + 1);
            scrollToBottomRef.current = false;
            const olderMessages = toChatOrder(nextPage.content);
            setMessages((currentMessages) => {
                const updatedMessages = [...olderMessages, ...currentMessages];
                messageCacheRef.current.set(selectedConversationId, {
                    messages: updatedMessages,
                    page: nextPage.page,
                    totalPages: nextPage.totalPages
                });
                return updatedMessages;
            });
            setMessagePage(nextPage.page);
            setMessageTotalPages(nextPage.totalPages);
            requestAnimationFrame(() => {
                container.scrollTop = container.scrollHeight - previousScrollHeight;
            });
        } catch {
            toast.error("Không thể tải thêm tin nhắn");
        } finally {
            messageLoadingRef.current = false;
            setMessageLoadingMore(false);
        }
    };

    const handleSend = () => {
        const content = messageContent.trim();
        const client = stompClientRef.current;
        const selectedConversation = conversations.find((conversation) => conversation.id === selectedConversationId);
        const peer = selectedConversation ? getConversationPeer(selectedConversation, user?.username) : undefined;
        const isAIChat = selectedConversationId === null || isAIUsername(peer?.username);
        if (!content || sending) return;
        if (!client?.connected) {
            toast.warning("Chatbot đang kết nối, vui lòng thử lại sau giây lát");
            return;
        }
        if (!isAIChat && !peer?.id) {
            toast.error("Không xác định được người nhận");
            return;
        }

        const optimisticMessage: IMessage = {
            id: `local-${Date.now()}`,
            content,
            conversationId: selectedConversationId ?? undefined,
            createdAt: new Date(),
            sender: {
                name: user?.firstName || user?.lastName ? `${user.firstName} ${user.lastName}` : user?.username,
                username: user?.username,
                avatar: user?.avatar
            }
        };

        setMessages((currentMessages) => [...currentMessages, optimisticMessage]);
        if (selectedConversationId !== null) {
            const cachedMessages = messageCacheRef.current.get(selectedConversationId);
            if (cachedMessages) {
                messageCacheRef.current.set(selectedConversationId, {
                    ...cachedMessages,
                    messages: [...cachedMessages.messages, optimisticMessage]
                });
            }
        }
        setMessageContent("");
        setSending(true);
        client.publish({
            destination: isAIChat ? "/app/ai/chat" : "/app/chat",
            body: JSON.stringify(isAIChat ? {
                conversationId: selectedConversationId,
                content
            } : {
                conversationId: selectedConversationId,
                receiverId: peer?.id,
                content
            })
        });
    };

    return (
        <main className="container-fluid py-3 py-md-4">
            <div className="row g-0 mx-auto overflow-hidden rounded-4 border bg-white shadow-sm" style={{ height: "calc(100dvh - 3rem)", maxWidth: "1280px" }}>
                <aside className="col-12 col-md-4 col-lg-3 d-flex h-100 flex-column border-end bg-light-subtle">
                    <div className="d-flex align-items-center justify-content-between gap-2 border-bottom bg-white p-3">
                        <div>
                            <h1 className="h5 mb-1 fw-bold text-dark">Tin nhắn</h1>
                            <small className="text-secondary">Các cuộc hội thoại của bạn</small>
                        </div>
                        <Button variant="light" className="rounded-circle" onClick={handleNewChat} aria-label="Tạo cuộc hội thoại mới">
                            <i className="bi bi-pencil-square" aria-hidden="true" />
                        </Button>
                    </div>

                    <div className="list-group list-group-flush flex-grow-1 overflow-y-auto overflow-x-hidden">
                        <button
                            type="button"
                            className={`list-group-item list-group-item-action border-0 border-bottom overflow-hidden p-3 text-start ${selectedConversationId === null ? "active" : ""}`}
                            onClick={handleNewChat}
                        >
                            <div className="d-flex w-100 align-items-center gap-3 overflow-hidden">
                                <span className="d-flex align-items-center justify-content-center rounded-circle bg-success-subtle text-success" style={{ width: "40px", height: "40px" }}>
                                    <i className="bi bi-stars" aria-hidden="true" />
                                </span>
                                <span className="min-w-0 flex-grow-1">
                                    <span className="d-block fw-semibold">Trợ lý học vụ AI</span>
                                    <small className="d-block w-100 text-truncate opacity-75">Hỏi đáp về học tập và đào tạo</small>
                                </span>
                            </div>
                        </button>

                        {conversations.map((conversation) => (
                            <button
                                type="button"
                                key={conversation.id}
                                className={`list-group-item list-group-item-action border-0 border-bottom overflow-hidden p-3 text-start ${selectedConversationId === conversation.id ? "active" : ""}`}
                                onClick={() => void handleSelectConversation(conversation.id)}
                            >
                                <div className="d-flex w-100 align-items-center gap-3 overflow-hidden">
                                    <Image
                                        src={conversation.receiver?.avatar || conversation.sender?.avatar || "/file.svg"}
                                        alt=""
                                        width={40}
                                        height={40}
                                        roundedCircle
                                        className="flex-shrink-0"
                                    />
                                    <span className="min-w-0 flex-grow-1">
                                        <span className="d-block text-truncate fw-semibold">{getConversationName(conversation, user?.username)}</span>
                                        <small className="d-block w-100 text-truncate text-secondary">{conversation.lastMessage || "Chưa có tin nhắn"}</small>
                                    </span>
                                </div>
                            </button>
                        ))}
                    </div>
                </aside>

                <section className="col-12 col-md-8 col-lg-9 d-flex h-100 flex-column bg-white">
                    <header className="d-flex align-items-center justify-content-between gap-3 border-bottom p-3">
                        <div className="d-flex align-items-center gap-3">
                            <span className="d-flex align-items-center justify-content-center rounded-circle bg-success-subtle text-success" style={{ width: "42px", height: "42px" }}>
                                <i className="bi bi-robot fs-5" aria-hidden="true" />
                            </span>
                            <div>
                                <h2 className="h6 mb-1 fw-bold">Trợ lý học vụ AI</h2>
                                <small className="text-secondary">
                                    <span className={`d-inline-block rounded-circle me-1 bg-${socketState === "connected" ? "success" : "secondary"}`} style={{ width: "7px", height: "7px" }} />
                                    {socketState === "connected" ? "Đang trực tuyến" : token ? "Đang kết nối..." : "Ngoại tuyến"}
                                </small>
                            </div>
                        </div>
                        <i className="bi bi-shield-check text-success" title="Kết nối được xác thực" aria-label="Kết nối được xác thực" />
                    </header>

                    <div ref={messageContainerRef} onScroll={handleMessageScroll} className="flex-grow-1 overflow-auto bg-body-tertiary p-3 p-md-4" style={{ minHeight: 0 }} aria-live="polite">
                        {messagesLoading ? (
                            <div className="d-flex h-100 align-items-center justify-content-center">
                                <Spinner animation="border" variant="success" />
                            </div>
                        ) : messages.length === 0 ? (
                            <div className="d-flex h-100 align-items-center justify-content-center text-center">
                                <div className="p-4">
                                    <i className="bi bi-stars display-5 text-success opacity-75" aria-hidden="true" />
                                    <h3 className="h5 mt-3 fw-bold">Bạn đang cần hỗ trợ điều gì?</h3>
                                    <p className="mb-0 text-secondary">Hãy đặt câu hỏi về môn học, đào tạo hoặc tuyển sinh.</p>
                                </div>
                            </div>
                        ) : (
                            <div className="d-flex flex-column gap-3">
                                {messageLoadingMore && <div className="d-flex justify-content-center"><Spinner animation="border" size="sm" variant="success" /></div>}
                                {messages.map((message) => {
                                    const isMine = message.sender?.username === user?.username;
                                    const bookMessage = message.messageType === "BOOK" ? message.book : undefined;
                                    return (
                                        <div key={message.id} className={`d-flex ${isMine ? "justify-content-end" : "justify-content-start"}`}>
                                            <div className={`d-flex align-items-end gap-2 ${isMine ? "flex-row-reverse" : ""}`} style={{ maxWidth: "min(80%, 680px)" }}>
                                                {!isMine && <span className="d-flex align-items-center justify-content-center flex-shrink-0 rounded-circle bg-success-subtle text-success" style={{ width: "32px", height: "32px" }}><i className="bi bi-robot" aria-hidden="true" /></span>}
                                                <div className={`rounded-4 px-3 py-2 ${isMine ? "bg-success text-white rounded-bottom-0" : "bg-white border text-dark rounded-bottom-0 shadow-sm"}`}>
                                                    {bookMessage ? (
                                                        <Link href={`/book-exchange/${bookMessage.id}`} className="d-flex align-items-center gap-3 text-reset text-decoration-none">
                                                            <Image
                                                                src={bookMessage.image?.imageUrl || "/file.svg"}
                                                                alt={bookMessage.name}
                                                                width={64}
                                                                height={64}
                                                                className="rounded-3 object-fit-cover flex-shrink-0"
                                                            />
                                                            <span>
                                                                <strong className="d-block">{bookMessage.name}</strong>
                                                                <small className="d-block opacity-75">{formatBookPrice(bookMessage.price)}</small>
                                                                <small className="d-block mt-1 text-decoration-underline">Xem sách</small>
                                                            </span>
                                                        </Link>
                                                    ) : (
                                                        <p className="mb-1 text-break" style={{ whiteSpace: "pre-wrap" }}>{message.content}</p>
                                                    )}
                                                    <small className={isMine ? "text-white-50" : "text-secondary"}>{formatTime(message.createdAt)}</small>
                                                </div>
                                            </div>
                                        </div>
                                    );
                                })}
                                <div ref={messageEndRef} />
                            </div>
                        )}
                    </div>

                    <div className="border-top bg-white p-3">
                        <Form onSubmit={(event) => { event.preventDefault(); handleSend(); }}>
                            <div className="d-flex align-items-end gap-2">
                                <Form.Control
                                    as="textarea"
                                    rows={1}
                                    value={messageContent}
                                    placeholder="Nhập câu hỏi cho trợ lý AI..."
                                    aria-label="Tin nhắn gửi chatbot"
                                    className="rounded-4 bg-light shadow-none"
                                    onChange={(event) => setMessageContent(event.target.value)}
                                    onKeyDown={(event) => {
                                        if (event.key === "Enter" && !event.shiftKey) {
                                            event.preventDefault();
                                            handleSend();
                                        }
                                    }}
                                    disabled={sending || socketState !== "connected"}
                                />
                                <Button type="submit" variant="success" className="rounded-circle flex-shrink-0" style={{ width: "42px", height: "42px" }} disabled={sending || socketState !== "connected" || !messageContent.trim()} aria-label="Gửi tin nhắn">
                                    {sending ? <Spinner animation="border" size="sm" /> : <i className="bi bi-send" aria-hidden="true" />}
                                </Button>
                            </div>
                        </Form>
                    </div>
                </section>
            </div>
        </main>
    );
};

export default ConversationPageClient;

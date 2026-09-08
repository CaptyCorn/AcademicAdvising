import { loadConversationMessages, loadConversations } from "@/actions/conversation.action";
import ConversationPageClient from "./_component/ConversationPageClient";

interface PageProps {
    searchParams: Promise<{ conversationId?: string | string[] }>
}

const ConversationPage = async ({ searchParams }: PageProps) => {
    const params = await searchParams;
    const rawConversationId = Array.isArray(params.conversationId) ? params.conversationId[0] : params.conversationId;
    const initialConversationId = rawConversationId ? Number(rawConversationId) : null;
    let conversations: IConversation[] = [];

    try {
        const result = await loadConversations(0);
        conversations = result.content;
    } catch {
        conversations = [];
    }

    return (
        <ConversationPageClient
            initialConversations={conversations}
            initialConversationId={Number.isInteger(initialConversationId) ? initialConversationId : null}
            loadConversations={loadConversations}
            loadMessages={loadConversationMessages}
        />
    );
};

export default ConversationPage;
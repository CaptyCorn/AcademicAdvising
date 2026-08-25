'use client'

import { requestCommentCreate } from "@/actions/comment.action";
import { AuthContext } from "@/app/_context/AuthContext";
import { ChangeEvent, use, useRef, useState, useTransition, type FormEvent } from "react";
import { useRouter } from "next/navigation";
import { Button, Card, Form, Image } from "react-bootstrap";
import { toast } from "react-toastify";

interface IProps {
    postId: number
}

const CreateComment = (props: IProps) => {
    const { postId } = props;
    const { user } = use(AuthContext);
    const router = useRouter();

    const [content, setContent] = useState<string>("");
    const [isPending, startTransition] = useTransition();
    const textareaRef = useRef<HTMLTextAreaElement>(null);

    const handleContentInput = (event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const textarea = event.currentTarget as HTMLTextAreaElement;
        textarea.style.height = "auto";
        textarea.style.height = `${textarea.scrollHeight}px`;
        setContent(textarea.value);
    };

    const handleSubmit = (event: SubmitEvent) => {
        event.preventDefault();

        const commentContent = content.trim();
        if (!commentContent) {
            toast.warning("Vui lòng nhập nội dung bình luận");
            return;
        }

        startTransition(async () => {
            const response = await requestCommentCreate(commentContent, String(postId));
            if (response.success) {
                setContent("");
                if (textareaRef.current) {
                    textareaRef.current.style.height = "auto";
                }
                toast.info(response.message);
                router.refresh();
            } else {
                toast.error(response.message);
            }
        });
    };

    return (
        <Card className="border border-top-0 rounded-0 rounded-bottom-4 shadow-sm">
            <Card.Body className="p-2 p-md-3">
                <Form onSubmit={() => handleSubmit}>
                    <div className="d-flex align-items-center gap-2">
                        <Image
                            src={user?.avatar || "/file.svg"}
                            alt="Ảnh đại diện"
                            width={36}
                            height={36}
                            roundedCircle
                            className="flex-shrink-0"
                        />
                        <div className="flex-grow-1">
                            <Form.Label htmlFor="comment-content" className="visually-hidden">
                                Viết bình luận
                            </Form.Label>
                            <Form.Control
                                id="comment-content"
                                as="textarea"
                                ref={textareaRef}
                                rows={1}
                                wrap="soft"
                                value={content}
                                maxLength={500}
                                placeholder="Chia sẻ suy nghĩ của bạn..."
                                className="bg-light border rounded-3 shadow-none px-3 py-2"
                                style={{ resize: "none", overflow: "hidden" }}
                                onChange={handleContentInput}
                                disabled={isPending}
                            />
                        </div>
                        <Button type="submit" variant="link" className="text-decoration-none fw-semibold p-0" disabled={isPending || !content.trim()}>
                            {isPending ? "Đang gửi..." : "Gửi"}
                        </Button>
                    </div>
                </Form>
            </Card.Body>
        </Card>
    );
}

export default CreateComment;
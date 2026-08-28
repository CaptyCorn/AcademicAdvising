"use client";

import { loadComments } from "@/actions/comment.action";
import Link from "next/link";
import moment from "moment";
import "../../../../../../node_modules/moment/locale/vi";
import { useEffect, useRef, useState } from "react";
import { Card, Image, Spinner } from "react-bootstrap";
import CreateComment from "./CreateComment";

interface IProps {
    comments: IComment[],
    post: IPosts,
    page: number,
    totalElements: number,
    totalPages: number,
}

const PostDetail = (props: IProps) => {
    const { post, comments: initialComments, page: initialPage, totalElements, totalPages } = props;
    const [comments, setComments] = useState(initialComments);
    const [page, setPage] = useState(initialPage);
    const [loading, setLoading] = useState(false);
    const loadMoreRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const target = loadMoreRef.current;
        if (!target) return;

        const observer = new IntersectionObserver(async ([entry]) => {
            if (!entry.isIntersecting || loading || page >= totalPages - 1) return;

            setLoading(true);
            try {
                const nextPage = await loadComments(String(post.id), page + 1);
                setComments((currentComments) => [...currentComments, ...nextPage.content]);
                setPage(nextPage.page);
            } finally {
                setLoading(false);
            }
        }, { rootMargin: "240px" });

        observer.observe(target);
        return () => observer.disconnect();
    }, [loading, page, post.id, totalPages]);

    return(
        <main className="container py-4 py-md-5">
            <div className="row">
            <div className="col-12 col-xl-9 col-xxl-8 mx-auto">
            <Link href="/" className="d-inline-flex align-items-center gap-2 mb-4 text-decoration-none text-secondary fw-semibold small">
                <span aria-hidden="true">←</span>
                <span>Quay lại bảng tin</span>
            </Link>

            <Card className="border rounded-top-4 rounded-bottom-0 shadow-sm overflow-hidden">
                <Card.Body className="p-4 p-md-5">
                    <div className="text-uppercase text-success fw-bold small mb-4">Bình luận</div>
                    <div className="d-flex align-items-start gap-3">
                        <Image
                            src={post.user.avatar || "/file.svg"}
                            alt={`Ảnh đại diện của ${post.user.name || post.user.username}`}
                            width={48}
                            height={48}
                            roundedCircle
                            className="flex-shrink-0"
                        />
                        <div className="min-w-0">
                            <div className="fw-semibold text-dark">{post.user.name || post.user.username}</div>
                            <div className="d-flex gap-2 small text-secondary">@{post.user.username} <span aria-hidden="true">•</span> {moment(post.createdAt).fromNow()}</div>
                        </div>
                    </div>

                    <p className="mb-0 mt-4 text-break text-dark fs-5 lh-lg">{post.content}</p>
                </Card.Body>
                <div className="d-flex flex-wrap align-items-center gap-2 border-top px-4 px-md-5 py-3 small fw-semibold text-secondary">
                    <i className="bi bi-chat" aria-hidden="true" />
                    <span>{post.commentCount ?? totalElements ?? comments.length} bình luận</span>
                    <span aria-hidden="true">•</span>
                    <span>Đang mở</span>
                </div>
            </Card>

            <CreateComment postId={post.id} />

            <section className="mt-3" aria-label="Bình luận">
                <div className="list-group rounded-4 shadow-sm">
                    {comments.length ? comments.map((comment, index) => (
                        <article
                            key={comment.id}
                            className={`list-group-item border-0 p-3 p-md-4 ${index < comments.length - 1 ? "border-bottom" : ""}`}
                        >
                            <div className="d-flex align-items-start gap-3">
                                <Image
                                    src={comment.avatarUserComment || "/file.svg"}
                                    alt={`Ảnh đại diện của ${comment.nameUserComment || "Người dùng"}`}
                                    width={40}
                                    height={40}
                                    roundedCircle
                                    className="flex-shrink-0"
                                />
                                <div className="flex-grow-1 min-w-0">
                                    <div className="d-flex flex-wrap align-items-baseline gap-2">
                                        <span className="fw-bold text-dark">{comment.nameUserComment || "Người dùng"}</span>
                                        <small className="text-secondary">{moment(comment.createdAt).fromNow()}</small>
                                    </div>
                                    <p className="mb-0 mt-2 text-break text-dark lh-base">{comment.content}</p>
                                </div>
                            </div>
                        </article>
                    )) : (
                        <div className="list-group-item border-0 rounded-4 bg-white p-5 text-center text-secondary">
                            <p className="mb-1 fw-semibold text-dark">Chưa có bình luận nào</p>
                            <small>Hãy là người đầu tiên phản hồi bài viết này.</small>
                        </div>
                    )}
                </div>
                <div ref={loadMoreRef} className="d-flex justify-content-center py-3" aria-live="polite">
                    {loading && <Spinner animation="border" size="sm" variant="primary" />}
                    {!loading && comments.length > 0 && page >= totalPages - 1 && (
                        <small className="text-secondary">Bạn đã xem hết bình luận.</small>
                    )}
                </div>
            </section>
            </div>
            </div>
        </main>
    );
}

export default PostDetail;

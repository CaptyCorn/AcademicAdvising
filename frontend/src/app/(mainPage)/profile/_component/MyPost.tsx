'use client';

import Link from "next/link";
import moment from "moment";
import { useEffect, useRef, useState } from "react";
import { Card, Image, Spinner } from "react-bootstrap";
import { toast } from "react-toastify";

interface IProps {
    initialPosts: IPosts[];
    initialPage: number;
    totalElements: number;
    totalPages: number;
    loadMore: (page: number) => Promise<IPageResponse<IPosts>>;
}

const MyPost = ({ initialPosts, initialPage, totalElements, totalPages, loadMore }: IProps) => {
    const [posts, setPosts] = useState(initialPosts);
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
                const next = await loadMore(page + 1);
                setPosts((current) => [...current, ...next.content]);
                setPage(next.page);
            } catch {
                toast.error("Không thể tải thêm bài viết");
            } finally {
                setLoading(false);
            }
        }, { rootMargin: "200px" });
        observer.observe(target);
        return () => observer.disconnect();
    }, [loadMore, loading, page, totalPages]);

    return (
        <section>
            <div className="d-flex align-items-center justify-content-between mb-3">
                <div><h2 className="h5 fw-bold mb-1">Bài viết của tôi</h2><small className="text-secondary">{totalElements} bài viết</small></div>
            </div>
            {posts.length === 0 ? (
                <Card className="border-0 rounded-4 shadow-sm"><Card.Body className="p-5 text-center text-secondary"><i className="bi bi-journal-text fs-1 d-block mb-3" /><p className="mb-0">Bạn chưa có bài viết nào.</p></Card.Body></Card>
            ) : (
                <div className="d-flex flex-column gap-3">
                    {posts.map((post) => <Card key={post.id} className="border-0 rounded-4 shadow-sm"><Card.Body className="p-4">
                        <div className="d-flex align-items-start gap-3">
                            <Image src={post.user.avatar || "/file.svg"} alt="Ảnh đại diện" width={42} height={42} roundedCircle className="flex-shrink-0" />
                            <div className="flex-grow-1 min-w-0">
                                <div className="d-flex justify-content-between gap-2"><span className="fw-semibold">{post.user.name || post.user.username}</span><small className="text-secondary text-nowrap">{moment(post.createdAt).fromNow()}</small></div>
                                <p className="text-break mt-3 mb-3">{post.content}</p>
                                <Link href={`/posts/${post.id}`} className="small text-secondary text-decoration-none"><i className="bi bi-chat me-1" />{post.commentCount ?? 0} bình luận</Link>
                            </div>
                        </div>
                    </Card.Body></Card>)}
                </div>
            )}
            <div ref={loadMoreRef} className="d-flex justify-content-center py-4">{loading ? <Spinner animation="border" size="sm" /> : page >= totalPages - 1 && posts.length > 0 ? <small className="text-secondary">Bạn đã xem hết bài viết.</small> : null}</div>
        </section>
    );
};

export default MyPost;

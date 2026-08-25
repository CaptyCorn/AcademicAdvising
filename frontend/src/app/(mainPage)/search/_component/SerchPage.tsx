'use client'

import Link from "next/link";
import moment from "moment";
import "../../../../../node_modules/moment/locale/vi";
import { useEffect, useRef, useState } from "react";
import { Button, Form, Image, InputGroup, Spinner } from "react-bootstrap";
import { toast } from "react-toastify";

interface IProps {
    keyword: string,
    posts: IPosts[],
    totalElements: number,
    page: number,
    totalPages: number,
    loadMore: (page: number) => Promise<{
        content: IPosts[];
        page: number;
        totalPages: number;
    }>
}

const SearchPage = (props: IProps) => {
    const { keyword, posts: initialPosts, totalElements, page: initialPage, totalPages, loadMore } = props;
    const [posts, setPosts] = useState(initialPosts);
    const [page, setPage] = useState(initialPage);
    const [loading, setLoading] = useState(false);
    const loadMoreRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const target = loadMoreRef.current;
        if (!target || !keyword) return;

        const observer = new IntersectionObserver(async ([entry]) => {
            if (!entry.isIntersecting || loading || page >= totalPages - 1) return;

            setLoading(true);
            try {
                const nextPage = await loadMore(page + 1);
                setPosts((currentPosts) => [...currentPosts, ...nextPage.content]);
                setPage(nextPage.page);
            } catch {
                toast.error("Không thể tải thêm bài viết");
            } finally {
                setLoading(false);
            }
        }, { rootMargin: "240px" });

        observer.observe(target);
        return () => observer.disconnect();
    }, [keyword, loadMore, loading, page, totalPages]);

    return (
        <main className="container py-4 py-md-5">
            <div className="row">
                <div className="col-12 col-xl-9 col-xxl-8 mx-auto">
                    <h1 className="h4 fw-semibold text-dark mb-4">Tìm kiếm bài viết</h1>

                    <Form method="get" action="/search" className="mb-4">
                        <InputGroup className="bg-white border rounded-pill shadow-sm px-3 py-2">
                            <InputGroup.Text className="border-0 bg-transparent p-0">
                                {/* <i className="bi bi-search text-secondary" aria-hidden="true" /> */}
                            </InputGroup.Text>
                            <Form.Control
                                type="search"
                                name="kw"
                                defaultValue={keyword}
                                placeholder="Tìm kiếm"
                                aria-label="Từ khóa tìm kiếm"
                                className="border-0 bg-transparent shadow-none px-3 py-0"
                            />
                            <Button type="submit" variant="link" className="border-0 text-secondary text-decoration-none p-0" aria-label="Tìm kiếm">
                                <i className="bi bi-search" aria-hidden="true" />
                            </Button>
                        </InputGroup>
                    </Form>

                    {keyword ? (
                        <div className="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
                            <div className="text-secondary">
                                Kết quả cho <span className="fw-bold text-dark">“{keyword}”</span>
                            </div>
                            <small className="text-secondary">{totalElements} bài viết</small>
                        </div>
                    ) : (
                        <div className="alert alert-light border text-secondary mb-3" role="status">
                            Nhập từ khóa để bắt đầu tìm kiếm.
                        </div>
                    )}

                    {keyword && posts.length > 0 && (
                        <div className="list-group rounded-4 shadow-sm">
                            {posts.map((post, index) => (
                                <article
                                    key={post.id}
                                    className={`list-group-item border-0 p-3 p-md-4 ${index < posts.length - 1 ? "border-bottom" : ""}`}
                                >
                                    <div className="d-flex align-items-start gap-3">
                                        <Image
                                            src={post.user.avatar || "/file.svg"}
                                            alt={`Ảnh đại diện của ${post.user.name || post.user.username}`}
                                            width={44}
                                            height={44}
                                            roundedCircle
                                            className="flex-shrink-0"
                                        />
                                        <div className="flex-grow-1 min-w-0">
                                            <div className="d-flex flex-wrap align-items-baseline gap-2">
                                                <span className="fw-bold text-dark">{post.user.name || post.user.username}</span>
                                                <small className="text-secondary">{moment(post.createdAt).fromNow()}</small>
                                            </div>
                                            <small className="text-secondary">@{post.user.username}</small>
                                            <p className="mb-2 mt-2 text-break text-dark lh-lg">{post.content}</p>
                                            <Link href={`/posts/${post.id}`} className="small text-decoration-none text-secondary">
                                                <i className="bi bi-chat me-1" aria-hidden="true" />
                                                {post.commentCount ?? 0} bình luận
                                            </Link>
                                        </div>
                                    </div>
                                </article>
                            ))}
                        </div>
                    )}

                    {keyword && posts.length > 0 && (
                        <div ref={loadMoreRef} className="d-flex justify-content-center py-4" aria-live="polite">
                            {loading && <Spinner animation="border" size="sm" variant="primary" />}
                            {!loading && page >= totalPages - 1 && <small className="text-secondary">Bạn đã xem hết bài viết.</small>}
                        </div>
                    )}

                    {keyword && posts.length === 0 && (
                        <div className="border rounded-4 bg-white p-5 text-center text-secondary shadow-sm">
                            <i className="bi bi-search fs-2 d-block mb-3" aria-hidden="true" />
                            <p className="mb-1 fw-semibold text-dark">Không tìm thấy bài viết</p>
                            <small>Hãy thử một từ khóa khác.</small>
                        </div>
                    )}
                </div>
            </div>
        </main>
    );
}

export default SearchPage;
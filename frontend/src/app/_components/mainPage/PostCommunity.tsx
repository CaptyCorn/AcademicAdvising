'use client'

import { AuthContext } from "@/app/_context/AuthContext";
import { usePostCreate } from "@/app/_context/PostCreateContext";
import { use, useEffect, useRef, useState } from "react";
import { Button, Card, Dropdown, Image, Spinner } from "react-bootstrap";
import PostModelCreate from "./PostModelCreate";
import styles from "./PostCommunity.module.css";
import moment from "moment";
import "../../../../node_modules/moment/locale/vi";
import Link from "next/link";

interface Iprops {
    posts: IPosts[],
    page: number,
    totalElements: number,
    totalPages: number,
    loadMore: (page: number) => Promise<{
        content: IPosts[];
        page: number;
        totalPages: number;
    }>
}

const PostCommunity = (props: Iprops) => {
    const { posts: initialPosts, page: initialPage, totalPages, loadMore } = props;
    const { user } = use(AuthContext);
    const [posts, setPosts] = useState(initialPosts);
    const [page, setPage] = useState(initialPage);
    const [loading, setLoading] = useState(false);
    const { showCreatePost, openCreatePost, closeCreatePost } = usePostCreate();
    const loadMoreRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const target = loadMoreRef.current;
        if (!target) return;

        const observer = new IntersectionObserver(async ([entry]) => {
            if (!entry.isIntersecting || loading || page >= totalPages - 1) return;

            setLoading(true);
            try {
                const nextPage = await loadMore(page + 1);
                setPosts((current) => [...current, ...nextPage.content]);
                setPage(nextPage.page);
            } finally {
                setLoading(false);
            }
        }, { rootMargin: "240px" });

        observer.observe(target);
        return () => observer.disconnect();
    }, [loadMore, loading, page, totalPages]);

    return(
        <div className="container py-4">
            <div className="row">
                <div className="col-12 col-xl-7 mx-auto">
            <div className="d-flex align-items-center gap-3 p-3 mb-4 bg-white border rounded-4 shadow-sm">
                <Image src={user?.avatar || "/file.svg"} alt="Ảnh đại diện" width={46} height={46} roundedCircle className="flex-shrink-0" />
                <button type="button" className="flex-grow-1 border-0 bg-light text-start text-secondary rounded-pill px-4 py-3" onClick={openCreatePost}>
                    {user?.firstName || user?.lastName ? `${user.firstName} ${user.lastName}` : user?.username || "Bạn"}, bạn đang thắc mắc gì thế?
                </button>
                <Button variant="primary" className="rounded-pill px-4" onClick={openCreatePost}>Đăng</Button>
            </div>

            <div className="d-flex flex-column overflow-hidden rounded-4 shadow-sm">
                {posts.map((post, index) => (
                    <Card key={post.id} className={`border-0 rounded-0 bg-white ${index < posts.length - 1 ? "border-bottom" : ""}`}>
                        <Card.Body className="p-3 p-md-4">
                            <div className="d-flex align-items-start gap-3">
                                <Image src={post.user.avatar || "/file.svg"} alt="Ảnh đại diện" width={42} height={42} roundedCircle className="flex-shrink-0" />
                                <div>
                                    <div className="d-flex align-items-center gap-2">
                                        <span className="fw-semibold">{post.user.name || post.user.username}</span>
                                        <small className="text-secondary">{moment(post.createdAt).fromNow()}</small>
                                    </div>
                                    <small className="text-secondary">@{post.user.username}</small>
                                </div>
                                <Dropdown align="end" className="ms-auto">
                                    <Dropdown.Toggle variant="link" className={`p-0 border-0 text-secondary ${styles.moreButton}`} aria-label="Tùy chọn bài đăng">
                                        <i className="bi bi-three-dots-vertical" aria-hidden="true" />
                                    </Dropdown.Toggle>
                                    <Dropdown.Menu>
                                        <Dropdown.Item onClick={() => console.log("Báo cáo bài đăng", post.id)}>Báo cáo</Dropdown.Item>
                                        <Dropdown.Item onClick={() => navigator.clipboard?.writeText(`${window.location.origin}/posts/${post.id}`)}>Copy đường dẫn</Dropdown.Item>
                                    </Dropdown.Menu>
                                </Dropdown>
                            </div>
                            <Card.Text className="mb-3 mt-3 text-break">{post.content}</Card.Text>
                            <Link href={`/posts/${post.id}`} type="button" className="btn btn-link d-inline-flex align-items-center gap-2 p-0 text-secondary text-decoration-none">
                                <i className="bi bi-chat" aria-hidden="true" />
                                <span>{post.commentCount ?? 0}</span>
                            </Link>
                        </Card.Body>
                    </Card>
                ))}
            </div>

            <div ref={loadMoreRef} className="d-flex justify-content-center py-4" aria-live="polite">
                {loading && <Spinner animation="border" size="sm" variant="primary" />}
                {!loading && page >= totalPages - 1 && posts.length > 0 && <small className="text-secondary">Bạn đã xem hết bài đăng.</small>}
            </div>

            <PostModelCreate showModalCreate={showCreatePost} setShowModalCreate={(value) => value ? openCreatePost() : closeCreatePost()} />
                </div>
            </div>
        </div>
    );
}

export default PostCommunity;
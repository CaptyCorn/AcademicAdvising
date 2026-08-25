'use client'
import { requestPostCreate } from '@/actions/post.action';
import { AuthContext } from '@/app/_context/AuthContext';
import { use, useState, useTransition } from 'react';
import { useRouter } from 'next/navigation';
import { Form, Image } from 'react-bootstrap';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { toast } from "react-toastify";

interface IProps {
    showModalCreate: boolean,
    setShowModalCreate: (value: boolean) => void
}

const PostModelCreate = (props: IProps) => {
    const { showModalCreate, setShowModalCreate } = props;
    const { user } = use(AuthContext);
    const router = useRouter();

    const [content, setContent] = useState<string>("");
    const [isPending, startTransition] = useTransition()

    const handlePost = () => {
        if (!content.trim()) {
            toast.warning('Chưa nhập nội dung');
            return
        }

        startTransition(async () => {
            const resInfo = await requestPostCreate(content);
            if (resInfo.success) {
                toast.success(resInfo.message);
                handlecloseModal();
                router.refresh();
            }
            else toast.error(resInfo.message);
        })
    }

    const handlecloseModal = () => {
        setContent("");
        setShowModalCreate(false);
    }

    return (
        <Modal show={showModalCreate} onHide={handlecloseModal} centered>
            <Modal.Header closeButton className="border-0 pb-2">
                <Modal.Title className="fs-5 fw-semibold">Tạo bài đăng mới</Modal.Title>
            </Modal.Header>
            <Modal.Body className="pt-2">
                <div className="d-flex gap-3">
                    <Image src={user?.avatar || "/file.svg"} alt="Ảnh đại diện" width={44} height={44} roundedCircle />
                    <div className="flex-grow-1">
                        <div className="fw-semibold mb-3">{user?.firstName || user?.lastName ? `${user.firstName} ${user.lastName}` : user?.username || "Tài khoản"}</div>
                        <Form.Control
                            as="textarea"
                            rows={5}
                            value={content}
                            maxLength={500}
                            placeholder="Bạn đang thắc mắc gì thế?"
                            className="border-0 bg-light rounded-3 p-3"
                            onChange={(e) => setContent(e.target.value)}
                            autoFocus
                        />
                        <div className="text-end mt-3">
                            <small className="text-secondary">{content.length}/500</small>
                        </div>
                    </div>
                </div>
            </Modal.Body>
            <Modal.Footer className="border-0 pt-0">
                <Button variant="light" onClick={handlecloseModal}>Huỷ</Button>
                <Button variant="primary" onClick={handlePost} disabled={isPending || !content.trim()}>
                    {isPending ? 'Đang đăng...' : 'Đăng bài'}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default PostModelCreate;
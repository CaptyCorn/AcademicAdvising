"use client";

import moment from "moment";
import { useState, useTransition } from "react";
import { Button, Card, Form, Modal, Pagination, Spinner, Table } from "react-bootstrap";
import { toast } from "react-toastify";

import "../../../../../../node_modules/moment/locale/vi";

interface IProps {
    initialMajors: IMajor[],
    initialPage: number,
    initialTotalPages: number,
    initialTotalElements: number,
    loadMajors: (page: number) => Promise<IPageResponse<IMajor>>,
    createMajor: (name: string) => Promise<{ success: boolean, message: string, data?: IMajor }>
}

const formatCreatedAt = (value: Date) => moment(value).format("DD/MM/YYYY");

const MajorManagement = (props: IProps) => {
    const { initialMajors, initialPage, initialTotalPages, initialTotalElements, loadMajors, createMajor } = props;
    const [majors, setMajors] = useState(initialMajors);
    const [page, setPage] = useState(initialPage);
    const [totalPages, setTotalPages] = useState(initialTotalPages);
    const [totalElements, setTotalElements] = useState(initialTotalElements);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [name, setName] = useState("");
    const [isPending, startTransition] = useTransition();

    const handlePageChange = (nextPage: number) => {
        if (nextPage === page || isPending) return;

        startTransition(async () => {
            try {
                const result = await loadMajors(nextPage);
                setMajors(result.content);
                setPage(result.page);
                setTotalPages(result.totalPages);
                setTotalElements(result.totalElements);
            } catch {
                toast.error("Không thể tải danh sách ngành học");
            }
        });
    };

    const handleCreate = () => {
        const majorName = name.trim();
        if (!majorName) {
            toast.warning("Vui lòng nhập tên ngành học");
            return;
        }

        startTransition(async () => {
            const response = await createMajor(majorName);
            if (!response.success) {
                toast.error(response.message);
                return;
            }

            setName("");
            setShowCreateModal(false);
            toast.success(response.message);
            const result = await loadMajors(0);
            setMajors(result.content);
            setPage(result.page);
            setTotalPages(result.totalPages);
            setTotalElements(result.totalElements);
        });
    };

    return (
        <main className="container-fluid p-4 p-lg-5">
            <div className="d-flex flex-wrap align-items-end justify-content-between gap-3 mb-4">
                <div>
                    <small className="text-uppercase fw-bold text-primary">Quản lí dữ liệu</small>
                    <h1 className="h3 mb-1 mt-2 fw-bold text-dark">Quản lí ngành học</h1>
                    <p className="mb-0 text-secondary">Theo dõi và thêm các ngành học trong hệ thống.</p>
                </div>
                <Button variant="primary" className="rounded-3" onClick={() => setShowCreateModal(true)}>
                    <i className="bi bi-plus-lg me-2" aria-hidden="true" />
                    Thêm ngành học
                </Button>
            </div>

            <Card className="border-0 rounded-4 shadow-sm">
                <Card.Body className="p-0">
                    <div className="d-flex align-items-center justify-content-between gap-3 border-bottom p-4">
                        <span className="fw-semibold text-dark">Danh sách ngành học</span>
                        <small className="text-secondary">{totalElements.toLocaleString("vi-VN")} ngành</small>
                    </div>

                    <div className="table-responsive">
                        <Table hover className="mb-0 align-middle">
                            <thead className="table-light">
                                <tr>
                                    <th className="px-4 py-3" scope="col">ID</th>
                                    <th className="py-3" scope="col">Tên ngành học</th>
                                    <th className="py-3 text-end" scope="col">Ngày khởi tạo</th>
                                </tr>
                            </thead>
                            <tbody>
                                {majors.map((major, index) => (
                                    <tr key={major.id}>
                                        <td className="px-4 text-secondary">{page * 10 + index + 1}</td>
                                        <td className="fw-semibold text-dark">{major.name}</td>
                                        <td className="text-end pe-4 text-secondary">{formatCreatedAt(major.createdAt)}</td>
                                    </tr>
                                ))}
                                {!majors.length && (
                                    <tr><td colSpan={3} className="py-5 text-center text-secondary">Chưa có ngành học.</td></tr>
                                )}
                            </tbody>
                        </Table>
                    </div>

                    <div className="d-flex justify-content-center border-top p-4">
                        {isPending ? <Spinner animation="border" size="sm" variant="primary" /> : totalPages > 1 && (
                            <Pagination className="mb-0" size="sm">
                                <Pagination.Prev onClick={() => handlePageChange(page - 1)} disabled={page === 0} />
                                {Array.from({ length: totalPages }, (_, pageNumber) => (
                                    <Pagination.Item key={pageNumber} active={pageNumber === page} onClick={() => handlePageChange(pageNumber)}>
                                        {pageNumber + 1}
                                    </Pagination.Item>
                                ))}
                                <Pagination.Next onClick={() => handlePageChange(page + 1)} disabled={page >= totalPages - 1} />
                            </Pagination>
                        )}
                    </div>
                </Card.Body>
            </Card>

            <Modal show={showCreateModal} onHide={() => setShowCreateModal(false)} centered>
                <Modal.Header closeButton className="border-0 px-4 pt-4 pb-2">
                    <Modal.Title className="fs-5 fw-bold">Thêm ngành học</Modal.Title>
                </Modal.Header>
                <Modal.Body className="px-4 py-3">
                    <Form.Label htmlFor="major-name" className="fw-semibold">Tên ngành học</Form.Label>
                    <Form.Control
                        id="major-name"
                        value={name}
                        placeholder="Ví dụ: Công nghệ thông tin"
                        onChange={(event) => setName(event.target.value)}
                        onKeyDown={(event) => { if (event.key === "Enter") handleCreate(); }}
                        autoFocus
                    />
                </Modal.Body>
                <Modal.Footer className="border-0 px-4 pb-4 pt-0">
                    <Button variant="light" onClick={() => setShowCreateModal(false)}>Hủy</Button>
                    <Button variant="primary" onClick={handleCreate} disabled={isPending || !name.trim()}>
                        {isPending ? "Đang thêm..." : "Thêm ngành"}
                    </Button>
                </Modal.Footer>
            </Modal>
        </main>
    );
};

export default MajorManagement;

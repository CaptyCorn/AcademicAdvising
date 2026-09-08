"use client";

import { requestCreateBook, requestListMyBooks, requestUpdateBookStatus } from "@/actions/book.action";
import moment from "moment";
import "../../../../../node_modules/moment/locale/vi";
import { useEffect, useRef, useState, useTransition } from "react";
import { Button, Card, Dropdown, Form, Image, Modal, Pagination, Spinner, Table } from "react-bootstrap";
import { toast } from "react-toastify";

interface IProps {
    initialBooks: IBook[],
    initialPage: number,
    initialTotalPages: number,
    initialTotalElements: number,
    subjects: ISubject[],
    initialSubjectPage: number,
    initialSubjectTotalPages: number,
    loadSubjects: (page: number) => Promise<IPageResponse<ISubject>>
}

const statusLabel = (status?: string) => {
    if (status === "SOLD") return "Đã bán";
    if (status === "HIDDEN") return "Đã ẩn";
    return "Đang bán";
};

const statusVariant = (status?: string) => {
    if (status === "SOLD") return "secondary";
    if (status === "HIDDEN") return "light";
    return "success";
};

const MyBook = (props: IProps) => {
    const { initialBooks, initialPage, initialTotalPages, initialTotalElements, subjects: initialSubjects, initialSubjectPage, initialSubjectTotalPages, loadSubjects } = props;
    const [books, setBooks] = useState(initialBooks);
    const [page, setPage] = useState(initialPage);
    const [totalPages, setTotalPages] = useState(initialTotalPages);
    const [totalElements, setTotalElements] = useState(initialTotalElements);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");
    const [condition, setCondition] = useState("USED");
    const [subjectIds, setSubjectIds] = useState<number[]>([]);
    const [subjects, setSubjects] = useState(initialSubjects);
    const [subjectPage, setSubjectPage] = useState(initialSubjectPage);
    const [subjectTotalPages, setSubjectTotalPages] = useState(initialSubjectTotalPages);
    const [subjectOpen, setSubjectOpen] = useState(false);
    const [subjectLoading, setSubjectLoading] = useState(false);
    const [files, setFiles] = useState<File[]>([]);
    const [isPending, startTransition] = useTransition();
    const subjectLoadMoreRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const target = subjectLoadMoreRef.current;
        if (!target || !subjectOpen) return;

        const observer = new IntersectionObserver(async ([entry]) => {
            if (!entry.isIntersecting || subjectLoading || subjectPage >= subjectTotalPages - 1) return;

            setSubjectLoading(true);
            try {
                const result = await loadSubjects(subjectPage + 1);
                setSubjects((currentSubjects) => [...currentSubjects, ...result.content]);
                setSubjectPage(result.page);
                setSubjectTotalPages(result.totalPages);
            } catch {
                toast.error("Không thể tải thêm môn học");
            } finally {
                setSubjectLoading(false);
            }
        }, { rootMargin: "80px" });

        observer.observe(target);
        return () => observer.disconnect();
    }, [loadSubjects, subjectLoading, subjectOpen, subjectPage, subjectTotalPages]);

    const refreshBooks = async (nextPage: number = 0) => {
        const result = await requestListMyBooks(nextPage);
        setBooks(result.content);
        setPage(result.page);
        setTotalPages(result.totalPages);
        setTotalElements(result.totalElements);
    };

    const resetForm = () => {
        setName("");
        setDescription("");
        setPrice("");
        setCondition("USED");
        setSubjectIds([]);
        setFiles([]);
        setSubjectOpen(false);
        setShowCreateModal(false);
    };

    const handleCreate = () => {
        if (!name.trim() || !description.trim() || !price || files.length === 0) {
            toast.warning("Vui lòng nhập đủ thông tin và chọn ít nhất một ảnh");
            return;
        }

        startTransition(async () => {
            const formData = new FormData();
            formData.set("name", name.trim());
            formData.set("description", description.trim());
            formData.set("price", price);
            formData.set("condition", condition);
            subjectIds.forEach((subjectId) => formData.append("subjectIds", String(subjectId)));
            files.forEach((file) => formData.append("files", file));

            const response = await requestCreateBook(formData);
            if (!response.success) {
                toast.error(response.message);
                return;
            }

            resetForm();
            toast.success(response.message);
            await refreshBooks(0);
        });
    };

    const handleStatusChange = (bookId: number, status: string) => {
        startTransition(async () => {
            const response = await requestUpdateBookStatus(bookId, status);
            if (!response.success) {
                toast.error(response.message);
                return;
            }

            setBooks((currentBooks) => currentBooks.map((book) => book.id === bookId ? { ...book, status } : book));
            toast.success(response.message);
        });
    };

    const selectedSubjects = subjects.filter((subject) => subjectIds.includes(subject.id));

    return (
        <main className="container-fluid p-4 p-lg-5">
            <div className="d-flex flex-wrap align-items-end justify-content-between gap-3 mb-4">
                <div>
                    <small className="text-uppercase fw-bold text-primary">Kho sách cá nhân</small>
                    <h1 className="h3 mb-1 mt-2 fw-bold text-dark">Sách của tôi</h1>
                    <p className="mb-0 text-secondary">Quản lý những cuốn sách bạn đang đăng bán.</p>
                </div>
                <Button variant="primary" className="rounded-3" onClick={() => setShowCreateModal(true)}>
                    <i className="bi bi-plus-lg me-2" aria-hidden="true" />Thêm sách
                </Button>
            </div>

            <Card className="border-0 rounded-4 shadow-sm">
                <Card.Body className="p-0">
                    <div className="d-flex align-items-center justify-content-between gap-3 border-bottom p-4">
                        <span className="fw-semibold text-dark">Danh sách sách đăng bán</span>
                        <small className="text-secondary">{totalElements.toLocaleString("vi-VN")} sách</small>
                    </div>
                    <div className="table-responsive">
                        <Table hover className="mb-0 align-middle">
                            <thead className="table-light">
                                <tr>
                                    <th className="px-4 py-3">Sách</th>
                                    <th className="py-3">Giá</th>
                                    <th className="py-3">Tình trạng</th>
                                    <th className="py-3">Ngày đăng</th>
                                    <th className="py-3 text-end pe-4">Trạng thái bán</th>
                                </tr>
                            </thead>
                            <tbody>
                                {books.map((book) => (
                                    <tr key={book.id}>
                                        <td className="px-4">
                                            <div className="d-flex align-items-center gap-3">
                                                <Image src={book.image?.imageUrl || "/file.svg"} alt={book.name} width={56} height={56} className="rounded-3 object-fit-cover" />
                                                <span className="fw-semibold text-dark">{book.name}</span>
                                            </div>
                                        </td>
                                        <td className="fw-semibold text-success">{book.price.toLocaleString("vi-VN")} đ</td>
                                        <td><span className="badge text-bg-light">{book.condition === "NEW" ? "Mới" : "Đã qua sử dụng"}</span></td>
                                        <td className="text-secondary">{moment(book.createdAt).format("DD/MM/YYYY")}</td>
                                        <td className="text-end pe-4">
                                            <Form.Select size="sm" value={book.status || "AVAILABLE"} onChange={(event) => handleStatusChange(book.id, event.target.value)} disabled={isPending} className="d-inline-block w-auto">
                                                <option value="AVAILABLE">Đang bán</option>
                                                <option value="HIDDEN">Đã ẩn</option>
                                                <option value="SOLD">Đã bán</option>
                                            </Form.Select>
                                            <span className={`badge text-bg-${statusVariant(book.status)} ms-2 d-none d-xl-inline-block`}>{statusLabel(book.status)}</span>
                                        </td>
                                    </tr>
                                ))}
                                {!books.length && <tr><td colSpan={5} className="py-5 text-center text-secondary">Bạn chưa đăng bán cuốn sách nào.</td></tr>}
                            </tbody>
                        </Table>
                    </div>
                    <div className="d-flex justify-content-center border-top p-4">
                        {isPending ? <Spinner animation="border" size="sm" variant="primary" /> : totalPages > 1 && (
                            <Pagination className="mb-0" size="sm">
                                <Pagination.Prev onClick={() => refreshBooks(page - 1)} disabled={page === 0} />
                                {Array.from({ length: totalPages }, (_, pageNumber) => <Pagination.Item key={pageNumber} active={pageNumber === page} onClick={() => refreshBooks(pageNumber)}>{pageNumber + 1}</Pagination.Item>)}
                                <Pagination.Next onClick={() => refreshBooks(page + 1)} disabled={page >= totalPages - 1} />
                            </Pagination>
                        )}
                    </div>
                </Card.Body>
            </Card>

            <Modal show={showCreateModal} onHide={resetForm} centered size="lg">
                <Modal.Header closeButton className="border-0 px-4 pt-4 pb-2">
                    <Modal.Title className="fs-5 fw-bold">Thêm sách đăng bán</Modal.Title>
                </Modal.Header>
                <Modal.Body className="px-4 py-3">
                    <div className="row g-3">
                        <div className="col-12 col-md-8">
                            <Form.Group controlId="book-name">
                                <Form.Label className="fw-semibold">Tên sách</Form.Label>
                                <Form.Control value={name} onChange={(event) => setName(event.target.value)} placeholder="Nhập tên sách" autoFocus />
                            </Form.Group>
                        </div>
                        <div className="col-12 col-md-4">
                            <Form.Group controlId="book-price">
                                <Form.Label className="fw-semibold">Giá bán</Form.Label>
                                <Form.Control type="number" min="0" value={price} onChange={(event) => setPrice(event.target.value)} placeholder="VNĐ" />
                            </Form.Group>
                        </div>
                        <div className="col-12 col-md-6">
                            <Form.Group controlId="book-condition">
                                <Form.Label className="fw-semibold">Tình trạng</Form.Label>
                                <Form.Select value={condition} onChange={(event) => setCondition(event.target.value)}>
                                    <option value="USED">Đã qua sử dụng</option>
                                    <option value="NEW">Mới</option>
                                </Form.Select>
                            </Form.Group>
                        </div>
                        <div className="col-12 col-md-6">
                            <Form.Group controlId="book-subjects">
                                <Form.Label className="fw-semibold">Môn học liên quan</Form.Label>
                                <Dropdown show={subjectOpen} onToggle={setSubjectOpen} className="w-100">
                                    <Dropdown.Toggle variant="light" className="w-100 d-flex align-items-center justify-content-between border rounded-3 text-start">
                                        <span className={selectedSubjects.length ? "text-dark" : "text-secondary"}>
                                            {selectedSubjects.length ? `${selectedSubjects.length} môn đã chọn` : "Chọn môn học"}
                                        </span>
                                    </Dropdown.Toggle>
                                    <Dropdown.Menu className="w-100 overflow-auto p-1" style={{ maxHeight: "240px" }}>
                                        {subjects.map((subject) => {
                                            const isSelected = subjectIds.includes(subject.id);
                                            return (
                                                <Dropdown.Item
                                                    key={subject.id}
                                                    active={isSelected}
                                                    onClick={(event) => {
                                                        event.preventDefault();
                                                        setSubjectIds((currentIds) => isSelected
                                                            ? currentIds.filter((id) => id !== subject.id)
                                                            : [...currentIds, subject.id]);
                                                    }}
                                                >
                                                    <i className={`bi bi-check2 me-2 ${isSelected ? "visible" : "invisible"}`} aria-hidden="true" />
                                                    {subject.name}
                                                </Dropdown.Item>
                                            );
                                        })}
                                        <div ref={subjectLoadMoreRef} className="d-flex justify-content-center p-2">
                                            {subjectLoading && <Spinner animation="border" size="sm" variant="primary" />}
                                            {!subjectLoading && subjectPage >= subjectTotalPages - 1 && <small className="text-secondary">Đã tải hết môn học</small>}
                                        </div>
                                    </Dropdown.Menu>
                                </Dropdown>
                                {selectedSubjects.length > 0 && (
                                    <div className="d-flex flex-wrap gap-1 mt-2">
                                        {selectedSubjects.map((subject) => <span key={subject.id} className="badge rounded-pill text-bg-primary-subtle text-primary">{subject.name}</span>)}
                                    </div>
                                )}
                            </Form.Group>
                        </div>
                        <div className="col-12">
                            <Form.Group controlId="book-description">
                                <Form.Label className="fw-semibold">Mô tả</Form.Label>
                                <Form.Control as="textarea" rows={3} value={description} onChange={(event) => setDescription(event.target.value)} placeholder="Mô tả tình trạng và thông tin sách" />
                            </Form.Group>
                        </div>
                        <div className="col-12">
                            <Form.Group controlId="book-files">
                                <Form.Label className="fw-semibold">Ảnh sách</Form.Label>
                                <Form.Control
                                    type="file"
                                    accept="image/*"
                                    multiple
                                    onChange={(event) => setFiles(Array.from((event.currentTarget as HTMLInputElement).files || []))}
                                />
                            </Form.Group>
                        </div>
                    </div>
                </Modal.Body>
                <Modal.Footer className="border-0 px-4 pb-4 pt-0">
                    <Button variant="light" onClick={resetForm}>Hủy</Button>
                    <Button variant="primary" onClick={handleCreate} disabled={isPending || !name.trim() || !description.trim() || !price || files.length === 0}>
                        {isPending ? "Đang đăng..." : "Đăng bán"}
                    </Button>
                </Modal.Footer>
            </Modal>
        </main>
    );
};

export default MyBook;

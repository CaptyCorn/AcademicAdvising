"use client";

import { useEffect, useRef, useState, useTransition } from "react";
import { Button, Card, Dropdown, Form, Modal, Pagination, Spinner, Table } from "react-bootstrap";
import { toast } from "react-toastify";

interface IProps {
	initialSubjects: ISubject[],
	initialPage: number,
	initialTotalPages: number,
	initialTotalElements: number,
	initialMajors: IMajor[],
	initialMajorPage: number,
	initialMajorTotalPages: number,
	loadSubjects: (page: number, majorId?: number) => Promise<IPageResponse<ISubject>>,
	loadMajors: (page: number) => Promise<IPageResponse<IMajor>>,
	createSubject: (data: { name: string, description: string, majorId: number }) => Promise<{ success: boolean, message: string, data?: ISubject }>
}

const SubjectManagement = (props: IProps) => {
	const { initialSubjects, initialPage, initialTotalPages, initialTotalElements, initialMajors, initialMajorPage, initialMajorTotalPages, loadSubjects, loadMajors, createSubject } = props;
	const [subjects, setSubjects] = useState(initialSubjects);
	const [page, setPage] = useState(initialPage);
	const [totalPages, setTotalPages] = useState(initialTotalPages);
	const [totalElements, setTotalElements] = useState(initialTotalElements);
	const [showCreateModal, setShowCreateModal] = useState(false);
	const [name, setName] = useState("");
	const [description, setDescription] = useState("");
	const [majorId, setMajorId] = useState<number | "">("");
	const [majors, setMajors] = useState(initialMajors);
	const [majorPage, setMajorPage] = useState(initialMajorPage);
	const [majorTotalPages, setMajorTotalPages] = useState(initialMajorTotalPages);
	const [majorOpen, setMajorOpen] = useState(false);
	const [majorLoading, setMajorLoading] = useState(false);
	const [isPending, startTransition] = useTransition();
	const majorLoadMoreRef = useRef<HTMLDivElement>(null);

	useEffect(() => {
		const target = majorLoadMoreRef.current;
		if (!target || !majorOpen) return;

		const observer = new IntersectionObserver(async ([entry]) => {
			if (!entry.isIntersecting || majorLoading || majorPage >= majorTotalPages - 1) return;

			setMajorLoading(true);
			try {
				const result = await loadMajors(majorPage + 1);
				setMajors((currentMajors) => [...currentMajors, ...result.content]);
				setMajorPage(result.page);
				setMajorTotalPages(result.totalPages);
			} catch {
				toast.error("Không thể tải thêm ngành học");
			} finally {
				setMajorLoading(false);
			}
		}, { rootMargin: "80px" });

		observer.observe(target);
		return () => observer.disconnect();
	}, [loadMajors, majorLoading, majorOpen, majorPage, majorTotalPages]);

	const handlePageChange = (nextPage: number) => {
		if (nextPage === page || isPending) return;

		startTransition(async () => {
			try {
				const result = await loadSubjects(nextPage);
				setSubjects(result.content);
				setPage(result.page);
				setTotalPages(result.totalPages);
				setTotalElements(result.totalElements);
			} catch {
				toast.error("Không thể tải danh sách môn học");
			}
		});
	};

	const handleCloseModal = () => {
		setShowCreateModal(false);
		setName("");
		setDescription("");
		setMajorId("");
		setMajorOpen(false);
	};

	const handleCreate = () => {
		if (!name.trim() || !description.trim() || majorId === "") {
			toast.warning("Vui lòng nhập đủ thông tin môn học");
			return;
		}

		startTransition(async () => {
			const response = await createSubject({ name, description, majorId });
			if (!response.success) {
				toast.error(response.message);
				return;
			}

			handleCloseModal();
			toast.success(response.message);
			const result = await loadSubjects(0);
			setSubjects(result.content);
			setPage(result.page);
			setTotalPages(result.totalPages);
			setTotalElements(result.totalElements);
		});
	};

	const majorName = (id: number) => majors.find((major) => major.id === id)?.name || "Chưa xác định";
	const selectedMajorName = majors.find((major) => major.id === majorId)?.name || "Chọn ngành học";

	return (
		<main className="container-fluid p-4 p-lg-5">
			<div className="d-flex flex-wrap align-items-end justify-content-between gap-3 mb-4">
				<div>
					<small className="text-uppercase fw-bold text-primary">Quản lí dữ liệu</small>
					<h1 className="h3 mb-1 mt-2 fw-bold text-dark">Quản lí môn học</h1>
					<p className="mb-0 text-secondary">Theo dõi môn học và ngành đào tạo liên quan.</p>
				</div>
				<Button variant="primary" className="rounded-3" onClick={() => setShowCreateModal(true)}>
					<i className="bi bi-plus-lg me-2" aria-hidden="true" />
					Thêm môn học
				</Button>
			</div>

			<Card className="border-0 rounded-4 shadow-sm">
				<Card.Body className="p-0">
					<div className="d-flex align-items-center justify-content-between gap-3 border-bottom p-4">
						<span className="fw-semibold text-dark">Danh sách môn học</span>
						<small className="text-secondary">{totalElements.toLocaleString("vi-VN")} môn</small>
					</div>

					<div className="table-responsive">
						<Table hover className="mb-0 align-middle">
							<thead className="table-light">
								<tr>
									<th className="px-4 py-3" scope="col">ID</th>
									<th className="py-3" scope="col">Tên môn học</th>
									<th className="py-3" scope="col">Ngành học</th>
									<th className="py-3" scope="col">Mô tả</th>
								</tr>
							</thead>
							<tbody>
								{subjects.map((subject, index) => (
									<tr key={subject.id}>
										<td className="px-4 text-secondary">{page * 10 + index + 1}</td>
										<td className="fw-semibold text-dark">{subject.name}</td>
										<td><span className="badge rounded-pill text-bg-primary-subtle text-primary">{majorName(subject.majorId)}</span></td>
										<td className="text-secondary text-truncate" style={{ maxWidth: "280px" }}>{subject.description || "Chưa có mô tả"}</td>
									</tr>
								))}
								{!subjects.length && (
									<tr><td colSpan={4} className="py-5 text-center text-secondary">Chưa có môn học.</td></tr>
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

			<Modal show={showCreateModal} onHide={handleCloseModal} centered>
				<Modal.Header closeButton className="border-0 px-4 pt-4 pb-2">
					<Modal.Title className="fs-5 fw-bold">Thêm môn học</Modal.Title>
				</Modal.Header>
				<Modal.Body className="px-4 py-3">
					<Form.Group className="mb-3" controlId="subject-name">
						<Form.Label className="fw-semibold">Tên môn học</Form.Label>
						<Form.Control value={name} placeholder="Ví dụ: Lập trình hướng đối tượng" onChange={(event) => setName(event.target.value)} autoFocus />
					</Form.Group>
					<Form.Group className="mb-3" controlId="subject-major">
						<Form.Label className="fw-semibold">Ngành học</Form.Label>
						<Dropdown show={majorOpen} onToggle={setMajorOpen} className="w-100">
							<Dropdown.Toggle variant="light" className="w-100 d-flex align-items-center justify-content-between border rounded-3 text-start">
								<span className={majorId === "" ? "text-secondary" : "text-dark"}>{selectedMajorName}</span>
							</Dropdown.Toggle>
							<Dropdown.Menu className="w-100 overflow-auto p-1" style={{ maxHeight: "240px" }}>
								{majors.map((major) => (
									<Dropdown.Item key={major.id} active={major.id === majorId} onClick={() => { setMajorId(major.id); setMajorOpen(false); }}>
										{major.name}
									</Dropdown.Item>
								))}
								<div ref={majorLoadMoreRef} className="d-flex justify-content-center p-2">
									{majorLoading && <Spinner animation="border" size="sm" variant="primary" />}
									{!majorLoading && majorPage >= majorTotalPages - 1 && <small className="text-secondary">Đã tải hết ngành</small>}
								</div>
							</Dropdown.Menu>
						</Dropdown>
					</Form.Group>
					<Form.Group controlId="subject-description">
						<Form.Label className="fw-semibold">Mô tả</Form.Label>
						<Form.Control as="textarea" rows={3} value={description} placeholder="Mô tả ngắn về môn học" onChange={(event) => setDescription(event.target.value)} />
					</Form.Group>
				</Modal.Body>
				<Modal.Footer className="border-0 px-4 pb-4 pt-0">
					<Button variant="light" onClick={handleCloseModal}>Hủy</Button>
					<Button variant="primary" onClick={handleCreate} disabled={isPending || !name.trim() || !description.trim() || majorId === ""}>
						{isPending ? "Đang thêm..." : "Thêm môn"}
					</Button>
				</Modal.Footer>
			</Modal>
		</main>
	);
};

export default SubjectManagement;

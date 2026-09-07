"use client";

import Link from "next/link";
import { useEffect, useRef, useState, useTransition } from "react";
import { Alert, Button, Card, Dropdown, Form, InputGroup, Pagination, Spinner } from "react-bootstrap";
import { toast } from "react-toastify";

interface IBookFilters {
    kw: string,
    subjectId?: number,
    majorId?: number
}

interface IProps {
    books: IBook[],
    bookPage: number,
    bookTotalPages: number,
    bookTotalElements: number,
    majors: IMajor[],
    majorPage: number,
    majorTotalPages: number,
    subjects: ISubject[],
    subjectPage: number,
    subjectTotalPages: number,
    loadBooks: (params: IBookFilters & { page?: number }) => Promise<IPageResponse<IBook>>,
    loadMoreMajors: (page: number) => Promise<IPageResponse<IMajor>>,
    loadMoreSubjects: (page: number, majorId?: number) => Promise<IPageResponse<ISubject>>
}

const formatPrice = (price: number) => new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND"
}).format(price);

const conditionLabel = (condition: string) => {
    if (condition === "NEW") return "Mới";
    return "Đã qua sử dụng";
};

const BookPage = (props: IProps) => {
    const {
        books: initialBooks,
        bookPage: initialBookPage,
        bookTotalPages: initialBookTotalPages,
        bookTotalElements: initialBookTotalElements,
        majors: initialMajors,
        majorPage: initialMajorPage,
        majorTotalPages,
        subjects: initialSubjects,
        subjectPage: initialSubjectPage,
        subjectTotalPages,
        loadBooks,
        loadMoreMajors,
        loadMoreSubjects
    } = props;
    const [books, setBooks] = useState(initialBooks);
    const [bookPage, setBookPage] = useState(initialBookPage);
    const [bookTotalPages, setBookTotalPages] = useState(initialBookTotalPages);
    const [bookTotalElements, setBookTotalElements] = useState(initialBookTotalElements);
    const [majors, setMajors] = useState(initialMajors);
    const [majorPage, setMajorPage] = useState(initialMajorPage);
    const [subjects, setSubjects] = useState(initialSubjects);
    const [subjectPage, setSubjectPage] = useState(initialSubjectPage);
    const [filters, setFilters] = useState<IBookFilters>({ kw: "" });
    const [majorOpen, setMajorOpen] = useState(false);
    const [subjectOpen, setSubjectOpen] = useState(false);
    const [majorLoading, setMajorLoading] = useState(false);
    const [subjectLoading, setSubjectLoading] = useState(false);
    const [isFiltering, startFiltering] = useTransition();
    const majorLoadMoreRef = useRef<HTMLDivElement>(null);
    const subjectLoadMoreRef = useRef<HTMLDivElement>(null);

    const selectedMajor = majors.find((major) => major.id === filters.majorId);
    const selectedSubject = subjects.find((subject) => subject.id === filters.subjectId);

    useEffect(() => {
        const target = majorLoadMoreRef.current;
        if (!target || !majorOpen) return;

        const observer = new IntersectionObserver(async ([entry]) => {
            if (!entry.isIntersecting || majorLoading || majorPage >= majorTotalPages - 1) return;

            setMajorLoading(true);
            try {
                const nextPage = await loadMoreMajors(majorPage + 1);
                setMajors((currentMajors) => [...currentMajors, ...nextPage.content]);
                setMajorPage(nextPage.page);
            } catch {
                toast.error("Không thể tải thêm ngành học");
            } finally {
                setMajorLoading(false);
            }
        }, { rootMargin: "80px" });

        observer.observe(target);
        return () => observer.disconnect();
    }, [loadMoreMajors, majorLoading, majorOpen, majorPage, majorTotalPages]);

    useEffect(() => {
        const target = subjectLoadMoreRef.current;
        if (!target || !subjectOpen) return;

        const observer = new IntersectionObserver(async ([entry]) => {
            if (!entry.isIntersecting || subjectLoading || subjectPage >= subjectTotalPages - 1) return;

            setSubjectLoading(true);
            try {
                const nextPage = await loadMoreSubjects(subjectPage + 1, filters.majorId);
                setSubjects((currentSubjects) => [...currentSubjects, ...nextPage.content]);
                setSubjectPage(nextPage.page);
            } catch {
                toast.error("Không thể tải thêm môn học");
            } finally {
                setSubjectLoading(false);
            }
        }, { rootMargin: "80px" });

        observer.observe(target);
        return () => observer.disconnect();
    }, [filters.majorId, loadMoreSubjects, subjectLoading, subjectOpen, subjectPage, subjectTotalPages]);

    const updateBookResult = (result: IPageResponse<IBook>) => {
        setBooks(result.content);
        setBookPage(result.page);
        setBookTotalPages(result.totalPages);
        setBookTotalElements(result.totalElements);
    };

    const applyFilters = (nextFilters: IBookFilters) => {
        setFilters(nextFilters);
        startFiltering(async () => {
            try {
                updateBookResult(await loadBooks({ ...nextFilters, page: 0 }));
            } catch {
                toast.error("Không thể tải danh sách sách");
            }
        });
    };

    const handleMajorSelect = (major?: IMajor) => {
        const nextFilters = { ...filters, majorId: major?.id, subjectId: undefined };
        setSubjects([]);
        setSubjectPage(0);
        setMajorOpen(false);
        startFiltering(async () => {
            try {
                const [bookResult, subjectResult] = await Promise.all([
                    loadBooks({ ...nextFilters, page: 0 }),
                    loadMoreSubjects(0, major?.id)
                ]);
                setFilters(nextFilters);
                updateBookResult(bookResult);
                setSubjects(subjectResult.content);
                setSubjectPage(subjectResult.page);
            } catch {
                toast.error("Không thể áp dụng bộ lọc");
            }
        });
    };

    const handleBookPageChange = (nextPage: number) => {
        if (nextPage === bookPage || isFiltering) return;

        startFiltering(async () => {
            try {
                updateBookResult(await loadBooks({ ...filters, page: nextPage }));
                window.scrollTo({ top: 0, behavior: "smooth" });
            } catch {
                toast.error("Không thể tải trang sách");
            }
        });
    };

    return (
        <main className="container py-4 py-md-5">
            <div className="row">
                <div className="col-12 col-xl-10 col-xxl-9 mx-auto">
                    <div className="mb-3">
                        <h1 className="h4 mb-0 fw-semibold text-dark">Trao đổi sách</h1>
                    </div>

                    <Form onSubmit={(event) => { event.preventDefault(); applyFilters(filters); }} className="mb-3">
                        <InputGroup className="bg-white border rounded-pill shadow-sm px-3 py-2">
                            <InputGroup.Text className="border-0 bg-transparent p-0">
                                <i className="bi bi-search text-secondary" aria-hidden="true" />
                            </InputGroup.Text>
                            <Form.Control
                                type="search"
                                value={filters.kw}
                                placeholder="Tìm kiếm sách..."
                                aria-label="Tìm kiếm sách"
                                className="border-0 bg-transparent shadow-none px-3 py-0"
                                onChange={(event) => setFilters((current) => ({ ...current, kw: event.target.value }))}
                            />
                            <Button type="submit" variant="link" className="border-0 text-secondary text-decoration-none p-0" aria-label="Tìm kiếm sách" disabled={isFiltering}>
                                <i className="bi bi-search" aria-hidden="true" />
                            </Button>
                        </InputGroup>
                    </Form>

                    <div className="row g-2 mb-4">
                        <div className="col-12 col-md-6">
                            <Dropdown show={majorOpen} onToggle={setMajorOpen} className="w-100">
                                <Dropdown.Toggle variant="light" className="w-100 d-flex align-items-center justify-content-between border rounded-3 text-start">
                                    <span><i className="bi bi-mortarboard me-2 text-success" aria-hidden="true" />{selectedMajor?.name || "Tất cả ngành"}</span>
                                </Dropdown.Toggle>
                                <Dropdown.Menu className="w-100 overflow-auto p-1" style={{ maxHeight: "280px" }}>
                                    <Dropdown.Item active={!filters.majorId} onClick={() => handleMajorSelect()}>Tất cả ngành</Dropdown.Item>
                                    {majors.map((major) => (
                                        <Dropdown.Item key={major.id} active={major.id === filters.majorId} onClick={() => handleMajorSelect(major)}>
                                            {major.name}
                                        </Dropdown.Item>
                                    ))}
                                    <div ref={majorLoadMoreRef} className="d-flex justify-content-center p-2">
                                        {majorLoading && <Spinner animation="border" size="sm" variant="success" />}
                                        {!majorLoading && majorPage >= majorTotalPages - 1 && <small className="text-secondary">Đã tải hết ngành</small>}
                                    </div>
                                </Dropdown.Menu>
                            </Dropdown>
                        </div>
                        <div className="col-12 col-md-6">
                            <Dropdown show={subjectOpen} onToggle={setSubjectOpen} className="w-100">
                                <Dropdown.Toggle variant="light" className="w-100 d-flex align-items-center justify-content-between border rounded-3 text-start">
                                    <span><i className="bi bi-journal-text me-2 text-success" aria-hidden="true" />{selectedSubject?.name || "Tất cả môn học"}</span>
                                </Dropdown.Toggle>
                                <Dropdown.Menu className="w-100 overflow-auto p-1" style={{ maxHeight: "280px" }}>
                                    <Dropdown.Item active={!filters.subjectId} onClick={() => applyFilters({ ...filters, subjectId: undefined })}>Tất cả môn học</Dropdown.Item>
                                    {subjects.map((subject) => (
                                        <Dropdown.Item key={subject.id} active={subject.id === filters.subjectId} onClick={() => applyFilters({ ...filters, subjectId: subject.id })}>
                                            {subject.name}
                                        </Dropdown.Item>
                                    ))}
                                    <div ref={subjectLoadMoreRef} className="d-flex justify-content-center p-2">
                                        {subjectLoading && <Spinner animation="border" size="sm" variant="success" />}
                                        {!subjectLoading && subjectPage >= subjectTotalPages - 1 && <small className="text-secondary">Đã tải hết môn học</small>}
                                    </div>
                                </Dropdown.Menu>
                            </Dropdown>
                        </div>
                    </div>

                    <div className="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
                        <span className="text-secondary">{bookTotalElements} sách được tìm thấy</span>
                        <small className="text-secondary">Trang {bookTotalPages ? bookPage + 1 : 0} / {bookTotalPages}</small>
                    </div>

                    {isFiltering && books.length === 0 ? (
                        <div className="d-flex justify-content-center py-5"><Spinner animation="border" variant="success" /></div>
                    ) : books.length > 0 ? (
                        <div className="row g-3">
                            {books.map((book) => (
                                <div className="col-12 col-sm-6 col-lg-4" key={book.id}>
                                    <Link href={`/book-exchange/${book.id}`} className="d-block h-100 text-decoration-none text-reset">
                                        <Card className="h-100 border rounded-4 shadow-sm overflow-hidden">
                                            <div className="ratio ratio-4x3 position-relative overflow-hidden bg-light">
                                                <Card.Img src={book.image?.imageUrl || "/file.svg"} alt={book.name} className="card-img-top w-100 h-100 rounded-0 object-fit-cover" />
                                            </div>
                                            <Card.Body className="d-flex flex-column p-3">
                                                <div className="d-flex align-items-center justify-content-between gap-2 mb-2">
                                                    <span className="badge text-bg-light">{conditionLabel(book.condition)}</span>
                                                </div>
                                                <Card.Title className="h6 mb-3 fw-semibold text-dark">{book.name}</Card.Title>
                                                <div className="d-flex align-items-center justify-content-between gap-2 mt-auto">
                                                    <span className="fw-bold text-success">{formatPrice(book.price)}</span>
                                                    <span className="small text-secondary">
                                                        Xem chi tiết <i className="bi bi-arrow-up-right ms-1" aria-hidden="true" />
                                                    </span>
                                                </div>
                                            </Card.Body>
                                        </Card>
                                    </Link>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <Alert variant="light" className="border rounded-4 text-center text-secondary py-5">
                            <i className="bi bi-book fs-2 d-block mb-3" aria-hidden="true" />
                            Không tìm thấy sách phù hợp.
                        </Alert>
                    )}

                    {bookTotalPages > 1 && (
                        <div className="d-flex justify-content-center mt-4">
                            <Pagination className="mb-0" size="sm">
                                <Pagination.Prev onClick={() => handleBookPageChange(bookPage - 1)} disabled={bookPage === 0 || isFiltering} />
                                {Array.from({ length: bookTotalPages }, (_, pageNumber) => (
                                    <Pagination.Item key={pageNumber} active={pageNumber === bookPage} onClick={() => handleBookPageChange(pageNumber)} disabled={isFiltering}>
                                        {pageNumber + 1}
                                    </Pagination.Item>
                                ))}
                                <Pagination.Next onClick={() => handleBookPageChange(bookPage + 1)} disabled={bookPage >= bookTotalPages - 1 || isFiltering} />
                            </Pagination>
                        </div>
                    )}
                </div>
            </div>
        </main>
    );
}

export default BookPage;
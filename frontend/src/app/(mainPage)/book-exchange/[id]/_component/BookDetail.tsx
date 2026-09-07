"use client";

import Link from "next/link";
import { useState } from "react";
import { Badge, Card, Image } from "react-bootstrap";

interface IProps {
    book: IBookDetail
}

const formatPrice = (price: number) => new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND"
}).format(price);

const conditionLabel = (condition: string) => {
    if (condition === "NEW") return "Mới";
    if (condition === "LIKE_NEW") return "Gần như mới";
    return "Đã qua sử dụng";
};

const BookDetail = (props: IProps) => {
    const { book } = props;
    const [activeImageIndex, setActiveImageIndex] = useState(0);
    const activeImage = book.images[activeImageIndex] || book.images[0];

    return(
        <main className="container py-4 py-md-5">
            <div className="row">
                <div className="col-12 col-xl-10 col-xxl-9 mx-auto">
                    <Link href="/book-exchange" className="d-inline-flex align-items-center gap-2 mb-4 text-decoration-none text-secondary fw-semibold small">
                        <span aria-hidden="true">←</span>
                        <span>Quay lại trao đổi sách</span>
                    </Link>

                    <div className="row g-4 align-items-start">
                        <div className="col-12 col-lg-7">
                            <Card className="border-0 rounded-4 shadow-sm overflow-hidden">
                                <div className="ratio ratio-4x3 bg-light">
                                    <Image
                                        src={activeImage?.imageUrl || "/file.svg"}
                                        alt={book.name}
                                        className="w-100 h-100 rounded-0 object-fit-contain"
                                    />
                                </div>
                            </Card>

                            {book.images.length > 1 && (
                                <div className="d-flex gap-2 mt-3 overflow-auto pb-1" aria-label="Ảnh của sách">
                                    {book.images.map((image, index) => (
                                        <button
                                            key={image.id}
                                            type="button"
                                            className={`btn flex-shrink-0 border rounded-3 p-1 ${index === activeImageIndex ? "border-primary" : "border-light-subtle"}`}
                                            onClick={() => setActiveImageIndex(index)}
                                            aria-label={`Xem ảnh ${index + 1}`}
                                            aria-pressed={index === activeImageIndex}
                                        >
                                            <Image src={image.imageUrl} alt="" width={72} height={72} className="rounded-2 object-fit-cover" />
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>

                        <div className="col-12 col-lg-5">
                            <Card className="border-0 rounded-4 shadow-sm">
                                <Card.Body className="p-4 p-md-5">
                                    <Badge bg="light" text="dark" className="border mb-3">
                                        {conditionLabel(book.condition)}
                                    </Badge>
                                    <h1 className="h3 fw-bold text-dark mb-3">{book.name}</h1>
                                    <div className="fs-3 fw-bold text-success mb-4">{formatPrice(book.price)}</div>

                                    <div className="border-top pt-4 mb-4">
                                        <h2 className="h6 fw-bold text-dark mb-2">Mô tả</h2>
                                        <p className="mb-0 text-secondary lh-lg text-break">
                                            {book.description || "Chưa có mô tả cho sách này."}
                                        </p>
                                    </div>

                                    <div className="border-top pt-4">
                                        <h2 className="h6 fw-bold text-dark mb-3">Môn học liên quan</h2>
                                        <div className="d-flex flex-wrap gap-2">
                                            {book.subjects.length > 0 ? book.subjects.map((subject) => (
                                                <Badge key={subject.id} bg="success" className="fw-normal px-3 py-2">
                                                    {subject.name}
                                                </Badge>
                                            )) : (
                                                <span className="small text-secondary">Chưa cập nhật môn học.</span>
                                            )}
                                        </div>
                                    </div>
                                </Card.Body>
                            </Card>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    );
}

export default BookDetail;
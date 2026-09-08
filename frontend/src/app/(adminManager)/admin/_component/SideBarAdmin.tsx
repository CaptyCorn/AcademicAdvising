"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useState } from "react";

interface IAdminLink {
    href: string,
    label: string,
    icon: string
}

const dataLinks: IAdminLink[] = [
    { href: "/admin/majorManagement", label: "Quản lí ngành học", icon: "bi-diagram-3" },
    { href: "/admin/subjectManagement", label: "Quản lí môn học", icon: "bi-journal-text" },
    { href: "/admin/bookManagement", label: "Quản lí sách", icon: "bi-book" },
    { href: "/admin/userManagement", label: "Quản lí người dùng", icon: "bi-people" },
    // { href: "/admin/ai-documents", label: "Quản lí tài liệu AI", icon: "bi-file-earmark-text" }
];

const SideBarAdmin = () => {
    const pathname = usePathname();
    const [isDataOpen, setIsDataOpen] = useState(true);
    const isActive = (href: string) => href === "/admin" ? pathname === href : pathname.startsWith(href);

    return (
        <aside className="d-flex flex-column flex-shrink-0 border-end bg-white p-3" style={{ width: "280px", minHeight: "100vh" }}>
            <Link href="/admin" className="d-flex align-items-center gap-3 px-2 mb-4 text-decoration-none text-dark">
                <span className="d-flex align-items-center justify-content-center rounded-3 bg-primary text-white" style={{ width: "42px", height: "42px" }}>
                    <i className="bi bi-speedometer2 fs-5" aria-hidden="true" />
                </span>
                <span>
                    <strong className="d-block">Academic Advising</strong>
                    <small className="text-secondary">Khu vực quản trị</small>
                </span>
            </Link>

            <Link href="/" className="nav-link d-flex align-items-center gap-3 rounded-3 px-3 py-2 mb-3 text-secondary">
                <i className="bi bi-arrow-left" aria-hidden="true" />
                <span>Về trang chủ</span>
            </Link>

            <nav className="nav flex-column gap-1" aria-label="Điều hướng quản trị">
                <Link href="/admin" className={`nav-link d-flex align-items-center gap-3 rounded-3 px-3 py-2 ${isActive("/admin") ? "active bg-primary text-white" : "text-dark"}`}>
                    <i className="bi bi-grid-1x2-fill" aria-hidden="true" />
                    <span>Tổng quan</span>
                </Link>

                <button
                    type="button"
                    className="btn btn-light d-flex align-items-center justify-content-between gap-3 mt-3 px-3 py-2 text-start"
                    onClick={() => setIsDataOpen((current) => !current)}
                    aria-expanded={isDataOpen}
                >
                    <span className="d-flex align-items-center gap-3">
                        <i className="bi bi-database" aria-hidden="true" />
                        <span>Quản lí dữ liệu</span>
                    </span>
                    <i className={`bi bi-chevron-${isDataOpen ? "up" : "down"}`} aria-hidden="true" />
                </button>

                {isDataOpen && (
                    <div className="d-flex flex-column gap-1 border-start ms-3 ps-2">
                        {dataLinks.map((item) => (
                            <Link
                                key={item.href}
                                href={item.href}
                                className={`nav-link d-flex align-items-center gap-3 rounded-3 px-3 py-2 small ${isActive(item.href) ? "active bg-primary text-white" : "text-secondary"}`}
                            >
                                <i className={`bi ${item.icon}`} aria-hidden="true" />
                                <span>{item.label}</span>
                            </Link>
                        ))}
                    </div>
                )}

                <Link href="/admin/reports" className={`nav-link d-flex align-items-center gap-3 rounded-3 px-3 py-2 mt-3 ${isActive("/admin/reports") ? "active bg-primary text-white" : "text-dark"}`}>
                    <i className="bi bi-flag" aria-hidden="true" />
                    <span>Báo cáo bài đăng</span>
                </Link>
            </nav>

            <div className="mt-auto border-top pt-3 small text-secondary">
                <i className="bi bi-shield-lock me-2" aria-hidden="true" />
                Quyền quản trị viên
            </div>
        </aside>
    );
}

export default SideBarAdmin;
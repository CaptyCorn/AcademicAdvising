'use client';

import { LogoutAction } from "@/actions/auth.action";
import { AuthContext } from "@/app/_context/AuthContext";
import Image from "next/image";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { use, useEffect, useState, useTransition } from "react";
import { Image as BootstrapImage } from "react-bootstrap";
import styles from "./SideBar.module.css";

type IconName = "home" | "plus" | "search" | "calendar" | "chat" | "heart" | "user" | "chart" | "bookmark" | "book" | "store" | "more";

const mainItems: { href: string; label: string; icon: IconName }[] = [
	{ href: "/", label: "Dành cho bạn", icon: "home" },
	{ href: "/new-thread", label: "Bài đăng mới", icon: "plus" },
	{ href: "/search", label: "Tìm kiếm", icon: "search" },
	// { href: "/appointments", label: "Lịch tư vấn", icon: "calendar" },
	{ href: "/consultations", label: "Tin nhắn", icon: "chat" },
	{ href: "/activity", label: "Hoạt động", icon: "heart" },
	{ href: "/profile", label: "Trang cá nhân", icon: "user" },
	// { href: "/academic-results", label: "Thông tin chi tiết", icon: "chart" },
	// { href: "/saved", label: "Đã lưu", icon: "bookmark" },
];

const Icon = ({ name }: { name: IconName }) => {
	const paths = {
		home: <><path d="m3 10 9-7 9 7v10a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2Z" /><path d="M9 22v-7h6v7" /></>,
		plus: <><path d="M12 5v14M5 12h14" /></>,
		search: <><circle cx="10.8" cy="10.8" r="7.4" /><path d="m16.5 16.5 5 5" /></>,
		calendar: <><rect x="3" y="5" width="18" height="16" rx="2" /><path d="M8 3v4M16 3v4M3 11h18" /></>,
		chat: <><path d="m3 5 9 7 9-7" /><path d="M4 4h16a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V5a1 1 0 0 1 1-1Z" /></>,
		heart: <path d="M20.8 8.8c0 5.5-8.8 10.4-8.8 10.4S3.2 14.3 3.2 8.8A4.6 4.6 0 0 1 12 6.6a4.6 4.6 0 0 1 8.8 2.2Z" />,
		user: <><circle cx="12" cy="7" r="4" /><path d="M4 21a8 8 0 0 1 16 0" /></>,
		chart: <><rect x="3" y="3" width="18" height="18" rx="4" /><path d="M8 16v-5M12 16V8M16 16v-3" /></>,
		bookmark: <path d="M6 4a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v18l-6-4-6 4Z" />,
		book: <><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" /><path d="M6.5 2H20v19H6.5A2.5 2.5 0 0 1 4 18.5v-14A2.5 2.5 0 0 1 6.5 2Z" /></>,
		store: <><path d="M3 10h18" /><path d="M5 10v10h14V10" /><path d="M4 4h16l2 6H2l2-6Z" /><path d="M9 20v-6h6v6" /></>,
		more: <path d="M4 6h16M4 12h16M4 18h16" />,
	};

	return <svg viewBox="0 0 24 24" className={styles.icon} aria-hidden="true">{paths[name]}</svg>;
};

const SideBar = () => {
	const pathname = usePathname();
	const router = useRouter();
	const { user } = use(AuthContext);
	const [isCollapsed, setIsCollapsed] = useState(false);
	const [isMoreOpen, setIsMoreOpen] = useState(false);
	const [isLoggingOut, startLogout] = useTransition();
	const isActive = (href: string) => href === "/" ? pathname === "/" : pathname.startsWith(href);

	useEffect(() => {
		const mediaQuery = window.matchMedia("(max-width: 991.98px)");
		const updateCollapsed = () => setIsCollapsed(mediaQuery.matches);
		updateCollapsed();
		mediaQuery.addEventListener("change", updateCollapsed);
		return () => mediaQuery.removeEventListener("change", updateCollapsed);
	}, []);

	const handleLogout = () => {
		startLogout(async () => {
			await LogoutAction();
			router.push("/login");
		});
	};

	return (
		<aside className={`d-flex flex-column flex-shrink-0 min-vh-100 p-3 bg-white border-end ${styles.sidebar} ${isCollapsed ? styles.collapsed : ""}`}>
			<Link href="/" className="d-flex align-items-center gap-2 px-2 mb-3 text-dark text-decoration-none">
				<span className={`d-flex align-items-center justify-content-center overflow-hidden rounded-circle ${styles.logoWrap}`}>
					<Image src="/Logo_DH_Mở_TPHCM.png" alt="" width={42} height={42} priority />
				</span>
				<span className={`fw-bold ${styles.brandText}`}>Academic Advising</span>
			</Link>

			<nav className="nav flex-column gap-1 flex-grow-1" aria-label="Điều hướng chính">
				{mainItems.map((item) => (
					<Link key={item.href} href={item.href} title={isCollapsed ? item.label : undefined} className={`nav-link d-flex align-items-center gap-3 px-3 py-2 rounded-3 text-dark ${styles.navItem} ${item.label === "Tin nhắn" ? "mt-4" : ""} ${isActive(item.href) ? "bg-light fw-semibold" : ""}`}>
						<Icon name={item.icon} />
						<span className={styles.navLabel}>{item.label}</span>
					</Link>
				))}

				<div className="border-top my-3" />
				{/* <div className="d-flex justify-content-between px-3 mb-2 small text-secondary">
					<span className={styles.sectionLabel}>Bảng feed</span>
					<span className={styles.sectionLabel}>Chỉnh sửa</span>
				</div> */}
					{/* <Link href="/following" className={`nav-link px-3 py-2 rounded-3 text-dark ${styles.navItem}`}><span className={styles.navLabel}>Đang theo dõi</span></Link> */}
					<Link href="/self-posts" className={`nav-link d-flex align-items-center gap-3 px-3 py-2 rounded-3 text-dark ${styles.navItem}`}>
						<Icon name="book" />
						<span className={styles.navLabel}>Bài viết tự tạo</span>
					</Link>
					<Link href="/book-exchange" className={`nav-link d-flex align-items-center gap-3 px-3 py-2 rounded-3 text-dark ${styles.navItem}`}>
						<Icon name="store" />
						<span className={styles.navLabel}>Trao đổi sách</span>
					</Link>
                <div className="border-top my-3" />
                
			</nav>

			<div className="d-flex align-items-center gap-2 p-2 mb-2">
				<BootstrapImage src={user?.avatar || "/file.svg"} alt="Ảnh đại diện" width={38} height={38} roundedCircle className={styles.avatar} />
				<div className={`d-flex flex-column text-truncate ${styles.userInfo}`}>
					<strong>{user?.username || "Tài khoản"}</strong>
					<span>{user?.email || "Sinh viên"}</span>
				</div>
			</div>

			<button type="button" className={`btn text-start d-flex align-items-center gap-3 px-3 py-2 rounded-3 text-dark ${styles.moreButton}`} onClick={() => setIsMoreOpen((current) => !current)} aria-expanded={isMoreOpen}>
				<Icon name="more" />
				<span className={styles.moreLabel}>Xem thêm</span>
			</button>

			{isMoreOpen && (
				<div className={`bg-white border rounded-4 shadow-lg p-2 ${styles.moreMenu}`}>
					<button type="button" className="btn btn-link w-100 text-start text-danger text-decoration-none px-3 py-2" disabled={isLoggingOut} onClick={handleLogout}>
						{isLoggingOut ? "Đang đăng xuất..." : "Đăng xuất"}
					</button>
				</div>
			)}
		</aside>
	);
};

export default SideBar;

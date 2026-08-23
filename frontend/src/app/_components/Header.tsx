'use client'
import { LogoutAction } from "@/actions/auth.action";
import { AuthContext } from "@/app/_context/AuthContext";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { use, useTransition } from "react";
import { Button, Container, Dropdown, Image as BootstrapImage, Nav, Navbar } from "react-bootstrap";
import styles from "./Header.module.css";

const Header = () => {
    const { user, isAuthenticated, isLoading } = use(AuthContext);
    const router = useRouter();
    const [isPending, startTransition] = useTransition();

    const handleLogout = () => {
        startTransition(async () => {
            await LogoutAction();
            router.push("/login");
        });
    };

    return (
        <Navbar expand="lg" className={`border-bottom bg-white ${styles.navbar}`}>
            <Container>
                <Navbar.Brand as={Link} href="/" className="d-flex align-items-center gap-2">
                    <span className={styles.logoBox}>
                        <Image
                            src="/Logo_DH_Mở_TPHCM.png"
                            alt="Logo Trường Đại học Mở Thành phố Hồ Chí Minh"
                            width={42}
                            height={42}
                        />
                    </span>
                    <span className={styles.brandName}>Academic Advising</span>
                </Navbar.Brand>

                <Navbar.Toggle aria-controls="main-navigation" />
                <Navbar.Collapse id="main-navigation">
                    <Nav className="mx-auto my-3 my-lg-0 gap-lg-2">
                        <Nav.Link as={Link} href="/" className={styles.navLink}>Trang chủ</Nav.Link>
                        <Nav.Link as={Link} href="/book-exchange" className={styles.navLink}>Trao đổi sách</Nav.Link>
                        <Nav.Link as={Link} href="/notifications" className={styles.navLink}>Thông báo</Nav.Link>
                    </Nav>

                    <div className="d-flex align-items-center">
                        {isLoading ? (
                            <span className="text-secondary small">Đang tải...</span>
                        ) : isAuthenticated ? (
                            <Dropdown align="end">
                                <Dropdown.Toggle variant="light" className={`d-flex align-items-center gap-2 border-0 ${styles.userToggle}`}>
                                    <BootstrapImage
                                        src={user?.avatar || "/file.svg"}
                                        alt="Ảnh đại diện người dùng"
                                        roundedCircle
                                        className={styles.avatar}
                                    />
                                    <span className="d-none d-sm-inline small fw-semibold">{user?.username || "Tài khoản"}</span>
                                </Dropdown.Toggle>

                                <Dropdown.Menu className="shadow-sm border-0 mt-2">
                                    <Dropdown.Header>{user?.username || "Tài khoản"}</Dropdown.Header>
                                    <Dropdown.Item onClick={() => router.push("/profile")}>
                                        Thông tin cá nhân
                                    </Dropdown.Item>
                                    <Dropdown.Divider />
                                    <Dropdown.Item
                                        as={Button}
                                        variant="link"
                                        className="text-danger text-start w-100 text-decoration-none"
                                        disabled={isPending}
                                        onClick={handleLogout}
                                    >
                                        {isPending ? "Đang đăng xuất..." : "Đăng xuất"}
                                    </Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        ) : (
                            <Link href="/login" className="btn btn-primary btn-sm">Đăng nhập</Link>
                        )}
                    </div>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default Header;
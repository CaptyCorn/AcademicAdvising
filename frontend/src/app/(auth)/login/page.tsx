'use client'

import { LoginAction } from "@/actions/auth.action";
import { useActionState, useEffect } from "react";
import { Button, Form } from "react-bootstrap";
import styles from "./page.module.css";
import { useRouter } from "next/navigation";
import Image from "next/image";

const LoginPage = () => {
    const router = useRouter();
    const [state, action, pending] = useActionState(
        LoginAction,
        { success: false }
    );

    useEffect(() => {
        if (state.success) router.push("/");
    }, [router, state.success]);

    return (
        <main className="container-fluid min-vh-100 p-0">
            <div className="row g-0 min-vh-100">
                <section className={`col-md-5 d-none d-md-flex flex-column justify-content-center p-5 ${styles.brandPanel}`} aria-label="Academic Advising">
                    <div className={styles.brandLogo}>
                        <Image
                            src="/Logo_DH_Mở_TPHCM.png"
                            alt="Logo Trường Đại học Mở Thành phố Hồ Chí Minh"
                            width={120}
                            height={120}
                            priority
                        />
                    </div>
                    <p className="text-uppercase fw-bold small mt-4 mb-3">Academic Advising</p>
                    <h1 className="display-4 fw-normal">Đồng hành cùng hành trình học tập.</h1>
                    <p className="lead mt-3 mb-0">
                    Không gian tập trung cho sinh viên, cố vấn và những quyết định học tập sáng suốt.
                    </p>
                    <hr className="w-25 my-5" />
                    <p className="small mb-0">Hệ thống tư vấn học vụ</p>
                </section>

                <section className="col-md-7 d-flex align-items-center justify-content-center p-4 p-md-5 bg-light">
                    <div className="w-100" style={{ maxWidth: 430 }}>
                        <div className={`d-md-none mb-4 ${styles.mobileLogo}`}>
                            <Image
                                src="/Logo_DH_Mở_TPHCM.png"
                                alt="Logo Trường Đại học Mở Thành phố Hồ Chí Minh"
                                width={100}
                                height={100}
                                priority
                            />
                        </div>
                        <p className={`text-uppercase fw-bold small mb-2 ${styles.formEyebrow}`}>Chào mừng trở lại</p>
                        <h2 className="display-5 fw-normal">Đăng nhập</h2>
                        <p className="text-secondary mb-5">Truy cập hồ sơ và kế hoạch học tập của bạn.</p>

                        <Form action={action}>
                            <Form.Group className="mb-4" controlId="loginUsername">
                                <Form.Label className="small fw-semibold">Tên đăng nhập</Form.Label>
                                <Form.Control
                                    className="py-3"
                                    type="text"
                                    name="username"
                                    placeholder="Nhập tên đăng nhập"
                                    autoComplete="username"
                                    required
                                />
                            </Form.Group>

                            <Form.Group className="mb-4" controlId="loginPassword">
                                <div className="d-flex justify-content-between align-items-center">
                                    <Form.Label className="small fw-semibold">Mật khẩu</Form.Label>
                                    <a href="#forgot-password" className={`small text-decoration-none ${styles.formEyebrow}`}>Quên mật khẩu?</a>
                                </div>
                                <Form.Control
                                    className="py-3"
                                    type="password"
                                    name="password"
                                    placeholder="Nhập mật khẩu"
                                    autoComplete="current-password"
                                    required
                                />
                            </Form.Group>

                            <Form.Check
                                type="checkbox"
                                name="remember"
                                label="Ghi nhớ đăng nhập"
                                className="mb-4 text-secondary small"
                            />

                            <Button disabled={pending} variant="primary" type="submit" className="w-100 py-3 fw-semibold">
                                {pending ? "Đang đăng nhập..." : "Đăng nhập"}
                                {!pending && <span className="ms-2" aria-hidden="true">→</span>}
                            </Button>
                        </Form>

                        <p className="text-secondary text-center small mt-4 mb-0">Cần hỗ trợ? Liên hệ phòng công tác sinh viên.</p>
                    </div>
                </section>
            </div>
        </main>
    );
}

export default LoginPage;
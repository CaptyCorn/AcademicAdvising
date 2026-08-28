'use client'
import { RegisterAction } from "@/actions/auth.action";
import { useActionState, useEffect } from "react";
import Image from "next/image";
import Link from "next/link";
import { Button, Form } from "react-bootstrap";
import styles from "./page.module.css";
import { useRouter } from "next/navigation";

const userInfo = [{
    field: "firstName",
    label: "Tên",
    type: "text"
}, {
    field: "lastName",
    label: "Họ và tên lót",
    type: "text"
}, {
    field: "email",
    label: "Email",
    type: "email"
}, {
    field: "studentCode",
    label: "Mã số sinh viên",
    type: "number"
}, {
    field: "username",
    label: "Tên đăng nhập",
    type: "text"
}, {
    field: "password",
    label: "Mật khẩu",
    type: "password"
}, {
    field: "confirm",
    label: "Xác nhận mật khẩu",
    type: "password"
}] as const;

const RegisterPage = () => {
    const router = useRouter();
    const [state, action, pending] = useActionState(
        RegisterAction,
        { errors: {}, values: {} }
    );

    useEffect(() => {
        if (!state.success) return;

        const redirectTimer = window.setTimeout(() => router.push("/login"), 900);
        return () => window.clearTimeout(redirectTimer);
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
                    <h1 className="display-4 fw-normal">Bắt đầu hành trình học tập của bạn.</h1>
                    <p className="lead mt-3 mb-0">
                        Tạo tài khoản để kết nối với hệ thống tư vấn học vụ và quản lý kế hoạch học tập.
                    </p>
                    <hr className="w-25 my-5" />
                    <p className="small mb-0">Hệ thống tư vấn học vụ</p>
                </section>

                <section className="col-md-7 d-flex align-items-center justify-content-center p-4 p-md-5 bg-light">
                    <div className="w-100" style={{ maxWidth: 620 }}>
                        <div className={`d-md-none mb-4 ${styles.mobileLogo}`}>
                            <Image
                                src="/Logo_DH_Mở_TPHCM.png"
                                alt="Logo Trường Đại học Mở Thành phố Hồ Chí Minh"
                                width={100}
                                height={100}
                                priority
                            />
                        </div>

                        <div className="d-flex justify-content-between align-items-start gap-3 mb-4">
                            <div>
                                <p className={`text-uppercase fw-bold small mb-2 ${styles.formEyebrow}`}>TẠO TÀI KHOẢN</p>
                                <h2 className="display-5 fw-normal mb-2">Đăng ký</h2>
                                <p className="text-secondary mb-0">Điền thông tin để tham gia hệ thống.</p>
                            </div>
                            <Link href="/login" className={`small text-decoration-none ${styles.formEyebrow}`}>
                                Đăng nhập
                            </Link>
                        </div>

                        <Form action={action} className="mt-4">
                            {state.message && (
                                <div
                                    className={`alert ${state.success ? "alert-success" : "alert-danger"} small`}
                                    role="alert"
                                >
                                    {state.message}
                                </div>
                            )}

                            <div className="row g-3">
                                {userInfo.map((u) => (
                                    <Form.Group
                                        key={u.field}
                                        className={u.field === "firstName" || u.field === "lastName" ? "col-md-6" : "col-12"}
                                        controlId={u.field}
                                    >
                                        <Form.Label className="small fw-semibold">{u.label}</Form.Label>
                                        <Form.Control
                                            className="py-3"
                                            type={u.type}
                                            name={u.field}
                                            defaultValue={state?.values?.[u.field]}
                                            placeholder={`Nhập ${u.label.toLowerCase()}`}
                                            autoComplete={u.field === "password" || u.field === "confirm" ? "new-password" : u.field}
                                            required
                                        />
                                        {state?.errors?.[u.field]?.map((error) => (
                                            <Form.Text key={error} className="d-block text-danger mt-1">
                                                {error}
                                            </Form.Text>
                                        ))}
                                    </Form.Group>
                                ))}
                            </div>

                            <Form.Check
                                type="checkbox"
                                name="terms"
                                label="Tôi đồng ý với các điều khoản sử dụng."
                                className="mt-4 mb-4 text-secondary small"
                                required
                            />

                            <Button disabled={pending} variant="primary" type="submit" className="w-100 py-3 fw-semibold">
                                {pending ? "Đang đăng ký..." : "Đăng ký tài khoản"}
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

export default RegisterPage;
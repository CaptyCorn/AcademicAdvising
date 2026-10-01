'use client';

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Button, Card, Form, Image, Spinner } from "react-bootstrap";
import { toast } from "react-toastify";

interface IProps {
    profile: IUserProfile;
    updateProfile: (values: { firstName: string; lastName: string; phone: string; file: File | null }) => Promise<{ success: boolean; message: string }>;
}

const MyProfile = ({ profile, updateProfile }: IProps) => {
    const [editing, setEditing] = useState(false);
    const [saving, setSaving] = useState(false);
    const [avatarFile, setAvatarFile] = useState<File | null>(null);
    const [avatarPreview, setAvatarPreview] = useState(profile.avatar || "/file.svg");
    const router = useRouter();
    const [form, setForm] = useState({
        firstName: profile.firstName ?? "",
        lastName: profile.lastName ?? "",
        phone: profile.phone ?? ""
    });

    const resetForm = () => setForm({
        firstName: profile.firstName ?? "",
        lastName: profile.lastName ?? "",
        phone: profile.phone ?? ""
    });

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setSaving(true);
        try {
            const result = await updateProfile({ ...form, file: avatarFile });
            if (result.success) {
                toast.success(result.message);
                setEditing(false);
                setAvatarFile(null);
                router.refresh();
            } else toast.error(result.message);
        } catch {
            toast.error("Không thể cập nhật thông tin cá nhân");
        } finally {
            setSaving(false);
        }
    };

    return (
        <Card className="border-0 rounded-4 shadow-sm h-100">
            <Card.Body className="p-4">
                <div className="text-center mb-4">
                    <Image src={avatarPreview} alt="Ảnh đại diện" width={96} height={96} roundedCircle className="border object-fit-cover" />
                    <h1 className="h5 fw-bold mt-3 mb-1">{profile.firstName} {profile.lastName}</h1>
                    <p className="text-secondary mb-2">@{profile.username}</p>
                    {profile.role && <span className="badge text-bg-light border">{profile.role}</span>}
                </div>
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h2 className="h6 fw-bold mb-0">Thông tin cá nhân</h2>
                    {!editing && <Button variant="outline-primary" size="sm" onClick={() => setEditing(true)}>Chỉnh sửa</Button>}
                </div>
                <Form onSubmit={handleSubmit}>
                    <div className="row g-3">
                        <Form.Group className="col-6"><Form.Label className="small text-secondary">Tên</Form.Label><Form.Control value={form.firstName} disabled={!editing} onChange={(e) => setForm({ ...form, firstName: e.target.value })} /></Form.Group>
                        <Form.Group className="col-6"><Form.Label className="small text-secondary">Họ</Form.Label><Form.Control value={form.lastName} disabled={!editing} onChange={(e) => setForm({ ...form, lastName: e.target.value })} /></Form.Group>
                        <Form.Group className="col-12"><Form.Label className="small text-secondary">Tên đăng nhập</Form.Label><Form.Control value={profile.username ?? ""} disabled /></Form.Group>
                        <Form.Group className="col-12"><Form.Label className="small text-secondary">Email</Form.Label><Form.Control type="email" value={profile.email ?? ""} disabled /></Form.Group>
                        <Form.Group className="col-12"><Form.Label className="small text-secondary">Mã sinh viên</Form.Label><Form.Control value={profile.studentCode ?? ""} disabled /></Form.Group>
                        <Form.Group className="col-12"><Form.Label className="small text-secondary">Số điện thoại</Form.Label><Form.Control value={form.phone} disabled={!editing} onChange={(e) => setForm({ ...form, phone: e.target.value })} /></Form.Group>
                        {editing && <Form.Group className="col-12"><Form.Label className="small text-secondary">Ảnh đại diện</Form.Label><Form.Control type="file" accept="image/*" onChange={(e) => { const file = (e.currentTarget as HTMLInputElement).files?.[0] ?? null; setAvatarFile(file); if (file) setAvatarPreview(URL.createObjectURL(file)); }} /></Form.Group>}
                    </div>
                    {editing && <div className="d-flex justify-content-end gap-2 mt-4"><Button variant="light" onClick={() => { resetForm(); setAvatarFile(null); setAvatarPreview(profile.avatar || "/file.svg"); setEditing(false); }}>Hủy</Button><Button type="submit" variant="primary" disabled={saving}>{saving ? <Spinner size="sm" /> : "Lưu thay đổi"}</Button></div>}
                </Form>
            </Card.Body>
        </Card>
    );
};

export default MyProfile;

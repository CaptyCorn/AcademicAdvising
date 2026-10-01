"use server";

import { FormState, RegisterFormSchema, RegisterValues } from "@/config/definitions";
import { requestLogin, requestRegister, resquestProfile } from "@/services/auth.service";
import { cookies } from "next/headers";
import z from "zod";

export const LoginAction = async (
    preState: {
        success: boolean,
        message: string
    },
    formData: FormData
) => {
    const { username, password } = Object.fromEntries(formData.entries());

    const responseInfo = await requestLogin({
        username: username as string,
        password: password as string
    });
    console.log(responseInfo.success)
    if (!responseInfo.success) {
        return {
            success: false,
            message: responseInfo.message
        }
    } else {
        const cookieStore = await cookies();
        cookieStore.set('token', responseInfo.data, {
            httpOnly: true
        });
        return {
            success: true,
            message: responseInfo.message
        }
    }
}

export const RegisterAction = async (
    state: FormState,
    formData: FormData
) => {
    const values: RegisterValues = {
        firstName: String(formData.get("firstName") ?? ""),
        lastName: String(formData.get("lastName") ?? ""),
        email: String(formData.get("email") ?? ""),
        studentCode: String(formData.get("studentCode") ?? ""),
        username: String(formData.get("username") ?? "")
    };

    const validatedFields = RegisterFormSchema.safeParse({
        firstName: formData.get("firstName"),
        lastName: formData.get("lastName"),
        email: formData.get('email'),
        studentCode: formData.get("studentCode"),
        username: formData.get("username"),
        password: formData.get('password'),
        confirm: formData.get("confirm")
    })

    if (!validatedFields.success) {
        return {
            errors: z.flattenError(validatedFields.error).fieldErrors,
            values
        }
    }

    const responseInfo = await requestRegister(formData);
    if (!responseInfo) {
        return {
            success: false,
            message: "Đăng ký không thành công. Vui lòng thử lại.",
            values
        };
    }

    return {
        success: true,
        message: responseInfo.message ?? "Đăng ký tài khoản thành công."
    };
}

export const getProfile = async () => {
    const token = (await cookies()).get("token")?.value;
    const responseInfo = await resquestProfile(token!);
    return {
        token,
        responseInfo
    };
}

export const LogoutAction = async () => {
    (await cookies()).delete("token");
};

export const updateProfile = async (values: {
    firstName: string;
    lastName: string;
    phone: string;
    file: File | null;
}) => {
    const token = (await cookies()).get("token")?.value;
    const formData = new FormData();
    formData.append("firstName", values.firstName);
    formData.append("lastName", values.lastName);
    formData.append("phone", values.phone);
    if (values.file) formData.append("file", values.file);

    const res = await fetch(`${process.env.BASE_URL}/profile`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`
        },
        body: formData
    });
    const responseInfo = await res.json().catch(() => null);
    if (!res.ok) return { success: false, message: responseInfo?.message ?? "Không thể cập nhật thông tin cá nhân" };
    return { success: true, message: responseInfo?.message ?? "Cập nhật thông tin thành công" };
};
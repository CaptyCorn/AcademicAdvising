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
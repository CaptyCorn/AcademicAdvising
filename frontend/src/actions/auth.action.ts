"use server";

import { requestLogin, requestRegister, resquestProfile } from "@/services/auth.service";
import { cookies } from "next/headers";

export const LoginAction = async (
    preState: {success: boolean}, 
    formData: FormData
) => {
    const { username, password } = Object.fromEntries(formData.entries());

    const responseInfo = await requestLogin({
        username: username as string,
        password: password as string
    });
    const token = responseInfo.data;
    if (!token) {
        return {
            success: false
        }
    } else {
        const cookieStore = await cookies();
        cookieStore.set('token', token, {
            httpOnly: true
        });
        return {
            success: true
        }
    }
}

export const RegisterAction = async (
    preState: {success: boolean},
    formData: FormData
) => {
    const responseInfo = await requestRegister(formData);
    return { success: Boolean(responseInfo) };
}

export const getProfile = async () => {
    const token = (await cookies()).get("token")?.value;
    const responseInfo = await resquestProfile(token!);
    return {
        token,
        responseInfo
    };
}
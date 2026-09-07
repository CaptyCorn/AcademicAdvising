"use server";

import { callAPI, endpoints } from "@/config/apis";
import { cookies } from "next/headers";

export const requestListMajor = async (page: number = 0): Promise<IPageResponse<IMajor>> => {
    const token = (await cookies()).get('token')?.value;
    const res = await fetch(`${callAPI(endpoints['listMajor'])}?page=${page}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });

    if (!res.ok) throw new Error('Không thể tải danh sách ngành học');

    const responseInfo = await res.json();
    return responseInfo.data;
};
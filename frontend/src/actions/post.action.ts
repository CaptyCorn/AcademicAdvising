'use server'

import { callAPI, endpoints } from "@/config/apis"
import { revalidateTag } from "next/cache"
import { cookies } from "next/headers"

export const requestPostCreate = async (content: string) => {
    const token = (await cookies()).get('token')?.value

    const res = await fetch(`${callAPI(endpoints['createPost'])}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({'content': content})
    })
    const responseInfo = await res.json();

    if(responseInfo.success) {
        revalidateTag('list-posts', 'max');
        return {
            success: true,
            message: responseInfo.message
        }
    } 
    return {
            success: false,
            message: responseInfo.message
        }
}

export const loadPosts = async (page: number) => {
    const token = (await cookies()).get('token')?.value;
    const res = await fetch(`${callAPI(endpoints['posts'])}?page=${page}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });

    if (!res.ok) throw new Error('Không thể tải bài đăng');

    const responseInfo = await res.json();
    return responseInfo.data;
};

export const searchPosts = async (keyword: string, page: number = 0) => {
    const token = (await cookies()).get('token')?.value;
    const query = new URLSearchParams({
        kw: keyword,
        page: String(page)
    });
    const res = await fetch(`${callAPI(endpoints['posts'])}?${query.toString()}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });

    if (!res.ok) throw new Error('Không thể tìm kiếm bài đăng');

    const responseInfo = await res.json();
    return responseInfo.data;
};
'use server'

import { callAPI, endpoints } from "@/config/apis"
import { cookies } from "next/headers"

export const requestCommentCreate = async (content: string, postId: string) => {
    const token = (await cookies()).get('token')?.value;
    const res = await fetch(`${callAPI(endpoints['createComment'](postId))}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            'content': content
        })
    })
    const responseInfo = await res.json();
    if (responseInfo.success) {
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

export const loadComments = async (postId: string, page: number) => {
    const token = (await cookies()).get('token')?.value;
    const res = await fetch(`${callAPI(endpoints['listComment'](postId))}?page=${page}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });

    if (!res.ok) throw new Error('Không thể tải bình luận');

    const responseInfo = await res.json();
    return responseInfo.data;
};
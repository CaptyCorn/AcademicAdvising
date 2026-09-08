"use server";

import { callAPI, endpoints } from "@/config/apis";
import { cookies } from "next/headers";

export const loadConversations = async (page: number = 0): Promise<IPageResponse<IConversation>> => {
    const token = (await cookies()).get("token")?.value;
    const res = await fetch(`${callAPI(endpoints["listConversation"])}?page=${page}`, {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });

    if (!res.ok) throw new Error("Không thể tải danh sách hội thoại");

    const responseInfo = await res.json();
    return responseInfo.data;
};

export const loadConversationMessages = async (conversationId: number, page: number = 0): Promise<IPageResponse<IMessage>> => {
    const token = (await cookies()).get("token")?.value;
    const res = await fetch(`${callAPI(endpoints["listMessage"](String(conversationId)))}?page=${page}`, {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });

    if (!res.ok) throw new Error("Không thể tải tin nhắn");

    const responseInfo = await res.json();
    return responseInfo.data;
};

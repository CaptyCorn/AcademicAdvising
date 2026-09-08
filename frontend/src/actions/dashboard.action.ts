"use server";

import { callAPI, endpoints } from "@/config/apis";
import { cookies } from "next/headers";

const adminHeaders = async () => ({
    "Content-Type": "application/json",
    "Authorization": `Bearer ${(await cookies()).get("token")?.value}`
});

export const loadDashboardStats = async (): Promise<IDashboardStats> => {
    const res = await fetch(`${callAPI(endpoints["adminDashboard"])}`, {
        headers: await adminHeaders()
    });

    if (!res.ok) throw new Error("Không thể tải số liệu tổng quan");

    const responseInfo = await res.json();
    return responseInfo.data;
};

export const loadMonthlyStats = async (): Promise<IMonthlyStats> => {
    const res = await fetch(`${callAPI(endpoints["adminRevenue"])}`, {
        headers: await adminHeaders()
    });

    if (!res.ok) throw new Error("Không thể tải số liệu theo tháng");

    const responseInfo = await res.json();
    return responseInfo.data;
};

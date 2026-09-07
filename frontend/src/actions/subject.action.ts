"use server";

import { callAPI, endpoints } from "@/config/apis";
import { cookies } from "next/headers";

export const requestListSubject = async (page: number = 0, majorId?: number): Promise<IPageResponse<ISubject>> => {
	const token = (await cookies()).get('token')?.value;
	const query = new URLSearchParams({ page: String(page) });
	if (majorId) query.set('majorId', String(majorId));

	const res = await fetch(`${callAPI(endpoints['listSubject'])}?${query.toString()}`, {
		headers: {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${token}`
		}
	});

	if (!res.ok) throw new Error('Không thể tải danh sách môn học');

	const responseInfo = await res.json();
	return responseInfo.data;
};

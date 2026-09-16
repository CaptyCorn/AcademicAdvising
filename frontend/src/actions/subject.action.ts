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

export const requestCreateSubject = async (data: { name: string, description: string, majorId: number }): Promise<{ success: boolean, message: string, data?: ISubject }> => {
	const token = (await cookies()).get('token')?.value;
	const res = await fetch(`${callAPI(endpoints['createSubject'])}`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${token}`
		},
		body: JSON.stringify({
			name: data.name.trim(),
			description: data.description.trim(),
			majorId: String(data.majorId)
		})
	});

	const responseInfo = await res.json();
	return {
		success: res.ok && responseInfo.success,
		message: responseInfo.message || "Không thể thêm môn học",
		data: responseInfo.data
	};
};

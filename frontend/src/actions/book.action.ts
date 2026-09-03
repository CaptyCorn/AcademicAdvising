"use server";

import { callAPI, endpoints } from "@/config/apis";
import { cookies } from "next/headers";

interface IBookQuery {
	page?: number,
	kw?: string,
	subjectId?: number,
	majorId?: number
}

export const requestListBook = async (params: IBookQuery = {}): Promise<IPageResponse<IBook>> => {
	const token = (await cookies()).get('token')?.value;
	const query = new URLSearchParams({ page: String(params.page ?? 0) });
	if (params.kw?.trim()) query.set('kw', params.kw.trim());
	if (params.subjectId) query.set('subjectId', String(params.subjectId));
	if (params.majorId) query.set('majorId', String(params.majorId));

	const res = await fetch(`${callAPI(endpoints['listbook'])}?${query.toString()}`, {
		headers: {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${token}`
		}
	});

	if (!res.ok) throw new Error('Không thể tải danh sách sách');

	const responseInfo = await res.json();
	return responseInfo.data;
};

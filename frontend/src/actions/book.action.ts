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

export const requestBookContact = async (bookId: number): Promise<number> => {
	const token = (await cookies()).get('token')?.value;
	const res = await fetch(`${callAPI(endpoints['contactBook'](String(bookId)))}`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${token}`
		}
	});

	if (!res.ok) throw new Error('Không thể tạo cuộc hội thoại với người bán');

	return await res.json();
};

export const requestListMyBooks = async (page: number = 0): Promise<IPageResponse<IBook>> => {
	const token = (await cookies()).get('token')?.value;
	const res = await fetch(`${callAPI(endpoints['bookUser'])}?page=${page}`, {
		headers: {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${token}`
		}
	});

	if (!res.ok) throw new Error('Không thể tải sách của bạn');

	const responseInfo = await res.json();
	return responseInfo.data;
};

export const requestCreateBook = async (formData: FormData): Promise<{ success: boolean, message: string, data?: IBook }> => {
	const token = (await cookies()).get('token')?.value;
	const res = await fetch(`${callAPI(endpoints['createBook'])}`, {
		method: 'POST',
		headers: {
			'Authorization': `Bearer ${token}`
		},
		body: formData
	});

	const responseInfo = await res.json().catch(() => ({}));
	return {
		success: res.ok && responseInfo.success === true,
		message: responseInfo.message || `Không thể thêm sách (${res.status})`,
		data: responseInfo.data
	};
};

export const requestUpdateBookStatus = async (bookId: number, status: string): Promise<{ success: boolean, message: string }> => {
	const token = (await cookies()).get('token')?.value;
	const res = await fetch(`${callAPI(endpoints['updateBookStatus'](String(bookId)))}`, {
		method: 'PATCH',
		headers: {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${token}`
		},
		body: JSON.stringify({ status })
	});

	const responseInfo = await res.json().catch(() => ({}));
	return {
		success: res.ok && responseInfo.success === true,
		message: responseInfo.message || `Không thể cập nhật trạng thái sách (${res.status})`
	};
};

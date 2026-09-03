import { callAPI, endpoints } from "@/config/apis";
import { cookies } from "next/headers";
import BookDetail from "./_component/BookDetail";

interface PageProps {
    params: Promise<{id: string}>
}

const BookDetailPage = async ({params}: PageProps) => {
    const token = (await cookies()).get('token')?.value
    const { id } = await params
    const res = await fetch(`${callAPI(endpoints['bookDetail'](id))}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })

    const responseInfo = await res.json();
    
    return(
        <BookDetail book={responseInfo.data}/>
    );
}

export default BookDetailPage;
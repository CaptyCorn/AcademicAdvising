import { callAPI, endpoints } from "@/config/apis";
import { cookies } from "next/headers";
import PostDetail from "./_component/PostDetail";

interface PageProps {
    params: Promise<{id: string}>
}

const PostDetailPage = async ({params}: PageProps) => {
    const token = (await cookies()).get('token')?.value
    const {id} = await params
    const resPost = await fetch(`${callAPI(endpoints['postDetail'](id))}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });
    const responsePostInfo = await resPost.json();
    
    const resComment = await fetch(`${callAPI(endpoints['listComment'](id))}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });
    const responseCommentInfo = await resComment.json();

    return(
        <PostDetail 
            post={responsePostInfo.data}
            comments={responseCommentInfo.data.content}
            page={responseCommentInfo.data.page}
            totalElements={responseCommentInfo.data.totalElements}
            totalPages={responseCommentInfo.data.totalPages}
        />
    );
}

export default PostDetailPage;

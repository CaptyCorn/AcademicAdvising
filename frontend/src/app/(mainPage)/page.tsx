import { callAPI, endpoints } from "@/config/apis";
import { loadPosts } from "@/actions/post.action";
import { cookies } from "next/headers";
import PostCommunity from "../_components/mainPage/PostCommunity";
import Header from "../_components/mainPage/Header";

const HomePage = async () => {
  const cookieStore = await cookies();
  const token = cookieStore.get('token')?.value
  const res = await fetch(`${callAPI(endpoints['posts'])}`, {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  })

  const responseInfo = await res.json();
  const data = responseInfo.data

  return (
    <>
      <Header />
      <PostCommunity
        posts={data.content}
        page={data.page}
        size={data.size}
        totalElements={data.totalElements}
        totalPages={data.totalPages}
        loadMore={loadPosts}

      />
    </>
  );
}

export default HomePage;

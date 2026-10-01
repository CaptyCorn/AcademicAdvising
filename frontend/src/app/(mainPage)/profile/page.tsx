import MyPost from "./_component/MyPost";
import MyProfile from "./_component/MyProfile";
import { getProfile, updateProfile } from "@/actions/auth.action";
import { loadMyPosts } from "@/actions/post.action";

const ProfilePage = async () => {
    const [{ responseInfo }, posts] = await Promise.all([getProfile(), loadMyPosts(0)]);
    const profile = responseInfo.data ?? responseInfo;

    return(
        <main className="container py-4 py-md-5">
            <div className="row g-4">
                <div className="col-12 col-lg-5 col-xl-4"><MyProfile profile={profile} updateProfile={updateProfile} /></div>
                <div className="col-12 col-lg-7 col-xl-8"><MyPost initialPosts={posts.content} initialPage={posts.page} totalElements={posts.totalElements} totalPages={posts.totalPages} loadMore={loadMyPosts} /></div>
            </div>
        </main>
    );
}

export default ProfilePage;
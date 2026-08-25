import { searchPosts } from "@/actions/post.action";
import SearchPage from "./_component/SerchPage";

interface PageProps {
    searchParams: Promise<{ kw?: string | string[] }>
}

const SearchRoute = async ({ searchParams }: PageProps) => {
    const params = await searchParams;
    const keywordValue = params.kw;
    const keyword = (Array.isArray(keywordValue) ? keywordValue[0] : keywordValue)?.trim() || "";
    const result = keyword ? await searchPosts(keyword, 0) : null;

    return(
        <SearchPage
            key={keyword || "empty-search"}
            keyword={keyword}
            posts={result?.content ?? []}
            totalElements={result?.totalElements ?? 0}
            page={result?.page ?? 0}
            totalPages={result?.totalPages ?? 0}
            loadMore={searchPosts.bind(null, keyword)}
        />
    );
}

export default SearchRoute;
'use client';

import { createContext, useContext, useState } from "react";

interface PostCreateContextValue {
    showCreatePost: boolean;
    openCreatePost: () => void;
    closeCreatePost: () => void;
}

const PostCreateContext = createContext<PostCreateContextValue | null>(null);

export const PostCreateProvider = ({ children }: { children: React.ReactNode }) => {
    const [showCreatePost, setShowCreatePost] = useState(false);

    return (
        <PostCreateContext.Provider
            value={{
                showCreatePost,
                openCreatePost: () => setShowCreatePost(true),
                closeCreatePost: () => setShowCreatePost(false),
            }}
        >
            {children}
        </PostCreateContext.Provider>
    );
};

export const usePostCreate = () => {
    const context = useContext(PostCreateContext);
    if (!context) throw new Error("usePostCreate must be used inside PostCreateProvider");
    return context;
};

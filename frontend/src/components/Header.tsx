'use client'
import { AuthContext } from "@/app/_context/AuthContext";
import Link from "next/link";
import { use } from "react";

const Header = () => {
    const { user, isAuthenticated, isLoading } = use(AuthContext);
    return (
        <>
            <h1>Logo</h1>
            <h2>Home</h2>
            {
                isLoading ? (
                    "Loading..."
                 ) : isAuthenticated? (
                    <li>Chào: {user?.username}</li>
                 ) : (
                    <Link href={"/login"}>Đăng nhập</Link>
                 )
            }
            
        </>
    );
}

export default Header;
'use client'
import { getProfile } from "@/actions/auth.action";
import { usePathname } from "next/navigation";
import { useEffect, useState } from "react"

export const useAuth = () => {
    const [user, setUser] = useState<IUserProfile>({} as IUserProfile);
    const [isAuthenticated, setAuthenticated] = useState<boolean>(false);
    const [isLoading, setLoading] = useState<boolean>(true);
    const pathName = usePathname();
    const [token, setToken] = useState("");

    useEffect(() => {
        const getCurrentUser = async () => {
            try {
                const { responseInfo, token } = await getProfile();
                setUser(responseInfo.data);
                setToken(token!);
                setAuthenticated(true);
            } catch {
                setAuthenticated(false);
                setToken("");
            } finally {
                setLoading(false);
            };
        };
        getCurrentUser();
    }, [pathName])
    return { user, isAuthenticated, isLoading, token };
}
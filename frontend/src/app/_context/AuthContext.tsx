'use client'
import { useAuth } from "@/hooks/useAuth";
import { createContext } from "react";

interface AuthContextType{
    token: string,
    user: IUserProfile,
    isAuthenticated: boolean,
    isLoading: boolean
}

export const AuthProvider = ({ 
    children 
}: {
    children: Readonly<React.ReactNode>
}) => {
    const { token, user, isAuthenticated, isLoading } = useAuth();
    return (
        <AuthContext.Provider value={{ token, user, isAuthenticated, isLoading }}>
            {children}
        </AuthContext.Provider>
    );
}

export const AuthContext = createContext<Partial<AuthContextType>>({ 
    token: "",
    user: {} as IUserProfile,
    isAuthenticated: false,
    isLoading: true,
});
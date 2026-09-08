import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

const privatePath = ["/"];
const authPath = ["/login", "/register"];

const redirectToLogin = (request: NextRequest) => {
    return NextResponse.redirect(new URL('/login', request.url));
};

const isAdminRequest = async (request: NextRequest, token: string) => {
    const baseUrl = process.env.BASE_URL;
    if (!baseUrl) return false;

    try {
        const response = await fetch(`${baseUrl}/profile`, {
            headers: { Authorization: `Bearer ${token}` },
            cache: "no-store"
        });
        const responseInfo = await response.json();
        const role = String(responseInfo.data?.role || "").toUpperCase();
        return response.ok && ["ROLE_ADMIN", "ADMIN"].includes(role);
    } catch {
        return false;
    }
};

export async function proxy(request: NextRequest) {
    const sessionToken = request.cookies.get('token');
    const { pathname } = request.nextUrl;

    if (privatePath.includes(pathname) && !sessionToken) {
        return redirectToLogin(request);
    }

    if (authPath.includes(pathname) && sessionToken) {
        return NextResponse.redirect(new URL('/', request.url));
    }

    if (pathname.startsWith("/admin")) {
        if (!sessionToken) return redirectToLogin(request);
        if (!(await isAdminRequest(request, sessionToken.value))) {
            return NextResponse.redirect(new URL('/', request.url));
        }
    }

    return NextResponse.next();
}
 
export const config = {
    matcher: ["/", "/login", "/register", "/admin/:path*"],
}
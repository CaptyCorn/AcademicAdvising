import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

const privatePath = ["/"];
const authPath = ["/login", "/register"];

export function proxy(request: NextRequest) {
    const sessionToken = request.cookies.get('token');
    const { pathname } = request.nextUrl;

    if (privatePath.includes(pathname) && !sessionToken) {
        return NextResponse.redirect(new URL('/login', request.url));
    }

    if (authPath.includes(pathname) && sessionToken) {
        return NextResponse.redirect(new URL('/', request.url));
    }

    return NextResponse.next();
}
 
export const config = {
    matcher: ["/", "/login", "/register"],
}
import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

const privatePath = ["/"];
const authPath = ["/login", "/register"];

// This function can be marked `async` if using `await` inside
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
 
// Alternatively, you can use a default export:
// export default function proxy(request: NextRequest) { ... }
 
export const config = {
    matcher: ["/", "/login", "/register"],
}
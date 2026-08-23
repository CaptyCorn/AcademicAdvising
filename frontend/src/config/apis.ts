export const endpoints = {
    'login': '/login',
    'register': '/register',
    'profile': '/profile',
    'posts': '/posts',
}

export const callAPI = (endpoint: string) => {
    return process.env.BASE_URL + endpoint;
}
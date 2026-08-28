export const endpoints = {
    'login': '/login',
    'register': '/register',
    'profile': '/profile',

    'posts': '/posts',
    'createPost': '/post',
    'postDetail': (postId: string) => `/posts/${postId}`,

    'listComment': (postId: string) => `/posts/${postId}/comments`, 
    'createComment': (postId: string) => `/posts/${postId}/comment`, 
}

export const callAPI = (endpoint: string) => {
    return process.env.BASE_URL + endpoint;
}
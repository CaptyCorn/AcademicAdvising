export const endpoints = {
    'login': '/login',
    'register': '/register',
    'profile': '/profile',

    'posts': '/posts',
    'createPost': '/post',
    'postDetail': (postId: string) => `/posts/${postId}`,

    'listComment': (postId: string) => `/posts/${postId}/comments`, 
    'createComment': (postId: string) => `/posts/${postId}/comment`, 

    'listMajor': '/majors',
    'createMajor': '/major',
    'deleteMajor': (majorId: string) => `/majors/${majorId}`,

    'listSubject': '/subjects',
    'createSubject': '/subject',
    'deleteSubject': (subjectId: string) => `/subjects/${subjectId}`,

    'listbook': '/books',
    'bookDetail': (bookId: string) => `/books/${bookId}`,
    'createBook': '/books',
    'deleteBook': (bookId: string) => `/books/${bookId}`,
    'bookUser': '/books/user',
}

export const callAPI = (endpoint: string) => {
    return process.env.BASE_URL + endpoint;
}
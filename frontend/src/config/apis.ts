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
    'createMajor': '/admin/major',
    'deleteMajor': (majorId: string) => `/admin/majors/${majorId}`,

    'listSubject': '/subjects',
    'createSubject': '/admin/subject',
    'deleteSubject': (subjectId: string) => `/admin/subjects/${subjectId}`,

    'listbook': '/books',
    'bookDetail': (bookId: string) => `/books/${bookId}`,
    'createBook': '/books',
    'deleteBook': (bookId: string) => `/books/${bookId}`,
    'bookUser': '/books/user',

    'adminDashboard': '/admin/dashboard',
    'adminRevenue': '/admin/revenue',

    'listConversation': '/conversations',
    'listMessage': (conversationId: string) => `/conversations/${conversationId}/messages`,
}

export const callAPI = (endpoint: string) => {
    return process.env.BASE_URL + endpoint;
}
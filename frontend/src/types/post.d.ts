interface IPostUser {
    name: string,
    username: string,
    email: string,
    avatar: string,
    studentCode: string
}

interface IPosts {
    id: number,
    content: string,
    createdAt: Date,
    commentCount?: number,
    user: IPostUser
}
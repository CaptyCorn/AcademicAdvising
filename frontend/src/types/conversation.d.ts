interface IConversationUser {
    id?: number,
    name?: string,
    username?: string,
    avatar?: string
}

interface IConversation {
    id: number,
    lastMessage?: string,
    lastMessageTime?: Date,
    lastSender?: IConversationUser,
    sender?: IConversationUser,
    receiver?: IConversationUser,
    createdAt?: Date
}

interface IMessage {
    id: number | string,
    content?: string,
    createdAt?: Date,
    conversationId?: number,
    sender?: IConversationUser,
    messageType?: "TEXT" | "BOOK" | string,
    book?: {
        id: number,
        name: string,
        price: number,
        image?: IImage
    }
}

import { loadConversationMessages, loadConversations } from "@/actions/conversation.action";
import ConversationPageClient from "./_component/ConversationPageClient";

const ConversationPage = async () => {
    let conversations: IConversation[] = [];

    try {
        const result = await loadConversations(0);
        conversations = result.content;
    } catch {
        conversations = [];
    }

    return (
        <ConversationPageClient
            initialConversations={conversations}
            loadConversations={loadConversations}
            loadMessages={loadConversationMessages}
        />
    );
};

export default ConversationPage;
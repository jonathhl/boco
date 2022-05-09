/** Service for chat api-calls */
// Imports:
import axios from "axios"
import { CHAT_URL } from "@/services/api/urls"

/** Get-call for getting chatr messages */
export async function getChatMessages(token: string, chatId: number) {
    return axios.get(CHAT_URL + `/${chatId}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })
}

/** Get-call for getting all chats with other users for a user */
export async function getChats(token: string) {
    return axios
        .get(CHAT_URL, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
        .then((response) => {
            return response.data
        })
}

/** Post-call for sending chat massage */
export async function postChatMessage(
    token: string,
    chatId: number,
    message: string
) {
    return axios.post(
        CHAT_URL + `/${chatId}`,
        {
            message: message,
        },
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    )
}

// Get-call for getting notifications current user
export async function getNotificationsByID(id: number) {
    return axios
        .get(CHAT_URL + `/${id}`)
        .then((response) => {
            return response.data
        })
        .catch((error) => {
            console.error(`Unable to get notifications: ${error}`)
            return {}
        })
}

// Api-call for marking a message as read
export async function markAsRead(token: string, chatId: number) {
    return axios.put(
        CHAT_URL + `/${chatId}/notification`,
        {},
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    )
}

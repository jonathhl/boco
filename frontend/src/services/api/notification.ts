/** Service api-calls for notifications */
//Imports:
import axios from "axios"
import { NOTIFICATION_URL } from "@/services/api/urls"

// Get-call to get notifications
export async function getNotifications(token: string) {
    return axios.get(NOTIFICATION_URL, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })
}

// Put-call to update notifications
export async function putNotification(token: string, read: boolean) {
    return axios
        .put(
            NOTIFICATION_URL,
            {
                read: read,
            },
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        )
        .then(() => {
            return true
        })
        .catch((error) => {
            console.error(`Unable to put notifications: ${error}`)
            return false
        })
}

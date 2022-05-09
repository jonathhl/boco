/** Service api-calls for rating */
// Imports:
import axios from "axios"
import { RATING_URL } from "@/services/api/urls"

// Put-call to update rating
export async function putRating(
    token: string,
    rating: number,
    ratingId: number,
    review: string
) {
    return axios.put(
        RATING_URL + `/${ratingId}`,
        {
            rating: rating,
            review: review,
        },
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    )
}

// Get-call to get rating for a user
export async function getRatingsForUser(email: string, token: string) {
    return axios
        .get(RATING_URL + `/${email}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
        .then((response) => {
            return response.data
        })
        .catch(() => {
            throw "Unable to retrieve user data"
        })
}

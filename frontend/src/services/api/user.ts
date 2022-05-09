/** Service api-calls for users */
// Imports:
import axios from "axios"
import { AUTH_URL, USER_URL } from "@/services/api/urls"

// Get-call to get a token for a user
export async function getToken(
    username: string,
    password: string
): Promise<string> {
    return axios
        .post(AUTH_URL, {
            username: username,
            password: password,
        })
        .then((response) => {
            if (!response.data.token) {
                throw "Token from endpoint was empty."
            }

            return response.data.token
        })
        .catch((error) => {
            console.error(`Unable to retrieve token: ${error}`)
            return ""
        })
}

// Post-call for sending informations for a user
export async function postUser(
    email: string,
    firstName: string,
    lastName: string,
    phone: number,
    zip: number,
    password: string
) {
    return axios
        .post(USER_URL, {
            email: email,
            firstname: firstName,
            lastname: lastName,
            phone: phone,
            zip: zip,
            password: password,
        })
        .then(() => {
            return true
        })
        .catch((error) => {
            console.error(`Unable to post user: ${error}`)
            return false
        })
}

// Get-caall to get information about a user
export async function getUser(token: string) {
    return axios
        .get(USER_URL, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
        .then((response) => {
            return response.data
        })
        .catch((error) => {
            console.error(`Unable to retrieve user: ${error}`)
            return {}
        })
}

export async function putUser(
    token: string,
    email: string,
    firstName: string,
    lastName: string,
    phone: number,
    zip: number,
    password: string
) {
    return axios
        .put(
            USER_URL,
            {
                email: email,
                firstname: firstName,
                lastname: lastName,
                phone: phone,
                zip: zip,
                password: password,
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
            console.error(`Unable to put user: ${error}`)
            throw error
        })
}

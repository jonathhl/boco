/** Service class for api-calls for images */
//Imports
import axios from "axios"
import { IMAGE_URL } from "./urls"

// Post-call for uploading a image
export async function uploadImage(file: File, token: string) {
    let formData = new FormData()
    formData.append("image", file)
    return axios
        .post(IMAGE_URL, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
                Authorization: `Bearer ${token}`,
            },
        })
        .then((response) => {
            return response.data
        })
        .catch((e) => {
            return `Unable to upload image, error ${e}`
        })
}

// Delete-call for delete image
export async function deleteImageCall(id: number, token: string) {
    return axios.delete(IMAGE_URL + `/${id}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })
}

/** Service for api-calls for map*/
//Imports:
import axios from "axios"

const token = "9bd196a2f67f7d7bc53aed002328d85b"

function url(address: string) {
    return `http://api.positionstack.com/v1/forward?access_key=${token}&query=${address}`
}

// Get-call to get positions from search
export async function getPositionsFromQuery(address: string): Promise<any[]> {
    return axios.get(url(address)).then((response) => {
        return response.data
    })
}

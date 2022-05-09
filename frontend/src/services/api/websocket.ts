/* eslint-disable */

// Imports:
import Stomp, { Message } from "webstomp-client"
import SockJS from "sockjs-client"

// Variables:
const socket = new SockJS("http://localhost:8888/ws", null, {})
const client = Stomp.over(socket)
const observers: Array<Function> = []

// Connecting to websocket-server
export function connect(token: string, username: string) {
    return new Promise((resolve) => {
        client.connect(
            {
                Authorization: token,
            },
            () => {
                client.subscribe(`/queue/${username}`, (message: Message) => callObservers(message))

                resolve(true)
            },
            () => {
                resolve(false)
            }
        )
    })
}

// Runs when a new websocket message arrives
function callObservers(message: Message) {
    observers.forEach((f) => f(message))
}

// Adding a callback function
export function addObserver(callback: (m: Message) => any) {
    observers.push(callback)
}

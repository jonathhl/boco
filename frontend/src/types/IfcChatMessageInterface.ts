// Interface for chat message
export interface ChatMessage {
    type: "text"
    time: Date
    from: string
    message?: string
    attachment?: any
}

<!-- Base component for chat message layout-->
<template>
    <div ref="chatContainer" style="overflow: scroll">
        <span v-for="m in props.messages" :key="m.time">
            <!-- Uses the component for text chat message-->
            <text-chat-message-component
                v-if="m.type === MSG_TYPE_TEXT"
                :message="m"
            />
            <!-- Uses the component for rating chat message-->
            <rating-chat-message-component
                v-if="m.type === MSG_TYPE_RATING"
                :message="m"
                @update-chat="$emit('update-chat')"
            />
            <!-- Uses the component for request chat message-->
            <request-chat-message-component
                v-if="m.type === MSG_TYPE_REQUEST"
                :message="m"
                @update-chat="$emit('update-chat')"
            />
        </span>
    </div>
</template>

<script setup lang="ts">
/** Imports: */
import { defineProps, ref, watch, defineEmits } from "vue"
import { ChatMessage } from "@/types/IfcChatMessageInterface"
import TextChatMessageComponent from "@/components/chat/message/TextChatMessageComponent.vue"
import RatingChatMessageComponent from "@/components/chat/message/RatingChatMessageComponent.vue"
import RequestChatMessageComponent from "@/components/chat/message/RequestChatMessageComponent.vue"

/** Variables: */
const props = defineProps<{
    messages: ChatMessage[]
}>()
const emit = defineEmits(["update-chat"])
const chatContainer = ref<HTMLDivElement>()

const MSG_TYPE_TEXT = "MESSAGE"
const MSG_TYPE_RATING = "RATING"
const MSG_TYPE_REQUEST = "REQUEST"

/** Method for setting timeout */
watch(
    () => props.messages,
    async () => {
        setTimeout(() => {
            chatContainer.value!.scrollTop = chatContainer.value!.scrollHeight
        }, 100)
    }
)
</script>

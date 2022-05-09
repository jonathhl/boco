<!-- Base component for text chat message-->
<template>
    <!-- Uses the component for chat message-->
    <chat-message-component :sent-by-me="sentByMe">
        <h5 class="text-secondary-dimmed font-weight-regular mb-2">
            <b>{{ from + " " }} </b> {{ timeLabel }}
        </h5>
        <p>{{ messageText }}</p>
    </chat-message-component>
</template>

<script setup lang="ts">
/** Imports: */
import { defineProps, ref } from "vue"
import { ChatMessage } from "@/types/IfcChatMessageInterface"
import ChatMessageComponent from "@/components/chat/message/ChatMessageComponent.vue"
import { useStore } from "vuex"

/** Variables: */
const props = defineProps<{
    message: ChatMessage
}>()

const messageText = ref(props.message.message)
const from = ref(props.message.from)
const dateTime = new Date(props.message.time)
const timeLabel = `${dateTime.getDay()}/${dateTime.getMonth()}/${dateTime.getFullYear()}, ${dateTime.getHours()}.${dateTime.getMinutes()}`

const store = useStore()

const sentByMe = props.message.from == store.getters.email
</script>

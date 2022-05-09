<!-- Base component for chat text field-->
<template>
    <form @submit.prevent="sendMessage">
        <div class="d-flex">
            <input
                v-model="message"
                type="text"
                class="input w-100 text-secondary border pl-2"
                height="40"
                placeholder="Aa"
            />

            <!-- Send message button-->
            <v-btn
                class="text-primary-c ml-2"
                type="submit"
                height="40"
                width="55"
                rounded="lg"
                color="primary"
                icon="mdi-send"
            >
            </v-btn>
        </div>
    </form>
</template>

<script setup>
/** Imports: */
import { defineProps, ref } from "vue"

/** Variables: */
const props = defineProps({
    sendMessageCallback: Function,
})

const message = ref("")

const sendMessage = () => {
    props.sendMessageCallback(message.value).then(() => {
        message.value = ""
    })
}
</script>

<style scoped>
.input {
    height: 40px;
    border-radius: 12px;
}
</style>

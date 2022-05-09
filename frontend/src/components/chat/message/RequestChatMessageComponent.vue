<!-- Base component for request chat message-->
<template>
    <chat-message-component :sent-by-me="sentByMe">
        <v-container>
            <h3 v-if="!sentByMe">Leieforespørsel</h3>
            <h3 v-else>Ny leieforespørsel</h3>

            <h4 class="font-weight-light ma-1 text-left">
                <span v-if="!sentByMe">
                    <b>{{ fromName }}</b>
                </span>
                <span v-else>Du</span>

                har spurt om å leie
                <router-link
                    :to="{ name: 'detailedlisting', params: { id: listingId } }"
                    ><b>{{ listingTitle }}</b></router-link
                >
                fra
                <i>{{ startDate }} - {{ endDate }}</i>
            </h4>

            <v-container class="rounded pa-2 border mt-4" v-if="requestMessage">
                <p>{{ requestMessage }}</p>
            </v-container>

            <div class="mt-8">
                <span justify="center" v-if="accepted === 'NOT_SEEN'">
                    <v-row v-if="!sentByMe">
                        <v-col cols="12" lg="6" md="6">
                            <!-- Accept button-->
                            <v-btn
                                data-cy="accept"
                                class="w-100"
                                color="primary"
                                prepend-icon="mdi-check"
                                @click="accept"
                                >godta</v-btn
                            >
                        </v-col>

                        <v-col>
                            <!-- Reject button -->
                            <v-btn
                                data-cy="reject"
                                type="danger"
                                @click="reject"
                                prepend-icon="mdi-close"
                                variant="outlined"
                                class="w-100"
                                color="error"
                                >avvis</v-btn
                            >
                        </v-col>
                    </v-row>
                    <v-row v-else>
                        <h3
                            class="text-secondary-dimmed text-center w-100 mt-4 mb-4"
                        >
                            Venter på svar...
                        </h3>
                    </v-row>
                </span>
                <v-row v-else-if="accepted === 'ACCEPTED'">
                    <h3 class="text-success text-center w-100 mt-4 mb-4">
                        Forespørsel godkjent
                    </h3>
                </v-row>
                <v-row v-else-if="accepted === 'REJECTED'">
                    <h3 class="text-error text-center w-100 mt-4 mb-4">
                        Forespørsel avvist
                    </h3>
                </v-row>
            </div>
        </v-container>
    </chat-message-component>
</template>

<script setup lang="ts">
/** Imports: */
import { acceptRequest, rejectRequest } from "@/services/api/request"
import { defineEmits, ref, defineProps, computed } from "vue"
import { useStore } from "vuex"
import ChatMessageComponent from "@/components/chat/message/ChatMessageComponent.vue"
import { ChatMessage } from "@/types/IfcChatMessageInterface"

/** Variables: */
const store = useStore()

const props = defineProps<{ message: ChatMessage }>()
const emit = defineEmits(["update-chat"])

const sentByMe = props.message.from == store.getters.email

const requestItem = computed(() => props.message.attachment)
const listingItem = computed(() => requestItem.value.listing)

const fromName = ref(props.message.from)

const listingTitle = listingItem.value.title
const listingId = listingItem.value.id

const requestId = requestItem.value.requestId
const accepted = computed(() => requestItem.value.accepted)
const requestMessage = requestItem.value.message
const start: Date = new Date(requestItem.value.startDate)
const end: Date = new Date(requestItem.value.endDate)
const startDate = ref(`${start.getDay() + 1}.${start.getMonth() + 1}`)
const endDate = ref(`${end.getDay() + 1}.${end.getMonth() + 1}`)

/** Method for rejecting a request */
const reject = () => {
    rejectRequest(requestId, store.getters.token)
        .then(() => {
            emit("update-chat")
        })
        .catch((e) => {
            store.dispatch("postAlert", {
                title: "Feil ved oppdatering av forespørsel",
                message: "" + e,
                type: "error",
            })
        })
}

/** Method for accepting a request */
const accept = () => {
    acceptRequest(requestId, store.getters.token)
        .then(() => {
            emit("update-chat")
        })
        .catch((e) => {
            store.dispatch("postAlert", {
                title: "Feil ved oppdatering av forespørsel",
                message: "" + e,
                type: "error",
            })
        })
}
</script>

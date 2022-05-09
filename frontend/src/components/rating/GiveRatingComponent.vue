<!-- The component for giving a rating-->
<template>
    <!-- Uses the component for chat messages-->
    <ChatMessageComponent>
        <v-card-title class="text-subtitle-1">
            <div>
                Leieforholdet er gjennomført! Gi gjerne en vurdering til
                <b>{{ name }}</b
                >:
            </div>
        </v-card-title>

        <v-rating
            v-model="rating"
            class="ml-3 mt-2"
            color="warning"
            dense
            size="33"
        ></v-rating>
        <p v-if="warning" class="ml-4 text-error mt-2">
            <b>Rating kan ikke være tom!</b>
        </p>
        <div class="d-flex mt-5">
            <v-text-field
                v-model="review"
                density="compact"
                class="ml-3 mr-3"
                auto-grow="true"
                variant="outlined"
                label="Gi gjerne en kommentar..."
            ></v-text-field>
            <v-btn
                @click="submit"
                class="mr-3"
                height="39"
                width="42"
                icon="mdi-send"
                rounded="lg"
                color="primary"
            ></v-btn>
        </div>
    </ChatMessageComponent>
</template>

<script setup lang="ts">
/** Imports: */
import { ref, defineProps } from "vue"
import ChatMessageComponent from "@/components/chat/ChatMessageComponent.vue"
import { useField } from "vee-validate"
import { useStore } from "vuex"
import { putRating } from "@/services/api/rating"

const props = defineProps({
    name: String,
    request: Object,
})

/** Variables: */
const { value: rating } = useField<number>("rating")
const { value: review } = useField("review")

const warning = ref(false)
const store = useStore()

/** Methods for submit the rating */
const submit = () => {
    if (rating.value === undefined) {
        warning.value = true
    } else {
        warning.value = false
        if (store.getters.isLoggedIn) {
            putRating(
                store.getters.token,
                rating.value as number,
                review.value as number,
                props.request?.requestId.value.toString()
            )
                .then(() =>
                    store.dispatch("postAlert", {
                        message: "Tilbakemeldingen er registrert!",
                        type: "success",
                    })
                )
                .catch(() => {
                    store.dispatch("postAlert", {
                        message: `Registrering av tilbakemelding feilet!`,
                        type: "error",
                    })
                })
        } else {
            console.log("ikke logget inn")
        }
    }
}
</script>

<!-- The component for sending a request-->
<template>
    <v-card class="rounded-xl" elevation="2">
        <v-form @submit.prevent="submit">
            <v-card-title> Forespør leie </v-card-title>
            <v-container>
                <v-row>
                    <v-col>
                        <v-textarea
                            data-cy="message"
                            v-model="message"
                            type="text"
                            label="Melding"
                            variant="outlined"
                        >
                        </v-textarea>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="12">
                        <h3 class="mb-2">Leieperiode</h3>
                        <v-divider></v-divider>
                        <v-row class="mt-2">
                            <v-col cols="12" sm="6">
                                <v-text-field
                                    data-cy="startDate"
                                    v-model="startDate"
                                    label="Startdato"
                                    prepend-icon="mdi-calendar"
                                    type="date"
                                    :error-messages="errors.startDate"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-text-field
                                    data-cy="endDate"
                                    v-model="endDate"
                                    label="Sluttdato"
                                    prepend-icon="mdi-calendar"
                                    type="date"
                                    :error-messages="errors.endDate"
                                ></v-text-field>
                            </v-col>
                        </v-row>
                    </v-col>
                    <v-col cols="12">
                        <!-- Sending request button-->
                        <v-btn
                            data-cy="sendRequest"
                            class="mr-2 text-primary-c w-100"
                            color="primary"
                            rounded
                            type="submit"
                            >Send inn forespørsel
                        </v-btn>
                    </v-col>
                </v-row>
            </v-container>
        </v-form>
    </v-card>
</template>

<script setup lang="ts">
/** Imports: */
import { useField, useForm } from "vee-validate"
import { boolean, date, number, object, ref, string } from "yup"
import { postRequest } from "@/services/api/request"
import { useRouter } from "vue-router"
import { useRoute } from "vue-router"
import { useStore } from "vuex"

/** Variables: */
const route = useRoute()
const router = useRouter()
const store = useStore()

const validationSchema = object({
    email: string(),
    listingId: number(),
    startDate: date().required("Dette feltet er påkrevd"),
    endDate: date()
        .required()
        .min(ref("startDate"), "Sluttdato må være etter startdato"),
    message: string(),
    accepted: boolean(),
})

const { handleSubmit, errors } = useForm({
    validationSchema,
})

const { value: startDate } = useField("startDate")
const { value: endDate } = useField("endDate")
const { value: message } = useField("message")

/** Method for sending a request */
const submit = handleSubmit((values) => {
    if (values.startDate && values.endDate)
        postRequest(
            ~~route.params.id,
            new Date(values.startDate),
            new Date(values.endDate),
            values.message as string,
            store.getters.token
        )
            .then(() => {
                store.dispatch("postAlert", {
                    title: "Forespørsel sendt!",
                    message: " ",
                    type: "success",
                })

                router.push({ name: "landingpage" })
            })
            .catch((e) => {
                store.dispatch("postAlert", {
                    title: "Forespørsel kunne ikke sendes!",
                    message: "Error" + e,
                    type: "error",
                })
            })
})
</script>

<template>
    <v-container class="h-screen w-100">
        <h2 class="text-subtitle-1 font-weight-light ma-1 text-left">
            Forespørsel om leie av {{ rentalObject }} fra {{ nameRequest }}:
        </h2>

        <v-text-field
            variant="underlined"
            prepend-icon="mdi-calendar"
            readonly
            class="mr-6 pa-6"
            >{{ fromDate }} - {{ toDate }}
        </v-text-field>

        <v-card
            title="Melding:"
            :subtitle="requestText"
            elevation="2"
            outlined
            height="150"
        >
        </v-card>

        <v-row justify="center">
            <v-btn type="danger" class="ma-8 mr-12" width="200">avvis</v-btn>
            <v-btn class="text-primary-c ma-8 ml-12" color="primary" width="200"
                >godta</v-btn
            >
        </v-row>
        <h2 class="text-subtitle-2 font-weight-medium mt-6">
            Informasjon om leietaker:
        </h2>
        <UserCard></UserCard>
    </v-container>
</template>

<script lang="ts" setup>
import { ref } from "vue"
import UserCard from "@/components/user/UserCard.vue"

const rentalObject = ref("Skøyter")
const nameRequest = ref("Ola Nordmann")
const requestText = ref(
    "Hei! Jeg vil gjerne leie denne. Er den ledig på valgt dato?"
)
const fromDate = ref("2022-04-27")
const toDate = ref("2022-04-30")
</script>

<style scoped></style>

<template>
    <v-card elevation="0" class="mt-2" color="surface-nyance">
        <v-card-actions>
            <v-list-item
                prepend-avatar="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
                class="w-100"
                :title="nameRequest"
                :subtitle="phonenumber"
            >
                <router-link
                    to="LINK"
                    class="text-subtitle-2 text-blue-darken-4"
                    >Se profil</router-link
                >
            </v-list-item>
            <h2 class="text-subtitle-1 font-weight-light mr-1 text-no-wrap">
                {{ score }}
            </h2>
            <v-icon color="warning" icon="mdi-star" class="mr-2"></v-icon>
        </v-card-actions>
    </v-card>
</template>

<script setup lang="ts">
import { ref } from "vue"

const nameRequest = ref("Ola Nordmann")
const phonenumber = ref("+47 98765432")
const score = ref("4.8")
</script>

<style scoped></style>

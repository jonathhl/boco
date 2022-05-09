<template>
    <v-row>
        <v-col>
            <v-carousel>
                <v-carousel-item>
                    <v-img :src="IMAGE_URL + '/' + listing?.image" />
                </v-carousel-item>
            </v-carousel>
        </v-col>
    </v-row>
    <v-spacer />
    <v-row class="container">
        <v-spacer />
        <v-col>
            <v-card-content class="py-1">
                <h2>{{ listing?.title }}</h2>
                <i>{{ listing?.price }} kr/dagen</i>
            </v-card-content>
        </v-col>
        <v-col v-if="listing?.email || listing?.phone">
            <v-card-content class="py-1">
                <p v-if="listing?.phone">
                    <b>Telefonnummer: </b> {{ listing?.phone }}
                </p>
            </v-card-content>
        </v-col>
        <v-col>
            <v-card-content class="py-1">
                {{ listing?.description }}
            </v-card-content>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-btn
                v-if="userEmail === listing?.email"
                @click="$router.push({ name: 'editlisting' })"
                variant="outlined"
                class="ml-4 my-6"
            >
                Rediger
            </v-btn>
        </v-col>
    </v-row>
</template>

<script setup lang="ts">
import { defineProps } from "vue"
import { useStore } from "vuex"
import { IMAGE_URL } from "@/services/api/urls"

const store = useStore()
const userEmail = store.getters.email

defineProps({
    listing: Object,
})
</script>

<style scoped>
.container {
    display: flex;
    flex-flow: column wrap;
    flex-basis: auto;
    max-width: 80%;
}
</style>

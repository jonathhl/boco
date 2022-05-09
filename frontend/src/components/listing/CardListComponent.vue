<template>
    <v-container class="w-100">
        <HeaderComponent :text="props.title" />
        <v-row>
            <v-col
                v-for="(item, index) in props.listings"
                :key="index"
                cols="12"
                sm="6"
                lg="4"
                xl="2"
            >
                <ListingCardComponent :item="item" />
            </v-col>
        </v-row>
    </v-container>
</template>
<script setup lang="ts">
import { defineProps } from "vue"
import HeaderComponent from "@/components/HeaderComponent.vue"
import ListingCardComponent from "@/components/listing/ListingCardComponent.vue"

const props = defineProps({
    title: String,
    listings: Object,
})
</script>

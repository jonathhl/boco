<template>
    <v-card class="ma-4" height="90" color="primary-c">
        <v-list-item
            :title="props.rating.from"
            :subtitle="props.rating.review"
            class="w-100"
        >
            <RatingComponent :rating="props.rating.rating" />
        </v-list-item>
    </v-card>
</template>

<script lang="ts" setup>
import { defineProps } from "vue"
import RatingComponent from "@/components/rating/RatingComponent.vue"

const props = defineProps({
    rating: Object,
})
</script>

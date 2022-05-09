<!-- The component to get all ratings-->
<template>
    <!-- Uses the component for rating card-->
    <RatingCardComponent
        v-for="(rating, index) in ratings"
        :key="index"
        :rating="rating"
    />
</template>

<script setup lang="ts">
/** Imports: */
import { defineProps } from "vue"
import RatingCardComponent from "@/components/rating/RatingCardComponent.vue"

defineProps({
    ratings: Object,
})
</script>

<template>
    <CardListComponent title="Alle annonser" :listings="listings" />
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue"
import { getAllListings } from "@/services/api/listing"
import CardListComponent from "@/components/listing/CardListComponent.vue"

const listings = ref([])

onMounted(() => {
    getAllListings().then((data) => {
        listings.value = data
    })
})
</script>

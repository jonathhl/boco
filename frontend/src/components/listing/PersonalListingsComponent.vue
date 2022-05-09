<template>
    <CardListComponent
        :listings="listings"
        title="Annonser"
    ></CardListComponent>
</template>

<script setup lang="ts">
import { getPersonalListings } from "@/services/api/listing"
import { onMounted, ref } from "vue"
import { useStore } from "vuex"
import CardListComponent from "@/components/listing/CardListComponent.vue"

const store = useStore()
const listings = ref([])

onMounted(() => {
    getPersonalListings(store.getters.token).then((data) => {
        listings.value = data
    })
})
</script>

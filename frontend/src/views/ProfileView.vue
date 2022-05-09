<!-- View for a user profile-->
<template>
    <v-container>
        <UserCard :rating="average" :firstname="fullName" :email="email" />
    </v-container>

    <!-- Tabs-->
    <v-tabs fixed-tabs="true" background-color="primary">
        <v-tab @click="tab = `listing`"> Mine annonser </v-tab>

        <v-tab @click="tab = `ratings`"> Mine vurderinger </v-tab>
    </v-tabs>

    <!-- Uses the component for personal listings-->
    <PersonalListingsComponent v-if="tab === `listing`" />
    <!-- Uses the component for all ratings-->
    <AllRatingComponent :ratings="ratings" v-if="tab === `ratings`" />
</template>
<script setup lang="ts">
/** Imports: */
import UserCard from "../components/user/UserCardComponent.vue"
import PersonalListingsComponent from "../components/listing/PersonalListingsComponent.vue"
import { ref, onMounted } from "vue"
import AllRatingComponent from "@/components/rating/AllRatingComponent.vue"
import { getRatingsForUser } from "@/services/api/rating"
import { useStore } from "vuex"
import { getUser } from "@/services/api/user"

/** Variables */
const tab = ref("listing")
const store = useStore()
const ratings = ref([])
const average = ref()
const fullName = ref("")
const email = ref("")

/** Method to load rating before the view loads */
onMounted(() => {
    getRatingsForUser(store.getters.email, store.getters.token).then((data) => {
        ratings.value = data.ratings
        average.value = data.average
    })
    getUser(store.getters.token).then((data) => {
        fullName.value = data.firstname + " " + data.lastname
        email.value = data.email
    })
})
</script>

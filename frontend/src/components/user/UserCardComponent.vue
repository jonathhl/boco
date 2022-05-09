<!-- The component for a user card with information about a user -->
<template>
    <v-card elevation="0" class="mt-2" color="surface-nyance">
        <v-card-actions>
            <v-list-item
                data-cy="profile"
                :prepend-avatar="avatar"
                class="w-100"
                :title="firstname"
                :subtitle="email"
            >
                <!-- Edit button for editing userprofile -->
                <v-btn
                    class="mt-2"
                    data-cy="editbtn"
                    size="small"
                    variant="contained-text"
                    v-if="store.getters.email === email"
                    @click="$router.push({ name: 'edituser' })"
                    prepend-icon="mdi-pencil"
                    >Rediger</v-btn
                >
            </v-list-item>
            <!-- Shows the avarge rating for a user-->
            <RatingComponent :rating="rating"></RatingComponent>
        </v-card-actions>
    </v-card>
</template>

<script setup lang="ts">
/** Imports: */
import { ref, defineProps } from "vue"
import RatingComponent from "@/components/rating/RatingComponent.vue"
import { useStore } from "vuex"

/** Variables: */
const store = useStore()

defineProps({
    rating: Number,
    firstname: String,
    email: String,
})
const avatar = ref(require("@/assets/user-avatar-placeholder.png"))
</script>

<style scoped></style>

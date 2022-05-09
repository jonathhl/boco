<template>
    <v-container class="w-100">
        <HeaderComponent text="Mine annonser" />
        <v-row>
            <v-container>
                <PersonalListingsComponent />
            </v-container>
        </v-row>
    </v-container>
</template>
<script setup lang="ts">
import PersonalListingsComponent from "@/components/listing/PersonalListingsComponent.vue"
import HeaderComponent from "@/components/HeaderComponent.vue"
</script>

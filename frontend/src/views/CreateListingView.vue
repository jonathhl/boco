<template>
    <v-container>
        <v-row>
            <v-col cols="12">
                <HeaderComponent text="Hva vil du leie ut?" />
                <CreateListingComponent />
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup lang="ts">
import CreateListingComponent from "@/components/listing/CreateListingComponent.vue"
import HeaderComponent from "@/components/HeaderComponent.vue"
</script>

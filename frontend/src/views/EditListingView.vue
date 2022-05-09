<template>
    <v-container>
        <HeaderComponent text="Rediger annonse" />
        <v-row>
            <v-col cols="12">
                <EditListingComponent />
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup lang="ts">
import EditListingComponent from "@/components/listing/EditListingComponent.vue"
import HeaderComponent from "@/components/HeaderComponent.vue"
</script>

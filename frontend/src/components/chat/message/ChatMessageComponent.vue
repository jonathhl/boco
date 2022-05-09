<!--Base component for all chat messages-->

<template>
    <v-row class="w-100 mb-4" :justify="props.sentByMe ? 'end' : 'start'">
        <div class="w-75 ml-6 mt-4">
            <v-card
                rounded
                elevation="0"
                class="pa-2 pl-4"
                :class="{ 'bg-primary': props.requiresAction }"
                border
            >
                <slot></slot>
            </v-card>
        </div>
    </v-row>
</template>

<script setup lang="ts">
/** Imports: */
import { defineProps } from "vue"

const props = defineProps({
    requiresAction: Boolean,
    sentByMe: Boolean,
})
</script>

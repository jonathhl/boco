<!-- The rating component-->
<template>
    <div class="d-flex">
        <h2
            class="text-subtitle-1 font-weight-light text-no-wrap mt-1 mr-1"
            style="max-width: 30px; overflow: hidden"
        >
            {{ rating }}
        </h2>
        <v-icon color="warning" icon="mdi-star" class="mt-1"></v-icon>
    </div>
</template>

<script setup lang="ts">
/** Imports: */
import { defineProps } from "vue"

defineProps({
    rating: Number,
})
</script>

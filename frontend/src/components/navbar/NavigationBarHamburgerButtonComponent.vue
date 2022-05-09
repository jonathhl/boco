<!-- The component for hamburger button for navigation bar-->
<template>
    <v-app-bar-nav-icon
        @click="props.navigationToggleCallback"
        height="28"
        data-test-id="hamburger-menu-button"
        :class="{ 'text-background': props.transparentMode }"
    ></v-app-bar-nav-icon>
    <div v-if="props.notification">
        <v-badge dot color="error" class="notification" />
    </div>
</template>

<script setup lang="ts">
/** Imports: */
import { defineProps } from "vue"

const props = defineProps({
    notification: Boolean,
    navigationToggleCallback: Function,
    transparentMode: Boolean,
})
</script>

<style scoped>
.notification {
    margin-left: -12px;
    margin-bottom: 15px;
}
</style>

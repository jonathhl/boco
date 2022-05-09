<!-- The component for logo for navigation bar-->
<template>
    <v-img
        v-if="
            (!props.transparentMode && store.getters.theme === 'dark') ||
            (props.transparentMode && store.getters.theme === 'light')
        "
        src="@/assets/navbar-logo-dark.png"
        class="pointer"
        height="28"
        @click="$router.push({ name: 'landingpage' })"
    />
    <v-img
        v-else
        src="@/assets/navbar-logo-light.png"
        class="pointer"
        height="28"
        @click="$router.push({ name: 'landingpage' })"
        data-cy="navbar-logo"
    />
</template>

<script setup lang="ts">
/** Imports: */
import { defineProps } from "vue"
import { useStore } from "vuex"

/** Variables: */
const store = useStore()
const props = defineProps({
    transparentMode: Boolean,
})
</script>

<!-- The component for toogle the theme-->
<template>
    <!-- Change theme buttons -->
    <v-btn
        class="text-yellow-darken-3 w-100"
        v-if="currentTheme === 'dark'"
        @click="toggleTheme"
        prepend-icon="mdi-white-balance-sunny"
        variant="outlined"
        height="42"
        >Bytt tema</v-btn
    >

    <v-btn
        class="w-100"
        variant="outlined"
        v-else
        @click="toggleTheme"
        prepend-icon="mdi-moon-waxing-crescent"
        height="42"
    >
        Bytt tema
    </v-btn>
</template>

<script setup lang="ts">
/** Imports: */
import { useStore } from "vuex"
import { ref } from "vue"

/** Variables: */
const store = useStore()
const currentTheme = ref("light")

/** Method for toogle theme */
const toggleTheme = () => {
    store.dispatch("toggleTheme")
    currentTheme.value = store.getters.theme
}
</script>

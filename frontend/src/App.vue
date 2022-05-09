<template>
    <!-- This is the recommended layout according to the docs. -->

    <v-app :theme="theme">
        <NavigationDrawerComponent :transparent="transparentNavBar" />
        <GlobalAlertComponent></GlobalAlertComponent>

        <v-main>
            <!-- Show whatever router view is currently active. -->
            <router-view />
        </v-main>
    </v-app>
</template>

<script setup lang="ts">
/** Imports:  */
import { computed, defineEmits, onBeforeMount } from "vue"
import { useStore } from "vuex"
import { useCookies } from "vue3-cookies"
import NavigationDrawerComponent from "@/components/navbar/NavigationDrawerComponent.vue"
import GlobalAlertComponent from "@/components/GlobalAlertComponent.vue"
import { useRouter } from "vue-router"

const emit = defineEmits(["update-login-state"])
const store = useStore()
const router = useRouter()
const { cookies } = useCookies()

const transparentNavBar = computed(() => {
    return router.currentRoute.value.name == "landingpage"
})

const theme = computed(() => {
    return store.getters.theme
})

onBeforeMount(() => {
    if (cookies.isKey("token")) {
        const token = cookies.get("token")
        store.dispatch("setToken", { token })
        emit("update-login-state")
    }

    if (cookies.isKey("theme")) {
        const theme = cookies.get("theme")
        store.dispatch("setTheme", { theme })
    }
})
</script>

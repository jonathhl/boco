<!-- The component for the navigation drawer-->
<template>
    <v-navigation-drawer width="350" app v-model="isNavigationOpen">
        <!-- Uses the component for login popup-->
        <LoginPopupComponent
            v-model="showPopup"
            @update-login-state="updateLoginState"
        ></LoginPopupComponent>
        <v-list color="background">
            <v-list-item
                v-if="isAuthenticated"
                @click="$router.push({ name: 'userprofile' })"
                :prepend-avatar="avatar"
                :title="fullName"
                :subtitle="email"
                value="profile"
                data-cy="navdrawer-user-card"
            />
            <v-list-item
                v-else
                @click="showPopup = true"
                :prepend-avatar="avatar"
                title="Logg inn"
                value="login"
                data-cy="navdrawer-login-button"
            />
        </v-list>

        <v-divider />

        <div v-if="!chat">
            <v-list nav>
                <v-list-subheader>Utforsk</v-list-subheader>
                <!-- Find something to rent-->
                <v-list-item
                    prepend-icon="mdi-magnify"
                    title="Finn noe å leie"
                    @click="$router.push({ name: 'landingpage' })"
                />
                <!-- All listings-->
                <v-list-item
                    prepend-icon="mdi-format-list-bulleted"
                    title="Alle annonser"
                    @click="$router.push({ name: 'alllistingview' })"
                />

                <v-list-subheader :hidden="!isAuthenticated"
                    >Administrer</v-list-subheader
                >
                <!-- Create listing-->
                <v-list-item
                    :hidden="!isAuthenticated"
                    prepend-icon="mdi-plus"
                    title="Opprett annonse"
                    @click="$router.push({ name: 'createlisting' })"
                />
                <!-- My listings-->
                <v-list-item
                    :hidden="!isAuthenticated"
                    prepend-icon="mdi-animation"
                    title="Mine annonser"
                    @click="$router.push({ name: 'personallistingview' })"
                />
                <!-- Chat-->
                <v-list-item
                    :hidden="!isAuthenticated"
                    prepend-icon="mdi-chat"
                    title="Chat"
                    @click="chat = !chat"
                    ><div
                        v-if="unread"
                        class="d-flex justify-center align-center"
                    >
                        <v-badge dot color="error" class="ma-auto" inline />
                        <p class="ml-2 text-error text-body-2">Ulest melding</p>
                    </div>
                </v-list-item>

                <v-list-subheader>Få hjelp</v-list-subheader>
                <!-- FAQ -->
                <v-list-item
                    prepend-icon="mdi-help-circle-outline"
                    title="Ofte stilte spørsmål"
                    @click="$router.push({ name: 'faq' })"
                />
            </v-list>
        </div>

        <div v-else>
            <v-list nav>
                <v-list-item
                    prepend-icon="mdi-arrow-collapse-left"
                    title="Back"
                    @click="chat = !chat"
                />
                <v-list-item
                    v-for="chat in chats"
                    :key="chat"
                    @click="
                        router.push({
                            name: 'chat',
                            params: { id: chat.id },
                        })
                    "
                    class="w-100 text-left"
                    :prepend-avatar="avatar"
                    :title="chat.users[0].firstname"
                    :subtitle="chat.users[0].email"
                    value="user"
                >
                    <!--Component to show notification for new chat, currently not implemented -->
                    <div v-if="false" class="d-flex">
                        <v-badge dot color="error" class="ma-auto" inline />
                        <p class="ml-2 text-error text-body-2">Ulest melding</p>
                    </div>
                </v-list-item>
            </v-list>
        </div>

        <template v-slot:append>
            <v-divider></v-divider>
            <v-list-item class="py-4">
                <v-row>
                    <v-col>
                        <theme-toggle-component></theme-toggle-component>
                    </v-col>

                    <v-col col="6">
                        <!-- Logout button -->
                        <v-btn
                            class="text-error w-100"
                            v-if="isAuthenticated"
                            variant="outlined"
                            prepend-icon="mdi-logout"
                            title="Logg ut"
                            @click="logout"
                            height="42"
                            >Logg ut</v-btn
                        >
                    </v-col>
                </v-row>
            </v-list-item>
        </template>
    </v-navigation-drawer>

    <!--Navigation bar-->
    <v-app-bar
        v-if="!isChat"
        elevation="0"
        class="border-b-sm"
        border="none"
        elevate-on-scroll
        :class="{ 'bg-transparent': props.transparent }"
    >
        <navigation-bar-hamburger-button-component
            :notification="unread"
            :navigation-toggle-callback="toggleNavigationDrawer"
            :transparent-mode="props.transparent"
        ></navigation-bar-hamburger-button-component>

        <!-- Uses the component for logo-->
        <NavigationBarLogo :transparentMode="transparent"></NavigationBarLogo>

        <!-- Userprofile button-->
        <v-btn
            v-if="isAuthenticated"
            @click="$router.push({ name: 'userprofile' })"
            icon
        >
            <v-avatar
                :rounded="true"
                size="24"
                :image="avatar"
                class="rounded-circle"
                data-cy="navbar-user-avatar"
            />
        </v-btn>

        <!-- Login button-->
        <v-btn
            v-else
            text
            @click="showPopup = true"
            data-cy="navbar-login-button"
            :class="{ 'text-background': transparent }"
        >
            <v-icon icon="mdi-login" class="mr-1" />
            Logg inn
        </v-btn>
    </v-app-bar>

    <!-- If chat window is opened, navigation bar is switched out with chat-receiver information -->
    <v-app-bar
        v-else
        elevation="0"
        class="border-b-sm"
        border="none"
        elevate-on-scroll
        :class="{ 'bg-transparent': props.transparent }"
    >
        <navigation-bar-hamburger-button-component
            :transparent-mode="props.transparent"
            :notification="unread"
            :navigation-toggle-callback="toggleNavigationDrawer"
        ></navigation-bar-hamburger-button-component>

        <v-list-item
            :prepend-avatar="avatar"
            class="w-100 text-left"
            :title="chatReceiverInfo.firstname"
            :subtitle="chatReceiverInfo.email"
            data-cy="navbar-chat-name"
        >
        </v-list-item>
    </v-app-bar>
</template>
<script setup lang="ts">
/** Imports: */
import { ref, defineProps, onUpdated, computed, onBeforeMount } from "vue"
import { useStore } from "vuex"
import LoginPopupComponent from "@/components/user/LoginPopupComponent.vue"
import { getUser } from "@/services/api/user"
import { useRouter, useRoute } from "vue-router"
import ThemeToggleComponent from "@/components/navbar/ThemeToggleComponent.vue"
import NavigationBarLogo from "@/components/navbar/NavigationBarLogoComponent.vue"
import { getNotifications } from "@/services/api/notification"
import { connect, addObserver } from "@/services/api/websocket"
import NavigationBarHamburgerButtonComponent from "@/components/navbar/NavigationBarHamburgerButtonComponent.vue"
import { getChats } from "@/services/api/chat"
import { useCookies } from "vue3-cookies"

/** Variables */
const store = useStore()
const { cookies } = useCookies()
const props = defineProps({
    transparent: Boolean,
})
const router = useRouter()
const route = useRoute()

const showPopup = ref(false)
const isChat = ref(false)

const isNavigationOpen = ref(true)
const isAuthenticated = ref(false)
const fullName = ref("")
const email = ref("")
const chat = ref(false)
const unread = ref(false)

const updateIsChat = () => {
    isChat.value = route.name == "chat"
}

const chats = ref()

const getChatlist = () => {
    return getChats(store.getters.token).then((data) => {
        chats.value = data
    })
}

/** Method for update the login state */
const updateLoginState = async () => {
    getChatlist()
    isAuthenticated.value = store.getters.isLoggedIn
    if (isAuthenticated.value) {
        const token = store.getters.token
        let userinfo = await getUser(token)

        await store.dispatch("setUserInfo", userinfo)
        fullName.value = store.getters.fullName

        email.value = store.getters.email
        connect(token, store.getters.email).catch((e) => {
            store.dispatch("postAlert", {
                title: "Kunne ikke koble til websocket",
                message: "" + e,
                type: "error",
            })
        })
        //Notification observer
        addObserver((m) => {
            refreshNotifications()

            if (router.currentRoute.value.name != "chat") {
                if (m.body == "new_chat") {
                    //Sends alert to user at new notification
                    store.dispatch("postAlert", {
                        title: "Ny melding",
                        message: "Du har fått en ny melding",
                        type: "info",
                    })
                }
            }
        })
    }
}
const toggleNavigationDrawer = () => {
    isNavigationOpen.value = !isNavigationOpen.value
}

const chatReceiverInfo = computed(() => store.getters.chatReceiverInfo)

/** Method for logout */
const logout = () => {
    cookies.remove("token")
    store.dispatch("setUserInfo", {})
    router.push("landingpage")
    location.reload()
}

/** Method for updating the chat */
onUpdated(() => {
    updateIsChat()
})

/** Method for checking if notifications is unread or not */
const refreshNotifications = () => {
    const token = store.getters.token

    getNotifications(token).then((res) => {
        console.log(res)
        unread.value = res.data.unread as boolean
    })
}

updateLoginState()
refreshNotifications()

const avatar = ref(require("@/assets/user-avatar-placeholder.png"))
</script>

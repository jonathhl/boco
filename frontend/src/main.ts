import { createApp } from "vue"
import App from "./App.vue"
import router from "./router"
import store from "./store"
import vuetify from "./plugins/vuetify"
import VueGoogleMaps from "@fawmi/vue-google-maps"
import { loadFonts } from "./plugins/webfontloader"

export const MAPS_KEY = "AIzaSyDzde7Vw-GhM79uV74pjTBnYolKg_tTW78"

loadFonts()

createApp(App)
    .use(router)
    .use(store)
    .use(vuetify)
    .use(VueGoogleMaps, {
        load: {
            key: MAPS_KEY,
        },
    })
    .mount("#app")

import {
    createRouter,
    createWebHistory,
    Router,
    RouteRecordRaw,
} from "vue-router"

// @ts-ignore
const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "landingpage",
        component: () =>
            import(
                /* webpackChunkName: "landing-page" */ "@/views/LandingPageView.vue"
            ),
    },
    {
        path: "/faq",
        name: "faq",
        component: () =>
            import(/* webpackChunkName: "faq" */ "@/views/FaqView.vue"),
    },
    {
        path: "/userprofile",
        name: "userprofile",
        meta: {
            requiresAuth: true,
        },
        component: () =>
            import(
                /* webpackChunkName: "user-profile" */ "@/views/ProfileView.vue"
            ),
    },
    {
        path: "/user/create",
        name: "createuser",
        component: () =>
            import(
                /* webpackChunkName: "create-user" */ "@/views/CreateUserView.vue"
            ),
    },
    {
        path: "/user/edit",
        name: "edituser",
        meta: {
            requiresAuth: true,
        },
        component: () =>
            import(
                /* webpackChunkName: "edit-user" */ "@/views/EditUserView.vue"
            ),
    },
    {
        path: "/listings",
        name: "alllistingview",
        component: () =>
            import(
                /* webpackChunkName: "all-listing" */ "../views/AllListingView.vue"
            ),
    },
    {
        path: "/listings/search/:searchstring",
        name: "listingsearch",
        component: () =>
            import(
                /* webpackChunkName: "search-listing" */ "../views/SearchListingView.vue"
            ),
    },
    {
        path: "/my-listings",
        name: "personallistingview",
        meta: {
            requiresAuth: true,
        },
        component: () =>
            import(
                /* webpackChunkName: "personal-listing" */ "../views/PersonalListingView.vue"
            ),
    },
    {
        path: "/listing/create",
        name: "createlisting",
        meta: {
            requiresAuth: true,
        },
        component: () =>
            import(
                /* webpackChunkName: "create-listing" */ "@/views/CreateListingView.vue"
            ),
    },
    {
        path: "/listing/:id",
        props: true,
        name: "detailedlisting",
        component: () =>
            import(
                /* webpackChunkName: "detailed-listing" */ "../views/DetailedListingView.vue"
            ),
    },
    {
        path: "/listing/:id/edit",
        props: true,
        name: "editlisting",
        meta: {
            requiresAuth: true,
        },
        component: () =>
            import(
                /* webpackChunkName: "edit-listing" */ "../views/EditListingView.vue"
            ),
    },
    {
        path: "/chat/:id",
        props: true,
        name: "chat",
        meta: {
            requiresAuth: true,
        },
        component: () =>
            import(/* webpackChunkName: "chat" */ "../views/ChatView.vue"),
    },
]

import store from "@/store"

const router: Router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
})

router.beforeEach((to, from, next) => {
    if (to.matched.some((record) => record.meta.requiresAuth)) {
        if (!store.getters.isLoggedIn) {
            next({ name: "landingpage" })
        } else {
            next()
        }
    } else {
        next()
    }
})

export default router

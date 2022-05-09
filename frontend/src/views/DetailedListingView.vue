<template>
    <v-container>
        <v-row>
            <v-col>
                <ViewListingComponent :listing="listing" />
            </v-col>
        </v-row>
        <v-row v-if="!(userEmail === listing?.email)">
            <v-col>
                <UserCardComponent
                    :firstname="firstname"
                    :email="email"
                    :rating="average"
                />
            </v-col>
        </v-row>
        <v-row>
            <v-col>
                <v-card class="rounded-xl">
                    <v-card-title>Kart</v-card-title>
                    <v-card-content>
                        <p><b>Adressen er</b> {{ address }}</p>
                        <br />
                        <GMapMap
                            :center="mapCenter"
                            :zoom="16"
                            map-type-id="terrain"
                            style="height: 400px"
                        >
                            <GMapMarker
                                :key="positions.indexOf(position)"
                                :position="{
                                    lat: position.latitude,
                                    lng: position.longitude,
                                }"
                                v-for="position in positions"
                            />
                        </GMapMap>
                    </v-card-content>
                </v-card>
            </v-col>
        </v-row>
        <v-row v-if="!(userEmail === listing?.email)">
            <v-col>
                <send-request-component />
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue"
import { useRoute } from "vue-router"
import { getListingById } from "@/services/api/listing"
import ViewListingComponent from "@/components/listing/ViewListingComponent.vue"
import SendRequestComponent from "@/components/SendRequestComponent.vue"
import { getPositionsFromQuery } from "@/services/api/map"
import UserCardComponent from "@/components/user/UserCardComponent.vue"
import { useStore } from "vuex"
import { getRatingsForUser } from "@/services/api/rating"

const route = useRoute()
const store = useStore()
const id = route.params.id as string
const listing = ref()
const address = ref("")
const userEmail = store.getters.email

const mapCenter = ref({ lat: 0, lng: 0 })
const positions = ref()

getListingById(parseInt(id)).then((l) => {
    listing.value = l
    address.value = l.address
    getPositionsFromQuery(l.address).then((p: any) => {
        mapCenter.value = {
            lat: p.data[0].latitude,
            lng: p.data[0].longitude,
        }
        positions.value = p?.data
    })
})

const firstname = ref("")
const email = ref("")
const average = ref()

onMounted(() => {
    getListingById(parseInt(id))
        .then((data) => {
            console.log(data)

            email.value = data.email
            firstname.value = data.firstname
        })
        .then(() => {
            getRatingsForUser(email.value, store.getters.token).then((data) => {
                average.value = data.average
            })
        })
})
</script>

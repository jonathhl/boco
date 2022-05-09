<template>
    <v-container>
        <v-form @submit.prevent="submit" class="w-100">
            <v-col cols="12">
                <div
                    class="d-flex align-center text-center justify-center mt-1 w-100"
                >
                    <input
                        data-cy="searchbar"
                        placeholder="Hva ønsker du å leie?"
                        class="input w-100 border"
                        v-model="searchbar"
                    />
                    <v-btn
                        data-cy="searchbtn"
                        class="text-primary-c ml-3 mb-3"
                        type="submit"
                        height="50"
                        width="50"
                        color="primary"
                        icon="mdi-magnify"
                    >
                    </v-btn>
                </div>
            </v-col>
        </v-form>
        <CardListComponent
            :title="`Søkeresultater for '${searchstring}'`"
            :listings="listings"
        />
        <v-card class="ma-5">
            <v-card-title>Kart</v-card-title>
            <v-card-content>
                <GMapMap
                    :center="{ lat: 65.3, lng: 17.0 }"
                    :zoom="4"
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
    </v-container>
</template>

<script setup lang="ts">
import { onMounted, Ref, ref } from "vue"
import { useRoute, useRouter } from "vue-router"
import CardListComponent from "@/components/listing/CardListComponent.vue"
import { getListingsByQuery } from "@/services/api/listing"
import { getPositionsFromQuery } from "@/services/api/map"

const route = useRoute()
const router = useRouter()

const listings = ref([])
const searchstring = ref(String(route.params.searchstring))
const searchbar = ref("")

const positions: Ref<Array<any>> = ref([])

onMounted(() => {
    getListingsByQuery(searchstring.value).then((data) => {
        listings.value = data
        setPositionsFromListings(data)
    })
})

function setPositionsFromListings(listings: Array<any>) {
    listings.forEach((l) => {
        getPositionsFromQuery(l.address).then((position: any) => {
            positions.value.unshift(position?.data[0])
        })
    })
}

const submit = () => {
    router
        .replace({
            name: "listingsearch",
            params: { searchstring: searchbar.value },
        })
        .then(() => {
            location.reload()
        })
}
</script>
<style scoped>
.input {
    height: 52px;
    border: #233847;
    border-radius: 30px;
    margin-bottom: 12px;
    padding-left: 16px;
}
</style>

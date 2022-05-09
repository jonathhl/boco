<template>
    <v-row>
        <v-col>
            <v-carousel>
                <v-carousel-item>
                    <v-img v-model="listingImage" :src="listingImage" />
                </v-carousel-item>
            </v-carousel>
        </v-col>
    </v-row>
    <v-spacer />
    <v-row class="container">
        <v-spacer />
        <v-col>
            <v-card-content
                class=""
                v-model="listingTitle"
                :title="listingTitle"
            >
                <h2>{{ listingTitle }}</h2>
                <i>{{ listingPrice }} kr/dagen</i>
            </v-card-content>
        </v-col>
        <v-col>
            <v-card-content v-model="listingDescription">
                {{ listingDescription }}
            </v-card-content>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-btn
                v-model="listingEmail"
                v-if="$store.getters.email === listingEmail"
                @click="$router.push({ name: 'editListing' })"
                >Rediger</v-btn
            >
        </v-col>
    </v-row>
</template>

<script>
import { getListingById } from "@/services/api/listing"
import { useRoute } from "vue-router"
import { defineComponent, ref } from "vue"
import { IMAGE_URL } from "@/services/api/urls"

export default defineComponent({
    setup() {
        const route = useRoute()
        const id = route.params.id

        const listingImage = ref("")
        const listingTitle = ref("")
        const listingDescription = ref("")
        const listingPrice = ref("")
        const listingEmail = ref("")

        const getListing = () => {
            getListingById(id).then((data) => {
                console.log(data)
                listingImage.value = IMAGE_URL + "/" + data.image
                if (listingImage.value == null) {
                    listingImage.value =
                        "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.pinimg.com%2Foriginals%2F07%2F20%2F73%2F07207337cc8bba66bf8696082a975890.jpg&f=1&nofb=1"
                }
                listingTitle.value = data.title
                listingDescription.value = data.description
                listingPrice.value = data.price
                listingEmail.value = data.email
            })
        }

        getListing()

        return {
            getListing,
            listingImage,
            listingTitle,
            listingDescription,
            listingPrice,
            listingEmail,
        }
    },
})
</script>

<style scoped>
.container {
    display: flex;
    flex-flow: column wrap;
    flex-basis: auto;
    max-width: 80%;
}
</style>

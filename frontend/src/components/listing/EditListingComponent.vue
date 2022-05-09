<template>
    <v-form @submit.prevent="save">
        <v-container>
            <v-row>
                <v-col cols="12">
                    <v-text-field
                        data-cy="title"
                        label="Tittel"
                        variant="outlined"
                        v-model="title"
                        type="text"
                        :error-messages="errors.title"
                    ></v-text-field>
                </v-col>
                <v-col cols="12" sm="6">
                    <v-switch
                        v-model="isFree"
                        :label="priceSwitchText"
                        @click="changePriceLabel"
                        class="mr-5"
                        color="primary"
                    ></v-switch>
                </v-col>
                <v-col cols="12" sm="6" v-if="isFree">
                    <v-text-field
                        data-cy="price"
                        label="Pris"
                        suffix="kr/dag"
                        variant="outlined"
                        v-model="price"
                        type="number"
                        :error-messages="errors.price"
                    ></v-text-field>
                </v-col>
                <v-col cols="12" sm="6">
                    <v-switch
                        v-model="showPhone"
                        :label="phoneSwitchText"
                        @click="changePhoneLabel"
                        color="primary"
                    ></v-switch>
                </v-col>
                <v-col cols="12" sm="6" v-if="showPhone">
                    <v-text-field
                        data-cy="phone"
                        label="Mobilnummer"
                        variant="outlined"
                        v-model="phonenumber"
                        :error-messages="errors.phonenumber"
                    ></v-text-field>
                </v-col>
                <v-col cols="12">
                    <v-text-field
                        data-cy="address"
                        label="Adresse"
                        variant="outlined"
                        v-model="address"
                        :error-messages="errors.address"
                    ></v-text-field>
                </v-col>
                <v-col cols="12">
                    <v-textarea
                        data-cy="description"
                        label="Beskrivelse"
                        variant="outlined"
                        v-model="description"
                        :error-messages="errors.description"
                    ></v-textarea>
                </v-col>
                <v-col>
                    <v-file-input
                        accept="image/*"
                        label="Last opp bilde"
                        @change="imageSelect"
                    ></v-file-input>
                    <v-img :src="IMAGE_URL + '/' + imageId" height="200">
                        <div v-if="imageId">
                            <v-btn @click="deleteImage">Slett Bilde</v-btn>
                        </div>
                    </v-img>
                </v-col>
                <v-col class="justify-center d-flex" cols="12">
                    <v-btn
                        data-cy="save"
                        append-icon="mdi-check"
                        id="Save"
                        color="success"
                        class="text-primary-c"
                        type="submit"
                        >Lagre</v-btn
                    >
                    <v-btn
                        data-cy="delete"
                        append-icon="mdi-trash-can"
                        color="error"
                        class="text-primary-c"
                        @click="deleteClick"
                        >Slett</v-btn
                    >
                </v-col>
            </v-row>
        </v-container>
    </v-form>
</template>
<script setup lang="ts">
import { deleteImageCall, uploadImage } from "@/services/api/image"
import {
    deleteListing,
    getListingById,
    putListingById,
} from "@/services/api/listing"
import { IMAGE_URL } from "@/services/api/urls"
import { useField, useForm } from "vee-validate"
import { onMounted, ref } from "vue"
import { useRouter, useRoute } from "vue-router"
import { useStore } from "vuex"
import { number, object, string } from "yup"

const router = useRouter()
const route = useRoute()
const isFree = ref(false)
const showPhone = ref(false)
const priceSwitchText = ref("Gratis")
const phoneSwitchText = ref("Vis telefonnummer")
const imageId = ref()
const id: number = +route.params.id
const store = useStore()

const changePriceLabel = () => {
    if (isFree.value) {
        priceSwitchText.value = "Gratis"
    } else {
        priceSwitchText.value = "Ikke gratis, skriv inn pris for leie: "
    }
}

const changePhoneLabel = () => {
    if (showPhone.value) {
        phoneSwitchText.value = "Skjul telefonnummer"
    } else {
        phoneSwitchText.value = "Vis telefonnummer, venligst skriv inn:"
    }
}

const imageSelect = (e: Event) => {
    //@ts-ignore
    uploadImage(e.target.files[0], store.getters.token).then((data) => {
        imageId.value = data.id
    })
}

const deleteImage = () => {
    deleteImageCall(imageId.value, store.getters.token).then(() => {
        imageId.value = 0
    })
}

const validationSchema = object({
    title: string().required("Dette feltet er påkrevd"),
    price: number().nullable().typeError("Må være tall"),
    phonenumber: string().nullable().min(6).typeError("Må være minst 6 siffer"),
    description: string().nullable().typeError("Må være tekst"),
    address: string().required("Dette feltet er påkrevd"),
})

const { handleSubmit, errors } = useForm({
    validationSchema,
})

const { value: title } = useField<string>("title")
const { value: price } = useField<number>("price")
const { value: phonenumber } = useField<string>("phonenumber")
const { value: description } = useField<string>("description")
const { value: address } = useField<string>("address")

const save = handleSubmit((values) => {
    if (!isFree.value) price.value = 0
    if (!showPhone.value) phonenumber.value = ""
    if (values.description == undefined) description.value = ""

    putListingById(
        store.getters.token,
        id,
        title.value,
        description.value,
        price.value,
        address.value,
        imageId.value,
        phonenumber.value
    )
        .then(() => {
            store.dispatch("postAlert", {
                title: "Annonse oppdatert",
                message: "Annonsen din er nå oppdatert",
                type: "success",
            })
            router.push({ name: "personallistingview" })
        })
        .catch((e) => {
            store.dispatch("postAlert", {
                title: "Oppdatering av annonse feilet",
                type: "error",
                message: `En feil førte til at annonsen ikke kunne oppdateres. Grunn: ${e}`,
            })
        })
})

const deleteClick = () => {
    deleteListing(store.getters.token, id).then((data) => {
        if (data) {
            store.dispatch("postAlert", {
                message: "Annonse slettet",
                type: "success",
                title: "Annonse slettet",
            })
            router.push({ name: "personallistingview" })
        }
    })
}

onMounted(() => {
    getListingById(id)
        .then((listing) => {
            title.value = listing.title
            if (listing.price != 0) isFree.value = true
            price.value = listing.price
            if (listing.phone != "") showPhone.value = true
            phonenumber.value = listing.phone
            address.value = listing.address
            imageId.value = listing.image
            description.value = listing.description
        })
        .catch(() => {
            console.log("error retrieving listing")
        })
})
</script>

<style scoped>
#Save {
    margin-right: 10px;
}
</style>

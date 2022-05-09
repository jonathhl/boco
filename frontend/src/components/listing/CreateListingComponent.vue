<template>
    <v-form @submit.prevent="submit">
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
                        data-cy="isFreeCreate"
                        color="primary"
                        v-model="isFree"
                        :label="priceSwitchText"
                        @click="changePriceLabel()"
                        class="mr-5"
                    ></v-switch>
                </v-col>
                <v-col cols="12" sm="6" v-if="isFree">
                    <v-text-field
                        data-cy="priceCreate"
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
                        data-cy="showPhone"
                        color="primary"
                        v-model="showPhone"
                        :label="phoneSwitchText"
                        @click="changePhoneLabel()"
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
                        data-cy="publish"
                        color="primary"
                        class="text-primary-c"
                        type="submit"
                    >
                        Publiser
                    </v-btn>
                </v-col>
            </v-row>
        </v-container>
    </v-form>
</template>

<script setup lang="ts">
import { ref } from "vue"
import { useStore } from "vuex"
import { number, object, string } from "yup"
import { useField, useForm } from "vee-validate"
import { postListing } from "@/services/api/listing"
import { uploadImage, deleteImageCall } from "@/services/api/image"
import { useRouter } from "vue-router"
import { IMAGE_URL } from "@/services/api/urls"

const store = useStore()
const router = useRouter()

const isFree = ref(false)
const showPhone = ref(false)
const priceSwitchText = ref("Gratis")
const phoneSwitchText = ref("Vis telefonnummer")
const imageId = ref(0)

const changePriceLabel = () => {
    if (isFree.value) {
        priceSwitchText.value = "Gratis"
    } else {
        priceSwitchText.value = "Ikke gratis, skriv inn pris for leie"
    }
}

const changePhoneLabel = () => {
    if (showPhone.value) {
        phoneSwitchText.value = "Skjul telefonnummer"
    } else {
        phoneSwitchText.value = "Vis telefonnummer, vennligst skriv inn"
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
    address: string().required("Adresse er påkrevd"),
})

const { handleSubmit, errors } = useForm({
    validationSchema,
})

const { value: title } = useField<string>("title")
const { value: price } = useField<number>("price")
const { value: phonenumber } = useField<string>("phonenumber")
const { value: description } = useField<string>("description")
const { value: address } = useField<string>("address")

const submit = handleSubmit((values) => {
    if (!isFree.value) price.value = 0
    if (!showPhone.value) phonenumber.value = ""
    if (values.description == undefined) description.value = ""

    postListing(
        store.getters.token,
        title.value,
        description.value,
        price.value,
        address.value,
        imageId.value,
        phonenumber.value
    )
        .then(() => {
            store.dispatch("postAlert", {
                title: "Annonse publisert",
                message: "Annonsen din er nå publisert",
                type: "success",
            })
            router.push({ name: "personallistingview" })
        })
        .catch((e) => {
            store.dispatch("postAlert", {
                title: "Oppretting av annonse feilet",
                type: "error",
                message: `En feil førte til at annonsen ikke kunne opprettes. Grunn: ${e}`,
            })
        })
})
</script>
<style>
.v-switch__thumb {
    color: white;
}
</style>

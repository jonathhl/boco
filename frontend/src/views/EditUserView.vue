<!-- The view for editing a user-->
<template>
    <v-container class="m-6">
        <!-- Import for a standard header component-->
        <HeaderComponent text="Rediger bruker" />
        <!-- The form where a user can edit personal information-->
        <v-form @submit.prevent="updateUserInfo">
            <v-container>
                <v-row>
                    <v-col cols="12" md="6">
                        <v-text-field
                            data-cy="firstname"
                            label="Fornavn"
                            placeholder=""
                            variant="outlined"
                            v-model="firstname"
                            type="text"
                            :error-messages="errors.firstname"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="6">
                        <v-text-field
                            data-cy="lastname"
                            label="Etternavn"
                            placeholder=""
                            variant="outlined"
                            v-model="lastname"
                            type="text"
                            :error-messages="errors.lastname"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                        <v-text-field
                            data-cy="email"
                            label="Epost"
                            variant="outlined"
                            placeholder=""
                            v-model="email"
                            :error-messages="errors.email"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="6">
                        <v-text-field
                            data-cy="phonenumber"
                            label="Mobilnummer"
                            variant="outlined"
                            placeholder=""
                            v-model="phonenumber"
                            :error-messages="errors.phonenumber"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="6">
                        <v-text-field
                            data-cy="zipcode"
                            label="Postnummer"
                            variant="outlined"
                            placeholder=""
                            v-model="zipcode"
                            :error-messages="errors.zipcode"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                        <v-text-field
                            data-cy="password"
                            label="Passord"
                            variant="outlined"
                            placeholder=""
                            :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                            :type="show1 ? 'text' : 'password'"
                            @click:append="show1 = !show1"
                            v-model="password1"
                            :error-messages="errors.password1"
                        />
                        <v-text-field
                            data-cy="confirmPassword"
                            label="Gjenta Passord"
                            variant="outlined"
                            placeholder=""
                            :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                            :type="show2 ? 'text' : 'password'"
                            @click:append="show2 = !show2"
                            v-model="password2"
                            :error-messages="errors.password2"
                        />
                    </v-col>
                </v-row>
                <v-col class="justify-center d-flex" cols="12">
                    <!-- Save changes button-->
                    <v-btn
                        data-cy="editUser"
                        color="primary"
                        variant="outlined"
                        type="submit"
                        class="w-100"
                    >
                        Lagre endringer
                    </v-btn>
                </v-col>
            </v-container>
        </v-form>
    </v-container>
</template>

<script setup lang="ts">
/** Imports: */
import { onMounted, ref } from "vue"
import { object, string, number } from "yup"
import { useForm, useField } from "vee-validate"
import { getUser, putUser } from "@/services/api/user"
import { useStore } from "vuex"
import { useRouter } from "vue-router"
import HeaderComponent from "@/components/HeaderComponent.vue"

/** Variables: */
const store = useStore()

const show1 = ref(false)
const show2 = ref(false)

const router = useRouter()

const registered = () => {
    router.push({
        name: "landingpage",
    })
}

const validationSchema = object({
    firstname: string().required("Dette feltet er påkrevd"),
    lastname: string().required("Dette feltet er påkrevd"),
    email: string().required().email("Fyll inn en gyldig mailadresse"),
    phonenumber: number().min(8).required("Dette feltet er påkrevd"),
    zipcode: number().required("Dette feltet er påkrevd"),
    password1: string(),
    password2: string(),
})

const { handleSubmit, errors } = useForm({
    validationSchema,
    initialValues: {
        firstname: "",
        lastname: "",
        email: "",
        phonenumber: 0,
        zipcode: 0,
        password1: "",
        password2: "",
    },
})

const { value: firstname } = useField("firstname")
const { value: lastname } = useField("lastname")
const { value: email } = useField("email")
const { value: phonenumber } = useField("phonenumber")
const { value: zipcode } = useField("zipcode")
const { value: password1 } = useField("password1")
const { value: password2 } = useField("password2")

/** Method to save changes for user profile */
const updateUserInfo = handleSubmit((values) => {
    if (values.password2 == undefined) password2.value = ""

    putUser(
        store.getters.token,
        values.email as string,
        values.firstname as string,
        values.lastname as string,
        values.phonenumber as number,
        values.zipcode as number,
        password2.value as string
    )
        .then(() => {
            store.dispatch("postAlert", {
                message: "Endring av bruker gjennomført",
                type: "success",
                title: "Brukeroppdatering fullført",
            })
            registered()
        })

        .catch((e) => {
            store.dispatch("postAlert", {
                title: "Endring av bruker feilet",
                type: "error",
                message: `En feil førte til at brukerkonto ikke kunne oppdateres. Grunn: ${e}`,
            })
        })
})

/** Method to load the current user before the view is loaded */
onMounted(() => {
    getUser(store.getters.token).then((data) => {
        email.value = data.email
        firstname.value = data.firstname
        lastname.value = data.lastname
        phonenumber.value = data.phone
        zipcode.value = data.zip
    })
})
</script>

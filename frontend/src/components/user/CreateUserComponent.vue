<!--The component to create a new user for the application-->
<template>
    <!--The form a new users must fill out to create a user-->
    <v-form @submit.prevent="submit">
        <v-container>
            <v-row>
                <v-col class="d-flex flex-row mt-5 .rounded-xl">
                    <v-text-field
                        data-cy="firstname"
                        label="Fornavn"
                        placeholder="Ola"
                        variant="outlined"
                        v-model="firstname"
                        type="text"
                        class="mr-5"
                        :error-messages="errors.firstname"
                    ></v-text-field>
                    <v-text-field
                        data-cy="lastname"
                        label="Etternavn"
                        placeholder="Nordmann"
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
                        placeholder="ola.nodrmann@gmail.ru"
                        variant="outlined"
                        v-model="email"
                        :error-messages="errors.email"
                    ></v-text-field>
                    <v-text-field
                        data-cy="phonenumber"
                        label="Mobilnummer"
                        placeholder="93293300"
                        variant="outlined"
                        v-model="phonenumber"
                        :error-messages="errors.phonenumber"
                    ></v-text-field>
                </v-col>
                <v-col class="d-flex flex-row mt-5">
                    <v-text-field
                        data-cy="zipcode"
                        label="Postnummer"
                        placeholder="7030"
                        variant="outlined"
                        v-model="zipcode"
                        class="mr-5"
                        :error-messages="errors.zipcode"
                    ></v-text-field>
                    <v-text-field
                        data-cy="city"
                        label="Poststed"
                        placeholder="Trondheim"
                        variant="outlined"
                        v-model="city"
                        type="text"
                        :error-messages="errors.city"
                    ></v-text-field>
                </v-col>
                <v-col cols="12">
                    <v-text-field
                        data-cy="password"
                        label="Passord"
                        :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                        variant="outlined"
                        :type="show1 ? 'text' : 'password'"
                        @click:append="show1 = !show1"
                        v-model="password"
                        :error-messages="errors.password"
                    ></v-text-field>
                    <v-text-field
                        data-cy="confirmPassword"
                        label="Gjenta Passord"
                        variant="outlined"
                        :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                        :type="show2 ? 'text' : 'password'"
                        @click:append="show2 = !show2"
                        v-model="confirmPassword"
                        :error-messages="errors.confirmPassword"
                    ></v-text-field>
                </v-col>
                <!--Register button-->
                <v-col class="justify-center d-flex" cols="12">
                    <v-btn
                        data-cy="createUser"
                        color="primary"
                        class="text-primary-c w-100"
                        type="submit"
                        >Registrer deg</v-btn
                    >
                </v-col>
            </v-row>
        </v-container>
    </v-form>
</template>

<script setup lang="ts">
/** Imports: */
import { ref } from "vue"
import { object, string, number, ref as yupRef } from "yup"
import { useForm, useField } from "vee-validate"
import { postUser } from "@/services/api/user"
import router from "@/router"
import store from "@/store"

/** Variables: */
const show1 = ref(false)
const show2 = ref(false)

const validationSchema = object({
    firstname: string().required("Dette feltet er påkrevd"),
    lastname: string().required("Dette feltet er påkrevd"),
    email: string().required().email("Fyll inn en gyldig mailadresse"),
    phonenumber: number()
        .min(8)
        .typeError("Mobilnummer må være et tall")
        .required("Dette feltet er påkrevd"),
    zipcode: number().required("Dette feltet er påkrevd"),
    city: string().required("Dette feltet er påkrevd"),
    password: string().required("Dette feltet er påkrevd"),
    confirmPassword: string()
        .required("Dette feltet er påkrevd")
        .oneOf([yupRef("password"), null], "Passordene må matche"),
})

const { handleSubmit, errors } = useForm({
    validationSchema,
})

const { value: firstname } = useField("firstname")
const { value: lastname } = useField("lastname")
const { value: email } = useField("email")
const { value: phonenumber } = useField("phonenumber")
const { value: zipcode } = useField("zipcode")
const { value: city } = useField("city")
const { value: password } = useField("password")
const { value: confirmPassword } = useField("confirmPassword")

/** Methods for create a new user when the user submit a form */
const submit = handleSubmit((values) => {
    if (
        values.email &&
        values.firstname &&
        values.lastname &&
        values.phonenumber &&
        values.zipcode &&
        values.password
    )
        postUser(
            values.email,
            values.firstname,
            values.lastname,
            values.phonenumber,
            values.zipcode,
            values.password
        )
            .then(() => {
                store.dispatch("postAlert", {
                    message: "Registerering av bruker gjennomført",
                    type: "success",
                    title: "Brukerregisterring fullført",
                })
                router.push({ name: "landingpage" })
            })
            .catch((e) => {
                store.dispatch("postAlert", {
                    title: "Registrering av bruker feilet",
                    type: "error",
                    message: `En feil førte til at brukeren ikke kunne oppdateres. Grunn: ${e}`,
                })
            })
})
</script>

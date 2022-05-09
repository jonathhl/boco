<!-- The component for login popup-->
<template>
    <v-dialog>
        <v-card class="rounded-xl">
            <v-form @submit.prevent="submit">
                <v-col>
                    <v-card-title>
                        Logg inn
                        <v-spacer></v-spacer>
                        <!--Close-button -->
                        <v-btn @click="$emit('update:modelValue', false)">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-col class="text-center mt-3" v-if="errormessage">
                        <v-alert type="error">{{ errormessage }}</v-alert>
                    </v-col>
                </v-col>
                <v-container>
                    <v-col>
                        <v-text-field
                            data-cy="username"
                            v-model="username"
                            type="text"
                            label="Brukernavn (Epost)"
                            variant="outlined"
                            :error-messages="errors.username"
                        >
                        </v-text-field>
                    </v-col>
                    <v-col>
                        <v-text-field
                            data-cy="password"
                            v-model="password"
                            variant="outlined"
                            label="Passord"
                            :error-messages="errors.password"
                            :type="showPassword ? 'text' : 'password'"
                            :append-icon="
                                showPassword ? 'mdi-eye' : 'mdi-eye-off'
                            "
                            @click:append="showPassword = !showPassword"
                        >
                        </v-text-field>
                    </v-col>
                    <!-- Login button-->
                    <v-btn
                        data-cy="submit"
                        class="mr-2 text-primary-c"
                        color="primary"
                        type="submit"
                        rounded
                    >
                        Logg inn
                    </v-btn>

                    <!-- Register button. Sends you to the view where you can create a new user-->
                    <v-btn
                        data-cy="register"
                        rounded
                        variant="outlined"
                        @click="register(), $emit('update:modelValue', false)"
                    >
                        Registrer deg
                    </v-btn>
                </v-container>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script setup lang="ts">
/** Imports: */
import { ref, defineEmits } from "vue"
import { useForm, useField } from "vee-validate"
import { object, string } from "yup"
import { useRouter } from "vue-router"
import { getToken } from "@/services/api/user"
import { useCookies } from "vue3-cookies"
import { useStore } from "vuex"

/** Variables: */
const store = useStore()
const { cookies } = useCookies()
const router = useRouter()

const emit = defineEmits(["close", "update-login-state", "update:modelValue"])
const showPassword = ref(false) //Show / unhide password
const errormessage = ref("") //Cant log in

const validationSchema = object({
    username: string()
        .required("Dette feltet er påkrevd")
        .email("Brukernavnet må være en e-post"),
    password: string().required("Dette feltet er påkrevd"),
})

const { errors, handleSubmit } = useForm({
    validationSchema,
})

const { value: username } = useField("username")
const { value: password } = useField("password")

/** Method for login a user*/
const submit = handleSubmit(async (values) => {
    if (values.username && values.password) {
        try {
            let token = await getToken(values.username, values.password)

            if (!token) {
                throw "No token received"
            }

            cookies.set("token", token, "1d")
            await store.dispatch("setToken", { token })

            emit("update-login-state")
            await store.dispatch("postAlert", {
                title: "Login vellykket",
                type: "success",
            })

            errormessage.value = ""
            emit("update:modelValue", false)
        } catch (message) {
            console.error(`Unable to finish log in '${message}'`)
            errormessage.value = "Feil brukernavn eller passord"
        }
    } else {
        console.error("Login error")
    }
})

/**  Method for sending user to view to create new user */
const register = () => {
    router.push({ name: "createuser" })
}
</script>

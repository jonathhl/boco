<!-- The component for time picker-->
<template>
    <v-container>
        <v-row>
            <v-col cols="12" lg="6">
                <v-text-field
                    type="date"
                    label="Fra"
                    v-model="date1"
                ></v-text-field>
            </v-col>

            <v-col cols="12" lg="6">
                <v-text-field
                    type="date"
                    label="Til"
                    v-model="date2"
                ></v-text-field>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
/** Imports: */
import { useField } from "vee-validate"
import { string } from "yup"
const { value: date1 } = useField < string > "date1"
const { value: date2 } = useField < string > "date2"
</script>

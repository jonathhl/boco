<!-- View for FAQ-->
<template>
    <v-container>
        <v-row>
            <v-col>
                <!-- Import for a standard header component-->
                <HeaderComponent text="Ofte stilte spørsmål" />
                <v-expansion-panels variant="outlined" class="border rounded">
                    <v-expansion-panel
                        v-for="entry in entries"
                        :key="entry.question"
                        :title="entry.question"
                        :text="entry.answer"
                    ></v-expansion-panel>
                </v-expansion-panels>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup lang="ts">
/** Imports: */
import HeaderComponent from "@/components/HeaderComponent.vue"
const entries = [
    {
        question: "Fungerer det?",
        answer: "Ja bro.",
    },
]
</script>

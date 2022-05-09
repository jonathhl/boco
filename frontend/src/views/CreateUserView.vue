<!-- The view for creating a new user-->
<template>
    <v-container>
        <!-- Import for a standard header component-->
        <HeaderComponent text="Registrer ny bruker" />
        <v-row>
            <v-col>
                <!-- Uses the component for creating a new user-->
                <create-user-component />
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup lang="ts">
/** Imports: */
import CreateUserComponent from "@/components/user/CreateUserComponent.vue"
import HeaderComponent from "@/components/HeaderComponent.vue"
</script>

<template>
    <router-link
        :to="{ name: 'detailedlisting', params: { id: props.item.id } }"
        class="link w-100"
    >
        <v-card class="ma-0 item" variant="outlined">
            <v-img
                aspect-ratio="1"
                :src="`${url}/${props.item.image}`"
                v-if="props.item.image"
            ></v-img>
            <v-img v-else src="@/assets/smuggoat.jpg"></v-img>
            <v-card-title>{{ props.item.title }}</v-card-title>
            <v-card-text>
                <div>{{ props.item.price }} kr/dag</div>
            </v-card-text>
        </v-card>
    </router-link>
</template>
<script setup lang="ts">
import { defineProps } from "vue"
import { IMAGE_URL } from "@/services/api/urls"

const url = IMAGE_URL

const props = defineProps<{
    item: {
        id: number
        title: string
        price: number
        image: number
    }
}>()
</script>

<style scoped>
.item:hover {
    transform: scale(1.02);
}

.link {
    color: #2c3e50;
    text-decoration: none;
}
</style>

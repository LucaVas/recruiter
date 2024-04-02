<script setup lang="ts">
import type { MenuItem } from '../../layouts/DashboardLayout.vue';

const { menuItem } = defineProps<{
  menuItem: MenuItem;
}>();
defineEmits<{
  (e: 'closeMenu'): void;
}>();
</script>

<template>
  <div>
    <ul class="list-none">
      <li>
        <!-- group -->
        <div>
          <span class="font-medium">{{ menuItem.group }}</span>
        </div>
      </li>
      <!-- links -->
      <ul class="ml-4 list-none overflow-hidden p-0">
        <li v-for="link in menuItem.links" :key="link.name">
          <RouterLink
            :to="{ name: link.view }"
            @click="$emit('closeMenu')"
            class="border-round text-700 hover:surface-100 transition-duration-150 p-ripple flex cursor-pointer items-center p-3 transition-colors"
            ><i :class="link.icon"></i>
            <span class="ml-2 font-medium">{{ link.name }}</span>
          </RouterLink>
        </li>
      </ul>
    </ul>
  </div>
</template>

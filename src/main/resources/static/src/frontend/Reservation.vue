<template>
  <div class="restaurant-container">
    <header class="header">
      <h1>Laua broneerimine</h1>
      <p>Valige sobiv laud plaanilt</p>
    </header>

    <div v-if="loading" class="loader">Laadimine...</div>

    <div v-else class="floor-plan">
      <div
          v-for="table in tables"
          :key="table.id"
          class="table-marker"
          :style="{ left: table.xcoordinate + 'px', top: table.ycoordinate + 'px' }"
          :class="getTableClass(table)"
          @click="selectTable(table)"
      >
        <div class="table-info">
          <span class="table-id">№{{ table.id }}</span>
          <span class="table-cap">👥{{ table.capacity }}</span>
        </div>
      </div>
    </div>

    <div v-if="selectedTable" class="selection-detail">
      <h3>Valitud laud №{{ selectedTable.id }}</h3>
      <p>Mahutavus: {{ selectedTable.capacity }} inimest</p>
      <p>Tsoon: {{ selectedTable.zone }}</p>
      <button @click="$emit('go-to-customer-info', selectedTable)" class="btn-book">
        Jätka broneerimist
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

defineProps(['filterData']);
defineEmits(['go-to-customer-info']);

const tables = ref([]);
const selectedTable = ref(null);
const loading = ref(true);

const fetchTables = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/tables');
    if (!response.ok) throw new Error('Network response was not ok');
    tables.value = await response.json();
  } catch (error) {
    console.error("Fetch error:", error);
  } finally {
    loading.value = false;
  }
};

const getTableClass = (table) => {
  return {
    'near-window': table.nearWindow,
    'size-2': table.capacity <= 2,
    'size-4': table.capacity > 2 && table.capacity <= 4,
    'size-6': table.capacity > 4,
    'is-selected': selectedTable.value?.id === table.id
  };
};

const selectTable = (table) => {
  selectedTable.value = table;
};

onMounted(fetchTables);
</script>

<style scoped>
/* Добавьте стиль для выделения выбранного стола */
.is-selected {
  background-color: #4caf50 !important;
  box-shadow: 0 0 15px rgba(76, 175, 80, 0.5);
}
/* Остальные стили без изменений */
</style>
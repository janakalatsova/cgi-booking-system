<template>
  <div class="restaurant-container">
    <header class="header">
      <h1>Laua broneerimine</h1>
      <p>Valige sobiva lauda</p>
    </header>

    <div class="floor-plan">
      <div
          v-for="table in tables"
          :key="table.id"
          class="table-marker"
          :style="{ left: table.xcoordinate + 'px', top: table.ycoordinate + 'px' }"
          :class="{
       'near-window': table.nearWindow,
       'size-2': table.capacity <= 2,
       'size-4': table.capacity > 2 && table.capacity <= 4,
       'size-6': table.capacity > 4
    }"
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
      <p>Mahutavus: {{ selectedTable.capacity }} in.</p>
      <p>Tsoon: {{ selectedTable.zone }}</p>
      <button @click="$emit('go-to-customer-info', selectedTable)" class="btn-book">
        Reserveeri
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
const fetchTables = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/tables');
    if (!response.ok) throw new Error('Ошибка сервера');
    tables.value = await response.json();
  } catch (error) {
    console.error("Не удалось загрузить столы:", error);
  }
};

const selectTable = (table) => {
  selectedTable.value = table;
};

onMounted(fetchTables);
</script>

<style scoped>

.header {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-family: sans-serif;
}
.floor-plan {
  position: relative;
  width: 800px;
  height: 600px;
  background: #fdfdfd;
  border: 3px solid #444;
  margin: 20px auto;
  background-image: radial-gradient(#ddd 1px, transparent 1px);
  background-size: 20px 20px;
  border-radius: 8px;
}

.table-marker {
  position: absolute;
  width: 70px;
  height: 70px;
  background-color: #795548;
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
  transform: translate(-50%, -50%);
}

.size-2 {
  width: 60px;
  height: 60px;
}

.size-4 {
  width: 100px;
  height: 60px;
}

.size-6 {
  width: 150px;
  height: 70px;
}

.table-marker:hover {
  transform: translate(-50%, -50%) scale(1.05);
  background-color: #5d4037;
  z-index: 10;
}

.near-window {
  border: 4px solid #00bcd4;
}

.table-info {
  display: flex;
  flex-direction: column;
  text-align: center;
  color: white;
  font-weight: bold;
}

.table-id { font-size: 16px; }
.table-cap { font-size: 12px; }

.selection-detail {
  text-align: center;
  padding: 20px;
  background: #eee;
  border-radius: 8px;
  width: 300px;
  margin: 20px auto;
}

.btn-book {
  background: #4caf50;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}
</style>
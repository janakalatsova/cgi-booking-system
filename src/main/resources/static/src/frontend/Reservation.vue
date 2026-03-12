<template>
  <div class="restaurant-container">
    <button @click="$emit('go-back')" class="btn-secondary">← Muuda otsingut</button>
    <header class="header">
      <h1>Laua broneerimine</h1>
      <p>Valige sobiv laud plaanilt </p>
    </header>

    <div class="legend">
      <div class="legend-item"><span class="dot recommended"></span> Parim valik</div>
      <div class="legend-item"><span class="dot window"></span> Akna juures</div>
      <div class="legend-item"><span class="dot occupied"></span> Reserveeritud</div>
      <div class="legend-item"><span class="dot disabled"></span> Ei sobi filtritele</div>
      <div class="legend-item"><span class="dot selected"></span> Valitud laud</div>
    </div>

    <div class="floor-plan">
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
      <h3>Tsoon: {{ formatZone(selectedTable.zone) }}</h3>

      <button @click="$emit('go-to-customer-info', selectedTable)" class="btn-book">
        Reserveeri
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const props = defineProps(['filterData']);
const emit = defineEmits(['go-to-customer-info', 'go-back']);

const tables = ref([]);
const selectedTable = ref(null);

const getTableClass = (table) => {
  return {
    'near-window': table.nearWindow,
    'is-selected': selectedTable.value?.id === table.id,
    'is-occupied': table.occupied === true,
    'is-recommended': table.isRecommended === true,
    'is-disabled': !table.isSuitable,
    'size-2': table.capacity <= 2,
    'size-4': table.capacity > 2 && table.capacity <= 4,
    'size-6': table.capacity > 4
  };
};

const fetchTables = async () => {
  try {
    // 1. Сначала загружаем ВООБЩЕ ВСЕ столы для карты
    const allRes = await fetch(`http://localhost:8080/api/tables?start=${props.filterData.startTime}&end=${props.filterData.endTime}`);
    const allData = await allRes.json();
    const allTablesArray = Array.isArray(allData) ? allData : [];

    // 2. Теперь загружаем результаты поиска (подходящие столы)
    const searchRes = await fetch('http://localhost:8080/api/tables/search', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        guests: props.filterData.guests,
        startTime: props.filterData.startTime,
        endTime: props.filterData.endTime,
        zone: props.filterData.zone,
        ...props.filterData.preferences
      })
    });

    const suitableTables = await searchRes.json();
    const suitableIds = new Set(suitableTables.map(t => t.id));

    // 3. Объединяем: оставляем все столы, но помечаем, какие из них активны
    tables.value = allTablesArray.map(table => ({
      ...table,
      isSuitable: suitableIds.has(table.id) // Добавляем флаг пригодности
    }));

    // Помечаем первый из подходящих как рекомендованный
    if (suitableTables.length > 0) {
      const bestId = suitableTables[0].id;
      const bestTable = tables.value.find(t => t.id === bestId);
      if (bestTable) bestTable.isRecommended = true;
    }
  } catch (error) {
    console.error("Viga laudade laadimisel:", error);
  }
};


const formatZone = (zone) => {
  const zoneNames = {
    'TERRACE': 'Terrass',
    'PRIVATE': 'Privaatruum',
    'C_HALL': 'Sisesaal'
  };
  return zoneNames[zone] || zone;
};

const selectTable = (table) => {
  if (table.occupied || !table.isSuitable) return;

  selectedTable.value = table;
};



onMounted(fetchTables);
</script>

<style scoped>
.header, h1, p {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-family: sans-serif;
  color: #333;
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
  background-color: #795548;
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transform: translate(-50%, -50%);
}

.size-2 { width: 60px; height: 60px; }
.size-4 { width: 100px; height: 60px; }
.size-6 { width: 150px; height: 70px; }

.table-marker:hover:not(.is-occupied) {
  transform: translate(-50%, -50%) scale(1.05);
  background-color: #5d4037;
}

.near-window { border: 4px solid #00bcd4; }
.is-selected { outline: 4px solid #4caf50; outline-offset: 4px; }

.is-occupied {
  background-color: #ff4d4d !important; /* Насыщенный красный */
  cursor: not-allowed !important;
  opacity: 1; /* Убираем прозрачность, чтобы цвет был плотным */
  box-shadow: none;
}

.is-occupied .table-info {
  background-color: transparent !important; /* Убираем лишний фон у текста */
  color: #000 !important; /* Черный текст для контраста */
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
  font-weight: bold;
}

.btn-secondary {
  position: absolute;
  left: 20px; /* Добавьте отступ от левого края */
  top: 20px;  /* Добавьте отступ сверху */
  background: white;
  border: 1px solid #ced4da;
  padding: 10px 18px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  color: #795548;
  z-index: 100; /* Чтобы кнопка всегда была поверх других элементов */
  transition: all 0.2s;
}

.is-recommended {
  outline: 5px solid gold; /* Золотая рамка для лучшего стола */
  outline-offset: 4px;
  z-index: 10;
}

.is-disabled {
  color: black !important;
  opacity: 0.5;
  cursor: not-allowed !important;
}

/* Контейнер для легенды */
.legend {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.dot { width: 12px; height: 12px; border-radius: 50%; display: inline-block; }
.suitable { background-color: #795548; }
.recommended { border: 2px solid gold; background-color: #795548; }
.window { border: 2px solid #00bcd4; background-color: #795548; }
.occupied { background-color: #ff4d4d; }
.disabled { background-color: #795548; opacity: 0.5; }
.selected {background-color: #4caf50}


/* Плавное выделение при выборе */
.is-selected {
  outline: 4px solid #4caf50;
  outline-offset: 4px;
  transform: translate(-50%, -50%) scale(1.1) !important;
  z-index: 100;
}

</style>
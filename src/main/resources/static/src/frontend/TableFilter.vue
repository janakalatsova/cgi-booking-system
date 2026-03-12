<template>
  <div class="filter-wrapper">
    <div class="filter-card">
      <h2>Broneerimise detailid</h2>

      <div class="form-group">
        <label>Mitu inimest: <span class="guest-count">{{ filterData.guests }}</span></label>
        <div class="range-container">
          <input
              type="range" min="2" max="8" step="1"
              v-model.number="filterData.guests"
              class="custom-slider"
          />
        </div>
      </div>

      <div class="form-group">
        <label>Algusaeg</label>
        <input type="datetime-local" v-model="filterData.startTime" class="date-input" />
      </div>

      <div class="form-group">
        <label>Lõpuaeg</label>
        <input
            type="datetime-local"
            v-model="filterData.endTime"
            class="date-input"
        />
      </div>

      <div class="form-group">
        <label>Tsoon</label>
        <select v-model="filterData.zone" class="date-input">
          <option value="">Kõik tsoonid</option>
          <option value="TERRACE">Terrass</option>
          <option value="C_HALL">Sisesaal</option>
          <option value="PRIVATE">Privaatruum</option>
        </select>
      </div>

      <div class="form-group">
        <label>Eelistused:</label>
        <div class="checkbox-group">
          <label class="checkbox-item"><input type="checkbox" v-model="filterData.preferences.quiet" /><span>Vaikne koht</span></label>
          <label class="checkbox-item"><input type="checkbox" v-model="filterData.preferences.kids" /><span>Lastenurga lähedal</span></label>
          <label class="checkbox-item"><input type="checkbox" v-model="filterData.preferences.window" /><span>Akna juures</span></label>
        </div>
      </div>
      <button @click="confirmFilter" class="next-btn">Vaata vabu laudu</button>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue';
const emit = defineEmits(['go-to-map']);

const getCurrentLocalISO = (date = new Date()) => {
  const offset = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offset).toISOString().slice(0, 16);
};

const filterData = reactive({
  guests: 2,
  startTime: getCurrentLocalISO(),
  endTime: getCurrentLocalISO(new Date(Date.now() + 60 * 60 * 1000)),
  zone: '',
  preferences: { quiet: false, kids: false, window: false }
});

watch(() => filterData.startTime, (newVal) => {
  if (!newVal) return;
  const start = new Date(newVal);
  const end = new Date(start.getTime() + 60 * 60 * 1000);
  filterData.endTime = getCurrentLocalISO(end);
});

const confirmFilter = () => {
  const startTimeClean = filterData.startTime.slice(0, 16);
  const endTimeClean = filterData.endTime.slice(0, 16);

  const start = new Date(startTimeClean);
  const end = new Date(endTimeClean);

  if (end <= start) {
    alert("Lõpuaeg peab olema pärast algusaega!");
    return;
  }

  if ((end - start) > 3 * 60 * 60 * 1000) {
    alert("Broneering ei tohi olla pikem kui 3 tundi!");
    return;
  }

  emit('go-to-map', {
    ...filterData,
    startTime: startTimeClean,
    endTime: endTimeClean
  });
};
</script>

<style scoped>
.filter-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  font-family: sans-serif;
  padding: 20px;
}

.filter-card {
  background: white;
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

h2 {
  color: #5d4037;
  text-align: center;
  margin-bottom: 25px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.guest-count {
  font-weight: bold;
  color: #5d4037;
  font-size: 1.1em;
}

.checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
  background: #fcfaf9;
  padding: 12px;
  border-radius: 8px;
}

.checkbox-item {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  margin-bottom: 0;
  color: #444;
}

.checkbox-item input {
  width: 18px;
  height: 18px;
  accent-color: #5d4037;
  cursor: pointer;
}

.custom-slider {
  width: 100%;
  -webkit-appearance: none;
  height: 6px;
  background: #eee;
  border-radius: 5px;
}

.custom-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 20px;
  height: 20px;
  background: #5d4037;
  border-radius: 50%;
  cursor: pointer;
  border: 2px solid white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.range-numbers span {
  font-size: 11px;
  color: #ccc;
}

.range-numbers span.active {
  color: #5d4037;
  font-weight: bold;
}

.date-input, .comment-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-sizing: border-box;
}

.next-btn {
  width: 100%;
  background: #5d4037;
  color: white;
  border: none;
  padding: 14px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 10px;
}

.next-btn:hover {
  background: #795548;
}
</style>
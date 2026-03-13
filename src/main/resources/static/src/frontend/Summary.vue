<template>
  <div class="filter-wrapper">
    <div class="filter-card" v-if="reservation">
      <h2 style="color: #5d4037;">Broneering kinnitatud!</h2>
      <p>Aitäh, <strong>{{ reservation.name }}</strong>!</p>
      <hr style="border: 0; border-top: 1px dashed #ddd; margin: 15px 0;" />

      <div class="details-list">
        <p><strong>Laud:</strong> {{ reservation.tableName }}</p>
        <p><strong>Algusaeg:</strong> {{ formatTime(reservation.startTime) }}</p>
        <p><strong>Lõpuaeg:</strong> {{ formatTime(reservation.endTime) }}</p>
      </div>

      <button @click="$emit('go-home')" class="next-btn">Tagasi avalehele</button>
    </div>
    <div v-else class="filter-card">
      <p>Laadimine...</p>
    </div>
  </div>
</template>

<script setup>
defineProps(['reservation']);

const formatTime = (dateTimeStr) => {
  if (!dateTimeStr) return '';
  return dateTimeStr.replace('T', ' ').substring(0, 16);
};
</script>

<style scoped>
.filter-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.filter-card {
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0,0,0,0.1);
  text-align: center;
  max-width: 400px;
  width: 100%;
}

.details-list {
  text-align: left;
  margin: 20px 0;
  padding: 10px 20px;
  background: #fcfaf9;
  border-radius: 12px;
}

h2 {
  color: #5d4037;
  margin-bottom: 20px;
  font-size: 24px;
}

p {
  color: #555;
  font-size: 16px;
  line-height: 1.6;
  margin: 8px 0;
}

hr {
  border: 0;
  border-top: 1px dashed #ddd;
  margin: 20px 0;
}

strong {
  color: #333;
}

.next-btn {
  width: 100%;
  background: #5d4037;
  color: white;
  border: none;
  padding: 15px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 20px;
  transition: background 0.3s ease;
}

.next-btn:hover { background: #795548; }

.filter-card {
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
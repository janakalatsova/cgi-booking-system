<template>
  <div class="filter-wrapper">
    <div class="filter-card">
      <h2>Kliendi andmed</h2>
      <div class="form-group">
        <label>Nimi</label>
        <input v-model="customer.name" type="text" class="date-input" />
      </div>
      <div class="form-group">
        <label>Telefon</label>
        <input v-model="customer.phone" type="tel" class="date-input" @input="validatePhone" />
      </div>
      <button @click="submitBooking" class="next-btn">Kinnita</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
const emit = defineEmits(['go-to-summary']);

const customer = ref({
  name: '',
  phone: ''
});

const validatePhone = (event) => {
  let val = event.target.value.replace(/\D/g, '');

  if (val.length > 8) {
    val = val.slice(0, 8);
  }

  customer.value.phone = val;
};

const submitBooking = () => {
  if (!customer.value.name || !customer.value.phone) {
    alert("Palun täitke kõik väljad!");
    return;
  }

  if (customer.value.phone.length !== 8) {
    alert("Telefoni number peab olema täpselt 8 numbrit pikk!");
    return;
  }

  emit('go-to-summary', { ...customer.value });
};
</script>

<style scoped>
.filter-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}
.filter-card {
  background: white;
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 400px;
}
.date-input {
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
  font-weight: bold;
  cursor: pointer;
  margin-top: 15px;
}
</style>
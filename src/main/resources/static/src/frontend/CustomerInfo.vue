<template>
  <div class="filter-wrapper">
    <div class="filter-card">
      <h2>Kliendi andmed</h2>

      <div class="form-group">
        <label>Nimi</label>
        <input v-model="customer.name" type="text" class="date-input" placeholder="Teie nimi" />
      </div>

      <div class="form-group">
        <label>Telefon</label>
        <PhoneInput
            v-model="customer.phone"
            v-model:country-code="currentCountry"
            @update="handlePhoneUpdate"
        />
        <small v-if="customer.phone && !isPhoneValid" class="error-text">
          Palun sisestage korrektne number
        </small>
      </div>

      <button @click="submitBooking" class="next-btn">Kinnita broneering</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import PhoneInput from './PhoneInput.vue';

const emit = defineEmits(['go-to-summary']);

const customer = ref({ name: '', phone: '' });
const currentCountry = ref('EE');
const isPhoneValid = ref(false);
const e164Phone = ref('');

const handlePhoneUpdate = (results) => {
  isPhoneValid.value = results.isValid;
  e164Phone.value = results.e164; // Формат для БД: +372...
};

const submitBooking = () => {
  if (!customer.value.name || !customer.value.phone) {
    alert("Palun täitke kõik väljad!");
    return;
  }

  if (!isPhoneValid.value) {
    alert("Telefoni number ei ole korrektne!");
    return;
  }

  emit('go-to-summary', {
    name: customer.value.name,
    phone: e164Phone.value
  });
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
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 450px;
}
.form-group {
  margin-bottom: 20px;
  text-align: left;
}
.date-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-sizing: border-box;
}
.error-text {
  color: #e53935;
  font-size: 12px;
  margin-top: 5px;
  display: block;
}
.next-btn {
  width: 100%;
  background-color: #5d4037;
  color: white;
  padding: 15px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 16px;
  transition: background 0.3s;
}
.next-btn:hover {
  background-color: #4e342e;
}
</style>
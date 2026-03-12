<template>
  <div class="app-container">
    <Homepage v-if="currentPage === 'home'" @go-to-table-info="currentPage = 'table_filter'" />

    <TableFilter v-if="currentPage === 'table_filter'" @go-to-map="handleFilterConfirmed" />

    <Reservation
        v-if="currentPage === 'map'"
        :filterData="bookingData"
        @go-to-customer-info="handleTableSelected"
        @go-back="currentPage = 'table_filter'"
    />

    <CustomerInfo v-if="currentPage === 'customer_info'" @go-to-summary="handleBookingComplete" />

    <Summary v-if="currentPage === 'summary'" :reservation="finalReservation" @go-home="currentPage = 'home'" />
  </div>
</template>

<script setup>
import {ref} from 'vue';
import Homepage from './Homepage.vue';
import TableFilter from './TableFilter.vue';
import Reservation from './Reservation.vue';
import CustomerInfo from './CustomerInfo.vue';
import Summary from './Summary.vue';

const currentPage = ref('home');
const bookingData = ref(null);
const selectedTable = ref(null);
const finalReservation = ref(null);

const handleFilterConfirmed = (data) => {
  bookingData.value = data;
  currentPage.value = 'map';
};

const handleTableSelected = (table) => {
  selectedTable.value = table;
  currentPage.value = 'customer_info';
};

const handleBookingComplete = async (customerDetails) => {
  if (!selectedTable.value || !bookingData.value) {
    alert("Andmed on puudu. Palun alustage uuesti.");
    return;
  }

  const prefs = bookingData.value.preferences;
  const labels = {quiet: "Vaikne koht", kids: "Lastenurga lähedal", window: "Akna juures"};

  const selectedPrefsString = Object.keys(prefs)
      .filter(key => prefs[key])
      .map(key => labels[key])
      .join(", ");

  const reservationRequest = {
    startTime: bookingData.value.startTime,
    endTime: bookingData.value.endTime,
    numberOfGuests: bookingData.value.guests,
    customerName: customerDetails.name,
    customerPhone: customerDetails.phone,
    specialRequests: selectedPrefsString,
    restaurantTable: {
      id: selectedTable.value.id
    }
  };

  try {
    const response = await fetch('http://localhost:8080/api/reservations', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(reservationRequest),
    });

    if (response.ok) {
      finalReservation.value = {
        name: customerDetails.name,
        tableName: selectedTable.value.name || `Laud ${selectedTable.value.id}`,
        startTime: bookingData.value.startTime,
        endTime: bookingData.value.endTime
      };
      currentPage.value = 'summary';

      // Reset state
      bookingData.value = null;
      selectedTable.value = null;
    } else {
      const errorMsg = await response.text();
      alert("Viga broneerimisel: " + errorMsg);
    }
  } catch (err) {
    console.error("Booking failed:", err);
    alert("Serveri viga! Proovige hiljem uuesti.");
  }
};

const goBackToFilter = () => {
  step.value = 'filter';
};
</script>
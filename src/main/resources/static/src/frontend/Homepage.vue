<template>
  <div class="homepage-container">
    <h1>Tere tulemast meie restoranis</h1>
    <button @click="$emit('go-to-table-info')" class="reserve-btn">
      Reserveerida lauda saab siin
    </button>
  </div>
</template>

<style scoped>
.homepage-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 80vh;
  font-family: sans-serif;
}
.reserve-btn {
  background-color: #5d4037;
  color: white;
  padding: 15px 30px;
  border: none;
  border-radius: 8px;
  font-size: 18px;
  cursor: pointer;
  transition: transform 0.2s;
}
</style>
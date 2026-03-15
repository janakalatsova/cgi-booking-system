<template>
  <div class="base-phone-input">
    <select
        class="base-select"
        :value="countryCode"
        @change="onCountryChange"
    >
      <option value="EE">Estonia (+372)</option>
      <option value="LV">Latvia (+371)</option>
      <option value="LT">Lithuania (+370)</option>
      <option value="FI">Finland (+358)</option>
    </select>

    <input
        type="text"
        :value="modelValue"
        @input="onInput"
        :placeholder="placeholderText"
        class="phone-field"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { parsePhoneNumberFromString, AsYouType } from 'libphonenumber-js';

const props = defineProps({
  modelValue: String,
  countryCode: { type: String, default: 'EE' }
});

const emit = defineEmits(['update:model-value', 'update:country-code', 'update']);

const placeholderText = computed(() => {
  return props.countryCode === 'EE' ? '512 3456' : 'Phone number';
});

const onInput = (event) => {
  // 1. Оставляем только цифры из того, что ввел пользователь
  const onlyDigits = event.target.value.replace(/\D/g, '');

  // 2. Форматируем эти цифры согласно правилам страны (добавляем пробелы визуально)
  const formatted = new AsYouType(props.countryCode).input(onlyDigits);

  // 3. Проверяем валидность номера
  const phoneNumber = parsePhoneNumberFromString(formatted, props.countryCode);
  const isValid = phoneNumber ? phoneNumber.isValid() : false;
  const e164 = phoneNumber ? phoneNumber.format('E.164') : formatted;

  // Отправляем форматированное значение обратно в поле (с пробелами для красоты)
  emit('update:model-value', formatted);

  // Отправляем данные для валидации
  emit('update', {
    isValid: isValid,
    phoneNumber: formatted,
    e164: e164
  });
};

const onCountryChange = (event) => {
  emit('update:country-code', event.target.value);
};
</script>

<style scoped>
.base-phone-input {
  display: flex;
  gap: 10px;
  width: 100%;
}
.base-select {
  width: 130px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
  font-size: 14px;
}
.phone-field {
  flex: 1;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
}
</style>
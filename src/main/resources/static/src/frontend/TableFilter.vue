<script setup>
import { reactive, watch } from 'vue';
const emit = defineEmits(['go-to-map']);

const getInitialTime = (extraHours = 0) => {
  const date = new Date();
  date.setHours(date.getHours() + extraHours);
  date.setMinutes(0, 0, 0);
  const offset = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offset).toISOString().slice(0, 16);
};

const filterData = reactive({
  guests: 2,
  startTime: getInitialTime(1),
  endTime: getInitialTime(2),
  preferences: { quiet: false, kids: false, window: false }
});

// Auto-adjust end time when start time changes
watch(() => filterData.startTime, (newVal) => {
  if (!newVal) return;
  const start = new Date(newVal);
  const end = new Date(start.getTime() + 60 * 60 * 1000);
  filterData.endTime = new Date(end.getTime() - (end.getTimezoneOffset() * 60000)).toISOString().slice(0, 16);
});

const confirmFilter = () => {
  const start = new Date(filterData.startTime);
  const end = new Date(filterData.endTime);

  if (end <= start) {
    alert("Lõpuaeg peab olema pärast algusaega!");
    return;
  }

  if ((end - start) > 3 * 60 * 60 * 1000) {
    alert("Broneeringu maksimaalne pikkus on 3 tundi.");
    return;
  }

  emit('go-to-map', { ...filterData });
};
</script>
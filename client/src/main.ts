import '@/assets/style.css';
import '@/assets/base.css';
import 'primevue/resources/themes/aura-light-noir/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';

import { createApp } from 'vue';
import App from './App.vue';
import { createPinia } from 'pinia';
import ToastService from 'primevue/toastservice';
import PrimeVue from 'primevue/config';
import Ripple from 'primevue/ripple';

import router from './router';

const app = createApp(App);
app.use(createPinia());
app.directive('ripple', Ripple);

app.use(router).use(ToastService).use(PrimeVue);

app.mount('#app');

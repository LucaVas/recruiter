import '@/assets/style.css';
import '@/assets/base.css';
import 'primevue/resources/themes/lara-light-blue/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';

import { createApp } from 'vue';
import App from './App.vue';
import ToastService from 'primevue/toastservice';
import PrimeVue from 'primevue/config';
import Ripple from 'primevue/ripple';
import Tooltip from 'primevue/tooltip';

import router from './router';
import { createPinia } from 'pinia';
import piniaPluginPersistedState from 'pinia-plugin-persistedstate';

const app = createApp(App);

const pinia = createPinia();
pinia.use(piniaPluginPersistedState);

app.directive('ripple', Ripple);
app.directive('tooltip', Tooltip);

app.use(router).use(ToastService).use(PrimeVue).use(pinia);

app.mount('#app');

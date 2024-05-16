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

const app = createApp(App);
app.directive('ripple', Ripple);
app.directive('tooltip', Tooltip);

app.use(router).use(ToastService).use(PrimeVue);

app.mount('#app');

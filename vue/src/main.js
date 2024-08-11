import router from './router'
import store from './store'
import '@/assets/css/global.css'
import '@/assets/font/myFont.css'
import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import request from "@/utils/request";
import moment from 'moment';
import VueCookies from "vue-cookies";
import vueXlsxTable from 'vue-xlsx-table';
// main.js中全局引入插件


moment.locale('zh-cn'); //设置语言 或 moment.lang('zh-cn');
Vue.prototype.$moment = moment;//赋值使用
Vue.use(ElementUI);
Vue.config.productionTip = false
Vue.use(VueCookies);
Vue.use(vueXlsxTable, {rABS: false}) //Browser FileReader API have two methods to read local file readAsBinaryString and readAsArrayBuffer, default rABS false
Vue.prototype.request=request
Vue.prototype.$baseUrl=process.env.VUE_APP_BASEURL

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')



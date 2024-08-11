import { Notify } from 'vant';
export default {
  primary(text) {
    // 主要通知
    Notify({ type: 'primary', message: text, duration: 1000 });
  },
  success(text) {
    // 成功通知
    Notify({ type: 'success', message: text, duration: 1000 });
  },
  danger(text) {
    // 危险通知
    Notify({ type: 'danger', message: text, duration: 1000 });
  },
  warning(text) {
    // 警告通知
    Notify({ type: 'warning', message: text, duration: 1000 });
  },
}

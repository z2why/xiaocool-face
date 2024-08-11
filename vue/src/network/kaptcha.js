import axios from 'axios'
// 运行跨域携带cookie
axios.defaults.withCredentials = true
export function kaptcha(config) {
    let newVar = axios.create({
        baseURL: process.env.VUE_APP_BASEURL,
        timeout:3000
    });
    return newVar(config);
}
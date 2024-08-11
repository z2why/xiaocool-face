/*
*   cookie的封装
*   封装setCookie()、getCookie()、delCookie() 方法
* */
export default {
  /*
  * 存储cookie
  * */
  setCookie(cname,cvalue){
    document.cookie = cname+"="+cvalue;
  },

  /*
  * 获取cookie
  * */
  getCookie(cname){
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
      var c = ca[i].trim();
      if (c.indexOf(name)==0) { return c.substring(name.length,c.length); }
    }
    return "";
  },

  /*
  * 删除cookie
  * */
  delCookie(cname) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = this.getCookie(cname);
    if (cval !== ''){
      if (cval){
        document.cookie = cname + "=" + cval + ";expires=" + exp.toGMTString();
      }
    }
  },
}

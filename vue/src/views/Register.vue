<template>
  <div class="wrapper">


    <div class="login-box"
        style="margin: 240px auto; width: 500px; height: 481px; padding: 20px;font-size: x-large">
      <div class="avatar_box">
      <img src="../assets/logo.jpeg" class="an img" alt="" style="width: 100%;
      height: 100%;
        border-radius: 50%;
        background-color: rgb(238 238 238 / 39%)">
    </div>
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username" style="width: 75%;position: relative;top: 34px;left: 60px">
          <el-input size="medium" style="margin: 0px 0;" prefix-icon="el-icon-user" v-model="user.username" placeholder="  用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password" style="width: 75%;position: relative;left: 60px; top: 25px">
          <el-input size="medium" style="margin: 0px 0" prefix-icon="el-icon-lock" show-password placeholder="  密码"
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="password" style="width: 75%;position: relative;left: 60px; top: 19px">
          <el-input size="medium" style="margin: 0px 0" prefix-icon="el-icon-lock" show-password placeholder="  请确认密码"
                    v-model="user.confirmPassword"></el-input>
        </el-form-item>

        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="info" size="large" autocomplete="off" @click="$router.push('/')" style="position: absolute;left: 105px;top:20px">返回登录</el-button>
          <el-button type="warning" size="large" autocomplete="off" @click="login"style="position: absolute;right: 111px;top: 20px">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

import {setRoutes} from "@/router";

export default {
  name: "Register",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 5, max: 18, message: '长度在 5 到 18 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("注册成功")
            }  else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style scoped>

.wrapper {
  margin-top: 0;
  height: 100vh;
  background:url("../assets/bp.jpeg");
  background-size:100% 100%;
  overflow: hidden;

}

.login-box{
  border:1px solid #dccfcf;
  width: 350px;
  margin:180px auto;
  padding: 35px 80px 15px 35px;
  border-radius: 5px;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  box-shadow: 0 0 25px #909399;
  background-color:rgba(255,255,255,0.7);

}
.avatar_box{
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  /*边框阴影*/
  box-shadow: 0 0 10px #ddd;
  position: absolute;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;

}
::v-deep .login-box .el-input--medium .el-input__inner {
  height: 53px;
  line-height: 36px;
}
::v-deep .el-input--prefix .el-input__inner {
  padding-left: 36px;
  font-size: 16px;
}
::v-deep .el-form-item__content {
  /* line-height: 0px; */
  position: relative;
  font-size: 14px;
  height: 78px;
  margin: -2px;
}

</style>

<style scoped>
@-webkit-keyframes rotation{
  from {-webkit-transform: rotate(0deg);}
  to {-webkit-transform: rotate(360deg);}
}
.an:hover{
  -webkit-transform: rotate(360deg);
  animation: rotation 3s linear infinite;
  -moz-animation: rotation 3s linear infinite;
  -webkit-animation: rotation 3s linear infinite;
  -o-animation: rotation 3s linear infinite;
}

/deep/ .el-form-item__error {
  color: #E10000AD;
  font-size: 14px;}

.el-input--prefix .el-input__inner {
  padding-left: 38px;
}
/deep/ .el-form-item__error {
  color: #E10000AD;
  font-size: 14px;
  top: 78%;}
</style>
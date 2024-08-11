<template>

  <div class="wrapper" style="height:100vh;user-select: none">

    <div class="main-box">
      <div :class="['container', 'container-login', { 'is-txl': isLogin }]">
        <el-form :model="user" :rules="rules" ref="userForm" v-show="!loginByCode" style="margin-bottom: 10px">
          <h2 class="title">小酷人脸识别会议签到系统</h2>
          <div class="form__icons">
            <el-tooltip content="使用Gitee账号登录" placement="bottom" effect="light" :visible-arrow="false">
<!--              <a href="https://localhost:9091/uaa/oauth2/authorize/gitee">-->
                <img class="icons_svg" src="../assets/svg/gitee登录.svg" @click="loginGitee()">
<!--              </a>-->
            </el-tooltip>
            <el-tooltip content="使用手机/邮箱验证码登录" placement="bottom" effect="light" :visible-arrow="false">
              <img class="icons_svg" src="../assets/svg/验证码登录.svg" @click="loginByCode=!loginByCode">
            </el-tooltip>
            <el-tooltip content="使用QQ账号登录" placement="bottom" effect="light" :visible-arrow="false">
              <img class="icons_svg" src="../assets/svg/QQ登录.svg" @click="LoginQQ()">
            </el-tooltip>
            <el-tooltip content="使用微信账号登录" placement="bottom" effect="light" :visible-arrow="false">
              <img class="icons_svg" src="../assets/svg/微信登录.svg" @click="LoginWechat()">
            </el-tooltip>
          </div>
          <span class="text">or use username for login</span>
          <div style="display: flex;flex-direction: column;">
            <el-form-item prop="username">
              <el-input prefix-icon="el-icon-user" placeholder="用户名" v-model="user.username" clearable></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="medium" prefix-icon="el-icon-lock" show-password placeholder="密码"
                        v-model="user.password"></el-input>
            </el-form-item>
            <el-form-item prop="code" class="Verification">
              <div style="display: flex;flex-direction: row;align-items: baseline;">
                <el-input size="medium" prefix-icon="el-icon-unlock" placeholder="验证码"
                          v-model="user.code"></el-input>
                <el-image
                    v-loading="loading"
                    @load="imageLoaded"
                    :src='$baseUrl+"/uaa/user/getCode"'
                    id="codeImg"
                    style="width: 46%; cursor: pointer;border-radius: 7px;"
                    fit="cover"
                    @click="uploadImage()"
                    alt="验证码"
                    title="点击更换验证码"
                ></el-image>
              </div>
            </el-form-item>
            <el-form-item style="align-self: center">
              <el-button class="primary-btn"
                         style="font-size: medium;line-height: 0;
                   margin-top: 10px;" autocomplete="off" @click="login()"
                         @keyup.enter="enterSearch">登 录
              </el-button>
            </el-form-item>
          </div>
        </el-form>


        <!--      使用手机或者邮箱登录-->
        <el-form :model="user1" :rules="rulesCaptcha" ref="form" v-show="loginByCode" style="margin-bottom: 20px;">
          <h2 class="title">小酷人脸识别会议签到系统</h2>
          <div class="form__icons">
            <el-tooltip content="使用用户名密码登录" placement="bottom" effect="light">
              <img class="icons_svg" src="../assets/svg/ID登录.svg" @click="loginByCode=!loginByCode">
            </el-tooltip>
          </div>
          <span class="text">or use phone/email for login</span>
          <div style="display: flex;flex-direction: column;align-items: center;">
            <el-form-item prop="userNumber">
              <el-input prefix-icon="el-icon-message" placeholder="手机号/邮箱" v-model="user1.userNumber"
                        clearable></el-input>
            </el-form-item>

            <el-form-item prop="captcha">
              <el-input size="medium" prefix-icon="el-icon-unlock" placeholder="验证码"
                        v-model="user1.captcha">

                <template #suffix>
                  <div v-if="messageCodeVis" style="font-family:'微软雅黑'; line-height: 54px;">{{ countdown }}秒后重新获取</div>
                  <el-button v-if="(!messageCodeVis)&&EOP"
                             type="text" style="color: #676a6f;
                         font-family:'微软雅黑';line-height: initial;"
                             @click="sendCode">获取验证码
                  </el-button>
                </template>

              </el-input>
            </el-form-item>

            <el-form-item>
              <el-button class="primary-btn"
                         style="font-size: medium;line-height: 0;
                       margin-top: 18px;" autocomplete="off"
                         @click="login()" @keyup.enter="enterSearch">登 录
              </el-button>
            </el-form-item>
          </div>
        </el-form>

      </div>


      <div
          :class="['container', 'container-register', { 'is-txl is-z200': isLogin }]">
        <el-form :model="userRegister" :rules="rulesRegister" ref="userRegisterForm">
          <h2 class="title">创建账号</h2>
          <div class="form__icons">
            <el-tooltip content="使用Gitee注册并登录" placement="bottom" effect="light" :visible-arrow="false">
              <img class="icons_svg" src="../assets/svg/gitee登录.svg" @click="LoginGitee()">
            </el-tooltip>
            <el-tooltip content="使用QQ注册并登录" placement="bottom" effect="light" :visible-arrow="false">
              <img class="icons_svg" src="../assets/svg/QQ登录.svg" @click="LoginQQ()">
            </el-tooltip>
            <el-tooltip content="使用微信注册并登录" placement="bottom" effect="light" :visible-arrow="false">
              <img class="icons_svg" src="../assets/svg/微信登录.svg" @click="LoginWechat()">
            </el-tooltip>
          </div>
          <span class="text">or use username for register</span>
          <div v-show="!writeEmail">
            <el-form-item prop="username">
              <el-input prefix-icon="el-icon-user" placeholder="用户名" v-model="userRegister.username"
                        clearable></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="medium" prefix-icon="el-icon-lock" placeholder="密码" show-password
                        v-model="userRegister.password"></el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input size="medium" prefix-icon="el-icon-lock" placeholder="确认密码" show-password
                        v-model="userRegister.confirmPassword"></el-input>
            </el-form-item>
          </div>
          <div v-show="writeEmail">
            <el-form-item prop="email">
              <el-input prefix-icon="el-icon-message" placeholder="邮箱地址" v-model="userRegister.email"
                        clearable></el-input>
            </el-form-item>

            <el-form-item prop="emailcaptcha">
              <el-input size="medium" prefix-icon="el-icon-unlock" placeholder="验证码"
                        v-model="userRegister.emailcaptcha">
                <template #suffix>
                  <div v-if="emailCodeVis" style="font-family:'微软雅黑'; line-height: 54px;">{{ emailcountdown }}秒后重新获取
                  </div>
                  <el-button v-if="(!emailCodeVis)&&sendEmail" type="text"
                             style="color: #676a6f;font-family:'微软雅黑';line-height: initial;" @click="sendEmailCode">
                    获取验证码
                  </el-button>
                </template>

              </el-input>
            </el-form-item>
          </div>
          <div class="primary-btn" style="margin-top: 15px" @keyup.enter="enterSearch" @click="register()">注 册</div>
        </el-form>
      </div>

      <div :class="['switch', { login: isLogin }]">
        <div class="switch__circle"></div>
        <div class="switch__circle switch__circle_top"></div>
        <div class="switch__container">
          <h2>{{ isLogin ? 'Hello Friend !' : 'Welcome Back !' }}</h2>
          <div class="avatar_box">
            <img src="../assets/logo.jpeg" class="an img" alt="" style="width: 100%;
      height: 100%;
        border-radius: 35%;
          .brightness {-webkit-filter: brightness(0.30);filter: brightness(1%);}
">
          </div>
          <p>
            {{
              isLogin
                  ? '为保证账号安全使用,请确认信息无误'

                  : '如果您第一次使用,请先完成注册'
            }}
          </p>
          <div class="primary-btn" @click="isLogin = !isLogin" style="font-size: medium">
            {{ isLogin ? '立即登录' : '立即注册' }}
          </div>
        </div>

      </div>


    </div>
    <Footer style="top: 96%; position: absolute;"></Footer>

  </div>

</template>

<script>
import Footer from "@/components/Footer";

export default {
  name: 'Login_Register',
  components: {
    Footer
  },
  data() {
    return {
      user: {},
      user1: {},
      loading: true,
      userRegister: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 5, max: 18, message: '长度在 5 到 18 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ],
        code: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min: 4, max: 4, message: '验证码错误', trigger: 'blur'}
        ],
      },
      rulesCaptcha: {
        userNumber: [
          // 手机号或者邮箱
          {validator: this.validateEmailOrPhone, trigger: 'blur'}
        ],
        captcha: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min: 6, max: 6, message: '验证码错误', trigger: 'blur'}
        ],
      },

      rulesRegister: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 5, max: 18, message: '长度在 5 到 18 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {validator: this.valipassword, trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ],
        email: [
          {validator: this.validateEmail, trigger: 'blur'}
        ],
        emailcaptcha: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min: 6, max: 6, message: '验证码错误', trigger: 'blur'}
        ]
      },
      EOP: false,
      sendEmail: false,
      isLogin: false,
      writeEmail: false,
      userinfo: false,
      sendType: '',
      messageCodeVis: false,
      emailCodeVis: false,
      loginForm: {
        email: '',
        password: '',
      },
      registerForm: {
        name: '',
        email: '',
        password: '',
      },
      loginByCode: false,
      countdown: '',
      emailcountdown: ''
    }
  },
  created() {
    this.enterSearch();
    // 清除状态保持
    // 状态保持清除后刷新页面
  },

  methods: {
    uploadImage() {
      const captchaImg = document.getElementById("codeImg");
      if (captchaImg) {
        captchaImg.src = this.$baseUrl + "/uaa/user/getCode?" + new Date().getTime();
      }
      },
    LoginQQ() {
      this.request.get("/user/go")
    },
    login() {
      const loading = this.$loading({
        lock: true,
        text: '正在登录中！',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      //如果是根据验证码登录
      if (this.loginByCode) {
        //验证码类型是邮箱
        this.$refs['form'].validate((valid) => {
          if (valid) {  // 表单校验合法
            if (this.sendType === "email") {
              this.request.get("/uaa/oauth2/verifyEmailCaptcha",
                  {
                    params: {
                      email: this.user1.userNumber,
                      code: this.user1.captcha
                    },

                  }).then(res => {
                    loading.close();
                    if (res.code === "200") {
                      let userdto = {
                        email: this.user1.userNumber,
                        loginType: "email"
                      }
                      this.request.post("/uaa/oauth2/captchaLogin", userdto).then(res => {
                        if (res.code === "200") {
                          this.loginSuccess(res.data)
                        } else {
                          this.$message.error("登录失败")
                        }
                      })
                    } else {
                      this.$message.error("验证码有误！")

                    }
                  }
              )

            }
          }
        });

        //验证码类型是手机号
        if (this.sendType === "phone") {
          this.request.get("/uaa/oauth2/verifySmsCaptcha",
              {
                params: {
                  phone: this.user1.userNumber,
                  code: this.user1.captcha
                },

              }).then(res => {
               loading.close();
               if (res.code === "200") {
                  let userdto = {
                    telephone: this.user1.userNumber,
                    loginType: "phone"
                  }
                  this.request.post("/uaa/oauth2/captchaLogin", userdto).then(res => {
                    if (res.code === "200") {
                      this.loginSuccess(res.data)
                    } else {
                      this.$message.error("登录失败")
                    }
                  })
                } else {
                  this.$message.error("验证码有误！")
                }
              }
          )
        }
      }
      //用用户名密码登录
      else {
        this.$refs['userForm'].validate((valid) => {
          if (valid) {  // 表单校验合法
            this.request.post("/uaa/oauth2/doLogin", this.user).then(res => {
              loading.close();
              if (res.code === "200") {
                this.loginSuccess(res.data)
              } else {
                if (res.msg==="验证码错误"){
                  this.$message.error(res.msg)
                }
                else {
                  this.uploadImage()
                  this.$message.error(res.msg)

                }
              }
            })
          }
        });
      }

    },
    loginGitee(){
      window.location.href = 'https://xiaoku.store:9091/uaa/oauth2/authorize/gitee';
    },

    loginSuccess(user) {
      localStorage.setItem("user", JSON.stringify(user))  // 存储用户信息到浏览器
      for (let x in user.role) {
        console.log(user.role[x])
        if (user.role[x] === "admin") {
          this.$message.success("登陆成功")
          this.$router.push("/Admin/user")
          return null
        }
      }
      this.$message.success("登陆成功")
      this.$router.push("/AddMeeting")

    },
    // 校验是否是手机号或者邮箱
    validateEmailOrPhone(rule, value, callback) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      const phoneRegex = /^1[3-9]\d{9}$/; // 假设中国的手机号码格式

      if (!value) {
        this.EOP = false;
        return callback(new Error('请输入邮箱地址或者手机号码'));
      } else if (!emailRegex.test(value) && !phoneRegex.test(value)) {
        this.EOP = false;
        return callback(new Error('请输入正确邮箱地址或者手机号码'));
      } else if (emailRegex.test(value)) {
        callback();
        this.EOP = true;
        this.sendType = "email";
      } else if (phoneRegex.test(value)) {
        this.EOP = true;
        this.sendType = "phone";
      }
    },
    // 校验是否邮箱
    validateEmail(rule, value, callback) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!value) {
        this.sendEmail = false;
        return callback(new Error('请输入邮箱地址'));
      } else if (!emailRegex.test(value)) {
        this.sendEmail = false;
        return callback(new Error('请输入正确邮箱地址'));
      } else {
        callback();
        this.sendEmail = true;
      }
    },
    //校验两次密码是否一致
    valipassword(rule, value, callback) {
      if (value === '') {
        callback(new Error('请再次输入密码！'));
      }
      if (value !== this.userRegister.password) {
        return callback(new Error('两次密码输入不一致！'));
      } else {
        this.userinfo = true
        callback();
      }
    },
    // 发送邮件验证码或者短信验证码
    sendCode() {
      if (this.sendType === "email") {
        this.request.get("/uaa/oauth2/email-captcha",
            {
              params: {
                email: this.user1.userNumber,
                type: "login"
              },

            }).then(res => {
              this.$message.success(res.data)
            }
        )
      }
      if (this.sendType === "phone") {
        this.request.get("/uaa/oauth2/sms-captcha",
            {
              params: {
                phone: this.user1.userNumber,
                type: "login"
              },

            }).then(res => {
              this.$message.success(res.data)
            }
        )
      }
      this.countdown = 60
      this.messageCodeVis = true
      this.startCountdown()
    },
    /* 倒计时函数 */
    startCountdown() {
      const intervalId = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--;
        } else {
          clearInterval(intervalId)
          this.messageCodeVis = false
        }
      }, 1000)
    },


    // 发送邮件验证码
    sendEmailCode() {
      this.request.get("/uaa/oauth2/email-captcha",
          {
            params: {
              email: this.userRegister.email,
              type: "register"
            },

          }).then(res => {
            this.$message.success(res.data)
          }
      )
      this.emailcountdown = 60
      this.emailCodeVis = true
      this.startEmailCountdown()
    },
    /* 倒计时函数 */
    startEmailCountdown() {
      const intervalId = setInterval(() => {
        if (this.emailcountdown > 0) {
          this.emailcountdown--;
        } else {
          clearInterval(intervalId)
          this.emailCodeVis = false
        }
      }, 1000)
    },

    imageLoaded() {
      this.loading = false;
    },
    register() {

      if (!this.writeEmail && this.userinfo) {
        this.writeEmail = true;
      } else {
        const loading = this.$loading({
          lock: true,
          text: '正在完成注册！',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        this.$refs['userRegisterForm'].validate((valid) => {
          if (valid) {  // 表单校验合法
            this.request.get("/uaa/oauth2/verifyEmailCaptcha",
                {
                  params: {
                    email: this.userRegister.email,
                    code: this.userRegister.emailcaptcha
                  },

                }).then(res => {
                  loading.close()
                  if (res.code === "200") {
                    this.request.post("/uaa/user/register", this.userRegister).then(res => {
                      if (res.code === '200') {
                        this.$message.success("注册成功")
                        this.isLogin = !this.isLogin
                      } else {
                        this.$message.error(res.msg)
                      }
                    })
                  }
                }
            )

          }
        });
      }

    },

    enterSearch() {
      document.onkeydown = e => {
        //13表⽰回车键，baseURI是当前页⾯的地址，为了更严谨，也可以加别的，可以打印e看⼀下
        if (e.keyCode === 13 && e.target.baseURI.match()) {
          //回车后执⾏搜索⽅法
          if (this.isLogin) {
            this.register()
          } else {
            this.login()
          }
        }
      }
    },
  },
}
</script>

<style lang="scss" scoped>
::v-deep .el-input--prefix .el-input__inner {
  width: 330px;
  height: 45px;
  margin: 4px 0;
  font-size: 14px;
  letter-spacing: 0.15px;
  border: none;
  outline: none;
  padding-left: 38px;
  // font-family: 'Montserrat', sans-serif;
  background-color: #ecf0f3;
  transition: 0.25s ease;
  border-radius: 8px;
  box-shadow: inset 2px 2px 4px #d1d9e6;

  &::placeholder {
    color: #a0a5a8;
  }
}

::v-deep .Verification .el-input__inner {
  width: 200px;
}

::v-deep .el-form-item__error {
  color: #E10000AD;
  font-size: 14px;
  position: inherit;
  top: 78%;
}

.login-box {
  border: 1px solid #dccfcf;
  width: 350px;
  margin: 180px auto;
  padding: 35px 80px 15px 35px;
  border-radius: 5px;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  box-shadow: 0 0 25px #909399;
  background-color: rgba(255, 255, 255, 0.7);

}

::v-deep .el-form-item {
  margin-bottom: 10px
}

//
//::v-deep .el-image__inner{
//  vertical-align: sub;
//}

.form__icons {
  display: flex;
  flex-direction: row;
  margin-bottom: 10px;

  .icons_svg {
    width: 44px;
    height: 44px;
    margin: 0 10px;
    cursor: pointer;
  }
}

.avatar_box {
  height: 130px;
  width: 130px;
  border-radius: 35%;
  position: absolute;
  left: 49%;
  transform: translate(-50%, -50%);
  background-color: #ecf0f3;
  top: 16px;
}

@-webkit-keyframes rotation {
  from {
    -webkit-transform: rotate(0deg);
  }
  to {
    -webkit-transform: rotate(360deg);
  }
}

.an:hover {
  -webkit-transform: rotate(360deg);
  animation: rotation 3s linear infinite;
  -moz-animation: rotation 3s linear infinite;
  -webkit-animation: rotation 3s linear infinite;
  -o-animation: rotation 3s linear infinite;
}

.wrapper {
  height: 100vh;
  width: 100%;
  background-image: radial-gradient(circle, #f0f3f6, #fff, #b5c0c9);
  background-size: 120% 100%;

}

.main-box {
  z-index: 1;
  position: relative;
  left: 23%;
  top: 12%;
  width: 700px;
  min-width: 850px;
  min-height: 500px;
  height: 520px;
  padding: 25px;
  background-color: #ecf0f3;
  box-shadow: 10px 10px 10px #bec7d5, -10px -10px 10px #f9f9f9;
  border-radius: 12px;
  overflow: hidden;

  .container {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    width: 600px;
    height: 100%;
    padding: 25px;
    background-color: #ecf0f3;
    transition: all 1.25s;

    form {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      width: 100%;
      height: 100%;
      color: #a0a5a8;

      .title {
        font-size: 30px;
        font-weight: 700;
        line-height: 3;
        color: #181818;
      }

      .text {
        margin-top: 10px;
        margin-bottom: 12px;
      }
    }
  }

  .container-login {
    z-index: 100;
    left: calc(100% - 570px);
  }

  .container-register {
    left: calc(55% - 500px);
    z-index: 0;
  }

  .is-txl {
    transition: 1.25s;
    transform-origin: right;
  }

  .is-z200 {
    z-index: 200;
    transition: 1.25s;
  }

  .switch {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 320px;
    padding: 50px;
    z-index: 200;
    transition: 1.25s;
    background-color: #ecf0f3;
    overflow: hidden;
    box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #f9f9f9;
    color: #a0a5a8;

    .switch__circle {
      position: absolute;
      width: 440px;
      height: 460px;
      border-radius: 50%;
      background-color: #ecf0f3;
      box-shadow: inset 8px 8px 12px #d1d9e6, inset -8px -8px 12px #f9f9f9;
      bottom: -60%;
      left: -60%;
      transition: 1.25s;
      z-index: 1;
    }

    .switch__circle_top {
      top: -30%;
      left: 60%;
      width: 300px;
      height: 300px;
    }

    .switch__container {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      position: absolute;
      width: 400px;
      margin-top: 40px;
      padding: 50px 55px;
      transition: 1.25s;

      h2 {
        font-size: 27px;
        font-weight: 700;
        line-height: 3;
        color: #181818;
      }

      p {
        font-size: 14px;
        letter-spacing: 0.25px;
        text-align: center;
        line-height: 1.6;
      }
    }
  }

  .login {
    left: calc(100% - 319px);

    .switch__circle {
      left: 100px;
    }

    .switch__circle_top {
      left: -150px;
    }
  }

  .primary-btn {
    width: 140px;
    height: 50px;
    border-radius: 25px;
    margin-top: 25px;
    text-align: center;
    line-height: 50px;
    font-size: 14px;
    letter-spacing: 2px;
    background-color: #4b70e2;
    color: #f9f9f9;
    cursor: pointer;
    box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;

    &:hover {
      box-shadow: 4px 4px 6px 0 rgb(255 255 255 / 50%),
      -4px -4px 6px 0 rgb(116 125 136 / 50%),
      inset -4px -4px 6px 0 rgb(255 255 255 / 20%),
      inset 4px 4px 6px 0 rgb(0 0 0 / 40%);
    }
  }
}
</style>


<template>

  <div style="height: 65px; border-bottom: 1px solid rgb(215, 215, 215); display: flex;">

    <div class="demo-basic--circle">
      <div class="block">
        <el-avatar :size="60" style="position: absolute;top: 3px;left: 38px;"><img class="an img"
                                                                                   src="../assets/logo.jpg"></el-avatar>
      </div>
    </div>

    <div
        style="width: 300px; padding-left: 30px;font-weight: 300;font-family: Hiragino Sans GB; color: #ffcb2e;  position: absolute; left: 100px; font-size: x-large;}">
      小酷人脸识别后台
    </div>
    <div style="flex: 1"></div>
    <div style="width: 100px;">


      <el-dropdown style=" position: absolute;top: -27px;right: 33px;" popper-class="popper-select" placement="bottom"
                   visible-arrow="false">
        <div class="el-dropdown-link">
          <img :src="user.avatar" alt="" style="width: 48px; border-radius: 50%; position: relative; top: 35px;">
        </div>
        <el-dropdown-menu slot="dropdown" class="ddd">
          <template slot-scope="scope">
            <div @click="handleEdit(scope.row)">
              <el-dropdown-item>{{ user.name }}</el-dropdown-item>
            </div>
            <router-link :to="{ path: '/' }" replace @click.native="refresh" style="text-decoration: none;">
              <el-dropdown-item divided>退出登录</el-dropdown-item>
            </router-link>



          </template>
        </el-dropdown-menu>
      </el-dropdown>


    </div>
  </div>
</template>


<script>
export default {

  data() {
    return {
      name: "Header",
      circleSrc: "../assets/logo.png",
      sizeList: ["large"],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token'); // "John"
    if (token !== null) {
      localStorage.setItem("user", JSON.stringify({token: token}))
      this.request.get("/uaa/user/userInfo")
          .then(res => {
            if (res.code === "200") {
              let user = res.data
              user.token = token
              localStorage.setItem("user", JSON.stringify(user))  // 存储用户信息到浏览器
              this.user = user
            } else {
              this.$message.error("有误！")
            }
          })
    }
  },
  methods: {
    refresh() {
      // 状态保持清除后刷新页面
      this.request.get("/uaa/user/logout", {params: {id: this.user.username}})
          .then(res => {
            if (res.code === "200") {
              window.localStorage.clear();
              this.$message.success("退出成功");
              // 刷新验证码
              this.refreshCaptcha();
            } else {
              this.$message.error("有误！");
            }
          });
    },
    refreshCaptcha() {
      const captchaImg = document.getElementById("codeImg");
      if (captchaImg) {
        captchaImg.src = this.$baseUrl + "/uaa/user/getCode?" + new Date().getTime();
      }
    }
  }


}

</script>

<style scoped>
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

.img {
  border-radius: 200px;
}

::v-deep.ddd {
  font-size: large;
  width: 100px;
}

</style>
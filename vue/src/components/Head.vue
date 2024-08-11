<template>
  <div class="header">
    <!-- 左边 -->
    <div class="header-l">
      <img src="../assets/logo.jpeg" alt="" />
      <span style="font-size: x-large; font-family: Hiragino Sans GB; font-weight: 350">小酷人脸识别会议签到</span>
    </div>

    <div class="header-r">
      <!-- 右边 -->
      <div class="gl">
        <el-menu
            router
            :default-active="getActive()"
            class="el-menu-demo"
            mode="horizontal"
        >
          <el-menu-item index="/AddMeeting" style="padding-right: 10px; padding-left: 5px">加入会议</el-menu-item>
          <el-menu-item index="/userinfo" style="padding: 0 10px;">会议管理</el-menu-item>
        </el-menu>
      </div>

      <div v-if="qwq === 1">
        <ul class="right-ul" style="">
          <row style="padding: 20px 28px;"></row>
        </ul>
      </div>

      <div v-if="user == null">
        <ul class="right-ul" style="">
          <li>
            <a href="#" class="user-icon">
              <el-button type="primary" style="padding: 20px 13px;" @click="$router.push('/')" plain circle>登录</el-button>
            </a>
          </li>
        </ul>
      </div>
      <div v-show="qwq !== 0" class="float"></div>

      <el-popover
          popper-class="xdd"
          trigger="hover"
          visible-arrow
          width="200"
          @mouseenter="showLargeAvatar"
          @mouseleave="hideLargeAvatar"
      >
        <div class="el-dropdown-link" slot="reference">
          <img :src="user.avatar" :class="qwq === 0 ? 'avatar' : 'disavatar'" style="width: 56px; border-radius: 50%;">
        </div>

        <template slot-scope="content">
          <div class="hxd" align="center" style="user-select: none" @click="$router.push('/userinfo')" @mouseenter="keepLargeAvatarVisible" @mouseleave="startHideTimer">
            <div>
              <img :src="user.avatar" alt="" class="avatar1">
              <div class="username">{{ user.name }}</div>
            </div>

              <el-dropdown-item @click.native="refresh" divided>退出登录</el-dropdown-item>

          </div>
        </template>
      </el-popover>
    </div>
  </div>
</template>

<script>
export default {
  name: "Head",
  data() {
    return {
      index: '1',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      qwq: 0,
      hideTimer: null
    };
  },
  created() {
    /*获取当前用户信息*/
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token'); // "John"
    if (token !==null){
      localStorage.setItem("user", JSON.stringify({token:token}))
      this.request.get("/uaa/user/userInfo")
          .then(res=>{
            if (res.code==="200"){
              let user=res.data
              user.token=token
              localStorage.setItem("user", JSON.stringify(user))  // 存储用户信息到浏览器

            }
            else {
              this.$message.error("有误！")
            }
          })
    }
  },
  methods: {
    getActive() {
      console.log(this.$route.path)
      if (this.$route.path === '/userinfo/ScheduledMeeting' ||
          this.$route.path === '/userinfo/sign' ||
          this.$route.path === '/userinfo/tabCreateMeeting' ||
          this.$route.path === '/userinfo/meetinglist' ||
          this.$route.path === '/userinfo/applymeeting')
        return '/userinfo';

      else
        return this.$route.path;
    },
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
      this.$router.replace('/');

    },
    showLargeAvatar() {
      clearTimeout(this.hideTimer);
      this.qwq = 1;
    },
    hideLargeAvatar() {
      this.hideTimer = setTimeout(() => {
        this.qwq = 0;
      }, 440);
    },
    keepLargeAvatarVisible() {
      clearTimeout(this.hideTimer);
      this.qwq = 1;
    },
    startHideTimer() {
      this.hideTimer = setTimeout(() => {
        this.qwq = 0;
      }, 440);
    }
  },
};
</script>

<style scoped>
.avatar {
  visibility: visible;
  transition: visibility 0.2s;
}

.disavatar {
  visibility: hidden;
  display: none;
  position: fixed;
  right: 50px;
}

.el-dropdown-link:hover .avatar {
  visibility: hidden;
}

.el-popover.xdd .avatar {
  visibility: hidden;
}

.header {
  user-select: none;
  display: flex;
  justify-content: space-between;
  height: 65px;
  padding: 0 15px;
  box-shadow: 0px 4px 20px 8px #e4e4e4;
  transform: translate3d(0, 0, 0);
  border-radius: 0 0 10px 10px;
}

.header-l {
  display: flex;
  align-items: center;
}

.header-l img {
  width: 60px;
  height: 60px;
  margin-right: 15px;
  border-radius: 10px;
}

.header-r {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.header-r .el-menu {
  border: none;
}

.el-menu-demo {
  padding-right: 10px;
  padding-left: 10px;
  margin-left: 20px;
}

.el-menu-item {
  font-size: 16px;
}

.el-menu--horizontal > .el-menu-item.is-active {
  border-bottom: 1px solid;
  color: #409eff;
  border-radius: 0;
}

.el-menu--horizontal > .el-menu-item {
  color: #0f1112;
}

::v-deep .el-menu--horizontal > .el-submenu .el-submenu__title {
  color: #0f1112;
}

::v-deep .el-submenu__title {
  font-size: 16px;
}

.el-dropdown-menu {
  position: absolute;
  max-height: 300px;
  overflow-y: auto;
}

.el-dropdown-menu__item {
  font-size: large;
}

.avatar1 {
  width: 120px;
  border-radius: 50%;
  position: fixed;
  top: 20px;
  right: 43px;
}

.username {
  font-size: 18px;
  height: 60px;
  line-height: 100px;
}

.el-dropdown-link {
  transition-duration: 20ms;
  cursor: pointer;
  padding-top: 21px;
  height: 100px;
  padding-right: 20px;
}

.float {
  background-color: white;
  height: 56px;
  width: 56px;
  position: fixed;
  right: 35px;
  top: 3px;
}
</style>


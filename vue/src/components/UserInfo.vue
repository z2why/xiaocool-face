<template>
<el-container>
  <el-header style="height:74px;user-select: none">
    <Head></Head>
  </el-header>
  <div class="UserInfo" >
    <div class="aside">
      <h5 style="font-family: 'SourceHanSerifCN-Bold';">会议管理</h5>
      <el-row class="tac"  >
        <el-menu class="el-menu-vertical-demo"
                 router
                 active-text-color="#ffd04b"
                 :default-active="$route.path" >
          <el-menu-item  index="/userinfo" >
            <i class="el-icon-user-solid"></i>
            <span slot="title" >用户资料</span>
          </el-menu-item>

          <el-menu-item index="/userinfo/ScheduledMeeting" >
            <i class="el-icon-s-promotion"></i>
            <span slot="title">会议预约</span>
          </el-menu-item>


          <el-menu-item index="/userinfo/sign"  >
            <i class="el-icon-s-claim"></i>
            <span slot="title">会议报名</span>
          </el-menu-item>


          <el-menu-item
              index="/userinfo/tabCreateMeeting"
             >
            <i class="el-icon-data-line"></i>
            <span slot="title">我预约的会议</span>
          </el-menu-item>


<!--          <el-menu-item-->
<!--              index="7"-->
<!--              @click="$router.push('/userinfo/Im')">-->
<!--            <i class="el-icon-thumb"></i>-->
<!--            <span slot="title">人脸录入</span>-->
<!--          </el-menu-item>-->


          <el-menu-item index="/userinfo/meetinglist">
            <svg style="    width: 26px;height: 17px;margin-right: 3px;" t="1659627076977" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3361" width="200" height="200"><path d="M448 508.928v68.544A416.256 416.256 0 0 0 64 960H0a480 480 0 0 1 332.48-425.024c-84.672-50.176-142.08-141.44-142.08-246.976a288 288 0 0 1 576 0c0 33.856-6.464 65.92-17.28 96h-69.184c13.952-29.184 22.464-61.504 22.464-96 0-123.52-100.48-224-224.064-224S254.4 164.48 254.4 288c0 113.152 84.544 205.952 193.6 220.928zM1024 448H512v576h512V448zM576 512h384v448H576V512z m320 64h-256v64h256V576z m0 128h-256v64h256v-64z m-128 128h-128v64h128v-64z" fill="#707070" p-id="3362"></path></svg>
            <span slot="title">我报名的会议</span>
          </el-menu-item>

          <el-menu-item index="/userinfo/applymeeting" >
            <i class="el-icon-edit"></i>
            <span slot="title">会议审核历史</span>
          </el-menu-item>


<!--          <el-menu-item index="4"  @click="$router.push('/userinfo/remindmessage')">-->
<!--            <i class="el-icon-setting"></i>-->
<!--            <span slot="title">消息提醒</span>-->
<!--          </el-menu-item>-->


        </el-menu>
      </el-row>
    </div>

    <div class="UserInfo-right" style="padding-top: 13px;">
      <router-view></router-view>
      <Footer style="padding: 20px 0 10px 0"></Footer>

    </div>

  </div>
</el-container>
</template>

<script>
import MyInfo from "@/views/MyInfo";
import Head from "@/components/Head";
import Footer from "@/components/Footer";

export default {
  name: 'UserInfo',
   components: {
    Head,
    MyInfo,
    Footer
  },
  data() {
    return {
      screenHeight:'',
    };
  },

   mounted() {
    this.getScreenSize();
    },

  methods: {
    getScreenSize() {
      this.screenWidth = screen.width;
      this.screenHeight = screen.height;
      console.log(this.screenHeight);
    },
  },
  watch: {
    $route(to,from){
      // console.log(to)
      console.log(`前往路由：${to.path}`);
    }
  }
};

</script>

<style scoped>
.UserInfo {
  display: flex;
}

.aside {
  width: 266px;
  height: 100vh;
  padding-top: 13px;
  user-select: none
}
.aside h5 {
  text-align: center;
  width: 254px;
  height: 45px;
  line-height: 45px;
  font-size: 20px;
  font-weight: 600;
}


::v-deep .el-menu-item.is-active{
  color: #409EFF !important;
  border-radius: 0px 25px 25px 0px;
  position: relative;
}

.el-menu {
  border-right: none;
}

.UserInfo-right {
  width: 100%;
  padding: 12px 40px 0 30px;
  border-left: 1px solid #ccc;

}


</style>
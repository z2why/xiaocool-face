<template>
<div class="Sign">
<!--  <div STYLE="padding-top: 16PX;padding-left: 14px;" >-->
  <div>
    <el-card class="leftCard">
      <div class="signName">
        <div style="font-size: 20px">
           <i class="el-icon-document-remove" style="padding-right: 10px;font-size: 19px;"></i>参会名单<span style="font-size: 12px;"></span>
        </div>

        <div style="">
         <el-upload :show-file-list="false" :action='$baseUrl+"/AboutMeeting/import?meetingId="+meetingId'   accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
           <el-popover trigger="hover">
              <p style="display: flex;align-items: baseline;"> <span  style="user-select: none;line-height: 5px">数据导入,</span>
                <el-link :href='$baseUrl+"/file/34d52113787646f38c37f446420d4348.xlsx"' type="primary">模板下载</el-link></p>

            <el-button
              slot="reference"
              type="info"
              plain
              icon="el-icon-upload2"
              circle>
            </el-button>
            </el-popover>
          </el-upload>
         </div>

      </div>

      <div class="box">
      <div v-if="client!==undefined"  v-for="(client) in meetingList" :key="client.id" class="boxname">
        <div class="svg" v-if="client.participateStatus===1" > <i class="el-icon-user-solid" style="padding: 7px 0 0 6px;font-size: 20px;cursor: pointer" ></i></div>
        <div class="svg" v-if="client.participateStatus===0" > <i  class="el-icon-user"       style="padding: 7px 0 0 6px;font-size: 20px; cursor: pointer" ></i></div>
        <svg  style="padding-top: 3px;" v-if="client.participateStatus===2" t="1668886638484" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2548" width="32" height="32" ><path d="M512 544h-128a32 32 0 1 1 0-64h96v-128a32 32 0 1 1 64 0v160a32 32 0 0 1-32 32z m0-320a32 32 0 1 1 0 64 224 224 0 1 0 224 224 32 32 0 0 1 64 0 288 288 0 1 1-288-288z m267.52 172.48a32 32 0 1 1-56 31.04l-15.488-28a32 32 0 1 1 55.968-31.04l15.52 28z m-114.464-128.48a32 32 0 0 1-34.048 54.176l-27.104-17.024a32 32 0 1 1 34.08-54.176l27.072 17.024z" p-id="2549" fill="#8a8a8a"></path></svg>
        <div class="svg" v-if="client.participateStatus===3" ><img width="24" height="32" src="../assets/早退.svg"/></div>
        <span style="font-size: 18px;padding-left: 5px;">{{ client.uname }}</span>
      </div>
      </div>

    </el-card>
  </div>


  <el-card class="rightCard">
    <el-header class="header">
    <div class="title_right" style="user-select: none">
            <span @click="click">
              <img v-if="isFullscreen" src="@/assets/Closescreen.png" v-bind:title="message2"  class="m1">
              <img v-else src="@/assets/Fullscreen.png" class="m2" v-bind:title="message">
      <!--        {{isFullscreen?'退出全屏':'全屏'}}-->
            </span>
    </div>

      <div style="height:50px">
        <h1 align="center" style="line-height: 50px;font-size: 38px;user-select: none;height: 60px">{{ this.meetingName }}
        </h1>
      </div>

      <div>
      <el-tooltip content="结束会议" placement="bottom" effect="light" :visible-arrow="false" >
        <el-button  type="info" @click="close()"
                    plain icon="el-icon-close" circle ></el-button>

      </el-tooltip>
</div>


    </el-header>
    <el-divider></el-divider>



  <div class="container" align=center :style="isFullscreen?'height: 65vh;justify-content: space-evenly;':'margin-left: 50px;height: 58vh;'">

   <div>
    <img :style="isFullscreen?'height:480px;width:640px;':'height:390px;width:520px;'" :src='"https://face.xiaoku.store:5000/video_feed/"+code'   />
  </div>

    <div class="rcontainer">
     <div style="display: flex;flex-direction:column">

          <div style="display: flex;flex-direction: row;padding-bottom: 15px;">
            <img  width="32"  src="../imgs/签到.svg">
             <h1 style="padding: 5px 0 0 10px;">最近签到人员</h1>
          </div>
<!--      <el-divider class="divider"></el-divider>-->

          <div style="display: flex;flex-direction: row;align-items: center;margin-bottom: 29px;">
             <el-avatar :size="isFullscreen?150:123" src="../imgs/noSign.png"  @error="errorHandler">
               <img v-if="signInfo.faceImage!==undefined" :src="signInfo.faceImage" :style="isFullscreen?'height: 150px;':'height: 123px;'" style="width: 100%;">
             </el-avatar>
             <div style="display: flex;flex-direction: column;margin-left: 15px;">
               <div class="info">
                <div  style="font-size: xx-large;width: 100px;padding-bottom: 7px;margin-bottom: 5px;" >姓名:</div>
                <div  style="font-size: xx-large;width: 99px;color: cornflowerblue" >{{signInfo.name}}</div>
               </div>
               <div class="info">
               <div  style="font-size: x-large;width: 100px;" >UID:</div>
               <div  style="font-size: x-large;color: cornflowerblue;width: 99px;" >{{signInfo.id}}</div>
               </div>
             </div>
          </div>
    </div>




      <div style="">
          <div style="display: flex;flex-direction: row">
          <img  width="32"  src="../imgs/比率.svg">
          <h1 style="padding: 5px 0 0 10px;">签到率</h1></div>
      </div>
<!--      <el-divider class="divider"></el-divider>-->

      <div STYLE="display: flex;flex-direction: row;justify-content: space-evenly;height: 135px;"  >
           <h1 style=" font-size: 100px;color: #69bbe5">{{signNum}}</h1>
           <h1 style=" font-size: 80px;padding-top: 10px;">/</h1>
           <h1 style=" font-size: 100px;color: #00b231">{{allNum}}</h1>
      </div>
      <Progress :width="320" :progress="rate" :strokeWidth="10" :showInfo="true" style="margin-left: 33px;" />

    </div>
</div>
    <el-divider v-if="isFullscreen"></el-divider>


    <el-footer>
    <div style="height:215px;">
         <div style="display: flex;flex-direction: row;">
           <img src="../assets/line3.png" style=" height: 25px; width: 500px;">
           <h1 style="font-family: 'SourceHanSerifCN-Bold';font-size: x-large;width: 100%;text-align: center;">已签到人员列表</h1>
           <img src="../assets/line3.png" style=" height: 25px;  width: 500px;">
         </div>
              <div style="display: flex;align-items: baseline;flex-direction: row;" class="box1">
               <div style="" v-if="client!==undefined"  v-for="(client) in meetingList" :key="client.id" >
                  <span>
                  <div style="font-size: 20px;padding-right: 15px;" >

                    <img v-if="client.participateStatus===0" src="../imgs/noSign.png" style="height: 123px">
                    <div STYLE="padding-left: 15px">
                        <el-avatar :size="110" v-if="client.participateStatus!==0" style="margin-bottom:7px" @error="errorHandler">
                          <img  :src="client.faceImage" style="width: 100%">
                        </el-avatar>
                   </div>
                   <div align="center" v-if="client.participateStatus===0" style="font-size: large"> 用户名 </div>
                   <div align="center" v-if="client.participateStatus!==0" style="font-size: large"> {{ client.uname }}</div>
                  </div>
                  </span>
                </div>
              </div>
        </div>
    </el-footer>

  </el-card>
</div>
</template>

<script>
import Progress from '@/components/Progress'
import screenfull from 'screenfull'
import moment from "moment";

import axios from 'axios'

export default {
  name: "AllSign",
  components: {
    Progress
  },
  data() {
    return {
      wsUrl:null,
      localWebsocket: undefined,
      meetingList:[],
      isFullscreen: false,  // 是否为全屏状态
      signList:[],
      signName:'',
      message:'全屏',
      message2:'退出全屏',
      message1:'结束会议',
      signInfo:[],
      allNum:'',
      signNum:'',
      rate:'',
      meeting:{},
      meetingId:''
    };
    },
   created(){
     this.meeting=JSON.parse(localStorage.getItem("meeting"))
     this.meetingName=this.meeting.meetingName
     this.meetingId=this.meeting.meetingId
     this.code=this.meeting.code
     this.load();
  },
  async mounted(){
    this.init()

    try {
      await this.initLocalWebsocket()
    } catch (e) {
      console.log('websocket错误:' + e.message)
      this.$message.error('网络连接错误!')
    }

  },
  beforeDestroy() {
  },
  destroyed() {
    window.removeEventListener('resize', this.judgeCurrentScreen)
    this.localWebsocket.close()
  },
  methods: {
    async initLocalWebsocket() {
      console.log('初始化weosocket')
      this.wsUrl = 'ws://159.75.174.31:5000/ws/'+ this.code
      console.log('获取到wsurl:' + this.wsUrl)
      this.localWebsocket = new WebSocket(this.wsUrl)
      this.localWebsocket.onmessage = this.wseReceiveMessage
      this.localWebsocket.onopen = () => {
        console.log('localWebsocket打开')
      }
      this.localWebsocket.onerror = () => {
        console.log('localWebsocket错误')
        // 重连？

      }
      this.localWebsocket.onclose = (e) => {
        this.localWebsocket.send(this.code)
        this.localWebsocket.close() // 关闭 websocket
      }
    },
    wseReceiveMessage(e) { // 数据接收
      console.log('数据接收:')
      const username=JSON.parse(e.data)
      console.log(JSON.parse(e.data))

      this.request.get("/service/AboutMeeting/AllSign",{
        params: {
          name:username,
          code:this.code
        }
      }).then(res => {
            console.log(res)
            this.signInfo = res.data
            this.load()
          }
      )
    },

    load() {
      this.request.get("/service/AboutMeeting/findMember", {
        params: {
          pageNum: 1,
          code:this.code,
          pageSize: 999,
          checkStatus:1
          }
      }).then(res => {
        this.meetingList = res.data.records
        console.log(this.meetingList)
        this.request.get("/service/AboutMeeting/SignNum",{
          params: {
            meetingId:this.meetingList[0].meetingId
          }
        }).then(res => {
          this.signNum=res.data.allcount-res.data.signcount
          this.allNum=res.data.allcount
          this.rate=Number(this.signNum/this.allNum*100).toFixed(2)

        })
          }
      )
    },


    errorHandler() {

      return true
    },
    click() {
      if (!screenfull.isEnabled) {
        this.$message({ message: '你的浏览器不支持全屏', type: 'warning' })
        return false
      }
      screenfull.toggle()
    },
    change() {
      this.isFullscreen = screenfull.isFullscreen
    },
    init() {
      if (screenfull.isEnabled) {
        screenfull.on('change', this.change)
      }
    },
    destroy() {
      if (screenfull.isEnabled) {
        screenfull.off('change', this.change)
      }
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load();
    },
    close(){
      this.meeting.stage=2
      let t2= moment(this.meeting.startDate).format("YYYY-MM-DD HH:mm:ss")
      this.meeting.startDate=JSON.parse(JSON.stringify(t2))
      let t3= moment(this.meeting.endDate).format("YYYY-MM-DD HH:mm:ss")
      this.meeting.endDate=JSON.parse(JSON.stringify(t3))
      this.meeting.id=this.meeting.meetingId
      this.$confirm('将于现在关闭【'+  this.meetingName +'】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        //修改
        this.request.post('/service/meeting/MyMeeting', this.meeting)
            .then(res => {
              if (res.code === '200') {
                this.localWebsocket.close()
                this.closeRoom()
                localStorage.removeItem("meeting")
                this.$router.replace("/AddMeeting")

              } else {
                this.$message.error("失败!")
              }
            })
      })
    },
    closeRoom(){
      let url1= 'https://face.xiaoku.store:5000/deleteRoom/'
      axios.get(url1+this.code).then(res => {
                  if (res===true){
                    this.$message.success("成功结束会议!")
                  }
                  else {
                    this.$message.error("结束会议失败，请联系管理员")

                  }
                }
            )
          }
}






}
</script>

<style lang="less" scoped>
.Sign{
  user-select: none;
  display: flex;
  flex-direction: row;
}
.box{
  min-height: 87vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}
.boxname{
  margin-top: 5px;
  display: flex;;
  flex-direction: row;
  align-items: center;
}
.leftCard{
  background: whitesmoke;
  width: 240px;
  color: #333;
  height: 99vh;
}
.signName{
  line-height: 40px;
  display: flex;
  flex-direction: row;
  border-bottom: 1px solid #ccc;
  justify-content: space-between;
}
.rightCard{
  //padding: 10px 10px 0px;
  //  margin: 10px;
    height: 99vh;
    background: whitesmoke;
    width: 86%;


}
.svg{
  width:32px;
  height:32px
}
.header{
  display: flex;
  flex-direction:row ;
  justify-content: space-between;
}
.container{
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
}
.rcontainer{
  display: flex;
  flex-direction: column;
  margin-left: 80px;
}


.box{
  /*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
  // 滚动条整体部分
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }
  // 滚动条的轨道的两端按钮，允许通过点击微调小方块的位置。
  &::-webkit-scrollbar-button {
    display: none;
  }
  // 滚动条里面的小方块，能向上向下移动（或往左往右移动，取决于是垂直滚动条还是水平滚动条）
  &::-webkit-scrollbar-thumb {
    background: rgba(144, 147, 153, 0.3);
    cursor: pointer;
    border-radius: 4px;
  }
  // 边角，即两个滚动条的交汇处
  &::-webkit-scrollbar-corner {
    display: none;
  }
  // 两个滚动条的交汇处上用于通过拖动调整元素大小的小控件
  &::-webkit-resizer {
    display: none;
  }

}
.box:hover{
  overflow-y: auto;
  /*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
  // 滚动条整体部分
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }
  // 滚动条的轨道的两端按钮，允许通过点击微调小方块的位置。
  &::-webkit-scrollbar-button {
    display: none;
  }
  // 滚动条里面的小方块，能向上向下移动（或往左往右移动，取决于是垂直滚动条还是水平滚动条）
  &::-webkit-scrollbar-thumb {
    background: rgba(144, 147, 153, 0.3);
    cursor: pointer;
    border-radius: 4px;
  }
  // 边角，即两个滚动条的交汇处
  &::-webkit-scrollbar-corner {
    display: none;
  }
  // 两个滚动条的交汇处上用于通过拖动调整元素大小的小控件
  &::-webkit-resizer {
    display: none;
  }

}
.box1{
  /*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
  // 滚动条整体部分
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }
  // 滚动条的轨道的两端按钮，允许通过点击微调小方块的位置。
  &::-webkit-scrollbar-button {
    display: none;
  }
  // 滚动条里面的小方块，能向上向下移动（或往左往右移动，取决于是垂直滚动条还是水平滚动条）
  &::-webkit-scrollbar-thumb {
    background: rgba(144, 147, 153, 0.3);
    cursor: pointer;
    border-radius: 4px;
  }
  // 边角，即两个滚动条的交汇处
  &::-webkit-scrollbar-corner {
    display: none;
  }
  // 两个滚动条的交汇处上用于通过拖动调整元素大小的小控件
  &::-webkit-resizer {
    display: none;
  }

}
.box1:hover{
  width: 100%;
  overflow-x: auto;
}
.el-tab-pane::-webkit-scrollbar {
  width: 0 !important;
  height: 0 !important;
}

::v-deep .divider {
  color: #000;
}

#customchart >body>img {
  height: 383px;
}

.m-box{
  width: 100%;
  height: 100%;
  .container{
    width:100%;
    height: 100%;
  }
}


.m1:hover{
  margin-left: 2px;
  background: #798182b3;

}
.m1:active{
  margin-bottom: 2px;
}
.m2:hover{
  margin-left: 2px;
  background: #798182b3;
}
.m2:active{
  margin-bottom: 2px;
}

.el-divider--horizontal {
  margin: 0 0 10px;
}
.info{
  display: flex;
  flex-direction: row;
}
</style>
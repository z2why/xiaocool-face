<template>
  <div class="meeting-container">
    <div
        STYLE="display: flex; width: 100%;padding: 0 10px; flex-direction: row;flex-wrap: nowrap;justify-content: center;align-items: center;">
      <div v-show="isUserList">
        <el-card style="height: 99vh; background: whitesmoke;width: 235px;color: #333;user-select: none">
          <div style="padding-bottom: 10px; border-bottom: 1px solid #ccc;font-size: 18px">
            <i class="el-icon-bell" style="padding-right: 10px;">
            </i>在线用户<span style="font-size: 18px;"></span></div>
          <div class="box">
            <div style="padding: 10px 0;" v-if="client!==undefined"
                 v-for="(client) in clients" :key="client.userId" :client="client"
                 :is-room-admin="clients[0].isRoomAdmin">
              <i :class='client.isRoomAdmin?"el-icon-user-solid":"el-icon-user"'
                 style="margin-right: 8px; font-size: 18px; cursor: pointer"></i>
              <span style="font-size: 18px">{{ client.nickname }}</span>
            </div>
          </div>
        </el-card>
      </div>

      <div style="width: inherit;">
        <el-card style="padding: 10px  0; height: 99vh;background: whitesmoke;">
          <div style="height:40px;display: flex;flex-direction: column">
            <div><h1 align="center" style="line-height: 12px;font-size: 35px">
              {{ meetingName }}</h1></div>
          </div>

          <el-container>

            <el-header height="196px" style="user-select: none;border-radius: 5px;display: flex;" v-if="isPreview">
              <div @mouseenter="show=true" @mouseleave="show=false" style="width: 100%">
                <div class="left_btn" @click="leftSlide()">
                  <button type="button" class="el-carousel__arrow el-carousel__arrow--left" v-show="show">
                    <i class="el-icon-arrow-left"></i>
                  </button>
                </div>
                <div class="right_btn" @click="rightSlide()">
                  <button type="button" class="el-carousel__arrow el-carousel__arrow--right" v-show="show">
                    <i class="el-icon-arrow-right"></i>
                  </button>
                </div>
                <div class="header" ref="wrapperCon">
                  <template v-for="(client) in clients">
                    <preview
                        v-if="client!==undefined"
                        :key="client.userId"
                        :client="client"
                        :is-room-admin="clients[0].isRoomAdmin"
                        @banEvent="ban"
                        @microEvent="changeMicro"
                        @fullEvent="fullScreen"
                        @kickEvent="kick"
                        @viewEvent="changeView"
                        @changeStreamEvent="changeStream"/>
                  </template>
                </div>
              </div>
            </el-header>

            <div style="display: flex;flex-direction: row">
              <el-main>
                <div style="text-align: center ;width: 100%;overflow-y: hidden;min-height: 51vh;"
                     :style="isPreview?'height:calc(51vh)':'height:calc( 51vh + 200px )'">
                  <video ref="video_full"
                         style="max-height: 100%;height: 100%;user-select: none;width: inherit;padding: 10px;" muted
                         autoplay playsinline/>
                </div>

                <el-footer class="footer" :style="computedStyle">
                  <!-- 打开视频-->
                  <div class="button-flex">
                    <button class="btn" @click="changeView(user.id)">
                      <div class="button-svg">
                        <img class="img" style="width: 30px;padding-top: 3px;" v-show="clients[0].view"
                             src="../../imgs/02视频@2x.png"/>
                        <img class="img" style="width: 30px;" v-show="!clients[0].view"
                             src="../../imgs/07打开摄像头@2x.png"/>
                      </div>
                      <div>
                        <span v-text="clients[0].view?'关闭视频':'打开视频'" STYLE="font-size: small;"></span>
                      </div>
                    </button>
                  </div>

                  <!--  打开语音-->
                  <div class="button-flex">
                    <button class="btn" @click="changeMicro(user.id)">
                      <div class="button-svg">
                        <img class="img" v-show="!clients[0].muted" style="  width: 20px;height: 26px"
                             src="../../imgs/01语音@2x.png"/>
                        <img class="img" v-show="clients[0].muted" style="  width: 23px;height: 26px"
                             src="../../imgs/06取消静音@2x.png"/>
                      </div>
                      <div>
                        <span v-text="clients[0].muted?'打开语音':'关闭语音'" STYLE="font-size: small;"></span>
                      </div>
                    </button>
                  </div>

                  <!-- 切换摄像头/共享屏幕 -->
                  <div class="button-flex" :style="clients[0].isRoomAdmin?'':'padding-right: 50px;'">
                    <button class="btn" @click="changeStream(user.id)">
                      <div class="button-svg">
                        <img class="img" v-show="clients[0].nowStream==='screen'" style="width: 26px;"
                             src="../../imgs/15共享屏幕.png"/>
                        <img class="img" v-show="clients[0].nowStream==='camera'" style="width: 26px;"
                             src="../../imgs/16摄像头.png"/>
                      </div>
                      <div>
                        <span STYLE="font-size: small;" v-text="clients[0].nowStream==='screen'?'共享屏幕':'摄像头'"></span>
                      </div>
                    </button>
                  </div>

                  <!-- 隐藏侧边栏 -->
                  <div class="button-flex" style="margin-left: 20px">
                    <button class="btn" @click="isUserList=!isUserList"
                            :style="{'border-top-style':isUserList?'ridge':'none'}">
                      <div class="button-svg">
                        <img class="img" style="width: 23px;padding-top: 2px" src="../../imgs/17关闭用户列表.png"/>
                      </div>
                      <div>
                        <span STYLE="font-size: small;">参会名单</span>
                      </div>
                    </button>
                  </div>

                  <!-- 隐藏聊天框 -->
                  <div class="button-flex">
                    <button class="btn" @click="isChat=!isChat" :style="{'border-top-style':isChat?'ridge':'none'}">
                      <div class="button-svg">
                        <img class="img" style="width: 28px;" src="../../imgs/20聊天框.png"/>
                      </div>
                      <div>
                        <span STYLE="font-size: small;">聊天框</span>
                      </div>
                    </button>
                  </div>

                  <!-- 打开/隐藏预览框 -->
                  <div class="button-flex">
                    <button class="btn" @click="isPreview=!isPreview"
                            :style="{'border-top-style':isPreview?'ridge':'none'}">
                      <div class="button-svg">
                        <img class="img" v-show="isPreview" style="width: 30px;padding-top: 4px;"
                             src="../../imgs/21取消预览.png"/>
                        <img class="img" v-show="!isPreview" style="width: 30px;padding-top: 4px;"
                             src="../../imgs/22预览.png"/>
                      </div>
                      <div>
                        <span STYLE="font-size: small;">预览框</span>
                      </div>
                    </button>
                  </div>


                  <div class="button-flex" v-if="clients[0].isRoomAdmin" style="margin-left: 20px">
                    <!--              <div class="button-flex">-->
                    <button class="btn" @click="changeView('')" :style="{'border-top-style':!isView?'ridge':'none'}">
                      <div class="button-svg">
                        <img class="img" v-show="isView" style="width: 26px;" src="../../imgs/10全体禁视.png"/>
                        <img class="img" v-show="!isView" style="width: 26px;" src="../../imgs/09取消禁视.png"/>
                      </div>
                      <div>
                        <span STYLE="font-size: small;" v-text="isView?'全体禁视':'取消禁视'"></span>
                      </div>
                    </button>
                  </div>
                  <!-- 全体禁音-->
                  <div class="button-flex" v-if="clients[0].isRoomAdmin">
                    <!--            <div class="button-flex">-->
                    <button class="btn" @click="changeMicro('')" :style="{'border-top-style':isMuted?'ridge':'none'}">
                      <div class="button-svg">
                        <img class="img" v-show="!isMuted" style="width: 26px;" src="../../imgs/11全体禁音.png"/>
                        <img class="img" v-show="isMuted" style="width: 26px;" src="../../imgs/12取消禁音.png"/>
                      </div>
                      <div>
                        <span STYLE="font-size: small;" v-text="isMuted?'取消禁音':'全体禁音'"></span>
                      </div>
                    </button>
                  </div>
                  <!-- 全体禁言 -->
                  <div class="button-flex" v-if="clients[0].isRoomAdmin">
                    <!--            <div class="button-flex">-->
                    <button class="btn" @click="ban('')" :style="{'border-top-style':isBan?'ridge':'none'}">
                      <div class="button-svg">
                        <img class="img" v-show="!isBan" style="width: 27px;" src="../../imgs/13全体禁言.png"/>
                        <img class="img" v-show="isBan" style="width: 27px;" src="../../imgs/14取消禁言.png"/>
                      </div>
                      <div>
                        <span STYLE="font-size: small;" v-text="isBan?'取消禁言':'全体禁言'"></span>
                      </div>
                    </button>
                  </div>


                  <!-- 离开会议-->
                  <div style="align-self: center;padding-left: 70px;margin-right: 20px;"
                       @click="kick(user.id);selfLeave=true">
                    <el-button style="padding: 10px 23px;" type="danger" round
                               v-text="clients[0].isRoomAdmin?'结束会议':'离开会议'"></el-button>
                  </div>
                </el-footer>

              </el-main>


              <el-aside width="275px" style="overflow: hidden;"
                        :style="isPreview?'height:calc(60vh)':'height:calc( 60vh + 200px )'" v-show="isChat" id="bar"
                        ref="bar">
                <Chat :receive-msg="receiveMsg" ref="chatHeight" @chatEvent="sendChat" :someData="someData"
                      @noticeEvent="notice"/>
              </el-aside>

            </div>

          </el-container>

        </el-card>
      </div>
    </div>


    <el-dialog title="请输入您的昵称和会议密码：" :visible.sync="dialogFormVisible" @close="closeView" :close-on-click-modal="false">
      <el-form ref="roomFrom" :model="roomFormData" status-icon :rules="roomFromRules" label-width="100px">
        <el-form-item label="昵称:" prop="nickname">
          <el-input v-model="roomFormData.nickname" maxlength="20" minlength="1" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="房间号:" prop="roomId">
          <el-input v-model="roomFormData.roomId" maxlength="10" readonly placeholder autocomplete="off"/>
        </el-form-item>
        <el-form-item label="密码:" prop="roomPw">
          <el-input v-model="roomFormData.roomPw" type="password" maxlength="10" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="视频来源:" prop="roomPw">
          <el-radio v-model="roomFormData.radio" label="1">摄像头</el-radio>
          <el-radio v-model="roomFormData.radio" label="2">电脑屏幕</el-radio>
        </el-form-item>
        <el-form-item>
          <el-button @click="dialogFormVisible = false">取 消</el-button>


          <el-button v-show="this.qwq!==1" type="primary" @click="createOrEnterRoom('enter')">加 入</el-button>
          <el-button v-show="this.qwq===1" type="primary" @click="createOrEnterRoom('create')">创 建</el-button>
          <!--          <el-button  type="primary" @click="createOrEnterRoom('create')">创 建</el-button>-->

        </el-form-item>
      </el-form>
    </el-dialog>
  </div>

</template>

<script>

import Preview from './components/Preview'
import Chat from './components/Chat'
import adapter from 'webrtc-adapter'
import {getUrl} from '@/api/websocketInfo'
import moment from "moment"

export default {
  name: 'Meeting',
  components: {Preview, Chat},
  data() {
    var valiRoomId = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入5-10位纯数字'))
      } else {
        var reg = /^\d{5,10}$/
        if (!reg.test(value)) {
          return callback(new Error('请输入5-10位纯数字'))
        }
        callback()
      }
    }
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      show: false,
      selfLeave: false,
      users: [],
      screenHeight: 0,
      ch: '',
      eleHeight: 0,
      chatHeight: "",
      meetingId: '0',
      dialogFormVisible: false,
      localWebsocket: undefined,
      wsUrl: undefined,
      receiveMsg: '',
      isUserList: true, //是否显示用户列表
      isPreview: true, //是否显示预览框
      isChat: true,    //是否显示聊天框
      isInRoom: false,
      isBan: false,
      isView: true,
      isFullscreen: false,//是否大屏
      isMuted: false,
      fullScreenId: '',
      clients: [{
        userId: '0',
        nickname: '未连接',
        roomId: '0',
        localStream: undefined,
        peerConnection: undefined,
        muted: false,
        view: true,
        chat: true,
        isSelf: false,
        isRoomAdmin: false,
        nowStream: 'screen'
      }],
      roomFormData: {
        nickname: '',
        roomId: '',
        roomPw: '',
        radio: '2'
      },
      someData: {
        meetingId: ''
      },

      roomFromRules: {
        roomId: [
          {validator: valiRoomId, trigger: 'blur'}
        ],
        nickname: [
          {required: true, trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}

        ]
      }
    }
  },
  computed: {
    computedStyle() {
      let widthStyle = !this.isUserList && !this.isChat ? 'width: 77%;' : 'width: 100%;';
      let paddingStyle = this.clients[0] && this.clients[0].isRoomAdmin ? 'padding: 0 20px;' : 'padding: 0 70px;';
      return widthStyle + paddingStyle;
    }
  },

  created() {
    this.someData.meetingId = this.$route.params.meetingId
    this.user.id=this.user.userId
  },
  async mounted() {
    this.getScreenSize();
    this.dialogFormVisible = true
    this.meetingId = this.$route.params.meetingId
    this.meetingName = this.$route.params.meetingName
    this.roomId = this.$route.params.roomId
    // this.dialogFormVisible =false
    this.udid = this.$route.params.udid
    this.qwq = this.$route.params.qwq
    this.roomFormData.nickname = this.user.nickname
    this.roomFormData.roomId = this.roomId

    this.ResizeObserver();


    try {
      await this.initLocalWebsocket()
    } catch (e) {
      console.log('websocket错误:' + e.message)
      this.$message.error('网络连接错误!')
      this.closeView()
    }
  },
  //销毁
  beforeDestroy() {
    if (this.isInRoom) {
      const msg = new MessageModel(TYPE_COMMAND_KICK, this.roomFormData.roomId, '', this.clients[0].userId)
      this.wsSend(msg)
    }
    if (this.localWebsocket !== undefined) {
      this.localWebsocket.close() // 离开路由之后断开localWebsocket连接
    }
    this.stopV()
  },
  destroyed() {
    this.localWebsocket.close()
  },

  methods: {
    getScreenSize() {
      this.screenWidth = screen.width;
      this.screenHeight = screen.height;
    },

    ResizeObserver() {
      const resizeObserver = new ResizeObserver(entries => {
        for (let entry of entries) {
          const element = entry.target;
          const newSize = entry.contentRect;

          this.$refs.chatHeight.getChatHeight(newSize.height);

          console.log(`元素${element.id}的大小已变为：${newSize.width}x${newSize.height}`);
        }
      });

      resizeObserver.observe(document.getElementById('bar'));

    },
    // 左箭头
    leftSlide() {
      // 保存滚动盒子左侧已滚动的距离
      let left = this.$refs.wrapperCon.scrollLeft
      let num = 0
      clearInterval(this.timer)
      this.timer = null
      this.timer = setInterval(() => {
        //   !left:已经滚动到最左侧
        //   一次性滚动距离（可调节）
        if (!left || num >= 300) {
          // 停止滚动
          clearInterval(this.timer)
          this.timer = null
          return
        }
        // 给滚动盒子元素赋值向左滚动距离
        this.$refs.wrapperCon.scrollLeft = left -= 30
        // 保存向左滚动距离（方便判断一次性滚动多少距离）
        num += 30
      }, 20)
      // 20：速度（可调节）
    },
    rightSlide() {
      // 保存滚动盒子左侧已滚动的距离
      let left = this.$refs.wrapperCon.scrollLeft
      // 保存元素的整体宽度
      let scrollWidth = this.$refs.wrapperCon.scrollWidth
      // 保存元素的可见宽度
      let clientWidth = this.$refs.wrapperCon.clientWidth
      let num = 0
      clearInterval(this.timer)
      this.timer = setInterval(() => {
        // 已经滚动距离+可见宽度
        // left+clientWidth>=scrollWidth：滚动到最右侧
        // num>=300一次性滚动距离
        if (left + clientWidth >= scrollWidth || num >= 300) {
          clearInterval(this.timer)
          return
        }
        // 给滚动盒子元素赋值向左滚动距离
        this.$refs.wrapperCon.scrollLeft = left += 30
        // 保存向左滚动距离（方便判断一次性滚动多少距离）
        num += 30
      }, 20)
      // 20：速度（可调节）
    },


    // 设置本地播放器
    async startV() {
      if (this.roomFormData.radio === '2') {
        console.log(adapter.browserDetails.browser)
        var mediaStream = await navigator.mediaDevices.getDisplayMedia(constraints)
        var audioStream = await navigator.mediaDevices.getUserMedia({audio: true, video: false})
        console.log('本地播放器设置')
        const c0 = {
          userId: '0',
          roomId: '0',
          nickname: '未连接',
          localStream: mediaStream,
          peerConnection: undefined,
          muted: false,
          view: true,
          chat: true,
          isSelf: true,
          isRoomAdmin: false,
          nowStream: 'screen'
        }
        c0.localStream.addTrack(audioStream.getAudioTracks()[0])
        this.$set(this.clients, 0, c0)
        console.log('本地流')
        console.log(this.clients[0].localStream)
        console.log('本地播放器设置成功')
      } else {
        console.log(adapter.browserDetails.browser)
        var audioStream1 = await navigator.mediaDevices.getUserMedia({audio: true, video: true})
        console.log('摄像头设置')
        const c01 = {
          userId: '0',
          roomId: '0',
          nickname: '未连接',
          localStream: audioStream1,
          peerConnection: undefined,
          muted: false,
          view: true,
          chat: true,
          isSelf: true,
          isRoomAdmin: false,
          nowStream: 'camera'
        }
        this.$set(this.clients, 0, c01)
        console.log('本地流')
        console.log(this.clients[0].localStream)
        console.log('本地摄像头设置成功')
      }
    },
    stopV() {
      this.clients[0].localStream.getTracks().forEach(function (track) {
        track.stop()
      })
    },
    addV() {
      navigator.mediaDevices.getDisplayMedia({constraints}).then(stream => {
        this.clients[0].localStream = stream
      }).catch(error => {
        console.log(error)
      })
    },
    async changeStream(userId) {
      if (this.clients[0].nowStream === 'screen') {
        navigator.mediaDevices.getUserMedia({video: true, audio: true})
            .then((mediaStream) => {
              this.stopV()
              console.log('切换为摄像头')
              this.clients[0].localStream = mediaStream
              console.log('本地摄像头设置成功')
            }).catch((e) => {
          console.log('本地摄像头设置失败 ' + e.message)
        })
        this.clients[0].nowStream = 'camera'
      } else {
        this.stopV()
        var mediaStream = await navigator.mediaDevices.getDisplayMedia(constraints)
        var audioStream = await navigator.mediaDevices.getUserMedia({audio: true, video: false})
        console.log('切换为屏幕')
        this.clients[0].localStream = mediaStream
        this.clients[0].localStream.addTrack(audioStream.getAudioTracks()[0])
        console.log('本地播放器设置成功')
        this.clients[0].nowStream = 'screen'
      }
    },
    ban(userId) {
      console.log('ban:' + userId)
      if (userId === '') { // 全体禁言
        if (this.isBan) { // 恢复
          const msg = new MessageModel(TYPE_COMMAND_BAN, this.roomFormData.roomId, 'false', '')
          this.wsSend(msg)
        } else {
          // 全体禁言
          const msg = new MessageModel(TYPE_COMMAND_BAN, this.roomFormData.roomId, 'true', '')
          this.wsSend(msg)
        }
      } else {
        if (this.clients[userId].chat) { // 全员发送chat关闭
          const msg = new MessageModel(TYPE_COMMAND_BAN, this.roomFormData.roomId, 'true', userId)
          this.wsSend(msg)
        } else { // 全员发送chat开启
          const msg = new MessageModel(TYPE_COMMAND_BAN, this.roomFormData.roomId, 'false', userId)
          this.wsSend(msg)
        }
      }
    },
    changeMicro(userId) {
      console.log('changeMicro:' + userId)
      const uid = userId.toString();
      if (userId === '') {
        if (this.isMuted) {
          const msg = new MessageModel(TYPE_COMMAND_MUTED, this.roomFormData.roomId, 'false', '')
          this.wsSend(msg)
        } else {
          const msg = new MessageModel(TYPE_COMMAND_MUTED, this.roomFormData.roomId, 'true', '')
          this.wsSend(msg)
        }
      } else {
        if (uid === this.clients[0].userId) { // 自己开关麦克风，通知所有人
          if (this.clients[0].muted) {
            // 打开麦克风
            console.log()
            const msg = new MessageModel(TYPE_COMMAND_MUTED, this.roomFormData.roomId, 'false', this.clients[0].userId)
            this.wsSend(msg)
          } else {
            // 关闭麦克风
            const msg = new MessageModel(TYPE_COMMAND_MUTED, this.roomFormData.roomId, 'true', this.clients[0].userId)
            this.wsSend(msg)
          }
        } else { // 别人
          if (this.clients[0].isRoomAdmin) { // 自己是管理员，就要彻底开关他的麦克风
            if (this.clients[Number(userId)].muted) {
              // 通知所有人打开此人麦克风
              const msg = new MessageModel(TYPE_COMMAND_MUTED, this.roomFormData.roomId, 'false', userId)
              this.wsSend(msg)
            } else {
              // 通知所有人关闭此人麦克风
              const msg = new MessageModel(TYPE_COMMAND_MUTED, this.roomFormData.roomId, 'true', userId)
              this.wsSend(msg)
            }
          } else {
            if (this.clients[Number(userId)].muted) {
              this.clients[Number(userId)].muted = false
            } else {
              this.clients[Number(userId)].muted = true
            }
          }
        }
      }
    },
    fullScreen(userId) {
      if (this.isFullscreen) {
        this.$refs.video_full.srcObject = null
        this.isFullscreen = !this.isFullscreen
      } else {
        console.log('fullScreen:' + userId)
        if (userId === this.clients[0].userId) {
          console.log(this.clients[0].localStream)
          this.$refs.video_full.srcObject = this.clients[0].localStream
          this.fullScreenId = '0'
        } else {
          console.log(this.clients[userId].localStream)

          this.$refs.video_full.srcObject = this.clients[userId].localStream
          this.fullScreenId = userId
        }
        this.isFullscreen = !this.isFullscreen

      }


    },
    kick(userId) {

      //签退
      let t1 = moment().startOf('millisecond').format("YYYY-MM-DD HH:mm:ss")

      this.request.get("/service/meeting/MyMeeting/" + this.meetingId).then(res => {
        const meetinginfo = res.data
        meetinginfo.stage = 2


        if (meetinginfo.createUser === userId) {
          this.$confirm('将于现在结束【' + this.meetingName + '】会议, 是否确带继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'error'
          }).then(() => {
            this.request.post("/service/meeting/MyMeeting", meetinginfo).then(res => {
              if (res.code === '200') {
                // this.$message.success("已结束会议!")
              }
            })
            this.request.post("/service/AboutMeeting/finishMeeting", {
              meetingId: this.meetingId,
              signOutDate: JSON.parse(JSON.stringify(t1))
            }).then(res => {
              if (res.code === '200') {
                this.$message.success("签退成功")
                const msg = new MessageModel(TYPE_COMMAND_KICK, this.roomFormData.roomId, '', '')
                this.wsSend(msg)
              } else {
                this.$message.error("签退失败，请联系会议申请人员")
              }
            })
          })
        } else {
          this.request.get("/service/AboutMeeting/getUdid", {
            params: {
              meetingId: this.meetingId,
              userId: userId
            }
          }).then(res => {
            if (res.code === '200') {
              const form3 = {
                id: res.data,
                signOutDate: JSON.parse(JSON.stringify(t1))
              }
              let t2 = moment(meetinginfo.endDate).format("YYYY-MM-DD HH:mm:ss")
              if (t1 < t2) {
                form3.participateStatus = 3
              }
              this.request.post("/service/AboutMeeting/manualSign", form3)
                  .then(res => {

                    if (res.code === '200') {
                      if (this.selfLeave === true) {
                        this.$message.success("签退成功")
                      }
                      const msg = new MessageModel(TYPE_COMMAND_KICK, this.roomFormData.roomId, '', userId)
                      this.wsSend(msg)
                    } else {
                      this.$message.success("签退失败，请联系会议申请人员")
                    }
                  })
            }
          })
        }
      })

    },
    changeView(userId) {
      console.log(this.clients)
      const uid = userId.toString();
      if (userId === '') {
        if (this.isView) {
          const msg = new MessageModel(TYPE_COMMAND_VIEW, this.roomFormData.roomId, 'false', '')
          this.wsSend(msg)
        } else {
          const msg = new MessageModel(TYPE_COMMAND_VIEW, this.roomFormData.roomId, 'true', '')
          this.wsSend(msg)
        }
      } else {
        if (uid === this.clients[0].userId) { // 自己开关视频，通知所有人
          if (this.clients[0].view) {
            // 打开视频
            const msg = new MessageModel(TYPE_COMMAND_VIEW, this.roomFormData.roomId, 'false', this.clients[0].userId)
            this.wsSend(msg)
          } else {
            // 关闭视频
            const msg = new MessageModel(TYPE_COMMAND_VIEW, this.roomFormData.roomId, 'true', this.clients[0].userId)
            this.wsSend(msg)
          }
        } else { // 别人
          if (this.clients[0].isRoomAdmin) { // 自己是管理员，就要彻底开关他的视频
            // console.log("0.0")
            if (this.clients[Number(userId)].view) {
              // 通知所有人打开此人视频
              const msg = new MessageModel(TYPE_COMMAND_VIEW, this.roomFormData.roomId, 'false', userId)

              this.wsSend(msg)
            } else {
              // 通知所有人关闭此人视频
              const msg = new MessageModel(TYPE_COMMAND_VIEW, this.roomFormData.roomId, 'true', userId)
              this.wsSend(msg)
            }
          } else {

            if (this.clients[Number(userId)].view) {
              this.clients[Number(userId)].view = false

            } else {
              this.clients[Number(userId)].view = true

            }
          }
        }
      }
    },
    changeData() {
      console.log('子组件给我传递了一个方法');
    },
    notice(msg) {
      console.log('notice:' + msg)
    },
    sendChat(msg) {
      console.log('sendChat:' + msg)
      if (!this.clients[0].chat) {
        this.$message.error('已被禁言...')
        return
      }
      this.request.post("/service/message", {
        "userId": this.user.userId,
        "meetingId": this.meetingId,
        "content": msg
      })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("发送成功")
            } else {
              this.$message.error("发送失败！")
            }
          })
      //发送聊天信息给websocket
      msg = new MessageModel(TYPE_COMMAND_CHAT, this.roomFormData.nickname, msg, this.user.userId, this.user.avatar)
      this.wsSend(msg)
    },
    receiveMsgHandle(message) {
      console.log(message)
      this.receiveMsg = {
        content: message.roomId + ':' + message.message + '\n',
        timestamp: Date.now() // 或者使用一个计数器
      }
    },
    createOrEnterRoom(method) { // 进入房间
      this.$refs.roomFrom.validate((valid) => {
        if (valid) {
          var msg
          if (method === 'create') {
            msg = new MessageModel(TYPE_COMMAND_ROOM_CREATE, this.roomFormData.roomId, this.roomFormData.nickname, this.user.userId, this.roomFormData.roomPw)
            console.log('创建房间:' + JSON.stringify(msg))
            this.wsSend(msg)
          } else {
            console.log('加入房间:' + this.roomFormData.roomId)
            msg = new MessageModel(TYPE_COMMAND_ROOM_ENTER, this.roomFormData.roomId, this.roomFormData.nickname, this.user.userId, this.roomFormData.roomPw)
            this.wsSend(msg)
          }
        } else {
          console.log('表单验证错误')
          return false
        }
      })
    },
    successHandle(message) {
      // store.dispatch('user/setNickname', this.roomFormData.nickname)
      this.startV().then(() => {
        this.isInRoom = true
        this.dialogFormVisible = false
        this.clients[0].userId = message.userId
        this.clients[0].roomId = message.roomId
        this.clients[0].nickname = this.roomFormData.nickname
        if (message.message === 'create') {
          console.log('创建房间成功')
          this.clients[0].isRoomAdmin = true
        } else {
          console.log('进入房间成功')
          this.clients[0].isRoomAdmin = false
        }
        // 广播 自己准备好了,其他用户收到后就会创建连接
        var msg = new MessageModel(TYPE_COMMAND_READY, this.roomFormData.roomId, this.roomFormData.nickname, message.userId, '', this.clients[0].isRoomAdmin)
        console.log('发送准备完毕广播' + msg)
        this.wsSend(msg)
        this.$message.success('成功!')
      })
    },
    readyHandle(message) { // 收到上线的用户准备好信号，创建RTCPeerConnectio准备与他连接并发送offer
      if (this.clients[0].userId === message.userId) { // 是自己准备好了
        console.log("=======NO+======" + message)

      } else {
        console.log("我准备好了")
        console.log(message)
        var rtcPeerConnection = new RTCPeerConnection(iceServers)
        rtcPeerConnection.userId = message.userId
        for (const track of this.clients[0].localStream.getTracks()) {
          rtcPeerConnection.addTrack(track, this.clients[0].localStream)
        }
        rtcPeerConnection.ontrack = this.onTrack
        rtcPeerConnection.onicecandidate = this.onIceCandidate
        const remoteClient = {
          userId: message.userId,
          roomId: message.roomId,
          nickname: message.message,
          localStream: undefined,
          peerConnection: rtcPeerConnection,
          muted: false,
          view: true,
          chat: true,
          isSelf: false,
          // isRoomAdmin: JSON.parse(message.userId),
          isRoomAdmin: false,
          nowStream: 'screen'
        }
        this.$set(this.clients, Number(message.userId), remoteClient)
        console.log('准备完毕,添加了一个连接')
        // this.ch=1
        // this.init(this.ch)
        console.log(this.clients)
        console.log('创建offer')
        rtcPeerConnection.createOffer(offerOptions).then((description) => {
          console.log('创建offer,设置本地Description')
          console.log(description)
          rtcPeerConnection.setLocalDescription(description)
          var msg = new MessageModel(TYPE_COMMAND_OFFER, this.clients[0].roomId, this.messageDateToString(description), message.userId, this.roomFormData.nickname, this.clients[0].isRoomAdmin) // 字段不够用,把名字临时放在roomPw字段
          console.log('发送offer')
          this.wsSend(msg)
        }).catch()
      }
    },
    offerHandle(message) {
      var rtcPeerConnection = new RTCPeerConnection(iceServers)
      rtcPeerConnection.userId = message.userId
      console.log('本地流')
      console.log(this.clients[0].localStream)
      for (const track of this.clients[0].localStream.getTracks()) {
        rtcPeerConnection.addTrack(track, this.clients[0].localStream)
      }

      rtcPeerConnection.ontrack = this.onTrack
      rtcPeerConnection.onicecandidate = this.onIceCandidate
      const remoteClient = {
        userId: message.userId,
        roomId: message.roomId,
        nickname: message.roomPw,
        localStream: undefined,
        peerConnection: rtcPeerConnection,
        muted: false,
        view: true,
        chat: true,
        isSelf: false,
        isRoomAdmin: JSON.parse(message.userId),
        nowStream: 'screen'
      }
      this.$set(this.clients, Number(message.userId), remoteClient)
      console.log('接受到offer,添加了一个连接')
      // this.ch=1
      // this.init(this.ch)
      // console.log(this.clients)
      var sdp = JSON.parse(message.message)
      rtcPeerConnection.setRemoteDescription(new RTCSessionDescription(sdp))
          .then(
              console.log('收到offer,设置远程Description 完毕')
          )
      rtcPeerConnection.createAnswer(offerOptions)
          .then((description) => {
            console.log('创建answer,设置本地Description')
            rtcPeerConnection.setLocalDescription(description)
            var msg = new MessageModel(TYPE_COMMAND_ANSWER, this.clients[0].roomId, this.messageDateToString(description), message.userId)
            // console.log('发送answer:' + JSON.stringify(msg))
            this.wsSend(msg)
          })
          .catch(() => {
                console.log('创建answer,设置本地AndAnswer 错误')
              }
          )
    },
    answerHandle(message) {
      this.clients[Number(message.userId)].peerConnection.setRemoteDescription(JSON.parse(message.message)).then(
          console.log('收到answer,设置远程描述完毕')
      )
      // this.$set(this.clients[Number(message.userId)], 'peerConnection', this.clients[Number(message.userId)].peerConnection)
    },
    onIceCandidate(event) {
      console.log('onIceCandidate')
      // console.log(event)
      if (event.candidate === null) {
        console.log('Candidata为null')
        return
      }
      var msg = new MessageModel(TYPE_COMMAND_CANDIDATE, this.clients[0].roomId, this.messageDateToString(event.candidate), event.target.userId)
      // console.log('发送Candidate:' + JSON.stringify(msg))
      this.wsSend(msg)
    },
    candidateHandle(message) {
      console.log('收到Candidata')
      // console.log(message)
      console.log(this.clients)
      var newIceCandidata = new RTCIceCandidate(JSON.parse(message.message))
      this.clients[Number(message.userId)].peerConnection.addIceCandidate(newIceCandidata)
          .then(console.log('添加Candidata成功')).catch((error) => {
        console.log('添加Candidata失败:' + error)
      })
    },
    onTrack(event) {
      console.log('收到数据流' + event.target.userId)
      this.clients[Number(event.target.userId)].localStream = event.streams[0]
    },
    closeView() {
      if (this.isInRoom === false) {
        this.$router.go(-1)
      }
    },
    async initLocalWebsocket() {
      console.log('初始化weosocket')
      const response = await getUrl()
      this.wsUrl = response.data
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
        console.log('localWebsocket关闭' + e)
      }
    },
    wseReceiveMessage(e) { // 数据接收
      console.log('数据接收:')
      console.log(e.data)
      var str = e.data.toString().replace(/!@#/g, '')
      const message = JSON.parse(str)
      switch (message.command) {
        case TYPE_COMMAND_SUCCESS:
          this.successHandle(message)
          break
        case TYPE_COMMAND_ERROR:
          this.$message.error(message.message)
          break
        case TYPE_COMMAND_CHAT:
          this.receiveMsgHandle(message)
          break
        case TYPE_COMMAND_READY:
          this.readyHandle(message)
          break
        case TYPE_COMMAND_OFFER:
          message.message
          this.offerHandle(message)
          break
        case TYPE_COMMAND_ANSWER:
          this.answerHandle(message)
          break
        case TYPE_COMMAND_CANDIDATE:
          this.candidateHandle(message)
          break
        case TYPE_COMMAND_VIEW:
          this.viewHandle(message)
          break
        case TYPE_COMMAND_MUTED:
          this.mutedHandle(message)
          break
        case TYPE_COMMAND_BAN:
          this.banHandle(message)
          break
        case TYPE_COMMAND_KICK:
          this.kickHandle(message)
          break
      }
    },
    viewHandle(message) {
      if (message.userId === '') {
        if (message.message === 'true') {
          // 全体开启视频
          this.isView = true
          this.clients.forEach(c => {
            if (c !== undefined) {
              c.view = true
            }
          })
        } else {
          // 全体关闭视频
          if (this.fullScreenId) {
            this.$refs.video_full.srcObject = null
            this.isFullscreen = !this.isFullscreen
          }
          this.isView = false
          this.clients.forEach(c => {
            if (c !== undefined) {
              c.view = false
            }
          })
        }
      } else {
        if (message.userId === this.clients[0].userId) {
          if (message.message === 'true') {
            this.clients[0].view = true
            this.clients[0].localStream.getVideoTracks()[0].enabled = true
          } else {
            this.clients[0].view = false
            this.clients[0].localStream.getVideoTracks()[0].enabled = false
            if (this.fullScreenId === '0') {
              this.$refs.video_full.srcObject = null
              this.isFullscreen = 0

            }
          }
        } else {
          if (message.message === 'true') {
            this.clients[Number(message.userId)].view = true
          } else {
            this.clients[Number(message.userId)].view = false
            if (this.fullScreenId === message.userId) {
              this.$refs.video_full.srcObject = null
              this.isFullscreen = 0
            }
          }
        }
      }
    },
    getusers(message) {
      console.log(message)
    },

    mutedHandle(message) {
      if (message.userId === '') {
        if (message.message === 'true') {
          // 全体静音
          this.isMuted = true
          this.clients.forEach(c => {
            if (c !== undefined) {
              c.muted = true
            }
          })
        } else {
          // 全体取消静音
          this.isMuted = false
          this.clients.forEach(c => {
            if (c !== undefined) {
              c.muted = false
            }
          })
        }
      } else {
        if (message.userId === this.clients[0].userId) {
          if (message.message === 'true') {
            this.clients[0].muted = true
            this.clients[0].localStream.getAudioTracks()[0].enabled = false
          } else {
            this.clients[0].muted = false
            this.clients[0].localStream.getAudioTracks()[0].enabled = true
          }
        } else {
          if (message.message === 'true') {
            this.clients[Number(message.userId)].muted = true
          } else {
            this.clients[Number(message.userId)].muted = false
          }
        }
      }
    }, banHandle(message) {
      if (message.userId === '') {
        if (message.message === 'true') {
          // 全体禁言
          this.isBan = true
          this.clients.forEach(c => {
            if (c !== undefined) {
              c.chat = false
            }
          })
        } else {
          // 全体取消禁言
          this.isBan = false
          this.clients.forEach(c => {
            if (c !== undefined) {
              c.chat = true
            }
          })
        }
      } else {
        if (message.userId === this.clients[0].userId) {
          if (message.message === 'true') {
            this.clients[0].chat = false
          } else {
            this.clients[0].chat = true
          }
        } else {
          if (message.message === 'true') {
            this.clients[Number(message.userId)].chat = false
          } else {
            this.clients[Number(message.userId)].chat = true
          }
        }
      }
    },
    kickHandle(message) {
      //所有人
      if (message.userId === '') {
        this.$message.success("已结束会议!")
        this.$router.go(-1)
      }
      if (message.userId === this.clients[0].userId) {
        if (this.selfLeave === false) {
          this.$confirm('您已被请出会议！', '提示', {
            confirmButtonText: '确定',
            type: 'warning'
          })        }
        this.$router.go(-1)

      } else {
        this.clients[Number(message.userId)].peerConnection.close()
        this.clients.splice(Number(message.userId), 1)

      }

    },
    wsSend(data) { // 数据发送
      this.localWebsocket.send(JSON.stringify(data))
    },
    messageDateToString(data) { // 如果message字段是对象，就把他变成字符串，这样服务器解析不会报错
      return '!@#' + JSON.stringify(data) + '!@#'
    }

  }

}

class MessageModel {
  constructor(command, roomId, message, userId, roomPw) {
    this.command = command
    this.userId = userId
    this.roomId = roomId
    this.message = message
    this.roomPw = roomPw
  }
}

const TYPE_COMMAND_ROOM_ENTER = 'enterRoom'
const TYPE_COMMAND_ROOM_CREATE = 'createRoom'
const TYPE_COMMAND_READY = 'ready'
const TYPE_COMMAND_OFFER = 'offer'
const TYPE_COMMAND_ANSWER = 'answer'
const TYPE_COMMAND_CANDIDATE = 'candidate'

const TYPE_COMMAND_ERROR = 'error'
const TYPE_COMMAND_SUCCESS = 'success'
const TYPE_COMMAND_CHAT = 'chat'

const TYPE_COMMAND_MUTED = 'MUTED'
const TYPE_COMMAND_VIEW = 'VIEW'
const TYPE_COMMAND_BAN = 'BAN'
const TYPE_COMMAND_KICK = 'KICK'
const TYPE_COMMAND_GETUSER = "users"
// const TYPE_COMMAND_SIGN = 'SIGN'

const iceServers = {
  'iceServers': [
    {url: 'stun:stun.ekiga.net'},
    {url: 'stun:stun.ideasip.com'}
  ]
}
const offerOptions = {
  iceRestart: true,
  offerToReceiveAudio: true,
  offerToReceiveVideo: true
}
const constraints = {
  audio: true,
  video: true
}
</script>

<style lang="scss" scoped>

.box {
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

.box:hover {
  height: 110vh;
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

.meeting-container {
  height: 95%;
  border-radius: 15px;
  box-sizing: border-box;
  padding-top: 5px;
  justify-content: center;
  align-items: center;
  margin: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-content: center;
}

.el-header {
  background-color: #B3C0D1;
  padding: 0;
  margin: 0;
  //overflow-x: scroll;
  //overflow-y: hidden;
  white-space: nowrap;
}

.el-aside {
  background-color: #D3DCE6;
  margin: 0;
  padding: 8px 10px 0;
  border-radius: 10px;

}


.el-main {
  background-color: #F5F5F5;
  overflow-x: hidden;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.el-container {
}

.el-divider--horizontal {
  display: block;
  height: 1px;
  width: 100%;
  margin: -20px 0;
  background: #616161;
}

.btn {
  background: #E3E3E3;
  border-radius: 10px;
  width: 55px;
  height: 60px;
  border: none;
}

.btn:hover {
  color: #000000;
  -webkit-transform: translateY(-1px);
  transform: translateY(1px);
  box-shadow: 0 7px 14px rgb(50 50 93 / 10%), 0 3px 6px rgb(0 0 0 / 8%);
}

.left_btn {
  position: relative;
  font-size: 30px;
  cursor: pointer;
  top: 95px;
}

.right_btn {
  position: relative;
  font-size: 30px;
  cursor: pointer;
  top: 95px;
}

.el-carousel__arrow {
  border: none;
  outline: 0;
  padding: 0;
  margin: 0;
  height: 40px;
  width: 40px;
  -webkit-transition: .3s;
  transition: .3s;
  border-radius: 50%;
  background-color: rgba(31, 45, 61, .11);
  color: #FFF;
  z-index: 10;
  -webkit-transform: translateY(-50%);
  transform: translateY(-50%);
  text-align: center;
  font-size: 12px
}

.el-carousel__arrow--left {
  left: 16px
}

.el-carousel__arrow--right {
  right: 16px
}

.el-carousel__arrow:hover {
  background-color: rgba(31, 45, 61, .23)
}

.el-carousel__arrow i {
}

.header {
  overflow-x: hidden;
  overflow-y: hidden;
}

/*隐藏滚动条*/
.header .scroll_wrapper::-webkit-scrollbar {
  display: none;
}

.footer {
  width: 100%;
  align-self: center;
  user-select: none;
  height: 60px;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  justify-content: space-between;
  overflow: hidden;
  //align-items: baseline;
}

.button-flex {
  display: flex;
  flex-direction: column;
}

.button-svg {
  height: 30px;
}


</style>

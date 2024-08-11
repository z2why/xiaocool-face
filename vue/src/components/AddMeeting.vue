<template>
<el-container style="background: rgb(250 251 252);">
  <el-header style="background: rgb(255 255 255);">
    <Head/>
  </el-header>

  <div style="background: rgb(255 255 255);">
    <div class="addMeetingBody" :style="meetingId===''?{minHeight:screenHeight+'px'}:1">
      <div class="addMeetingBody-title">
        <div v-if="meetingId===''">加入会议</div>
        <div v-if="meetingId!==''">会议详情</div>
      </div>
      <div class="addMeetingBody-main"  v-if="meetingId===''">
<!--        <p>请输入会议号</p>-->
        <el-input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" class="cl" v-model="code" placeholder="请输入会议号"></el-input>
        <el-row class="btn-style" >
          <el-button type="primary" size="medium" @click="join" @keyup.enter="enterSearch">加入会议</el-button>
        </el-row>
      </div>
    </div>

<!--    手动签到弹窗-->
    <el-dialog title="手动签到"  :visible.sync="dialogTable">
      <Map></Map>

      <div style="z-index: 1;position: relative;left: 36%;top: 10px;">
        <el-button style="margin-right: 60px" @click="cback">返回</el-button>
        <el-button type="primary" @click="sign()">签到</el-button>
      </div>

    </el-dialog>

    <el-dialog title="人脸识别签到" :visible.sync="theface" >

      <div class="container">
        <!-- 开启摄像头 -->
        <!-- <el-button size="mini" @click="openPhoto">摄像头</el-button> -->
        <!-- canvas 截取流 -->
        <canvas ref="canvas" width="500" height="500" style="display: none;"></canvas>
        <!-- 图片展示 -->
        <div class="renlian-container">
          <div class="renlian">
            <video ref="video" muted autoplay playsinline></video>
            <div class="anim mins"></div>
          </div>
        </div>
      </div>
    </el-dialog>


    <div class="detail" STYLE="width: 100%;display: flex;flex-direction: column;align-items: center;" v-if="meetingId!==''">
      <div CLASS="MD">
      <el-card>
      <el-form :model="form" label-width="130px"   style="padding-right: 15px" >
        <el-form-item label="会议主题 :" prop="name">
          <el-input readonly v-model="form.mname"  style="width: 55%;font-size: large"></el-input>
        </el-form-item>
        <el-form-item label="会议时间 :" >
          <el-col :span="5" >
            <el-date-picker
                readonly
                v-model="form.startDate"
                type="datetime"
            >
            </el-date-picker>
          </el-col>
          <el-col class="line" :span="5" style="position: relative;left:12%;top: 3px" >  ——> </el-col>
          <el-col :span="5">
            <el-date-picker
                readonly
                v-model="form.endDate"
                type="datetime"
            >
            </el-date-picker>
          </el-col>
        </el-form-item>

        <el-form-item label="会议号 :" prop="code" >
          <el-input v-model="form.code" style="width: 55%"> </el-input>
        </el-form-item>

        <el-form-item label="会议情况 :" prop="status"  >
          <el-tag size="small"  style="left: 14px;height: 36px;font-size:large;position: relative;top:3px "  v-if="form.status === 0" >线下</el-tag>
          <el-tag size="small"  style="left: 14px;height: 36px;font-size:large;position: relative;top:3px " v-else-if="form.status === 1" >线上</el-tag>
        </el-form-item>



        <el-form-item label="审核情况 :" prop="show"  >
          <el-tag size="small"  style="left: 14px;height: 36px;font-size:large;position: relative;top:3px " v-if="form.show === 0" >未审核</el-tag>
          <el-tag size="small"  style="left: 14px;height: 36px;font-size:large;position: relative;top:3px " v-else-if="form.show === 1" >已通过</el-tag>
          <el-tag size="small"  v-else-if="form.show === 2" >未通过</el-tag>
        </el-form-item>


        <el-form-item label="报名情况 :" prop="checkStatus"  >
          <el-tag size="small"  style="left: 14px;height: 36px;font-size:large;position: relative;top:3px " type="danger" v-if="form.checkStatus === 0" >未报名</el-tag>
          <el-tag size="small"  style="left: 14px;height: 36px;font-size:large;position: relative;top:3px " v-else-if="form.checkStatus === 1" >已报名</el-tag>
        </el-form-item>

        <el-form-item prop="stage" label="会议状态 :">
          <el-tag size="small" style="left: 14px;height: 36px;font-size:large;position: relative;top:3px " v-if="form.stage === 0"  >未开启</el-tag>
          <el-tag size="small" style="left: 14px;height: 36px;font-size:large;position: relative;top:3px " v-else-if="form.stage === 1"  effect="dark">进行中</el-tag>
          <el-tag size="small" style="left: 14px;height: 36px;font-size:large;position: relative;top:3px " v-else-if="form.stage === 2"  type="info" effect="dark">已结束</el-tag>
        </el-form-item>

        <el-form-item label="会议地址 :" prop="address">
          <el-input v-model="form.address" style="width: 75%"></el-input>
        </el-form-item>

        <el-form-item label="创建时间 :" prop="code">
          <el-input v-model="form.createTime" style="width: 100%;"> </el-input>
        </el-form-item>

        <el-form-item label="会议简介 :" prop="address" >
          <div>
            <div v-html="form.description" style="left: 2%;position: relative; "></div>
          </div>
        </el-form-item>
      </el-form>


      </el-card>
        <div style="z-index: 1;position: relative;left: 34%;top: 30px;">
          <el-button style="margin-right: 60px" @click="back">返回</el-button>
          <el-button type="primary" @click="joinMeeting()">进入会议</el-button>
        </div>
    </div>
    </div>
    <div class="footer">
      <Footer style="padding-top: 20px"></Footer>
    </div>
  </div>

</el-container>
</template>

<script>
import Head from "@/components/Head";
import Map from "@/views/Map";
import moment from "moment";
import axios from 'axios'
import theMeeting from "@/views/meeting/index"
import Footer from "@/components/Footer";

export default {
  name: "AddMeeting",
  components: {
    Map,
    Head,
    theMeeting,
    Footer
  },

  children: [
    {
     name:'Map',
      path:'Map',
     components: Map
    },

  ],

  data() {
    return {
      nowTime: new Date(), // 当前时间
      dialogTable:false,
      theface:false,
      meetingId: '',
      form: '',
      code: '',
      uname:'',
      faceIn:1,
      screenHeight:'',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      oldScreen:''
    };
  },
  created() {
    this.enterSearch()
  },
  mounted(){
  this.oldScreen=this.screenHeight= document.documentElement.clientHeight-120;

    let t = 0;
    const that = this;
    window.addEventListener('resize', function () {
      if (t === 0){
      that.screenHeight= document.documentElement.clientHeight-70;
      t++;
      }
      else {
        that.screenHeight=that.oldScreen;
       t--;
      }
    } )

},
  methods: {
    check() {
    },
    back() {
      this.meetingId = ''
      this.code = ''
    },
    sign(){
      this.request.get("/service/AboutMeeting/findMember", {
        params: {
          pageNum: 1,
          pageSize: 100,
          userId:this.user.userId,
          code:this.form.code
        }}).then(res => {
        const form3=res.data.records[0]
        let t1=moment().startOf('millisecond').format("YYYY-MM-DD HH:mm:ss")
        form3.signInDate=JSON.parse(JSON.stringify(t1))

        let t2= moment(this.form.startDate).format("YYYY-MM-DD HH:mm:ss")
        if ( t2 >= t1 ){
          form3.participateStatus=1
        }
        if  (t2 < t1) {
          form3.participateStatus=2
        }
        form3.signAdress=localStorage.getItem("location");

        if (this.form.participateStatus===0){
          this.request.post("/service/AboutMeeting/manualSign",form3)
              .then(res => {
                if (res.code === '200') {
                  this.$message.success("签到成功")
                  if(this.form.createUser===this.user.userId){
                    this.$message.success("创建成功")
                    this.$router.push({
                      name: "theMeeting",
                      params:{
                        meetingName: form3.mname,
                        meetingId:form3.meetingId,
                        roomId:form3.code,
                        udid:form3.udid,
                        qwq:1
                      }
                    })
                  }
                  else{
                    this.$message.success("加入成功")
                    this.$router.push({
                      name: "theMeeting",
                      params:{
                        meetingName: form3.mname,
                        meetingId:form3.meetingId,
                        roomId:form3.code,
                        udid:form3.udid,
                      }
                    })
                  }
                  // this.form.checkStatus = 1
                } else {
                  this.$message.error("签到失败")
                }
              })}
        else{

          this.$message.error("您已签到,请勿重复签到！")
          if(this.form.createUser===this.user.userId){
            this.$message.success("创建成功")
            this.$router.push({
              name: "theMeeting",
              params:{
                meetingName: form3.mname,
                meetingId:form3.meetingId,
                roomId:form3.code,
                udid:form3.udid,
                qwq:1
              }
            })
          }
          else{
            this.$message.success("加入成功")
            this.$router.push({
              name: "theMeeting",
              params:{
                meetingName: form3.mname,
                meetingId:form3.meetingId,
                roomId:form3.code,
                udid:form3.udid
              }
            })
          }
        }
      })
    },

    join() {
      this.request.get("/service/AboutMeeting/join", {
            params: {
              code: this.code,
              userId: this.user.userId
            }
          }
      ).then(res => {
        if (res.code === '200') {
          this.form = res.data
          this.meetingId = res.data.id
          if (res.data.mname === undefined) {
            this.form.mname = res.data.name
            this.form.checkStatus = 0
          }
          if (res.data.checkStatus === 0) {
            this.$confirm('是否报名此次会议？', '您还未报名此会议', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning',
              center: true
            }).then(() => {
              const from1 = {
                // meetingId: res.data.meetingId,
                checkStatus: 1,
                userId: this.user.userId
              }
              if (res.data.udid !== undefined) {
                from1.id = res.data.udid
              }
              if (res.data.meetingId !== undefined) {
                from1.meetingId = res.data.meetingId
              }
              if (res.data.meetingId === undefined) {
                from1.meetingId = res.data.id
              }
              this.request.post('/AboutMeeting', from1)
                  .then(res => {
                    if (res.code === '200') {
                      this.$message.success("报名成功")
                      this.form.checkStatus = 1
                      res.checkStatus = 1
                    } else {
                      this.$message.error("报名失败")
                    }
                  })
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消报名'
              });
            })
          }
        } else {
          this.$message.error("会议号有误！")
        }
      })
    },
    cback(){
      this.dialogTable=false
    },
    joinMeeting(){
      if (this.form.status===0&&this.form.stage!==2){
        if (this.form.createUser===this.user.userId){
        this.$message.success("加入成功")
          this.form.meetingName=this.form.mname
          localStorage.setItem("meeting", JSON.stringify(this.form))  // 存储用户信息到浏览器
          this.$router.push("/AllSign")
        }
        else {
          this.$message.success("您无权操作！")

        }
      }
      else if (this.form.stage===2){
        this.$message({
          showClose: true,
          //message: res.msg,
          type:'error',
          message: '该会议已结束'

        })
      }
      else {
        if (this.form.createUser===this.user.userId){
        if(this.faceIn===1){
        this.theface=true
        this.openPhoto()
      }
        if(this.faceIn===0){
          this.dialogTable=true
          this.faceIn=1
        }
     }
        else {
          if (this.form.stage===1){
            if(this.faceIn===1){
              this.theface=true
              this.openPhoto()
            }
            if(this.faceIn===0){
              this.dialogTable=true
              this.faceIn=1
            }
          }
          else {
            this.$message({
              showClose: true,
              //message: res.msg,
              type:'warning',
              message: '会议还未开始！'

            })
          }
        }
     }
    },
    enterSearch(){
      document.onkeydown = e =>{
           //13表⽰回车键，baseURI是当前页⾯的地址，为了更严谨，也可以加别的，可以打印e看⼀下
        if (e.keyCode === 13 && e.target.baseURI.match(/AddMeeting/)) {
               //回车后执⾏搜索⽅法
          this.join()
        }
      }
    },

//人脸识别
    openPhoto() {
      navigator.mediaDevices
          .getUserMedia({
            audio: true, video: { width: 720, height: 720 }
          })
          .then((success) => {
            // 摄像头开启成功
            this.$refs["video"].srcObject = success; //srcObject 是实时流
            // 实时拍照效果
            this.$refs["video"].play();
            let _this=this
            setTimeout(function()  {
              _this.photograph()
            }, 1140);
          })
          .catch(() => {
            //摄像头开启失败
            this.theface=false
            this.$message.error("识别失败！即将进行手动签到")
            this.dialogTable=true
              this.faceIn=1
          });

    },



    base64toFile (dataBase64, filename = 'file') {
      let arr = dataBase64.split(',')
      let mime = arr[0].match(/:(.*?);/)[1]  //设置file⽂件流的type名称
      let suffix = mime.split('/')[1]  //设置file⽂件流的name名称
      const bstr = window.atob(arr[1]);
      let n = bstr.length
      const u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      return new File([u8arr], `${filename}.${suffix}`, {
        type: mime
      })
    },

    // 拍照
    photograph() {
      let photoInfo= this.$refs["canvas"].getContext("2d");
      // 把当前内容渲染到canvas上
      photoInfo.drawImage(this.$refs["video"], 0, 0, 480, 480);

      // //canvas图片 转base64格式、图片格式转换、图片质量压缩
      let img = this.$refs["canvas"].toDataURL("image/jpg");
      let imgBase64 = this.$refs["canvas"].toDataURL("image/jpg", 0.7);
      let string = imgBase64.replace("data:image/jpg;base64,", "");
      string=this.base64toFile(imgBase64, 'camera');
      let formData=new FormData()
      formData.append('image',string)
      var url = 'https://face.xiaoku.store:5000/recognition'
      axios.post(url, formData)
          .then(res => {
            //人脸识别成功
            let that= this
            if (res.data.results[0].username===this.user.name){
              //签到信息
              let t1=moment().startOf('millisecond').format("YYYY-MM-DD HH:mm:ss")
              const form3= {
                id:that.form.udid,
                meetingId:that.form.meetingId,
                participateStatus:that.form.participateStatus,
                userId:that.user.userId
              }
              form3.signInDate=JSON.parse(JSON.stringify(t1))
              // this.form.udid=form3.id
              let t2= moment(that.form.startDate).format("YYYY-MM-DD HH:mm:ss")
              if ( t2 >= t1 ){
                form3.participateStatus=1
              }
              if  (t2 < t1) {
                form3.participateStatus=2
              }
              if (that.form.participateStatus===0){
                that.request.post("/service/AboutMeeting/manualSign",form3)
                    .then(res => {
                      if (res.code === '200') {
                        if (that.form.createUser === that.user.userId) {
                          const from1 =that.form
                          from1.id=that.form.meetingId
                          from1.stage=1
                          from1.name=that.form.mname
                          from1.startDate=moment(that.form.startDate).format("YYYY-MM-DD HH:mm:ss")
                          from1.endDate=moment(that.form.endDate).format("YYYY-MM-DD HH:mm:ss")
                          // 修改
                          that.request.post('/service/meeting/MyMeeting', from1).then(that.$message.success("创建成功"))
                          that.$router.push({
                            name: "theMeeting",
                            params: {
                              meetingName: that.form.mname,
                              meetingId: that.form.meetingId,
                              roomId: that.form.code,
                              udid: that.form.udid,
                              qwq: 1
                            }
                          })
                        } else {
                          that.$message.success("加入成功")
                          that.$router.push({
                            name: "theMeeting",
                            params: {
                              meetingName: that.form.mname,
                              meetingId: that.form.meetingId,
                              udid: that.form.udid,
                              roomId: that.form.code
                            }
                          })
                        }
                      }})}
              else{
                that.$message.error("您已签到,请勿重复签到！")
                if(that.form.createUser===that.user.userId){
                  const from1 =that.form
                  from1.id=that.form.meetingId
                  from1.stage=1
                  from1.name=that.form.mname
                  from1.startDate=moment(that.form.startDate).format("YYYY-MM-DD HH:mm:ss")
                  from1.endDate=moment(that.form.endDate).format("YYYY-MM-DD HH:mm:ss")

                  that.request.post('/service/meeting/MyMeeting', from1)
                  that.$message.success("创建成功")
                  that.$router.push({
                    name: "theMeeting",
                    params:{
                      meetingName: that.form.mname,
                      meetingId:that.form.meetingId,
                      roomId:that.form.code,
                      udid:that.form.udid,
                      qwq:1
                    }
                  })
                }
                else{
                  that.$message.success("加入成功!")
                  that.$router.push({
                    name: "theMeeting",
                    params:{
                      meetingName: that.form.mname,
                      meetingId:that.form.meetingId,
                      roomId:that.form.code,
                      udid:that.form.udid
                    }
                  })
                }}
            }else {
              that.theface=false
              that.$message.error("识别失败！即将进行手动签到")
                 that.faceIn=0
                if(that.faceIn===0){
                  that.dialogTable=true
                  that.faceIn=1
                }

            }
          })
          .catch(err => {
            this.theface=false
            this.$message.error("识别失败！即将进行手动签到")
            this.faceIn=0
            if(this.faceIn===0){
              this.dialogTable=true
              this.faceIn=1
            }
          })
    }



  }
};



</script>


<style scoped>


::v-deep .el-input__inner {

  -webkit-appearance: none;
  background-color: #FFF;
  border-radius: 4px;
  border: 1px solid #DCDFE6;
  box-sizing: border-box;
  color: #606266;
  display: inline-block;
  font-size: large;
  height: 50px;
  line-height: 51px;
  border-top-width: 2px;
  outline: 0;
  padding: 0 15px;
  transition: border-color .2s cubic-bezier(.645,.045,.355,1);
  width: 100%;
}

.cl{
  top: 10px;
  width: 140%;
  left: -55px;
}
.addMeetingBody {
  text-align: center;
  margin-top: 10px;
}

.addMeetingBody .addMeetingBody-title  div{
  user-select: none;
  height: 120px;
  font-weight: 400;
  line-height: 120px;
  font-size: 35px;
  background-color: #fafbfc;
  border-bottom: 1px solid #f4f5f6;

}

.addMeetingBody-main{
  position: relative;
  width: 300px;
  /*height: 383px;*/
  margin: 90px auto 0;
  top: 41px;
}
.addMeetingBody-main p{
  margin-bottom:32px ;
}

::v-deep  .cl .el-input__inner{
  text-align: center;
}
.btn-style{
  margin-top: 70px;
}
.btn-check{
  position: absolute;
  left: 1122px;
  top: 328px;
}
.MD{
  width: 900px;
  padding: 26px;
  margin: 14px;
}




.el-tag--small {
  height: 33px;
  padding: 10px 9px 31px;
  line-height: 22px;
  font-size: initial;


}

::v-deep .detail .el-input__inner {
  border: 1px solid #ffffff;
  font-size: large;
  font-family: 微软雅黑;
  width: 314px;

}
::v-deep .MD .el-input__inner:hover {
  border-color: #ffffff;
}
::v-deep .MD .el-form-item__label {
  font-size: large;
}
::v-deep .MD .el-form-item__label {
  text-align: right;
  vertical-align: middle;
  float: left;
  color: #606266;
  line-height: 50px;
  padding: 0px 12px 0px 0px;
  box-sizing: border-box;
}
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.renlian-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.renlian {
  background: #303133;
  border-radius: 50%;
  border: 5px solid #909399;
  overflow: hidden;
  width: 80%; /* 使用百分比宽度，确保自适应 */
  max-width: 477px; /* 保持最大尺寸 */
  aspect-ratio: 1; /* 保持宽高比为1，确保是圆形 */
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

video {
  width: 100%;
  height: 100%;
  min-height: 100%;
  object-fit: cover;
  transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
}

.anim.mins {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  box-shadow: inset 0px 0px 0px 18px #ff1010c7;
  animation: idea 3s infinite linear;
}

@keyframes idea {
  100%{
    -webkit-clip-path: polygon(100% 0%, 100% 100%, 0% 100%, 0% 0%, 100% 0%, 50% 50%, 50% 0%);
  }
  87.5% {
    -webkit-clip-path: polygon(100% 0%, 100% 100%, 0% 100%, 0% 0%, 50% 0%, 50% 50%, 50% 0%);
  }
  75% {
    -webkit-clip-path: polygon(100% 0%, 100% 100%, 0% 100%, 0% 50%, 0% 50%, 50% 50%, 50% 0%);
  }
  62.5% {
    -webkit-clip-path: polygon(100% 0%, 100% 100%, 0% 100%, 0% 100%, 0% 100%, 50% 50%, 50% 0%);
  }
  50% {
    -webkit-clip-path: polygon(100% 0%, 100% 100%, 100% 100%, 50% 100%, 50% 100%, 50% 50%, 50% 0%);
  }
  37.5% {
    -webkit-clip-path: polygon(100% 0%, 100% 100%, 100% 100%, 100% 100%, 100% 100%, 50% 50%, 50% 0%);
  }
  25% {
    -webkit-clip-path: polygon(100% 0%, 100% 50%, 100% 50%, 100% 50%, 100% 50%, 50% 50%, 50% 0%);
  }
  12.5% {
    -webkit-clip-path: polygon(100% 0%, 100% 0%, 100% 0%, 100% 0%, 100% 0%, 50% 50%, 50% 0%);
  }
  0% {
    -webkit-clip-path: polygon(100% 0%, 100% 0%, 100% 0%, 100% 0%, 50% 0%, 50% 50%, 50% 0%);
    -webkit-box-shadow: inset 0px 0px 0px 18px #999;
  }
}
</style>


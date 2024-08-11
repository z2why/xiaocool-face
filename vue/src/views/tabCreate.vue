<template>
  <div class="created">
    <el-table :data="tableData"   stripe style="width: 100%" v-loading="loading" element-loading-text="拼命加载中">
      <el-table-column fixed prop="startDate" label="开始时间"  width="200" sortable>
      </el-table-column>
      <el-table-column prop="name" label="会议主题" width="auto" align="center" min-width="200">
        <template slot-scope="scope">
          <a @click="MeetingDetail(scope.row)" style="color: #409eff;cursor:pointer;font-size: 16px">{{scope.row.name}}</a>
        </template>
      </el-table-column>
      <el-table-column prop="code" label="会议号" align="center" width="180"> </el-table-column>


      <el-table-column
          prop="participateStatus" label="会议情况"  width="auto" align="center">
        <template slot-scope="scope">
          <el-tag size="small"  v-if="scope.row.status === 0"  type="info " >线下</el-tag>
          <el-tag size="small" v-else-if="scope.row.status === 1"  >线上</el-tag>
        </template>
      </el-table-column>


      <el-table-column
          prop="show" label="审核状况" width="auto" align="center">
        <template slot-scope="scope">
          <el-tag size="small"  v-if="scope.row.show === 0"  type="info " >未审核</el-tag>
          <el-tag size="small" v-else-if="scope.row.show === 1"  >已通过</el-tag>
          <el-tag size="small" v-else-if="scope.row.show === 2" type="warning" effect="dark">未通过</el-tag>
        </template>
      </el-table-column>



      <el-table-column prop="city" align="center" label="操作" fixed="right" width="360">
        <template slot-scope="scope">

              <el-button size="mini" v-if="scope.row.stage===0" slot="title" plain
                      style="margin-left: 5px;margin-right: 4px;"   @click="meetingHandle(scope.row)">开始会议
              </el-button>
          <el-button size="mini" v-if="scope.row.stage===1" slot="title" plain
                     style="margin-left: 5px;margin-right: 4px;"   @click="meetingHandle(scope.row)">继续会议
          </el-button>

          <el-button size="mini" style="margin-right: 9px; margin-left: 14px;"
                     v-if="scope.row.stage===2" type="info" plain disabled>已结束</el-button>

          <el-divider direction="vertical"></el-divider>
          <el-button style="padding-top: 20px;" type="text" @click="MeetingMember(scope.row)">数据操作</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" @click="MeetingInformation(scope.row)">数据统计</el-button>
        </template>
      </el-table-column>

    </el-table>


    <div style="margin: 10px 0;position: relative;left: 35%">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[3, 5, 7]"
          :page-size="pageSize"
          background
          layout="prev,sizes,  pager, next, jumper"
          >
      </el-pagination>
    </div>

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

    <!--    手动签到弹窗-->
    <el-dialog title="手动签到"  :visible.sync="dialogTable">
      <Map></Map>

      <div style="z-index: 1;position: relative;left: 36%;top: 10px;">
        <el-button style="margin-right: 60px" @click="cback">返回</el-button>
        <el-button type="primary" @click="sign()">签到</el-button>
      </div>

    </el-dialog>




    <el-dialog title="会议详情"  :visible.sync="dialogTableVisible">

      <template slot-scope="scope">

        <!--form类型的会议详情-->
        <div class="detail">
          <el-form :model="form" label-width="130px"   style="padding-right: 15px" >
            <el-form-item label="会议主题 :" prop="name">
              <el-input readonly v-model="form.name"  style="width: 55%;font-size: large"></el-input>
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
              <el-col class="line" :span="6" style="position: relative;left:17%" >  ——> </el-col>
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


            <el-form-item label="会议情况 :" prop="status" >
              <el-tag size="small"  v-if="form.status === 0" style="left: 14px;height: 36px;font-size:large;position: relative" >线下</el-tag>
              <el-tag size="small" v-else-if="form.status === 1" style="left: 14px;height: 36px;font-size:large;position: relative" >线上</el-tag>
            </el-form-item>


            <el-form-item label="审核情况 :" prop="show" >
              <el-tag size="small"  style="left: 14px;height: 36px;font-size:large;position: relative" type="info" v-if="form.show === 0" >未审核</el-tag>
              <el-tag size="small"   style="left: 14px;height: 36px;font-size:large;position: relative" v-else-if="form.show === 1" >已通过</el-tag>
              <el-tag size="small" style="left: 14px;height: 36px;font-size:large;position: relative" effect="dark" type="info" v-else-if="form.show === 2" >未通过</el-tag>
            </el-form-item>



            <el-form-item label="会议地址 :" prop="address">
              <el-input v-model="form.address" style="width: 75%"></el-input>
            </el-form-item>

            <el-form-item label="创建时间 :" prop="code">
              <el-input v-model="form.createTime" style="width: 45%"> </el-input>
            </el-form-item>

            <el-form-item label="会议简介 :" prop="address" >
              <div>
                <div v-html="form.description" style="left: 2%;position: relative; "></div>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </template>
    </el-dialog>
<!--参会人员-->
    <div class="MeetingMember">
      <el-dialog title="数据操作"  :visible.sync="operation">
        <template slot-scope="scope">

          <div>
<!--            v-bind:title="message"-->
            <el-tooltip class="tooltip" content="数据导出" placement="bottom" effect="light" :visible-arrow="false">
            <el-button
              style=" position: absolute;  left: 25%;  top: 6%;"
              icon="el-icon-download"
              @click="exp()" circle></el-button>
            </el-tooltip>

          <el-upload :action='$baseUrl+"/AboutMeeting/import?meetingId="+meetingId' show-file-list="true" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
            <el-popover trigger="hover">
             <p> <span style="font-size: 16px;position: relative;top: 3px;user-select: none">数据导入,</span> <el-link :href='$baseUrl+"/file/34d52113787646f38c37f446420d4348.xlsx"' type="primary">模板下载</el-link></p>
              <el-button
                  slot="reference"   style=" position: absolute;  left: 18%;  top: 6%;"
                  icon="el-icon-upload2"
                  circle></el-button>
            </el-popover>
          </el-upload>
          </div>

          <el-table :data="getData"   style="margin-bottom: 10px" v-loading="loading1" element-loading-text="拼命加载中">

            <el-table-column
                prop="code" label="会议号" width="100" align="center">
            </el-table-column>

            <el-table-column
                prop="userId" label="用户编号" width="180" align="center">
            </el-table-column>

            <el-table-column
                prop="uname" label="用户姓名" width="180" align="center">
            </el-table-column>

            <el-table-column
                prop="participateStatus" label="签到情况" width="180" align="center">
              <template slot-scope="scope">
                <el-tag size="small"  v-if="scope.row.participateStatus === 0"  type="info " >未签到</el-tag>
                <el-tag size="small" v-else-if="scope.row.participateStatus === 1"  >已签到</el-tag>
                <el-tag size="small" v-else-if="scope.row.participateStatus === 2" type="warning" effect="dark">迟到</el-tag>
                <el-tag size="small" v-else-if="scope.row.participateStatus === 3" type="warning" effect="dark">早退</el-tag>
              </template>
            </el-table-column>

            <el-table-column
                prop="signInDate" label="参会时间" width="250" align="center">

            </el-table-column>

                      <el-table-column
                          prop="signOutDate" label="签退时间" width="300" align="center">
                      </el-table-column>


          </el-table>
          <div style="margin-top: 10px;position: relative;left: 35%">
          </div>

        </template>
      </el-dialog>
    </div>

    <div CLASS="MeetingInformation">
    <el-dialog title="数据统计" @open="open()" destroy-on-close :visible.sync="dialogTables" width="960px" >
      <el-divider></el-divider>

      <el-row :gutter="10" style="margin-bottom: 30px">
        <el-col :span="6" style="padding-left: 25px; padding-right: 5px;">
          <el-card>
            <div style="color:rgb(135 191 143)"><i class="el-icon-star-off"></i>
              系统会议数目
            </div>
            <div style="padding: 10px 0;text-align: center;font-weight: bold">
              {{allmeeting}}
            </div>
          </el-card>
        </el-col>



        <el-col :span="6" style="padding-right: 25px;" >
          <el-card>
            <div style="color: rgb(135 191 143)"><icon class="el-icon-user"> </icon>
              系统参会总数
            </div>
            <div style="padding: 10px 0;text-align: center;font-weight: bold">
              {{information.Allnum}}
            </div>
          </el-card>
        </el-col>



        <el-col :span="6" style="padding-left: 30px;padding-right: 0px">
          <el-card>
            <div style="color: #409eff"><i CLASS="el-icon-user-solid"></i>
              该会参会人数
            </div>
            <div style="padding: 10px 0;text-align: center;font-weight: bold">
              {{Nums}}
            </div>
          </el-card>
        </el-col>


        <el-col :span="6" style="padding-left: 10px; padding-right: 20px;">
          <el-card>
            <div style="color: #409eff"><i CLASS="el-icon-success"></i>
              该会议签到率
            </div>
            <div style="padding: 10px 0;text-align: center;font-weight: bold">
              {{rate}}%
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="5">
        <el-col :span="12">
          <div id="main" style="width: 500px;height: 450px"></div>
        </el-col>
        <el-col :span="12">
          <div id="pie" style="width: 500px;height: 460px"></div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>



  </div>
</template>

<script>
import * as echarts from 'echarts'
import Map from "@/views/Map";
import moment from "moment";
import axios from 'axios'
export default {
  name: "tabCreate",
  components: {
    Map
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
      qwq:1,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      mid:'',
      message:'数据导出',
      message1:'数据导入',
      meetingId:'',
      tableData: [],
      getData:[],
      pageNum: 1,
      pageSize: 5,
      dialogTableVisible:false,
      dialogTable:false,
      operation:false,
      dialogTables:false,
      information:{},
      Allnum:'',
      Earlynum:'',
      LATERnum:'',
      Alrnum:'',
      faceIn:1,
      theface:false,
      Nums:'',
      rate:'',
      allmeeting:'',
      loading:true,
      loading1:true
    };
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
      drawLine() {
        let chartDom = document.getElementById('main');
        let myChart = echarts.init(chartDom);
        var option = {
          title:{
            text:'系统各季度会议情况',
            size:'large',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          xAxis: {
            type: 'category',
            data: ["第一季度","第二季度","第三季度","第四季度"]
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: [],
              type: 'line'
            },
            {
              data: [],
              type: 'bar'
            }
          ]
        };

        this.request.get("/service/echarts/members").then(res=>{
          this.allmeeting=res.data[0]+res.data[1]+res.data[2]+res.data[3]
          option.series[0].data= res.data
          option.series[1].data= res.data
          myChart.setOption(option);
        })

      },
      drawBar(){
        echarts.init( document.getElementById('pie'));
        let myChart = echarts.init(document.getElementById('pie'));
        const option = {
          title:{
            text:'该会议人员参会情况',
            size:'large',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              name: '该会议人员参会情况',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data:[]
            }
          ]
        };
        this.request.get("/service/echarts/hello",
            {
              params: {
                meetingId:this.mid
              }}
        ).then(res=>{
          option.series[0].data= res.data
          if (res.data[0].value!==0){
            this.rate=(res.data[0].value/this.Nums).toFixed(2)*100
          }
          else{
            this.rate=0
          }
          option.series[0].data.push({name:'未签到',value:this.Nums-res.data[0].value-res.data[1].value-res.data[2].value})
          myChart.setOption(option);
        })
      },

      open() {
        const t = this;
        setTimeout(() => {
          //  执行echarts画图方法
          t.drawBar();
          t.drawLine();


        },0)
      },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load();
    },
    imp(){

    },


    exp(){
      this.downloadLoading = true
      const infos=this.getData
      import('@/utils/Export2Excel.js').then(excel => {
        const tHeader = ['会议号', '用户编号', '用户姓名', '签到情况', '入会时间','签退时间']
        const filterVal = ['code', 'userId', 'uname', 'participateStatus', 'signOutDate']
        for (let i=0; i<this.getData.length; i++){
          //0未签到 1 已签到 2 迟到 3早退
          if (this.getData[i].participateStatus===0){
            infos[i].participateStatus='未签到'
          }
          if (this.getData[i].participateStatus===1){
            infos[i].participateStatus='已签到'
          }
          if (this.getData[i].participateStatus===2){
            infos[i].participateStatus='迟到'
          }
          if (this.getData[i].participateStatus===3){
            infos[i].participateStatus='早退'
          }
        }
        excel.export_json_to_excel2(tHeader,infos, filterVal, this.mename+'参会人员信息')
        this.downloadLoading = false
      })

    },



    load() {
      this.request.get("/service/meeting/MyMeeting/findCreate", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          createUser: this.user.userId
        }

      }).then(res => {
            this.tableData = res.data.records
            this.loading=false
          }
      )
    },
    MeetingInformation(row){
      this.mid=row.id
      this.dialogTables = true
      this.mename=row.name
      this.request.get("/service/AboutMeeting/findMember", {
        params: {
          pageNum: 1,
          pageSize: 100,
          code:row.code
        }
      }).then(res => {
        this.Nums=res.data.total
          }
      )


      this.request.get("/service/echarts",{
        params:{
          meetingId:row.id

        }
      }).then(res =>{
            this.information=res.data

            // this.meeting.meetingId=row.id
          }
      )

    },

    meetingHandle(row){
      if (row.stage === 0 && row.show === 1||row.stage === 1 && row.show === 1) {
          this.$confirm('将于现在开启【' + row.name + '】会议, 是否确带继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.request.get("/service/AboutMeeting/join", {
                  params: {
                    code: row.code,
                    userId: this.user.userId
                  }
                }
            ).then(res => {
                  if (res.code === '200') {
                    this.form = res.data
                    const from1 =row
                    from1.stage=1
                    // 修改
                    this.request.post('/service/meeting/MyMeeting', from1)
                        .then(res => {
                          this.form.meetingId=row.id

                          console.log(this.form)
                          this.joinMeeting(this.form)
                        })
                  }
                  else{
                    this.$message({
                      type:'error',
                      showClose: true,
                      message: '有误！'})
                  }
                }

            )
          })

        }
      else {
        this.$message({
          type:'error',
          showClose: true,
          message: '还未通过审核！'})
      }
    },
    MeetingMember(row){
      this.operation = true
      this.mename=row.name
      this.request.get("/service/AboutMeeting/findMember", {
        params: {
          pageNum: 1,
          pageSize: 100,
          code:row.code
        }
      }).then(res => {
        this.getData = res.data.records
        this.meetingId=row.id
        this.loading1=false
          }
      )

    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    MeetingDetail(row){
      this.form=row
      console.log(this.form)
      this.form.meetingId=row.id
      this.dialogTableVisible = true
    },




    // 开始会议
    joinMeeting(row){
      if (row.status===0){
        if (row.createUser===this.user.userId){
          row.meetingName=row.mname
          localStorage.setItem("meeting", JSON.stringify(row))
          this.$router.push("/AllSign")
        }
        else {
          this.$message({
            type:'error',
            showClose: true,
            message: '无权操作！'})
        }
      }
      else {
        if(this.faceIn===1){
          this.theface=true
          this.openPhoto()
        }
        if(this.faceIn===0){
          this.dialogTable=true
          this.faceIn=1
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
                        that.$message.success("签到成功")
                        if (that.form.createUser === that.user.userId) {
                          that.$message.success("创建成功")
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
              this.theface=false
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
    },
    cback(){
      this.dialogTable=false
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


  },
}
</script>

<style scoped lang="scss">
::v-deep .el-tooltip__popper{
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
  border: 1px solid #EBEEF5;
  color: #606266;
}
.el-tag--small {
  height: 33px;
  padding: 4px 9px;
  line-height: 22px;
  font-size: initial;
}

::v-deep .el-table__fixed {
  height: 100% !important;

}
::v-deep .detail .el-input__inner {
  border: 1px solid #ffffff;
  font-size: large;
  font-family: 微软雅黑;
  width: 245px;
}
::v-deep .el-input__inner:hover {
  border-color: #ffffff;
}
::v-deep  .el-form-item__label {
  font-size: large;
}

::v-deep .el-dialog__title {
  line-height: 41px;
  font-size: 20px;
  padding-left: 10px;
  color: #000000;
}

::v-deep .MeetingMember  .el-dialog__body {
  padding: 3px 20px 10px 43px;
}
::v-deep .MeetingInformation .el-dialog__body {
  padding-top:0;
}
::v-deep .el-submenu.is-opened>.el-submenu__title .el-submenu__icon-arrow{
  display: none;

}

::v-deep .el-submenu>.el-submenu__title .el-submenu__icon-arrow{
  display: none;
}



::v-deep .el-menu--horizontal > .el-submenu .el-submenu__title{
 padding: 0;
}



::v-deep .el-menu--horizontal > .el-submenu .el-submenu__title:hover {
  background-color: transparent ;

}
.el-menu::after, .el-menu::before {
  display: table;
  content: none;
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

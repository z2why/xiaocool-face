<template>
<!--申请会议-->
  <div style="user-select: none;" >
    <div id="biaotou" >
      <div style="height: 80px;user-select: none;">
        <h1>预约会议</h1>
      </div>
    </div>

    <el-card style="width: 1100px"  v-if="qwq===2">
      <h1 align="center">申请成功！您的会议号为:<span style="font-size: xx-large;user-select: text;padding-left: 20px">{{form.code}}</span></h1>

      <!--form类型的会议详情-->
      <div class="detail">
        <el-form :model="form" label-width="130px"   id="selectForm"  style="padding-right: 15px;"  >
          <el-form-item label="会议主题 :" prop="name"  >
            <el-input readonly v-model="form.name"  style="width: 55%;font-size: 22px"></el-input>
          </el-form-item>
          <el-form-item label="会议时间 :" >
            <el-col :span="5">
              <el-date-picker
                  readonly
                  v-model="form.startDate"
                  type="datetime"
                  :append-to-body="false"
              />
            </el-col>
            <el-col class="line" :span="5" style="position: relative;left:12%" >  ——> </el-col>
            <el-col :span="5">
              <el-date-picker
                  readonly
                  v-model="form.endDate"
                  type="datetime"
                  align="center"
                  :append-to-body="false"
              >
              </el-date-picker>
            </el-col>
          </el-form-item>

          <el-form-item label="会议号 :" prop="code" >
            <el-input v-model="form.code" style="width: 55%"> </el-input>
          </el-form-item>


          <el-form-item label="会议情况 :" prop="status" >
            <el-tag size="small"  v-if="form.status === 0" style="left: 14px;height: 37px;font-size:large;position: relative" >线下</el-tag>
            <el-tag size="small" v-else-if="form.status === 1" style="left: 14px;height: 36px;font-size:large;position: relative" >线上</el-tag>
          </el-form-item>



          <el-form-item label="会议地址 :" prop="address">
            <el-input v-model="form.address" style="width: 75%;font-size: 22px"></el-input>
          </el-form-item>

          <el-form-item label="创建时间 :" prop="code">
            <el-input v-model="form.createTime" style="width: 45%"> </el-input>
          </el-form-item>

          <el-form-item label="会议简介 :" prop="address" >
            <div>
              <div v-html="form.description" style="height:auto;left: 2%;position: relative;font-size: 22px "></div>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-card>


    <el-card style="width:90%;margin-bottom: 10px;" v-else-if="qwq===1" >
      <div style="padding: 15px;">
      <el-form :model="form" label-width="140px"   style="padding-right: 15px" :rules="rules">
        <el-form-item label="会议主题 :" prop="name">
          <el-input v-model="form.name" clearable style="width: 50%;font-size: 16px"></el-input>
        </el-form-item>


        <el-form-item label="会议时间  :" required >
          <el-date-picker
              required
              :picker-options="pickerOptions1"
              editable="false"
              v-model="form.value1"
              value-format="yyyy-MM-dd hh:mm:ss"
              type="datetimerange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              >
          </el-date-picker>


        </el-form-item>


        <el-form-item label="会议类型 :"  label-width="140px"   prop="status" required >
          <el-radio-group v-model="form.status" style="padding-right: 15px" >
            <el-radio :label="1" >线上</el-radio>
            <el-radio :label="0">线下</el-radio>
          </el-radio-group>
        </el-form-item>


        <el-form-item label="会议地址 :" prop="address">
          <el-input v-model="form.address" style="width: 75%;font-size: 16px"></el-input>
        </el-form-item>


        <el-form-item label="会议简介 : " >
          <div style="border: 1px solid #ccc;">
            <Editor
                style="height: 200px; overflow-y: hidden;"
                v-model="form.description"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="onCreated"
            />
          </div>
        </el-form-item>
      <div STYLE=" padding-left: 35%">
        <el-form-item >
          <el-button @click="$router.push('/AddMeeting')">返回</el-button>
          <el-button type="primary" @click="save" style="position: relative;left: 30px">预定会议</el-button>
        </el-form-item>
      </div>
      </el-form >
    </div>
    </el-card>
  </div>
</template>

<script>

import moment from "moment"
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
export default {
  name: "ScheduledMeeting",
  components: { Editor, Toolbar },
  data() {

    return {
      // serverIp: serverIp,
      pickerOptions1: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        },},
      rules:{

        name: [
          { required: true, message: '请输入会议名称', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }

        ],
        time: [
          { required: true, message: '请选择日期', trigger: 'blur' }
        ],
      },
      dialogVisible:false,
      editor: null,
      toolbarConfig: { },
      editorConfig: { placeholder: '' },
      mode: 'default',
      qwq:1,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      form: {},
    }

  },

  methods: {
    // async getUser() {
    //   let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
    //   return (await this.request.get("/service/user/" + username)).data
    // },
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },

    save() {
      console.log(this.form)
      if(this.form.name!==undefined&&this.form.status!==undefined&&this.form.value1!==undefined){
      const random = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
      var code = ''
      const rdm=[5,6]
      for (let i = 0; i <rdm[Math.floor(Math.random()*2)]; i++) {
        let index = Math.floor(Math.random() * 9);
        code += random[index];
      }
      this.form.code = code;
      this.form.startDate=moment(this.form.value1[0]).format("YYYY-MM-DD HH:mm:ss")
      this.form.endDate=moment(this.form.value1[1]).format("YYYY-MM-DD HH:mm:ss")
      this.form.createTime=moment().startOf('millisecond').format("YYYY-MM-DD HH:mm:ss")
      console.log(this.form.code)
      this.form.createUser=this.user.userId
      this.request.post("/service/meeting/MyMeeting", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("预约成功!")
          this.qwq=2;
          console.log(res,"会议信息")
        } else {
          this.$message.error("预约失败！")
        }
      })}
      else{
        this.$message.error("请输入信息！")
      }
    },

  },
  mounted() {
    // 模拟 ajax 请求，异步渲染编辑器
    // setTimeout(() => {
    //   this.form.description = '<p>'+this.meeting.description+'</p>'
    // }, 1500)
  },
  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器
  },
}
</script>
<style>

.el-popper[x-placement^=bottom] .popper__arrow{
  display: none;
}
.thet{
  background-color: black;
  margin-top: 100px;
}
</style>
<style scoped>

h1 {
  font-weight: 400;
  height: 60px;
  line-height: 60px;
  font-size: 26px;
}
::v-deep .el-form-item__label{
  font-size: medium;
  padding: 4px 24px 15px 4px;
}


::v-deep .el-radio__label{
  font-size: 16px;
}

::v-deep .el-form-item__content{
  line-height: 46px;
}
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
/deep/ .el-form-item__error {
  color: #E10000AD;
  font-size: 14px;
  top: 80%;
}
::v-deep .el-picker-panel__body{
  margin-top: 100px;
}

.detail{
  padding: 60px;

}
#selectForm >>> .el-form-item__label{
  font-size: 22px;
}


::v-deep .detail .el-input__inner {
  border: 1px solid #ffffff;
  font-size: 22px;
  font-family: 微软雅黑;
  width: 300px;
  padding-top: 5px;}
::v-deep .detail .el-input__inner:hover {
  border-color: #ffffff;
}
::v-deep  .el-form-item__label {
  font-size: medium;
}

.el-radio__label .el-form-item__label {
  font-size: medium;
}
::v-deep .el-dialog__title {
  line-height: 41px;
  font-size: 20px;
  padding-left: 10px;
  color: #000000;
}

::v-deep .el-tag--small {

  line-height: 31px;
}

</style>


<style src="@wangeditor/editor/dist/css/style.css"></style>
<template>
<!--补签审核-->
<div class="MeetingMember">
    <el-table :data="getData" v-loading="loading" element-loading-text="拼命加载中"  style="margin-bottom: 10px">

      <el-table-column
          prop="code" label="会议号" width="130" align="center">
      </el-table-column>

      <el-table-column
          prop="mname" label="会议名称" min-width="250" width="auto" align="center">
        <template slot-scope="scope">
          <a @click="MeetingDetail(scope.row)" style="color: #409eff;cursor:pointer;font-size: 16px">{{scope.row.mname}}</a>
        </template>
      </el-table-column>

      <el-table-column
          prop="userId" label="用户编号" width="100" align="center">
      </el-table-column>

      <el-table-column
          prop="uname" label="用户姓名" width="180" align="center">
      </el-table-column>


      <el-table-column
          prop="participateStatus" label="签到情况" width="130" align="center">
        <template slot-scope="scope">
          <el-tag size="small"  v-if="scope.row.participateStatus === 0"  type="info " >未签到</el-tag>
          <el-tag size="small" v-else-if="scope.row.participateStatus === 1"  >已签到</el-tag>
          <el-tag size="small" v-else-if="scope.row.participateStatus === 2" type="warning" effect="dark">迟到</el-tag>
          <el-tag size="small" v-else-if="scope.row.participateStatus === 3" type="warning" effect="dark">早退</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          prop="stage" label="会议状态" width="130" align="center">
        <template slot-scope="scope">
          <el-tag size="small"  v-if="scope.row.stage === 0"  >未开启</el-tag>
          <el-tag size="small" v-else-if="scope.row.stage === 1" type="warn" effect="dark">进行中</el-tag>
          <el-tag size="small" v-else-if="scope.row.stage === 2" type="info" effect="dark">已结束</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          label="操作"
          prop="icon"
          width="250"
          align="center">

        <template slot-scope="scope" >
          <el-button icon="el-icon-check" type="success" v-model="scope.row.participateStatus" active-color="#13ce66"
                     size="medium" inactive-color="#ccc" @click="check(scope.row.udid,scope.row.participateStatus,scope.row.mname,scope.row.userId,scope.row.meetingId,scope.row.uname)" plain>通过
          </el-button>
          <el-button icon="el-icon-close" type="warning" v-model="scope.row.participateStatus" active-color="#13ce66"
                     size="medium" inactive-color="#ccc" @click="checkout(scope.row.udid,scope.row.participateStatus,scope.row.mname,scope.row.userId,scope.row.meetingId,scope.row.uname)" plain>不通过
          </el-button>
  <!--          <el-button icon="el-icon-close" type="danger" v-model="scope.row.participateStatus" active-color="#13ce66"-->
  <!--                     size="medium"    inactive-color="#ccc" @click="checkout(scope.row.udid,scope.row.participateStatus)" plain>不通过-->
  <!--          </el-button>-->
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


  <div style="margin-top: 10px;position: relative;left: 35%">
      <el-dialog title="会议详情"  :visible.sync="dialogTableVisible">

        <template slot-scope="scope">

          <!--form类型的会议详情-->
          <div class="detail">
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
                <el-tag size="small"  style="padding-left: 10px" v-if="form.show === 0" >未审核</el-tag>
                <el-tag size="small"   style="left: 14px;height: 36px;font-size:large;position: relative" v-else-if="form.show === 1" >已通过</el-tag>
                <el-tag size="small"  type="danger" v-else-if="form.show === 2" >不通过</el-tag>
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

    </div>


</div>

</template>

<script>
export default {
  name: "tabAudit",
  data(){
    return{
      getData:[],
      pageNum: 1,
      dialogTableVisible:false,
      pageSize: 5,
      loading:true,
      mname:'',
      uname:'',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },

  created() {
    //请求分页查询数据
    // this.user=  JSON.parse(localStorage.getItem("user"))
    this.load()
  },
  methods: {

    load() {
      this.request.get("/service/AboutMeeting/findMember", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          createUser:this.user.userId
        }

      }).then(res => {
            this.getData = res.data.records
            this.loading=false
          }
      )
    },
    check(udid,participateStatus,mname,userId,meetingId,uname) {
      this.$confirm('是否通过 "'+uname+'" 关于《'+ mname + '》 的补签申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const from1 = {
          id: udid,
          participateStatus: 1,
          meetingId:meetingId,
          userId: userId

        }
        //修改
        this.request.post('/service/AboutMeeting', from1)
            .then(res => {
              if (res.code === '200') {
                this.$message.success("补签成功")
                this.load()
              } else {
                this.$message.error("补签失败")
              }
            })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消提交'
        });
        this.load() })
    },
    MeetingDetail(row){
      this.form=row
      this.dialogTableVisible = true
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },

    checkout(udid,participateStatus,mname,userId,meetingId,uname) {
      this.$confirm('是否拒绝 "'+uname+'" 关于《'+ mname + '》 的补签申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const from1 = {
          id: udid,
          meetingId:meetingId,
          userId: userId,
          supplementarySign:0
        }
        //修改
        this.request.post('/service/AboutMeeting', from1)
            .then(res => {
              if (res.code === '200') {
                  this.$message.warning("操作成功")
                this.load()
              } else {
                this.$message.error("操作失败")
                this.load()
              }
            })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消提交'
        });
        this.load() })
    },


  }}
</script>

<style scoped>
*{
font-size: larger;
}
.el-tag--small {
  height: 33px;
  padding: 4px 13px;
  line-height: 22px;
  font-size: initial;
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
</style>
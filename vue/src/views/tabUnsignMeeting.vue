<template>

  <div CLASS="unjoin">

  <el-table :data="tableData"   stripe style="width: 100%" v-loading="loading" element-loading-text="拼命加载中">
    <el-table-column fixed prop="startDate" label="开始时间"  sortable>
    </el-table-column>
    <el-table-column prop="name" label="会议主题"> </el-table-column>
    <el-table-column prop="code" label="会议号" width="180"> </el-table-column>
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
        prop="stage" label="会议状态" width="230" align="center">
      <template slot-scope="scope">
        <el-tag size="small"  v-if="scope.row.stage === 0"  >未开启</el-tag>
        <el-tag size="small" v-else-if="scope.row.stage === 1" type="primary" effect="dark">进行中</el-tag>
        <el-tag size="small" v-else-if="scope.row.stage === 2" type="info" effect="dark">已结束</el-tag>
      </template>
    </el-table-column>

    <el-table-column prop="city" align="center"  label="操作">
      <template slot-scope="scope">
      <el-button type="text" @click="buqian(scope.row)">补签申请</el-button>
      <el-divider direction="vertical"></el-divider>
      <el-button type="text" @click="MeetingDetail(scope.row)">会议详情</el-button>
    </template>
    </el-table-column>

  </el-table>


    <div style="margin: 10px 0;position: relative;left: 35%">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[8]"
          :page-size="pageSize"
          background
          layout="  prev,sizes, pager, next, jumper"
         >
      </el-pagination>
    </div>

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

            <el-form-item  label="会议情况" prop="status" >
              <el-tag size="small" style="left: 14px;height: 36px;font-size:large;position: relative" v-if="form.status === 0"  type="info " >线下</el-tag>
              <el-tag size="small" style="left: 14px;height: 36px;font-size:large;position: relative" v-else-if="form.status === 1"  >线上</el-tag>
            </el-form-item>

            <el-form-item label="审核情况 :" prop="show" >
              <el-tag size="small"   style="left: 14px;height: 36px;font-size:large;position: relative" v-if="form.show === 0" >未审核</el-tag>
              <el-tag size="small"   style="left: 14px;height: 36px;font-size:large;position: relative" v-else-if="form.show === 1" >已通过</el-tag>
              <el-tag size="small"   style="left: 14px;height: 36px;font-size:large;position: relative" v-else-if="form.show === 2" >未通过</el-tag>
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
</template>

<script>
export default {
  name: "tabUnsignMeeting",
  data() {
    return {
      user:{},
      tableData: [],
      pageNum: 1,
      pageSize: 8,
      dialogTableVisible:false,
      loading:true,
    };
  },
  created() {
    //请求分页查询数据
    this.user=  JSON.parse(localStorage.getItem("user"))
    this.load()
  },
  methods: {
    load() {
      this.request.get("/service/AboutMeeting/findMeeting", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          code: this.code,
          userId: this.user.userId,
          participateStatus:1,
          myMeeting:1,
          supplementarySign:1
        }

      }).then(res => {
            this.tableData = res
            this.loading=false
          }
      )
    },
    buqian(row){
      this.$confirm('将于现在提交补签申请, 是否确带继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const from1 = {
          id: row.uid,
          supplementarySign: 1,
        }
        //修改
        this.request.post('/service/AboutMeeting', from1)
            .then(res => {
              if (res.code === '200') {
                this.$message.success("申请成功,等待会议申请者通过!")
                this.load()
              } else {
                this.$message.error("申请失败!")
              }
            })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消提交'
        });
        this.load()
      }
    )},
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
      this.dialogTableVisible = true
    },
  },
}
</script>

<style scoped>
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
</style>
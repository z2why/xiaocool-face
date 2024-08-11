<template>
  <div style="padding: 10px">
    <div id="biaotou">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="font-size: larger">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i>首页</el-breadcrumb-item>
        <el-breadcrumb-item>会议审核</el-breadcrumb-item>
      </el-breadcrumb>
      <el-divider></el-divider>
    </div>
    <div style="margin: 10px 0">
      <div style="padding: 10px 0">


        <el-input
            placeholder="请输入会议编号" v-model="code" style="width:13%;position:relative;top: 3px;">
          <i slot="suffix" class="el-input__icon el-icon-search"></i>
        </el-input>


        <el-input
            placeholder="请输入会议名称" v-model="name" style="width:13%;position:relative;left: 15px;top: 3px;">
          <i slot="suffix" class="el-input__icon el-icon-search"></i>
        </el-input>

        <el-input
            placeholder="请输入会议发起人" v-model="createUser" style="width:13%;position:relative;left: 30px;top: 3px;">
          <i slot="suffix" class="el-input__icon el-icon-search"></i>
        </el-input>

        <el-button type="primary" style="float: right;padding: 12px 20px;position: absolute; left: 914px; "
                   @click="load" plain>搜索
        </el-button>
        <el-button type="warning" style="float: right;padding: 12px 20px;position: absolute; left: 990px; "
                   @click="reset" plain>重置
        </el-button>


      </div>

    </div>
    <el-table
        v-loading="loading" element-loading-text="拼命加载中"
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        highlight-current-row
        border
        stripe
        @selection-change="handleSelectionChange">

      <el-table-column
          type="selection"
          width="40"
      align="center">
      </el-table-column>

      <el-table-column
          prop="code"
          label="会议编号"
          width="140">
      </el-table-column>

      <el-table-column
          prop="name"
          label="会议主题"
          width="160">
      </el-table-column>

      <el-table-column
          prop="createUser"
          label="会议发起人"
          width="120">
      </el-table-column>

      <el-table-column
          prop="stage"
          label="会议状态"
          align="center"
          width="140">
        <template slot-scope="scope">
          <el-tag size="small"  v-if="scope.row.stage === 0"  >未开启</el-tag>
          <el-tag size="small" v-else-if="scope.row.stage === 1" type="warn" effect="dark">进行中</el-tag>

          <el-tag size="small" v-else-if="scope.row.stage === 2" type="info" effect="dark">已结束</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          prop="address"
          label="地点"
          width="150">
      </el-table-column>

      <el-table-column
          prop="startDate"
          label="开始时间"
          width="220">
      </el-table-column>

      <el-table-column
          prop="endDate"
          label="结束时间"
          width="220">
      </el-table-column>

<!--      <el-table-column-->
<!--          prop="show"-->
<!--          label="审核状态"-->
<!--          width="140"-->
<!--          align="center"-->
<!--      >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.show === 0" >未审核</el-tag>-->
<!--          <el-tag size="small" v-else-if="scope.row.show === 1" >已通过</el-tag>-->
<!--          <el-tag size="small" v-else-if="scope.row.show === 2" >未通过</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column
          prop="icon"
          align="center">

          <template slot-scope="scope">
          <el-button icon="el-icon-check" type="warning" v-model="scope.row.show" active-color="#13ce66"
                     size="medium" inactive-color="#ccc" @click="check(scope.row.id,scope.row.show,scope.row.name)" plain>通过
          </el-button>
          <el-button icon="el-icon-close" type="danger" v-model="scope.row.show" active-color="#13ce66"
                     size="medium"    inactive-color="#ccc" @click="checkout(scope.row.id,scope.row.show,scope.row.name)" plain>不通过
          </el-button>
        </template>
      </el-table-column>

    </el-table>

    <div style="margin: 10px 0;left: 40%; position: absolute;">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[8]"
          :page-size="pageSize"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>


</template>
<script >
export default {
  name: "auditBook",
  data() {

    return {
      code:'',
      name: '',
      createUser: '',
      form: {},
      dialogVisible: false,
      pageNum: 1,
      total: 0,
      tableData: [],
      search: '',
      pageSize: 8,
      headerBg: 'headerBg',
      loading:true,
      multipleSelection: [],

      statusData: {
        '0': {
          label: '未审核'
        },
        '1': {
          label: '已通过'
        },
        '2': {
          label: '审核未通过'
        }

      }

    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {

    load() {
      this.request.get("/auditMeeting/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          code: this.code,
          name: this.name,
          createUser:this.createUser,

        }
      }).then(res => {
            this.tableData = res.records
            this.total = res.total
            this.loading=false
          }
      )

    },
    handleSelectionChange(val) {

      this.multipleSelection = val

    },
    reset() {
      this.code = ""
      this.name =""
      this.createUser= ''
      this.load()
    },

    check(id,show,name) {
      this.$confirm('将于现在通过”' + name + '”会议的审核, 是否确带继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const from1 = {
          id: id,
          show: 1,
        }
        //修改
        this.request.post('/service/meeting/MyMeeting', from1)
            .then(res => {
              if (res.code === '200') {
                this.$message.success("申请成功")
                this.load()
              } else {
                this.$message.error("申请失败")
              }
            })
      })
    },
    checkout(id,show,name) {
      this.$confirm('将于现在不通过”' + name + '”会议的审核, 是否确带继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const from2 = {
          id: id,
          show: 2,
        }
        //修改
        this.request.post('/service/meeting/MyMeeting', from2)
            .then(res => {
              console.log("/meeting/MyMeeting/")
              this.$message({
                showClose: true,
                message: res.code?'成功不通过！':res.msg,
              });
            })
      })
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
  },
}
</script>
<style scoped>
.el-tag{
  font-size: 17px;
}

.el-tag--small {
  height: 36px;
  padding: 7px 11px;
  line-height: 22px;
}
</style>




<template>
  <div style="padding: 10px 10px 0;" >
    <div id="biaotou">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="font-size: larger">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i>首页</el-breadcrumb-item>
        <el-breadcrumb-item>会议管理</el-breadcrumb-item>
      </el-breadcrumb>
      <el-divider ></el-divider>
    </div>

    <div style="margin: 10px 0;padding: 10px 0;display: flex;flex-direction: row;justify-content: space-between; " >
      <div style="display: flex;gap: 3px;" >
      <el-button  @click="handleAdd"  icon="el-icon-circle-plus-outline">添加</el-button>
        <el-upload :action='$baseUrl+"/service/meeting/MyMeeting/import"' :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess">
          <el-button   icon="el-icon-bottom" >导入</el-button>
        </el-upload>
        <el-button  icon="el-icon-top" @click="exp">导出</el-button>

        <el-popconfirm
            title="确定批量删除吗？" confirm-button-text='确定' cancel-button-type="success" @confirm="delBatch" icon="el-icon-warning" icon-color="red">
          <el-button slot="reference"
                     type="danger" icon="el-icon-remove-outline" style="margin-left: 20px" :disabled="delBtlStatu" >批量删除</el-button>
        </el-popconfirm>
      </div>

      <div style="display: flex" >
      <el-input
            placeholder="请输入会议编号"  v-model="code" style="width: 180px;margin-top: 1px;margin-right: 10px" >
          <i slot="suffix" class="el-input__icon el-icon-search"></i>
        </el-input>

        <el-button type="primary"   @click="load" plain>搜索</el-button>
        <el-button type="warning"  style="margin-left: 5px;" @click="reset" plain>重置</el-button>

      </div>
    </div>

   <div style="display: flex;flex-direction: column">
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
          label="会议名称"
          width="190">
      </el-table-column>

      <el-table-column
          prop="createUser"
          align="center"
          label="会议发起人ID"
      width="140">
      </el-table-column>

      <el-table-column
          prop="stage"
          label="会议状态"
          align="center"
          width="140">
        <template slot-scope="scope">
          <el-tag size="small"  v-if="scope.row.stage === 0"  >未开启</el-tag>
          <el-tag size="small" v-else-if="scope.row.stage === 1"  effect="dark">进行中</el-tag>

          <el-tag size="small" v-else-if="scope.row.stage === 2" type="info" effect="dark">已结束</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          prop="show"
          label="审核状态"
          width="140"
          align="center"
      >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.show === 0" >未审核</el-tag>
          <el-tag size="small" v-else-if="scope.row.show === 1" >已通过</el-tag>
          <el-tag size="small" type="danger" v-else-if="scope.row.show === 2" >未通过</el-tag>
        </template>
      </el-table-column>

<!--      <el-table-column-->
<!--          prop="address"-->
<!--          label="地点"-->
<!--      width="150">-->
<!--      </el-table-column>-->

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



      <el-table-column
          prop="icon"
          fixed="right"
          width="250"
          align="center">

        <template slot-scope="scope">
          <el-button   icon="el-icon-check"  style="background-color:#FFF0;" v-model="scope.row.show" active-color="#13ce66"
                     size="mini" inactive-color="#ccc" @click="check(scope.row.id,scope.row.show,scope.row.name)" plain>通过
          </el-button>
          <el-button icon="el-icon-close" type="danger" v-model="scope.row.show" active-color="#13ce66"
                     size="mini"    inactive-color="#ccc" @click="checkout(scope.row.id,scope.row.show,scope.row.name)" plain>不通过
          </el-button>

<!--          <el-divider direction="vertical"></el-divider>-->


          <el-button type="text" @click="handleEdit(scope.row)" style="padding-top: 20px;">编辑</el-button>
          <el-divider direction="vertical"></el-divider>

          <template>
            <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
              <el-button type="text" slot="reference">删除数据</el-button>
            </el-popconfirm>
          </template>

        </template>
      </el-table-column>

    </el-table>

    <div style="margin: 10px 0;align-self: center;user-select: none">
      <el-pagination

          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[4]"
          :page-size="pageSize"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

    </div>

   </div>
      <el-dialog
          title="会议信息"
          :visible.sync="dialogVisible"  width="55%"  >
        <el-form :model="form"  label-width="100px">

        <el-form-item label="会议名称 :" prop="name">
          <el-input v-model="form.name" style="width: 250px" autocomplete="off"></el-input>
        </el-form-item>


        <el-form-item label="会议状态 :" v-if="this.form" prop="stage">
          <el-select v-model="form.stage"  style="width: 100px;">
            <el-option label="未开始" :value=0></el-option>
            <el-option label="进行中" :value=1></el-option>
            <el-option label="已结束" :value=2></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="会议地点 :" prop="address">
          <el-input v-model="form.address"></el-input>
        </el-form-item>

        <el-form-item label="会议时间 :" v-if="isEdit">
          <el-date-picker
              v-model="form.startDate"
              type="datetime"
              placeholder="开始时间"
              prop="startDate">
          </el-date-picker> =>
           <el-date-picker
              v-model="form.endDate"
              type="datetime"
              placeholder="结束时间"
              prop="endDate">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="会议审核 :" prop="show">
          <el-radio-group v-model="form.show" >
            <el-radio :label="0" >未审核</el-radio>
            <el-radio :label="1" >已通过</el-radio>
            <el-radio :label="2" >未通过</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="会议简介 :">
          <div style="border: 1px solid #ccc;">
            <Editor
                style="height: 230px; overflow-y: hidden;"
                v-model="form.description"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="onCreated"
            />
          </div>
        </el-form-item>

        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false;load()">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
      </template>
      </el-dialog>

  </div>
</template>

<script>
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import axios from 'axios'

export default {
  name: "Meeting",
  components: { Editor, Toolbar },
  data(){

    return{
      qwq:'',
      isEdit: true,
      form:{},
      editor: null,
      loading:true,
      toolbarConfig: { },
      editorConfig: { placeholder: '' },
      mode: 'default',
      dialogVisible:false,
      currentPage:1,
      total:0,
      tableData:[],
      search: '',
      pageSize:4,
      headerBg: 'headerBg',
      code: '',
      data:{show:''},
      multipleSelection:[],
      delBtlStatu: true,

    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods:{
    handleAdd(){
      this.dialogVisible = true;
      this.form = {}
    },
    handleDelete(id){
      this.request.delete("/service/meeting/MyMeeting/"+id).then(res => {
        console.log(res)
        if(res.code === '200'){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }

      })

    },
    mouseenter(data){
      this.$set(data, 'show', true)
    },
    mouseleave(data){
      this.$set(data, 'show', false)
    },
    delBatch(){
      let ids = this.multipleSelection.map(v=> v.id) // [{},{}] =>[1,2,3]
      this.request.post("/service/meeting/MyMeeting/del/batch",ids).then(res => {
        if(res.code === '200'){
          this.$message.success("批量删除成功")
          this.load()
        }else{
          this.$message.error("批量删除失败")
        }

      })


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
              console.log("/service/meeting/MyMeeting/")
              this.$message({
                showClose: true,
                message: res.code?'成功不通过！':res.msg,

              });
              this.load()
            })
      })
    },


    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    save(){
      this.request.post("/service/meeting/MyMeeting", this.form).then(res => {
        if(res.code === '200'){
          this.$message.success("保存成功")
          this.dialogVisible =false
          this.load()
        }else{
          this.$message.error("保存失败")
        }

      })
    },
    //编辑器
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },
    beforeDestroy() {
      const editor = this.editor
      if (editor == null) return
      editor.destroy() // 组件销毁时，及时销毁编辑器
    },


    load(){
      this.request.get("/service/meeting/MyMeeting/page",{
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          code : this.code
        }
      }).then(res => {
            this.tableData = res.data.records
            this.total = res.data.total
            this.loading=false
          }
      )
    },
    handleSelectionChange(val){
      this.multipleSelection=val
      this.delBtlStatu = val.length == 0
    },
    reset(){
      this.code=""
      this.load()
    },


    handleEdit(row){
      this.form = row
      this.dialogVisible = true
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.currentPage = pageNum
      this.load()
    },

    //上传头像
    filesUploadSuccess(res){
      this.form.avatar = res;
      this.load();
    },

    //上传人脸识别照片
    filesUploadSuccess1(res){
      this.form.faceImage = res;
      this.load();
    },

    meetingHandle(id,name,stage,show,qwq){
      if (stage === 0 && show === 1) {
        if (qwq === 0){
          this.$confirm('将于现在开启【' + name + '】会议, 是否确带继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            const from1 = {
              id: id,
              stage: 1,
            }
            //修改
            this.request.post('/service/meeting/MyMeeting', from1)
                .then(res => {
                  this.$message({
                    showClose: true,
                    //message: res.msg,
                    message: res.code ? '已开启会议！' : res.msg,
                    //type: res.code?'success':'error',

                  })

                      .catch(()=>{});

                  this.load()
                })
          })
              .catch(()=>{})
          this.load()
        }
        if (qwq === 1){
          this.$confirm('将于现在暂缓【' + name + '】会议, 是否确带继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            const from1 = {
              id: id,
              stage: 3,
            }
            //修改
            this.request.post('/service/meeting/MyMeeting', from1)
                .then(res => {
                  this.$message({
                    showClose: true,
                    //message: res.msg,
                    message: res.code ? '已暂缓会议！' : res.msg,
                    //type: res.code?'success':'error',

                  });
                  this.load()
                })
          })
              .catch(()=>{})
        }



      }else {
        if (show !== 1)
        {
          this.$message('请先通过申请')
          this.load()
        }
        else{
          if (stage === 1 || stage===3)
          {
          this.$confirm('将于现在结束【' + name + '】会议, 是否确带继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            const from2 = {
              id: id,
              stage: 2,
            }
            //修改
            this.request.post('/service/meeting/MyMeeting', from2)
                .then(res => {
                  this.$message({
                    showClose: true,
                    //message: res.msg,
                    message: res.code ? '已结束会议！' : res.msg,

                    //type: res.code?'success':'error',
                  });
                  this.load()
                })
          })
              .catch(()=>{})
        }}

      }

    },
    async exp(){
     // 设置请求头
      let token = JSON.parse(localStorage.getItem("user")).token
      const headers = {
        'Content-Type': 'application/json;charset=utf-8',
        'token': token
      };
      console.log(token)
      try {
        // 发起 GET 请求到服务器端的 /export 接口
        const response = await axios({
          method: 'get',
          headers:headers,
          url: this.$baseUrl+"/service/meeting/MyMeeting/export", // 确保这是你的正确 API URL
          responseType: 'blob', // 告诉 axios 我们期望得到一个 blob 响应
        });

        // 创建一个 blob 对象，这将作为文件的实际内容
        const url = window.URL.createObjectURL(new Blob([response.data]));

        // 创建一个链接元素
        const link = document.createElement('a');
        link.href = url;

        // 设置下载的文件名
        const fileName = '用户信息.xlsx';
        link.setAttribute('download', fileName);

        // 触发点击事件开始下载
        document.body.appendChild(link);
        link.click();

        // 清理创建的链接元素
        document.body.removeChild(link);
      } catch (error) {
        // 处理错误
        console.error('下载 Excel 文件时出错:', error);
      }

    }

  }
}

</script>

<style scoped  lang="scss">
.el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;

  line-height: 60px;
}
.el-main {
  background-color: #E9EEF3;
  color: #333;
}

.el-tag{
  font-size: 16px;
}



.el-tag--small {
  height: 36px;
  padding: 7px 11px;
  line-height: 22px;
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
::v-deep .el-divider--horizontal{
  margin-bottom: 0;
}

::v-deep .el-form-item__label{
  font-size: 16px;
}


</style>

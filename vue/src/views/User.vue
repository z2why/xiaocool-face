<template>
  <div style="padding: 10px;">
    <div id="biaotou">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="font-size: larger">
        <el-breadcrumb-item :to="{ path: '/AddMeeting' }"><i class="el-icon-s-home"></i>首页</el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      </el-breadcrumb>
      <el-divider></el-divider>
    </div>

    <div style="margin: 10px 0;padding: 10px 0;display: flex;flex-direction: row;justify-content: space-between; ">
      <div style="display: flex;gap: 3px;">
        <el-button @click="handleAdd" icon="el-icon-circle-plus-outline">添加</el-button>
        <el-upload :action='$baseUrl+"/service/audit/user/import"' :show-file-list="false" accept="xlsx"
                   :on-success="handleExcelImportSuccess" style="display: inline-block">
          <el-button icon="el-icon-bottom">导入</el-button>
        </el-upload>
        <el-button icon="el-icon-top" @click="exp">导出</el-button>


        <el-popconfirm
            title="确定批量删除吗？" confirm-button-text='确定' cancel-button-type="success" @confirm="delBatch"
            icon="el-icon-warning" icon-color="red">
          <el-button slot="reference"
                     type="danger" style="margin-left: 20px" icon="el-icon-remove-outline" :disabled="delBtlStatu">批量删除
          </el-button>
        </el-popconfirm>
      </div>

      <div style="display: flex">

        <el-input
            style="width: 180px;margin-top: 1px;margin-right: 10px"
            placeholder="请输入用户名" v-model="username">
          <i slot="suffix" class="el-input__icon el-icon-search"></i>
        </el-input>

        <el-button type="primary" @click="load" plain>搜索</el-button>
        <el-button style="margin-left: 5px;" type="warning" @click="reset" plain>重置</el-button>
      </div>


    </div>
    <el-table
        ref="multipleTable"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        tooltip-effect="dark"
        @selection-change="handleSelectionChange"
        :header-cell-class-name="headerBg"
    >
      <el-table-column
          type="selection"
          width="50"
          align="center"
      >
      </el-table-column>

      <el-table-column
          label="编号"
          prop="id"
          width="90"
          align="center"
          sortable
      >
      </el-table-column>

      <el-table-column
          label="姓名"
          prop="name"
          align="center"
          width="100">
      </el-table-column>

      <el-table-column
          align="center"
          width="160"
          label="人脸识别照片">
        <template #default="scope">
          <div class="demo-image__preview">
            <el-image
                style="width: 100px; height: 100px"
                :src="scope.row.faceImage"
                :preview-src-list="[scope.row.faceImage]">
            </el-image>
          </div>
        </template>
      </el-table-column>

      <el-table-column
          label="用户名"
          prop="username"
          width="160">
      </el-table-column>

      <el-table-column
          label="密码"
          prop="password"
          width="160">
      </el-table-column>

      <el-table-column
          width="150"
          align="center"
          label="头像">
        <template #default="scope">
          <div class="demo-image__preview">
            <el-image
                style="width: 100px; height: 100px"
                :src="scope.row.avatar"
                :preview-src-list="[scope.row.avatar]">
            </el-image>
          </div>
        </template>
      </el-table-column>


      <el-table-column
          prop="email"
          width="230"
          label="邮箱">
      </el-table-column>


      <el-table-column
          prop="sex"
          label="性别"
          width="80"
          align="center">
        <template slot-scope="scope">
          <el-tag size="large" v-if="scope.row.sex === 0">女</el-tag>
          <el-tag size="large" v-else-if="scope.row.sex === 1">男</el-tag>
          <el-tag size="large" v-else-if="scope.row.sex === 2">其他</el-tag>
        </template>
      </el-table-column>

      <!--            <el-table-column-->
      <!--          label="签到时间"-->
      <!--          prop="timeopen">-->
      <!--      </el-table-column>-->

      <!--      <el-table-column-->
      <!--          label="签退时间"-->
      <!--          prop="timeout">-->
      <!--      </el-table-column>-->


      <el-table-column
          fixed="right"
          label="操作"
          width="240"
          align="center"
      >
        <template slot-scope="scope">
          <el-button icon="el-icon-edit"
                     style="margin-right: 10px"
                     @click="handleEdit(scope.row)">编辑
          </el-button>
          <el-popconfirm
              title="确定删除吗？" confirm-button-text='确定' cancel-button-type="success" @confirm="handleDelete(scope.row.id)"
              icon="el-icon-warning" icon-color="red">
            <el-button slot="reference"
                       type="danger" icon="el-icon-remove-outline">删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0;display: flex;justify-content: center;">
      <el-pagination

          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[3]"
          :page-size="pageSize"
          background
          layout="total, sizes, prev, pager,
          next, jumper"
          :total="total">
      </el-pagination>
      <el-dialog
          title="用户信息"
          :visible.sync="dialogVisible" width="44%">
        <el-form :model="form" label-width="80px" style="display: flex;flex-direction: row;height: 270px;">
          <div>

          <el-form-item label="头像:">
            <el-upload
                class="upload_box"
                ref="upload"
                :limit="limitNum"
                :class="{ uploadIconOne: showImg, uploadIcon: noneUploadImg }"
                :on-change="handleEditChange"
                :http-request="ImgUploadSectionFile"
                :before-upload="beforeAvatarUpload"
                :with-credentials="true"
                :auto-upload="true"
                 accept=".png, .jpg"
                 list-type="picture-card"
                :file-list="fileList"
                action="string"
            >
              <!-- 加号标识 -->
              <i slot="default" class="el-icon-plus"></i>
              <!-- 上传后显示 -->
              <div slot="file" slot-scope="{file}">
                <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
                <span class="el-upload-list__item-actions">

                <span
                      class="el-upload-list__item-delete"
                      @click="handleRemove(file)">
                 <i class="el-icon-delete"></i>
                </span>
            </span>
              </div>
            </el-upload>


          </el-form-item>
          </div>


          <div style="margin-left: 40px">
          <el-form-item label="姓名:">
            <el-input v-model="form.name" ></el-input>
          </el-form-item>

          <el-form-item label="性别:" style="margin-left: -20px;" prop="sex" label-width="100px">
            <el-radio-group v-model="form.sex">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
              <el-radio :label="2">其他</el-radio>
            </el-radio-group>
          </el-form-item>


          <el-form-item label="用户名:">
            <el-input v-model="form.username" ></el-input>
          </el-form-item>

          <el-form-item label="密码:">
            <el-input v-model="form.password" ></el-input>
          </el-form-item>
          </div>

        </el-form>

        <template>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
// editor.create()

export default {
  name: "User",
  components: {Editor, Toolbar},

  data() {

    return {
      form: {},
      dialogVisible: false,
      currentPage: 1,
      total: 0,
      tableData: [],
      search: '',
      pageSize: 3,
      headerBg: 'headerBg',
      username: '',
      multipleSelection: [],
      delBtlStatu: true,
      editor: null,
      html: '<p>hello</p>',
      toolbarConfig: {},
      editorConfig: {placeholder: '请输入内容...'},
      mode: 'default', // or 'simple'

      //头像模块
      fileList:[],
      needfile: {},
      showImg: true, //默认展示
      noneUploadImg: false, //
      limitNum:1,

    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },
    handleAdd() {
      this.dialogVisible = true;
      this.form = {}
    },
    handleDelete(id) {
      this.request.delete("/service/audit/user/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }

      })

    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id) // [{},{}] =>[1,2,3]
      this.request.post("/service/audit/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }

      })


    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    save() {
      this.request.post("/service/audit/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }

      })
    },


    load() {

      this.request.get("/service/audit/user/page", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          username: this.username
        }
      }).then(res => {
            this.tableData = res.data.records
            this.total = res.data.total
          }
      )
    },

    //多选
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
      this.delBtlStatu = val.length == 0
    },
    reset() {
      this.username = ""
      this.load()
    },


    handleEdit(row) {
      this.form = row
      this.noneUploadImg  = true;
      this.fileList=[{
        url:row.avatar
      }]

      this.dialogVisible = true
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    },


    exp() {
      window.open(this.$baseUrl + "/service/audit/user/export")
    },





    // 删除图片
    handleRemove(file) {
      console.log("要删除的文件=====", file);
      this.fileList = [];
      this.needfile = {};

      this.noneUploadImg = false;
    },

    // on-change添加文件，上传成功和上传失败时都会被调用
    handleEditChange(file, fileList) {
      this.needfile = file.raw;
      this.noneUploadImg  = fileList.length >= this.limitNum;
    },

    // http-request自定义上传
    ImgUploadSectionFile(param) {
      const formData = new FormData();
      formData.append("files",param.file)
      formData.append("folder","xiaocool/头像")
      console.log("http自定义上传==========>",formData)
      this.request.post("/service/cos/upload",formData).then(res=>{
        if (res.code==="200"){
          this.form.avatar=res.data
        }
        else {
          this.$message.error("上传失败！")
        }
      })
    },

    // before-upload上传文件之前的钩子，参数为上传的文件
    // 若返回 false 或者返回 Promise 且被 reject，则停止上传
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error("上传图片只能是 JPG 或 PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },

    // 文件上传成功时的钩子
    handleSuccess(file) {
      // console.log(file);
      const data = file.data;
      //然后数据、逻辑处理
    }


  },

  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器
  },


}

</script>

<style scoped lang="scss">
.el-tag {
  font-size: 18px;
}
.dialog-footer{
  display: flex;
  justify-content: center;
  align-items: center;
  .el-button{
    margin:0 20px;
  }
}
::v-deep .el-divider--horizontal {
  margin-bottom: 0;
}


::v-deep .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.uploadIcon ::v-deep .el-upload--picture-card {
  display: none !important; /* 上传按钮隐藏 */
}
 ::v-deep .el-upload-list__item {
     transition: none !important;
 }

</style>
<style>
* {
  font-family: 微软雅黑;
  font-size: 16px;
}
</style>
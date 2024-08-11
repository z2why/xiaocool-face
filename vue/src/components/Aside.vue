<template>
  <div style="user-select: none ">

    <el-menu
        style="width: 200px; height: 100vh"
        class="el-menu-vertical-demo"
        background-color="rgb(48,65,86)"
        text-color="#fff"
        active-text-color="#ffd04b"
        :default-active="$route.path"
        :unique-opened="true"
        router
      >

      <el-menu-item index="1" @click="$router.push('/AddMeeting')" >
        <span slot="title"><i class="el-icon-s-home"></i>主页</span>
      </el-menu-item>

      <el-submenu index="2-3" >
        <template slot="title" style="font-family: 'Kunstler Script';"><i class="el-icon-user"></i>用户系统</template>
        <el-menu-item index="/admin/user">
          <i class="el-icon-s-custom"></i>
          <span slot="title" >用户管理</span>
        </el-menu-item>

        <el-menu-item index="/admin/file">
          <i class="el-icon-folder-add"></i>
          <span slot="title" >文件上传</span>
        </el-menu-item>
      </el-submenu>
        <el-submenu  >
          <template slot="title" style="font-family: 'Kunstler Script';"><i class="el-icon-setting"></i>会议系统</template>
          <el-menu-item index="/admin/Meeting">
          <i class="el-icon-notebook-1"></i>
          <span slot="title">会议管理</span>
        </el-menu-item>
<!--          <el-menu-item index="4"  @click="$router.push('AuditMeeting')">-->
<!--            <i class="el-icon-notebook-2"></i>-->
<!--            <span slot="title">会议审核</span>-->
<!--          </el-menu-item>-->
        </el-submenu>


    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside"
}
</script>

<style scoped>
.el-menu{
  border-right: none;

}
/deep/ .el-submenu__title{
  font-size: 16px;
}

</style>
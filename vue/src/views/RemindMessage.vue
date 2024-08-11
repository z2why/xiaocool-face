<template>
  <div class="RemindMessage">
    <div style="height: 80px">
    <h1>请注意查收您的消息</h1>
    </div>
    <el-tabs type="border-card">
      <el-tab-pane label="未读消息">
        <el-table :data="tableData" border style="width: 100%" v-loading="loading" element-loading-text="拼命加载中">
          <el-table-column
            prop="title"
            label="消息标题"
            width="150"
          ></el-table-column>
          <el-table-column
            prop="content"
            label="消息内容"
            width="600"
          ></el-table-column>
          <el-table-column prop="ArriveTime" label="送达时间">
          </el-table-column>
          <el-table-column label="送达时间">
            <el-button type="text">详情</el-button>
            <el-button type="text">删除</el-button></el-table-column
          >
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="已读消息">
        <el-table :data="tableData" border style="width: 100%" v-loading="loading1" element-loading-text="拼命加载中">
          <el-table-column
            prop="title"
            label="消息标题"
            width="150"
          ></el-table-column>
          <el-table-column
            prop="content"
            label="消息内容"
            width="600"
          ></el-table-column>
          <el-table-column prop="ArriveTime" label="送达时间">
          </el-table-column>
          <el-table-column label="送达时间">
            <el-button type="text">详情</el-button>
            <el-button type="text">删除</el-button></el-table-column
          >
        </el-table></el-tab-pane
      >
    </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
        tableData: [
          {
            title: "您的审核申请已通过",
            content:
                "恭喜您，您的会议申请请求已通过！",
            ArriveTime: "2022-6-23 21:02:51",
          },],
          loading:true,
          loading1:true
    };
  },
};
</script>

<style scoped>
.RemindMessage h1 {
  font-weight: 400;
  height: 60px;
  line-height: 60px;
  font-size: 26px;
}

::v-deep div.cell {
  font-size: 12px;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
</style>
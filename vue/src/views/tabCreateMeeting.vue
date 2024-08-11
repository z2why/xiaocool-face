<template>
  <div class="MeetingList" >
    <div>
      <div STYLE="height: 80px;">
      <h1>我的会议</h1>
      </div>
      <el-tabs v-model="activeName" >
        <el-tab-pane label="创建的会议" name="first" >

          <click1></click1>
        </el-tab-pane>

        <el-tab-pane label="补签情况审核" name="second" >
          <click2></click2>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script>
import tabCreate from "@/views/tabCreate";
import tabAudit from "@/views/tabAudit"
export default {
  name: "tabCreateMeeting",
  components:{
   click1:tabCreate,
   click2:tabAudit
  },
  data() {
    return {
      user:{},
      activeName: "first",
      tableData: [],
      pageNum: 1,
      pageSize: 3,

    };
  },
  created() {
    //请求分页查询数据
    this.user=  JSON.parse(localStorage.getItem("user"))

  },



  methods: {
  },

};
</script>

<style scoped>
.MeetingList{
user-select: none}
.MeetingList h1 {
  font-weight: 400;
  height: 70px;
  line-height: 60px;
  font-size: 26px;
}
.el-tag--small {
  height: 33px;
  padding: 4px 14px;
  line-height: 22px;
  font-size: initial;
}
::v-deep .el-tabs__item {
  font-size: 17px;
}
</style>
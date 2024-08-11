<template >

  <div class="MeetingList"  style="user-select: none">
    <div STYLE="height: 70px">
    <h1>已报名会议</h1>
    </div>

    <el-tabs v-model="activeName" >
      <el-tab-pane label="全部会议" name="first" >

        <click1></click1>
      </el-tab-pane>

      <el-tab-pane label="未及时参加/签到的会议" name="second" >
      <click2></click2>
      </el-tab-pane>

      <el-tab-pane label="即将召开的会议" name="third"  >
        <cilck3></cilck3>
      </el-tab-pane>

    </el-tabs>
  </div>

</template>







<script>
import tabMyMeeting from "@/views/tabMyMeeting";
import tabUnsignMeeting from "@/views/tabUnsignMeeting";
import tabWillMeeting from "@/views/tabWillMeeting";

export default{
 name:"MeetingList",
  components:{
  click1:tabMyMeeting,
  click2:tabUnsignMeeting,
  cilck3:tabWillMeeting
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
.MeetingList h1 {
  font-weight: 400;
  height: 60px;
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
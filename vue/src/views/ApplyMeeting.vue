<template>
  <div class="ApplyMeeting">
    <div style="height: 70px;user-select: none;">
    <h1>会议申请审核历史</h1>
    </div>
    <div class="main"  v-loading="loading" element-loading-text="拼命加载中" >
    <el-collapse v-model="activeNames" :key='index' @change="handleChange" accordion v-for="(item, index) in notice" style="user-select: none">
      <el-collapse-item :title='"关于会议  "+item.name+"  的审核申请 "+item.createTime' :name="index + ''">
        <template>
        <h2 style="text-align: center;font-size: 22px">{{ item.name }} </h2>
        <br />

        <div v-html="item.description" style="text-indent: 2rem">
          {{ item. description}}
        </div>
        </template>
        <br />
        <h2 style="text-align: center">该会议审核历史</h2>
        <br />
        <div>
          <el-steps :active="item.show+1" align-center :key="item.show" >
            <el-step
              title="发起审批"
              :description= 'user.name+" 发起 "+item.name +" " + item.createTime'
            ></el-step>

            <el-step v-if="item.show+1===2"
              title="管理员审批通过"
              :description='item.name + " 审批通过 " + item.createTime '
            ></el-step>

            <el-step v-if="item.show+1===3"
                     title="管理员审批未通过"
                     :description='item.createUser+"审批未通过 "+item.name + item.createTime'
            ></el-step>



          </el-steps>
        </div>
      </el-collapse-item  >
    </el-collapse>

    <el-card v-if="notice.length===0">

      您还没有审核申请，请先 <router-link to="ScheduledMeeting" style="color: #3a8ee6">申请</router-link> 会议！
    </el-card>

    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeNames: ['0'],
      createTime:'',
      notice :[],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      loading:true
    };
  },
  created() {
    this.request.get("/service/meeting/MyMeeting/type",{
      params: {
        createUser:this.user.userId
      } }).then(res => {
      this.notice = res.data.splice(0, 5)
      this.loading=false
    })
  },
  methods: {
    handleChange(){

    }
  },
};
</script>

<style scoped>
.ApplyMeeting h1 {
  font-weight: 400;
  height: 60px;
  line-height: 60px;
  font-size: 26px;
}
</style>

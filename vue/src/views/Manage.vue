<template>
    <div style="">
    <el-container>
    <el-header style="height: 65px; line-height: 63px;border-bottom: 1px solid rgb(215, 215, 215);  background: #14253cc7;" >
      <Header/>
    </el-header>

      <el-container  >
      <el-aside :width="sideWidth + 'px'" style="width: 200px; min-height: calc(100vh - 50px);box-shadow: 2px 0 6px rgb(0 21 41 / 35%);background-color:rgb(48,65,86)">
        <Aside/>
      </el-aside>

  <el-main>

    <router-view @refreshUser="getUser" />
       <Footer style="padding-top: 20px"></Footer>
  </el-main>


    </el-container>
  </el-container>



    </div>
</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

export default {
  name: 'Manage',
  components: {
    Aside,
    Header,
    Footer
  },
  created() {
    // 从后台获取最新的User数据
    this.getUser()
  },
  data(){

    return{
      sideWidth: 200,
    }
  },
  methods:{
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // 从后台获取User数据
        this.request.get("/uaa/user/userInfo").then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
        })
      }
    }

}
}
</script>

<style scoped>
.el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;

  line-height: 60px;
}
.el-main {
  background-color: #E9EEF3;
  color: #333;
}
</style>
<style>
.el-table__empty-block{
  background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABACAMAAADbASJGAAABPlBMVEUAAADX19fa2trZ2dna2trZ2dn19fX19fXt7e319fX19fX19fXb29vZ2dnZ2dn29vb19fX29vb8/PzY2NjZ2dn29vb19fX19fXZ2dnZ2dnZ2dnZ2dn19fXa2tra2tr4+Pja2tr19fXZ2dn29vbb29vZ2dn19fX29vb19fXZ2dn19fX19fXZ2dn29vb29vbX19f29vbi4uLZ2dnj4+Ph4eHZ2dnZ2dnZ2dn29vb09PTZ2dn19fXa2trZ2dn19fXZ2dn19fX19fX29vb19fXZ2dn09PTY2Nj19fXa2tr39/fX19fd3d3Z2dnY2Nj19fXZ2dn29vbh4eH09PT29vbY2NjZ2dnZ2dn09PTY2NjZ2dn09PT19fXX19f6+vr19fXZ2dnp6eni4uLo6Ojg4ODk5OTx8fHt7e3r6+v4+Pjd3d2+WHiwAAAAXXRSTlMAQL+/74DlRhnp1bf85qKLfmMRBvv18KGHamFQTEszLh/89dzc186+sq2ZlV1XUjc2KCUR9OzSzMfBtLCpmpGOgHpxbmdfVT06Hgz84NPFxLGwqKeYk3h2dnFdSi2FEAZaAAACXElEQVRYw+3U11LjQBCF4WMbW3LC2YAxxuScc1xy2JyzuiUHDO//AqsLahEYSyNpuKH4rlXzV7V6Bk6WA46q8GdgjAWsl+BDhbtLAUd9YzwAz/5yCCKWhnkZHq3wW4jJMefgzXAeopY5CU9GWYWwAIfgwQcOwIUSf4Nru3wCV8LcB5d6+RVc6na7yH08BddCXIEL/RyEe7lRrkJYJf8OXlTzYznhS5hMrsCTBfEpb+Qv4FG/6L5s8yw8O+HvELDHv+BDmEsiH/XClz0ecL4gX+HTdteC04+bgl/qxnrVdgXXgq/hWyUZsjuli9vsOq7abJDbhOwihv7QKoed3sWE8ZBDRGvTGF6CjQtuaG1cR5r2O93LTQkRLdFtO62EJiNiBGEjaEiJNNZgo6smJaLzGTr6yU0pkes3yX50cMarmpSI1kxwsANONCVFNNKNDlqkeY+Ie4k8h0hQViT8EnmJPNvI0qisyKdLPCY7vx+juqxIjcZT5SzuW9yPkklixBRNDeLO3CSZpEdMPcptYjBO9FQRM3MJ02mB/jNupDSuLRGKngIKWdT4WkaEWCeLc8TJ4oqvZETMY8hiBhNkVa/LiNTrZDWNObJqccN/o8EtsioDM2RV45uG7kvthnW6Py3T7yhZ6Ab7ZLTIIjZ/e1FSEXoiscPs3cN1/J6ewEQmh3uUdLxIEhV3DhU85vzHRymDi+wcL8KOWk5P+ihFJtNlFUJUJXPQMzEyRMIKsXjPQUZR4Z66+Gcuc5ROTX+Ob22Ox0Yi0WJhaKhQjEZGYuObW/Ev06n0UWZeGczCzj/Kd2oUicxurAAAAABJRU5ErkJggg==");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 75px;
  margin-top: 25px;
  margin-bottom: 60px;
}
.el-table__empty-block span{
   position: relative;
   top: 50px;
 }
</style>
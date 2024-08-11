<template>
<div>
  <div class="MyInfo">
    <div style="数据统计: 60px">
      <h1>注册人脸识别图片</h1>
    </div>
  </div>
  <el-divider></el-divider>
  <el-card style="
    width: 702px;
    height: 602px;" v-if="this.ux===1">

    <svg t="1659631163282" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5466" width="36" height="36"><path d="M231.36 218.16m78.86 0l448.4 0q78.86 0 78.86 78.86l0 448.4q0 78.86-78.86 78.86l-448.4 0q-78.86 0-78.86-78.86l0-448.4q0-78.86 78.86-78.86Z" fill="#EFEFEF" p-id="5467"></path><path d="M725.68 476.52c0 130.92-86.14 287.86-192.42 287.86s-192.42-156.94-192.42-287.86 86.14-221.9 192.42-221.9 192.42 90.96 192.42 221.9z" fill="#99D1FF" p-id="5468"></path><path d="M533.26 780.38c-55.16 0-108.28-36.34-149.62-102.34-36.82-58.78-58.8-134.12-58.8-201.52 0-135.62 89.6-237.88 208.42-237.88s208.42 102.26 208.42 237.88c0 67.4-22 142.74-58.8 201.52-41.32 65.96-94.46 102.34-149.62 102.34z m0-509.76c-47.6 0-91.68 20-124.12 56.48-33.74 37.88-52.3 90.94-52.3 149.4 0 61.64 20.16 130.62 54 184.54 34.76 55.5 79.4 87.32 122.5 87.32s87.74-31.84 122.5-87.32c33.76-54 54-122.9 54-184.54 0-58.46-18.58-111.52-52.3-149.4-32.6-36.42-76.68-56.48-124.28-56.48z" fill="#18659E" p-id="5469"></path><path d="M534.54 548.62c-52.32 0-101.68-5.06-139-14.24-45.86-11.3-68.16-27.26-68.16-48.82s22.3-37.52 68.16-48.82c37.32-9.18 86.68-14.24 139-14.24s101.68 5.06 139 14.26c45.86 11.3 68.16 27.26 68.16 48.82s-22.3 37.52-68.16 48.82c-37.34 9.16-86.7 14.22-139 14.22z m-174.36-63.06c3.8 4 17.32 12.22 50.84 19.56 34 7.42 77.78 11.5 123.52 11.5s89.62-4 123.52-11.5c33.5-7.34 47.04-15.64 50.84-19.56-3.8-4-17.32-12.22-50.84-19.56-34-7.42-77.78-11.5-123.52-11.5s-89.62 4-123.52 11.5c-33.52 7.32-47.02 15.62-50.86 19.56z" fill="#18659E" p-id="5470"></path><path d="M517.26 254.62h32v509.76h-32zM146.88 357.16a16 16 0 0 1-16-16v-222a16 16 0 0 1 16-16h245.82a16 16 0 0 1 0 32H162.88v206a16 16 0 0 1-16 16zM392.7 915.1H146.88a16 16 0 0 1-16-16V632a16 16 0 0 1 32 0v251.1h229.82a16 16 0 0 1 0 32zM926.88 915.1H683.44a16 16 0 0 1 0-32h227.44V632a16 16 0 0 1 32 0v267.1a16 16 0 0 1-16 16zM926.88 357.16a16 16 0 0 1-16-16v-206H683.44a16 16 0 1 1 0-32h243.44a16 16 0 0 1 16 16v222a16 16 0 0 1-16 16z" fill="#18659E" p-id="5471"></path></svg>
    <span style=" padding-left: 14px; position: relative; top: -11px; font-size: 18px;">上传人脸识别照片</span>
    <el-divider></el-divider>
    <div class="camera-box" style="position: relative; top: -307px; left: -5% ">

<!--        <el-form-item label="用户名姓名:">-->
<!--          <el-input v-model="form.user_name"  style="width:20%"></el-input>-->
<!--        </el-form-item>-->
    <div class="renlian" >

      <img style="pointer-events: none;width: 130%; height: 129%;position: absolute; left: -15%;top: -14%;" id="img" src="../imgs/timg.png" >
      <div class="box">
        <div class="line"></div>
        <div class="bottom">

        </div>
      </div>
          <input style="right: -40px; opacity: 0;width: 280px; height: 275px;    top: -36px;position: absolute;"
                 type="file" id="fileup"  @change="getfile($event)" >
            <el-button type="primary" plain @click="save($event)" style="left: 60px;position: relative;    top: 38px;" >上 传</el-button>

  </div>


 </div>
  </el-card>



<!--  <el-card style="width: 1257px;-->
<!--    height: 793px" v-if="this.ux===2">-->
<!--    <svg t="1659631982744" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7781" width="25" height="25"><path d="M370.323 184l-34.607 132.29L127 344.086V841h773V344.055l-210.831-28.207L654.677 184H370.323z m-82.459 78.132l24.412-93.317c6.907-26.4 30.758-44.815 58.047-44.815h284.354c27.289 0 51.14 18.415 58.047 44.815l24.303 92.901 170.93 22.87c29.796 3.986 52.043 29.407 52.043 59.47V841c0 33.137-26.863 60-60 60H127c-33.137 0-60-26.863-60-60V344.087c0-30.076 22.267-55.504 52.079-59.475l168.785-22.48z" fill="#239fe0" p-id="7782"></path><path d="M512.5 781C387.96 781 287 680.04 287 555.5S387.96 330 512.5 330 738 430.96 738 555.5 637.04 781 512.5 781z m0-60c91.403 0 165.5-74.097 165.5-165.5S603.903 390 512.5 390 347 464.097 347 555.5 421.097 721 512.5 721z" fill="#239fe0" p-id="7783"></path></svg>-->
<!--    <span style="       padding-left: 14px; position: relative; top: -5px; font-size: 18px;">拍取人脸识别照片</span>-->
<!--    <el-divider></el-divider>-->

<!--    <div>-->
<!--      &lt;!&ndash;开启摄像头&ndash;&gt;-->
<!--&lt;!&ndash;      <el-button size="mini" @click="openPhoto">摄像头</el-button>&ndash;&gt;-->
<!--      &lt;!&ndash;canvas截取流&ndash;&gt;  &lt;!&ndash;图片展示&ndash;&gt;-->
<!--      <div class="rain">-->

<!--        <div class="rainbow" style="  padding: 5px 5px 5px 5px;  left: auto;width: 500px; height: 500px;">-->
<!--          <video  ref="video" style=" height: 100%;width:100%" muted autoplay playsinline></video>-->
<!--        </div>-->
<!--        <svg  @click="photograph" style="position: relative; left: 217px; top: 70px;}" t="1659901146637" class="icon" viewBox="0 0 1032 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1641" width="64" height="64"><path d="M512 1024C229.236953 1024 0 794.784665 0 512 0 229.236953 229.236953 0 512 0c282.763047 0 512 229.236953 512 512 0 282.763047-229.215335 512-512 512z m0-68.247931c245.082925 0 443.752069-198.669144 443.752069-443.752069 0-245.061307-198.669144-443.730451-443.752069-443.730451-245.061307 0-443.730451 198.669144-443.730451 443.730451 0 245.082925 198.669144 443.752069 443.730451 443.752069z m0-47.797332C293.355852 907.954737 116.045263 730.687384 116.045263 512c0-218.665766 177.267353-395.954737 395.954737-395.954737S907.954737 293.312616 907.954737 512 730.687384 907.954737 512 907.954737z" p-id="1642" fill="#ffffff"></path></svg>-->
<!--      </div>-->
<!--      <el-button @click="ux=1">已有照片,进行上传</el-button>-->
<!--      &lt;!&ndash;      视频流&ndash;&gt;-->
<!--      <div class="renlian1"  style="position:relative;border:5px solid #909399;left: 633px; top: -511px; height: 500px; width: 500px;">-->
<!--        <canvas ref="canvas" height="500" width="500"-->
<!--                style="position: absolute;left: -21px; transform: rotateY(180deg); PADDING: 0px 0px 0px 12px; width: 511px;height: 511px;"></canvas>-->

<!--        &lt;!&ndash;      确认&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button size="mini" style="position: relative;top: 1px" @click="photograph">拍照</el-button>&ndash;&gt;-->
<!--      </div>-->


<!--    </div>-->

<!--  </el-card>-->
</div>
</template>
<script>
import axios from 'axios'

export default {
  props:{

  },
  created(){
   this.openPhoto();
  }
,
  data() {
    return {
      ux:1,
      form:{},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },

  methods: {
    getfile(event){
    this.file=event.target.files[0];
      var reader = new  FileReader();
      reader.readAsDataURL(this.file);
      reader.onloadend = function () {
        document.getElementById("img").src =  reader.result;
      }
    },
    save(event){
      event.preventDefault();
      const formData=new FormData()
      formData.append('username',this.user.name)
      formData.append('image',this.file)
      console.log(this.file)
      let url = 'https://face.xiaoku.store:5000/register'
      axios.post(url, formData)
          .then(res => {
            alert("注册成功！")
            const file=new FormData()
            file.append('files',this.file)
            file.append("folder","xiaocool/人脸识别照片")

            this.request.post("/service/cos/upload", file)
                .then(res => {
                  console.log(res)
                  this.form={
                    id:this.user.userId,
                    faceImage:res.data
                  }
                  this.request.post("/service/user", this.form).then(res => {
                    if (res.code === '200') {
                      this.$message.success("保存成功")
                    }
                  })
                })
          })
          .catch(err => {
            alert(err)
          })



    },
    openPhoto() {
      navigator.mediaDevices
          .getUserMedia({
            audio: true, video: { width: 720, height: 720 }
          })
          .then((success) => {
            // 摄像头开启成功
            this.$refs["video"].srcObject = success; //srcObject 是实时流
            // 实时拍照效果
            this.$refs["video"].play();
          })
          .catch((error) => {
            //摄像头开启失败
            console.error("摄像头开启失败");
          });
      // let _this=this
      // setTimeout(function()  {
      //   _this.photograph()
      // }, 2000);

    },
    base64toFile (dataBase64, filename = 'file') {
      let arr = dataBase64.split(',')
      let mime = arr[0].match(/:(.*?);/)[1]  //设置file⽂件流的type名称
      let suffix = mime.split('/')[1]  //设置file⽂件流的name名称
      const bstr = window.atob(arr[1]);
      let n = bstr.length
      const u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      return new File([u8arr], `${filename}.${suffix}`, {
        type: mime
      })
    },
    photograph() {
      let photoInfo= this.$refs["canvas"].getContext("2d");
      // 把当前内容渲染到canvas上
      photoInfo.drawImage(this.$refs["video"], 0, 0,480,480);

      // //canvas图片 转base64格式、图片格式转换、图片质量压缩
      let img = this.$refs["canvas"].toDataURL("image/jpg");
      let imgBase64 = this.$refs["canvas"].toDataURL("image/jpg", 0.7);
      let string = imgBase64.replace("data:image/jpg;base64,", "");
      string=this.base64toFile(imgBase64, 'camera');
      console.log(string)
      let formData=new FormData()
      console.log(formData)
      formData.append('image',string)
      var url = 'https://face.xiaoku.store:5000/recognition'
      axios.post(url, formData)
          .then(res => {
            console.log(res.data)
          })
          .catch(err => {
            alert(err)
          })
    }
  }

}

</script>
<style scoped>
.renlian  {
  position: relative;
  height: 200px; width: 200px;
  margin: 400px auto;

  background-size: 100% 100%;
}

.renlian .box {
  width: 30vw;
  height: 30vw;
  max-height: 30vh;
  max-width: 30vh;
  position: relative;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  overflow: hidden;
  border: 0.1rem solid rgba(3, 169, 244, 0.2);
}

.renlian .line {
  height: calc(100% - 2px);
  width: 100%;
  background: linear-gradient(180deg, rgba(0, 255, 51, 0) 43%, #03a9f4 211%);
  border-bottom: 2px solid #03a9f4;
  transform: translateY(-100%);
  animation: radar-beam 2s infinite;
  animation-timing-function: cubic-bezier(0.3, 0, 0.43, 0.7);
  animation-delay: 1.4s;
}

.renlian .box:after,
.renlian .box:before,
.renlian .bottom:after,
.renlian .bottom:before {
  content: '';
  display: block;
  position: absolute;
  width: 3vw;
  height: 3vw;

  border: 0.2rem solid transparent;
}

.renlian .box:after,
.renlian .box:before {
  top: 0;
  border-top-color: #03a9f4;
}

.renlian .bottom:after,
.renlian .bottom:before {
  bottom: 0;
  border-bottom-color: #03a9f4;
}

.renlian .box:before,
.renlian .bottom:before {
  left: 0;
  border-left-color: #03a9f4;
}

.renlian .box:after,
.renlian .bottom:after {
  right: 0;
  border-right-color: #03a9f4;
}


video {
  width: 100%;
  transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
}

.rain>img{
  HEIGHT: 662PX;
  WIDTH: 499PX;
  POSITION: absolute;
  TOP: 295PX;

}
</style>
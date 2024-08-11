<template>
  <div class="MyInfo">
    <div style="height: 80px;user-select: none;display: flex;flex-direction: row">
      <h1>个人资料</h1>
      <el-tooltip class="tooltip" content="跳转审核会议" placement="bottom" effect="light">
        <div style="height: 45px;"><img v-if="this.user.role==='ROLE_ADMIN'" @click="$router.replace('/admin/meeting')"
             style="width: 32px;margin:16px 0 0 8px;align-self: flex-start;" src="../assets/admin.svg"></div>
      </el-tooltip>

    </div>

    <div style=" display: flex; flex-direction: row"  v-loading="loading" element-loading-text="拼命加载中">
      <div style="user-select: none;min-width: 730px;">
        <el-card class="box-card" v-if="ux===2">
          <div slot="header" class="clearfix box-card-header">
            <span><icon CLASS="el-icon-user"></icon> 基本信息</span>
          </div>
          <span>
        <h1>登录后即可查看</h1></span>

        </el-card>
        <el-card class="box-card" v-if="ux!==2">
          <div slot="header" class="clearfix box-card-header">
            <span><icon CLASS="el-icon-user-solid" style="padding-right: 10px"></icon>基本信息</span>
          </div>
          <div class="MyInfo-body">
            <div>
              <div class="avatar1"
                   style="    padding-top: 15px;padding-left: 11px; cursor: pointer;position: relative;top: 9px;"
                   @mouseenter="ovo=1" @mouseleave="ovo=0" @click="cropImageFormVisible=true">
                <img :src="user.avatar"/>
                <p v-show="ovo===1" class="avatar2"
                   style="color:#ffffff;font-weight:500;position: absolute;width: 160px;z-index: 1;left: 15px;top: 40px;font-size: 16PX">
                  更换头像</p>
              </div>


              <span style="padding-top: 30px;">{{ user.name }}</span>
            </div>
            <div style="padding-top: 45px;padding-left: 10px;">
              <div>用户编号 :</div>
              <div>{{ user.id }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="box-card" v-if="ux!==2">
          <div slot="header" class="clearfix box-card-header">
            <span><icon CLASS="el-icon-star-off" style="padding-right: 10px;"></icon>信息修改</span>


            <el-button
                v-bind:title="message"
                @click="handleEdit()"
                type="primary"
                icon="el-icon-edit"
                circle
                size="mini"
            ></el-button>

          </div>
          <div class="MyInfo-body">
            <div>
              <div align="center">姓名 :</div>
              <div>{{ user.name }}</div>
            </div>
            <div>
              <div align="center">兴趣爱好 ：</div>
              <div>{{ user.hobby }}</div>
            </div>
            <div>
              <div align="center">手机号码 ：</div>
              <div>{{ user.telephone }}</div>
            </div>
            <div>
              <div align="center">邮箱 ：</div>
              <div>{{ user.email }}</div>
            </div>
            <div>
              <div align="center">性别 ：</div>
              <div size="large" style="padding-left: 5px" v-if="user.sex === 0">女</div>
              <div size="large" style="padding-left: 5px" v-else-if="user.sex === 1">男</div>
              <div size="large" style="padding-left: 5px" v-else-if="user.sex === 2">其他</div>
            </div>

            <div style="height: auto;">
              <div align="center">个人简介 ：</div>
              <div>
                <div v-html="user.profile"
                     style="width: auto;min-height: 30PX;display: flex;
                     align-items: center;padding-left: 10px"></div>
              </div>
            </div>
          </div>
        </el-card>
      </div>


      <div style="user-select: none;">
        <el-card style="padding: 10px;width: 426px;margin-left: 30px;height: 535px">
          <svg style="margin-left: 10px;margin-top: 10px" t="1659631163282" class="icon" viewBox="0 0 1024 1024"
               version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5466" width="36" height="36">
            <path
                d="M231.36 218.16m78.86 0l448.4 0q78.86 0 78.86 78.86l0 448.4q0 78.86-78.86 78.86l-448.4 0q-78.86 0-78.86-78.86l0-448.4q0-78.86 78.86-78.86Z"
                fill="#EFEFEF" p-id="5467"></path>
            <path
                d="M725.68 476.52c0 130.92-86.14 287.86-192.42 287.86s-192.42-156.94-192.42-287.86 86.14-221.9 192.42-221.9 192.42 90.96 192.42 221.9z"
                fill="#99D1FF" p-id="5468"></path>
            <path
                d="M533.26 780.38c-55.16 0-108.28-36.34-149.62-102.34-36.82-58.78-58.8-134.12-58.8-201.52 0-135.62 89.6-237.88 208.42-237.88s208.42 102.26 208.42 237.88c0 67.4-22 142.74-58.8 201.52-41.32 65.96-94.46 102.34-149.62 102.34z m0-509.76c-47.6 0-91.68 20-124.12 56.48-33.74 37.88-52.3 90.94-52.3 149.4 0 61.64 20.16 130.62 54 184.54 34.76 55.5 79.4 87.32 122.5 87.32s87.74-31.84 122.5-87.32c33.76-54 54-122.9 54-184.54 0-58.46-18.58-111.52-52.3-149.4-32.6-36.42-76.68-56.48-124.28-56.48z"
                fill="#18659E" p-id="5469"></path>
            <path
                d="M534.54 548.62c-52.32 0-101.68-5.06-139-14.24-45.86-11.3-68.16-27.26-68.16-48.82s22.3-37.52 68.16-48.82c37.32-9.18 86.68-14.24 139-14.24s101.68 5.06 139 14.26c45.86 11.3 68.16 27.26 68.16 48.82s-22.3 37.52-68.16 48.82c-37.34 9.16-86.7 14.22-139 14.22z m-174.36-63.06c3.8 4 17.32 12.22 50.84 19.56 34 7.42 77.78 11.5 123.52 11.5s89.62-4 123.52-11.5c33.5-7.34 47.04-15.64 50.84-19.56-3.8-4-17.32-12.22-50.84-19.56-34-7.42-77.78-11.5-123.52-11.5s-89.62 4-123.52 11.5c-33.52 7.32-47.02 15.62-50.86 19.56z"
                fill="#18659E" p-id="5470"></path>
            <path
                d="M517.26 254.62h32v509.76h-32zM146.88 357.16a16 16 0 0 1-16-16v-222a16 16 0 0 1 16-16h245.82a16 16 0 0 1 0 32H162.88v206a16 16 0 0 1-16 16zM392.7 915.1H146.88a16 16 0 0 1-16-16V632a16 16 0 0 1 32 0v251.1h229.82a16 16 0 0 1 0 32zM926.88 915.1H683.44a16 16 0 0 1 0-32h227.44V632a16 16 0 0 1 32 0v267.1a16 16 0 0 1-16 16zM926.88 357.16a16 16 0 0 1-16-16v-206H683.44a16 16 0 1 1 0-32h243.44a16 16 0 0 1 16 16v222a16 16 0 0 1-16 16z"
                fill="#18659E" p-id="5471"></path>
          </svg>
          <span style=" padding-left: 14px; position: relative; top: -10px; font-size: 18px;">上传人脸识别照片</span>
          <el-divider></el-divider>
          <div class="camera-box" style="position: relative; top: -307px;">
            <div class="renlian">

              <img style="pointer-events: none;width: 130%; height: 129%;position: absolute; left: -15%;top: -14%;"
                   id="img" src="../imgs/timg.png">
              <div class="box">
                <div class="line"></div>
                <div class="bottom">

                </div>
              </div>
              <input
                  style="right: -40px; opacity: 0;width: 280px; height: 275px;cursor:pointer;  top: -36px;position: absolute;"
                  type="file" id="fileup" @change="getfile($event)">
              <el-button type="primary" plain @click="save($event)"
                         style="left: 60px;position: relative;    top: 60px;">上 传
              </el-button>

            </div>


          </div>
        </el-card>
      </div>


    </div>
    <el-dialog title="更换头像" :visible.sync="cropImageFormVisible">
      <div class="cropper-content">
        <div class="cropper-box">
          <div class="cropper">
            <vue-cropper
                ref="cropper"
                :img="option.img"
                :outputSize="option.outputSize"
                :outputType="option.outputType"
                :info="option.info"
                :canScale="option.canScale"
                :autoCrop="option.autoCrop"
                :autoCropWidth="option.autoCropWidth"
                :autoCropHeight="option.autoCropHeight"
                :fixed="option.fixed"
                :fixedNumber="option.fixedNumber"
                :full="option.full"
                :fixedBox="option.fixedBox"
                :canMove="option.canMove"
                :canMoveBox="option.canMoveBox"
                :original="option.original"
                :centerBox="option.centerBox"
                :height="option.height"
                :infoTrue="option.infoTrue"
                :maxImgSize="option.maxImgSize"
                :enlarge="option.enlarge"
                :mode="option.mode"
                @realTime="realTime"
                @imgLoad="imgLoad"
                style="background-image:none"
            >
            </vue-cropper>
          </div>
          <!--底部操作工具按钮-->
          <div class="footer-btn">
            <div class="scope-btn">
              <label class="btn" for="uploads">选择头像</label>
              <input type="file" id="uploads" style="position:absolute; clip:rect(0 0 0 0);"
                     accept="image/png, image/jpeg, image/gif, image/jpg" @change="selectImg($event)">
              <input class="btn1" type="button" value="+" title="放大" @click="changeScale(1)"/>
              <input class="btn1" type="button" style value="-" title="缩小" @click="changeScale(-1)"/>
              <input class="btn1" type="button" value="↺" title="左旋转" @click="rotateLeft"/>
              <input class="btn1" type="button" value="↻" title="右旋转" @click="rotateRight"/>
            </div>
            <div class="upload-btn">
              <el-button size="mini" type="success" plain @click="uploadImg('blob')">确定 <i class="el-icon-upload"></i>
              </el-button>
            </div>
          </div>
        </div>
        <!--预览效果图-->
        <div class="show-preview">
          <div :style="previews.div" class="preview">
            <img :src="previews.url" :style="previews.img">
          </div>
        </div>
      </div>

    </el-dialog>


    <div style="margin: 10px 0; position: absolute;font-size: large">
      <el-dialog
          title="用户信息"
          :visible.sync="dialogVisible" width="50%">
        <el-form :model="form" label-width="80px">

          <el-form-item label="编号:">
            <el-input v-model="form.id" readonly style="width:20%"></el-input>
          </el-form-item>

          <el-form-item label="姓名:">
            <el-input v-model="form.name" style="width: 25%"></el-input>
          </el-form-item>

          <el-form-item label="性别:" prop="sex" label-width="100px" style="margin-left: -20px;">
            <el-radio-group v-model="form.sex">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
              <el-radio :label="2">其他</el-radio>
            </el-radio-group>
          </el-form-item>


          <el-form-item label="用户名:">
            <el-input v-model="form.username" style="width: 45%" readonly></el-input>
          </el-form-item>

          <el-form-item label="密码:">
            <el-input v-model="form.password" style="width: 45%"></el-input>
          </el-form-item>

          <el-form-item label="电话号码:">
            <el-input v-model="form.telephone" style="width: 45%"></el-input>
          </el-form-item>

          <el-form-item label="爱好:">
            <el-input v-model="form.hobby" style="width: 60%"></el-input>
          </el-form-item>

          <el-form-item label="个人简介 : ">
            <div style="border: 1px solid #ccc;">
              <Toolbar
                  style="border-bottom: 1px solid #ccc"
                  :editor="editor"
                  :defaultConfig="toolbarConfig"
                  :mode="mode"
              />
              <Editor
                  style="height: 230px; overflow-y: hidden;"
                  v-model="form.profile"
                  :defaultConfig="editorConfig"
                  :mode="mode"
                  @onCreated="onCreated"
              />
            </div>
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveInfo">确 定</el-button>
          </span>
        </template>
      </el-dialog>


    </div>

  </div>

</template>

<script>
import axios from 'axios'
import Vue from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import {VueCropper} from "vue-cropper";

export default Vue.extend({
  name: 'MyInfo',
  components: {
    Editor,
    Toolbar,
    VueCropper
  },
  created() {
    this.getUser().then(res => {
      this.form = res
      this.user=res
      this.loading=false
    })
    this.ux = this.user.length
  },
  data() {

    return {
      loading:true,
      rCardHeight: "500px",
      cropImageFormVisible: false,
      user: '',
      id: '',
      ovo: 0,
      message: '信息修改',
      form: {},
      dialogVisible: false,
      editor: null,
      toolbarConfig: {},
      editorConfig: {placeholder: '请输入内容...'},
      mode: 'default', // or 'simple'
      ux: '',
      previews: {},
      option: {
        img: JSON.parse(localStorage.getItem("user")).avatar,             //裁剪图片的地址
        outputSize: 1,       //裁剪生成图片的质量(可选0.1 - 1)
        outputType: 'jpeg',  //裁剪生成图片的格式（jpeg || png || webp）
        info: true,          //图片大小信息
        canScale: true,      //图片是否允许滚轮缩放
        autoCrop: true,      //是否默认生成截图框
        autoCropWidth: 300,  //默认生成截图框宽度
        autoCropHeight: 300, //默认生成截图框高度
        fixed: true,         //是否开启截图框宽高固定比例
        fixedNumber: [1, 1], //截图框的宽高比例
        full: false,         //false按原比例裁切图片，不失真
        fixedBox: true,      //固定截图框大小，不允许改变
        canMove: false,      //上传图片是否可以移动
        canMoveBox: true,    //截图框能否拖动
        original: false,     //上传图片按照原始比例渲染
        centerBox: false,    //截图框是否被限制在图片里面
        height: true,        //是否按照设备的dpr 输出等比例图片
        infoTrue: false,     //true为展示真实输出图片宽高，false展示看到的截图框宽高
        maxImgSize: 3000,    //限制图片最大宽度和高度
        enlarge: 1.5,          //图片根据截图框输出比例倍数
        mode: '360px 360px'  //图片默认渲染方式
      }

    }
  },

  methods: {
    async getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      const userinfo=(await this.request.get("/service/user/" + username)).data
      userinfo.userId=userinfo.id
      return userinfo
    },
    logout() {
      this.$store.commit("logout")
      this.$message.success("退出成功")
      this.$router.push('/')
    },
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },    //上传头像
    filesUploadSuccess(res) {
      this.form.avatar = res;

    },

    //上传人脸识别照片
    filesUploadSuccess1(res) {
      console.log("1" + res)
      this.form.faceImage = res;
    },


    //编辑用户信息
    handleEdit() {
      this.form = {
        id: this.user.id,
        name: this.user.name,
        telephone: this.user.telephone,
        sex: this.user.sex,
        email: this.user.email,
        hobby: this.user.hobby,
        username: this.user.username,
        password: this.user.password,
        profile: this.user.profile,
        faceImage: this.user.faceImage
      }
      this.dialogVisible = true
    },
    saveInfo() {
      this.request.post("/service/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          // 触发父级更新User的方法
          this.$emit("refreshUser")
          // 更新浏览器存储的用户信息
          this.getUser().then(res => {
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user", JSON.stringify(res))
            location.reload();
          })
        } else {
          this.$message.error("保存失败")
        }

      })
    },


    //初始化函数
    imgLoad(msg) {
      console.log("工具初始化函数=====" + msg)
    },
    //图片缩放
    changeScale(num) {
      num = num || 1
      this.$refs.cropper.changeScale(num)
    },
    //向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft()
    },
    //向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight()
    },
    //实时预览函数
    realTime(data) {
      this.previews = data
    },
    //选择图片
    selectImg(e) {
      let file = e.target.files[0]
      if (!/\.(jpg|jpeg|png|JPG|PNG)$/.test(e.target.value)) {
        this.$message({
          message: '图片类型要求：jpeg、jpg、png',
          type: "error"
        });
        return false
      }
      //转化为blob
      let reader = new FileReader()
      reader.onload = (e) => {
        let data
        if (typeof e.target.result === 'object') {
          data = window.URL.createObjectURL(new Blob([e.target.result]))
        } else {
          data = e.target.result
        }
        this.option.img = data
      }
      //转化为base64
      reader.readAsDataURL(file)
    },
    //上传图片
    uploadImg(type) {
      if (type === 'blob') {
        //获取截图的blob数据
        this.$refs.cropper.getCropBlob(async (data) => {
          console.log(data)
          var result = new File([data], this.user.name+'头像.jpeg', {type: 'image/jpeg', lastModified: Date.now()});
          let formData = new FormData();
          formData.append('files', result)
          formData.append("folder","xiaocool/头像")
          this.request.post("/service/cos/upload", formData)
              .then(res => {
                console.log(res)
                this.form = {
                  id: this.user.userId,
                  avatar: res.data
                }
                this.request.post("/service/user", this.form).then(res => {
                  if (res.code === '200') {
                    this.$message.success("保存成功")
                    // 触发父级更新User的方法
                    this.$emit("refreshUser")

                    // 更新浏览器存储的用户信息
                    this.getUser().then(res => {
                      res.token = JSON.parse(localStorage.getItem("user")).token
                      localStorage.setItem("user", JSON.stringify(res))
                      location.reload();
                    })
                  }
                })
              })
        })
            .catch(err => {
              alert(err)
            })
      }
    },
    getfile(event) {
      this.file = event.target.files[0];
      var reader = new FileReader();
      reader.readAsDataURL(this.file);
      reader.onloadend = function () {
        document.getElementById("img").src = reader.result;
      }
    },
    save(event) {
      event.preventDefault();
      let formData = new FormData()
      formData.append('username', this.user.name)
      formData.append('image', this.file)
      console.log(this.file)
      let url = 'https://face.xiaoku.store:5000/register'
      axios.post(url, formData)
          .then(res => {
            alert("注册成功！")
            let file = new FormData()
            file.append('files', this.file)
            file.append("folder","xiaocool/人脸识别照片")

            this.request.post("/service/cos/upload", file)
                .then(res => {
                  console.log(res)
                  this.form = {
                    id: this.user.userId,
                    faceImage: res.data
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
            audio: true, video: {width: 720, height: 720}
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
    base64toFile(dataBase64, filename = 'file') {
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
      let photoInfo = this.$refs["canvas"].getContext("2d");
      // 把当前内容渲染到canvas上
      photoInfo.drawImage(this.$refs["video"], 0, 0, 480, 480);
      // //canvas图片 转base64格式、图片格式转换、图片质量压缩
      let img = this.$refs["canvas"].toDataURL("image/jpg");
      let imgBase64 = this.$refs["canvas"].toDataURL("image/jpg", 0.7);
      let string = imgBase64.replace("data:image/jpg;base64,", "");
      string = this.base64toFile(imgBase64, 'camera');
      console.log(string)
      let formData = new FormData()
      console.log(formData)
      formData.append('image', string)
      var url = 'https://face.xiaoku.store:5000/recognition'
      axios.post(url, formData)
          .then(res => {
            console.log(res.data)
          })
          .catch(err => {
            alert(err)
          })
    },


  },
  mounted() {
    // 监听右侧page元素宽度变化，更新底部固定按钮栏宽度
    // this.$nextTick(() => {
    //   this.$erd.listenTo(this.$refs.changeinfo, (ele) => {
    //     console.log(ele.clientHeight)
    //     //  监听到html元素尺寸变化后执行一些逻辑处理
    //     this.rCardHeight=ele.clientHeight+206 + "px";
    //   })
    // }),
    // 模拟 ajax 请求，异步渲染编辑器
    setTimeout(() => {
      this.form.profile =  this.user.profile
    }, 1500)

  },

  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器

    this.$erd.uninstall(this.$refs.leftCard)

  },


})
</script>

<style scoped lang="scss">
* {
  font-size: 17px;
}

.cropper-content {
  display: flex;
  display: -webkit-flex;
  justify-content: flex-end;

  .cropper-box {
    flex: 1;
    width: 100%;

    .cropper {
      width: 360px;
      height: 360px;
    }
  }

  .show-preview {
    // border: 1px solid #ccc;
    box-sizing: border-box;
    overflow: hidden;
    flex: 1;
    -webkit-flex: 1;
    display: flex;
    display: -webkit-flex;
    justify-content: center;
    -webkit-justify-content: center;

    .preview {
      overflow: hidden;
      border-radius: 50%;
      border: 1px solid #cccccc;
      background: #fff;
      margin-left: 0px;
    }
  }
}

.footer-btn {
  margin-top: 30px;
  display: flex;
  display: -webkit-flex;
  justify-content: flex-end;

  .scope-btn {
    display: flex;
    display: -webkit-flex;
    justify-content: space-between;
    padding-right: 10px;

    .btn1 {
      height: 32px;
      width: 32px;
      font-size: 20px;
      margin: 3px 5px;
      background-color: #fff;
      border: 1px solid #999;
      border-radius: 4px;
    }
  }

  .upload-btn {
    flex: 1;
    -webkit-flex: 1;
    display: flex;
    display: -webkit-flex;
    justify-content: center;
  }

  .btn {
    outline: none;
    display: inline-block;
    line-height: 1;
    white-space: nowrap;
    cursor: pointer;
    -webkit-appearance: none;
    text-align: center;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    outline: 0;
    -webkit-transition: .1s;
    transition: .1s;
    font-weight: 500;
    padding: 8px 15px;
    font-size: 18px;
    border-radius: 3px;
    color: #fff;
    background-color: #409EFF;
    border-color: #409EFF;
    margin-right: 10px;
  }
}


//上面是更改头像


.avatar1:hover img {
  -webkit-filter: brightness(0.8);
  filter: brightness(0.8)
}


::v-deep .avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

::v-deep .avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

::v-deep .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 200px;
  line-height: 200px;
  text-align: center;
}

::v-deep .avatar {
  width: 178px;
  height: 200px;
  display: block;
}

.MyInfo {
  min-width: 100vh;
}

.MyInfo h1 {
  font-weight: 400;
  height: 60px;
  line-height: 60px;
  font-size: 26px;
}

.box-card {
  border-radius: 10px;
  margin-bottom: 15px;
  padding: 0 30px 15px;
}

.box-card-header span {
  margin-right: 15px;
}

::v-deep .box-card-header .el-button {
  background: none;
  color: #409eff;
  border: none;
}

::v-deep .el-card__header,
::v-deep .el-card__body {
  padding: 18px 0px;

}

::v-deep .el-card__body {
  padding-top: 0px;

}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}


.MyInfo-body {
  font-size: 12px;
}

.MyInfo-body div:nth-child(1) img {
  width: 74px;
  height: 74px;
  border-radius: 100%;
}

.MyInfo-body > div {
  margin-top: 15px;
}

.MyInfo-body > div {
  display: flex;
  align-items: center;
  height: 38px;
}

.MyInfo-body > div div:nth-child(1) {
  min-width:100px;
  width: 110px;
  color: #888888;
}

.MyInfo-body > div div:nth-child(2) {
  text-align: left;
}

.renlian {
  position: relative;
  height: 200px;
  width: 200px;
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

.rain > img {
  HEIGHT: 662PX;
  WIDTH: 499PX;
  POSITION: absolute;
  TOP: 295PX;

}
</style>
<style src="@wangeditor/editor/dist/css/style.css"></style>
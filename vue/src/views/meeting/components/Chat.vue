<template>
  <div class="Chat-container" @click="closePopover">
    <el-input v-model="recText"  id="textarea_id" class="textarea_id" :rows="row" type="textarea" style="font-size: 17px;
     user-select: none;-webkit-user-select: auto" readonly placeholder="还没有人聊天..." :style="{'height':textareaHeight +'px'}" />


    <el-popover
        ref="popverRef"
        trigger="click"
        v-model="visible">
      <!-- 触发事件 -->

      <!-- 弹框内容 -->
      <div class="div_content" style="position: absolute;bottom: 1px;left: -46px;" @click.stop="noclosePopover">
        <VEmojiPicker @select="selectEmoji" />
      </div>
    </el-popover>
    <el-input  id="emojiInput" v-model="sendText" placeholder="请输入内容" maxlength="64"  class="input-with-select"  @keyup.enter.native="$emit('chatEvent',sendText);sendText=''">
      <el-button slot="prepend" size="mini"  @click.stop="togglePopover">😊</el-button>
      <el-button slot="append" size="mini" icon="el-icon-s-promotion"
                 @click="$emit('chatEvent',sendText);sendText=''"  />
    </el-input>


  </div>
</template>

<script>


import { VEmojiPicker } from 'v-emoji-picker';

export default {
  name: 'Chat',
  components: {
    VEmojiPicker
  },
  props: {
    someData: {
      type: Object,
      default: "",
    },
    client: {
      type: Object,
      default: null
    },
    receiveMsg: {
      type: String,
      require: false,
      default: ''
    }
  },
created(){
        this.request.get("/service/message/getMsg/", {
          params: {
            meetingId:this.someData.meetingId
          }
        }).then(res => {
          if (res.code==="200"){
            let Msgdata=[]
            for (let i=0;i<res.data.length;i++)
            {
              // if (i===res.data.length-1){
              // Msgdata[i]=res.data[i].nickname+":"+ res.data[i].content}
              // else {
                Msgdata[i]=res.data[i].nickname+":"+ res.data[i].content+"\n"
              // }

            }
            Msgdata = Msgdata.toString().replace(/,/g, "");
            this.recText= Msgdata
            this.scrollBottom();

          }
          else {
            this.$message.success("聊天记录出错啦！")
          }
        })
},

  mounted() {
    window.addEventListener('click', this.handleOutsideClick);
    this.getScreenSize();
  },
  data() {
    return {
      visible: false,
      recText: '',
      sendText: '',
      row:'',
      textareaHeight:'',
      editor: null,
      screenWidth: '',
      screenHeight: '',
      toolbarConfig: { },
      editorConfig: { placeholder: '请输入内容...' },
      mode: 'default', // or 'simple'
    }
  },

  watch: {
    receiveMsg: {
      handler(newVal) {
        this.receiveMsgHandle(newVal);
      },
      immediate: true,
      deep: true
    }
  },

  methods: {
    getChatHeight(chatHeight){
      console.log("chatHeight",chatHeight)
      this.textareaHeight=chatHeight-50;
    },
    getScreenSize() {
      this.screenWidth = document.body.clientWidth;
      this.screenHeight = document.body.clientHeight
      window.onresize = () => {
        return (() => {
          this.screenWidth = document.body.clientWidth;
          this.screenHeight = document.body.clientHeight;
          if (this.screenHeight>900) {
            this.row=21
          }
          else {
            this.row=15
          }
        })();
      };

      if (this.screenHeight>900) {
        this.row=21
      }
      else {
        this.row=15
      }
    },
    scrollBottom(){
    this.$nextTick(() => {
      setTimeout(() => {
        const textarea = document.getElementById('textarea_id');
        textarea.scrollTop = textarea.scrollHeight;
      }, 13)
    })
  },

    handleOutsideClick(event) {
      // 获取组件的根元素
      const container = this.$el;
      // 判断点击事件的目标是否在组件以外
      if (!container.contains(event.target)) {
        this.closePopover();
      }
    },
    closePopover() {
      this.visible = false;
    },
    togglePopover() {
      this.visible = !this.visible;
    },
    noclosePopover() {
      this.visible = true;
    },
    selectEmoji(emoji) {
      console.log(emoji)
      var elInput = document.getElementById('emojiInput')//获取输入框元素
      // console.log(elInput);
      var start = elInput.selectionStart // 记录光标开始的位置
      var end = elInput.selectionEnd // 记录选中的字符 最后的字符的位置
      if (start === undefined || end === undefined) return
      var txt = elInput.value
      // 将表情添加到选中的光标位置
      var result =
          txt.substring(0, start) + emoji.data + txt.substring(end)
      elInput.value = result // 赋值给input的value
      // 重置光标位置
      elInput.focus()
      elInput.selectionStart = start + emoji.data.length
      elInput.selectionEnd = start + emoji.data.length
      console.log(result)
      this.sendText= result // 赋值(注意这里一定要赋值给表情输入框绑定的那个值)

    },
    receiveMsgHandle(newVal) {
      if (newVal && newVal.content) {
        this.recText += newVal.content;
        this.scrollBottom();
      }
    },
    enterSearch(){
      document.onkeydown = e =>{
        //13表⽰回车键，baseURI是当前页⾯的地址，为了更严谨，也可以加别的，可以打印e看⼀下
          //回车后执⾏搜索⽅法

          this.$emit('chatEvent', this.sendText)

      }
    },


  }
}
</script>



<style lang="scss" scoped>
::v-deep .el-popover{
  padding: 0;

}
.textarea_id{
  resize:none;
  -webkit-user-select:auto; /*webkit浏览器*/
}


::v-deep .el-textarea__inner {
  height: inherit;
  resize:none;
  overflow-y: hidden;
  /*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
  // 滚动条整体部分
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }
  // 滚动条的轨道的两端按钮，允许通过点击微调小方块的位置。
  &::-webkit-scrollbar-button {
    display: none;
  }
  // 滚动条里面的小方块，能向上向下移动（或往左往右移动，取决于是垂直滚动条还是水平滚动条）
  &::-webkit-scrollbar-thumb {
    background: rgba(144, 147, 153, 0.3);
    cursor: pointer;
    border-radius: 4px;
  }
  // 边角，即两个滚动条的交汇处
  &::-webkit-scrollbar-corner {
    display: none;
  }
  // 两个滚动条的交汇处上用于通过拖动调整元素大小的小控件
  &::-webkit-resizer {
    display: none;
  }

}
::v-deep .el-textarea__inner:corner-present{
  scrollbar-width: none;
}
::v-deep .el-textarea__inner:hover{
  overflow-y: auto;
  /*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
  scrollbar-width: thin;
  scrollbar-color: #999999 #f0f0f0;


  // 滚动条整体部分
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }
  // 滚动条的轨道的两端按钮，允许通过点击微调小方块的位置。
  &::-webkit-scrollbar-button {
    display: none;
  }
  // 滚动条里面的小方块，能向上向下移动（或往左往右移动，取决于是垂直滚动条还是水平滚动条）
  &::-webkit-scrollbar-thumb {
    background: rgba(144, 147, 153, 0.3);
    cursor: pointer;
    border-radius: 4px;
  }
  // 边角，即两个滚动条的交汇处
  &::-webkit-scrollbar-corner {
    display: none;
  }
  // 两个滚动条的交汇处上用于通过拖动调整元素大小的小控件
  &::-webkit-resizer {
    display: none;
  }
}

</style>

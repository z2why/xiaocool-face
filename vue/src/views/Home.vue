<template>
  <div class="resize-warpper">
    margin
    <div class="resize-element" ref="wrapper">
      padding
      <div class="resize-content">content</div>
      <IFRAME
          :src='"https://face.xiaoku.store:5000/video_feed/"+"123"'
          id="customchart"
          style="width:640px;height:480px;" :style="'transform: scale(1)'"
          frameBorder=0 marginwidth=0 marginheight=0 scrolling=no></IFRAME>


    </div>
    margin
  </div>
</template>

<script>
export default {
  name: 'ResizeObsever',
  mounted() {
    const myObserver = new ResizeObserver((entries) => {
      entries.forEach((entry) => {
        console.log('被监听元素content的宽高及位置', entry.contentRect)
        // bottom: 700 指top + height的值
        // height: 600 指元素本身的高度，不包含padding，border值
        // left: 100 指padding-left的值
        // right: 1143 指left + width的值
        // top: 100 指padidng-top的值
        // width: 1043  指元素本身的宽度，不包含padding，border值
        // x: 100
        // y: 100
        console.log('被监听元素的宽高', entry.borderBoxSize)
        // blockSize: 1000
        // inlineSize: 1443
        console.log('被监听元素content部分的宽高', entry.contentBoxSize)
        // blockSize: 600
        // inlineSize: 1043
        console.log('被监听元素', entry.target)
      })
    })
    myObserver.observe(this.$refs.wrapper)
  }
}
</script>

<style>
.resize-warpper {
  background: skyblue;
}
.resize-element {
  width: calc(100% - 700px);
  background: #ff2d52;
  height: 600px;
  padding: 100px;
  margin: 150px;
  border: 100px solid green;
}

.resize-content {
  width: 100%;
  height: 100%;
  background: yellow;
}
</style>

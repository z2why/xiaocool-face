<template>
  <div class="bottom">

    <a
        target="_blank"
        class="bottom-font"
        href="https://beian.miit.gov.cn/"
    >粤ICP备2023154753号</a
    >
    | &nbsp; Copyright © 2023-2024
  </div>
</template>

<script>
export default {
  name: "Footer"
}
</script>

<style lang="scss" scoped>
.bottom{
  text-align:center;
  bottom:0;
  margin:0 auto;
  width:100%;
  //color: #5c6b77;
  color: #999;
  font-size: 15px;
  user-select: none;

  .bottom-font {
    //color: #5c6b77;
    color: #999;
    font-size: inherit;
    text-decoration: none;

  }
}

</style>
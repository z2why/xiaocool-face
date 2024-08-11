<template>
  <div>
    <div id="container" style="width: 100%; height: calc(60vh - 100px)"></div>
  </div>
</template>

<script>


export default {
  name: "Map",
  data() {
    return {

      location1:this.location1
    }
  },

  created() {

  },
  mounted() {

    var location1;
    var map = new BMapGL.Map("container");

    var point = new BMapGL.Point(116.404, 39.915);
    map.centerAndZoom(point, 19);
    map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放



    var scaleCtrl = new BMapGL.ScaleControl();  // 添加比例尺控件
    map.addControl(scaleCtrl);
    var zoomCtrl = new BMapGL.ZoomControl();  // 添加缩放控件
    map.addControl(zoomCtrl);
    var cityCtrl = new BMapGL.CityListControl();  // 添加城市列表控件
    map.addControl(cityCtrl);
    // var geolocation = new BMapGL.Geolocation();
    // geolocation.getCurrentPosition(function(r){
    //   if(this.getStatus() == BMAP_STATUS_SUCCESS){
    //     const province = r.address.province
    //     const city = r.address.city
    //     alert(province)
    //     alert(city)
    //   }
    // });

    var opts = {
      width: 250,     // 信息窗口宽度
      height: 100,    // 信息窗口高度
      title: "您的位置信息："  // 信息窗口标题
    }



    var geolocation = new BMapGL.Geolocation();
    geolocation.getCurrentPosition(function(r) {
      if (this.getStatus() === BMAP_STATUS_SUCCESS) {
        var mk = new BMapGL.Marker(r.point);
        map.addOverlay(mk);
        map.panTo(r.point);
        // 创建地理编码实例
        var myGeo = new BMapGL.Geocoder();
        // 根据坐标得到地址描述
        myGeo.getLocation(new BMapGL.Point(r.point.lng, r.point.lat), function (result) {
          if (result) {
            location1 = result.address
            localStorage.setItem("location",location1)
            // alert(location1);
           var infoWindow = new BMapGL.InfoWindow(location1, opts);  // 创建信息窗口对象
             map.openInfoWindow(infoWindow, map.getCenter());        // 打开信息窗口
          }
        });
      }
      else {alert('failed:'  +this.getStatus()+"   "+"请检查网络状况!");}
    });


  },
  methods: {


  back() {
  },


  },

}

</script>

<style type="text/css" scoped >
body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
</style>
# 小酷人脸识别会议系统
 **项目体验地址：https://xiaoku.store** 

#### 开发者的话:

该项目是在大二时候开发的springboot + vue + onnx的基于深度学习的人脸识别会议系统,所以代码还是很稚嫩，后面重构成了springcloud Alibaba + vue + onnx的微服务项目但是核心，可以使用上面的体验地址体验一下哦！若你想并进行二次开发，请按照安装教程行动！

#### 介绍:
- 1、支持基本的会议管理功能，包括会议申请、开始会议、会议签到、结束会议等。
- 2、支持参会人信息管理，参会人信息可以预先进行人脸录入或照片录入。
- 3、会议开始后，参会人可以通过人脸识别进行签到，签到成功后系统可及时变更参会人员签到状态，如签到失败，也可通过手动签到等其它方式进行补签。
- 4、会议结束后，可以对参会人签到情况进行统计，包括签到人数、签到方式等。
- 5、支持线上会议/线下会议，线上会议由websocket+webrtc开发的线上会议室，线下会议由人脸识别模块(onnx模型)+fastapi+websocket开发的线下扫脸

#### 软件架构与安装说明:

1.  xiaocool-api：为项目后端接口，请先更改每个微服务项目中的application以及application-pro/dev.yaml，以及bootstrap以及bootstrap-pro/dev.yaml #已有注解，跟着注解完成即可，后端service模块本地测试环境需要https协议来开启图像，请使用openssl生成自签证书(仅本地使用，生产环境请使用ssl证书)
2.  vue：为项目前端，请运行npm install
3.  the face：为项目的人脸识别模块，请根据readme.md文件进行操作，可使用GPU或CPU运行
4.  请注意环境需要Mysql、redis、nacos2.0.3以上版本、sentinel

#### 项目截图：

登录界面：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-1.png) 

主页：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-2.png)

查看/编辑个人资料/上传人脸识别图片：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-3.png)

修改头像：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-4.png)

上传人脸识别照片：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-5.png)

预约会议：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-6.png)

查看我预约的会议：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-7.png)

查看会议详情：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-8.png)

查看会议数据

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-9.png)

查看已报名的会议：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-10.png)

查看会议审核历史：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-11.png)

点击进入会议并开启人脸识别：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-12.png)

识别成功进入会议选择投影电脑屏幕/摄像头:

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-13.png)

会议界面

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-14.png)

隐藏上边栏和侧边栏以及放大预览框：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-15.png)

转换为摄像头：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-16.png)

导出会议数据：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-17.png)

管理端对腾讯云cos文件进行管理：

![img](README%E5%9B%BE%E7%89%87%E5%A4%B9/xiaocool-18.png)

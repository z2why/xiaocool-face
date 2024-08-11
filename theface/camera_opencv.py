import os
import cv2
import redis
import logging
from base_camera import BaseCamera
from utils.FaceRecognitionUtil import FaceRecognition

# 设置Redis连接
redis_client = redis.StrictRedis(host='124.222.32.229', port=6379,password='Yys12300', db=0, decode_responses=True)
logging.basicConfig(level=logging.DEBUG)


class Camera(BaseCamera):
    video_source = 0

    def __init__(self, code):
        super(Camera, self).__init__(code)

    @classmethod
    def update(cls, received_rooms):
        code = received_rooms["code"]
        username = received_rooms["username"]
        # 检查该房间是否存在
        if redis_client.exists(f"room:{code}"):
            # 检查用户名是否已经在房间中
            if redis_client.sismember(f"room:{code}", username):
                return False
            # 添加用户名到房间的集合中
            redis_client.sadd(f"room:{code}", username)
            return username
        else:
            # 创建新的房间和用户名集合
            redis_client.sadd(f"room:{code}", username)
            return username

    @classmethod
    def delete_room(cls, code):
        if redis_client.exists(f"room:{code}"):
            redis_client.delete(f"room:{code}")

            return True
        else:
            return False

    @classmethod
    def get_room(cls, code):
        if redis_client.exists(f"room:{code}"):
            usernames = redis_client.smembers(f"room:{code}")
            return {"code": code, "usernames": list(usernames)}
        else:
            return None

    @classmethod
    def set_video_source(cls, source):
        cls.video_source = source

    @staticmethod
    def generate_frames(code):
        face_recognition = FaceRecognition()
        # 不断尝试初始化摄像头，直到成功为止
        while True:
            camera = cv2.VideoCapture(Camera.video_source, cv2.CAP_DSHOW)
            if camera.isOpened():
                break
            else:
                print("无法打开摄像头，正在重试...")
                camera.release()  # 确保在重试前正确释放任何资源


        while True:
            _, img = camera.read()
            results = face_recognition.recognition(image=img)
            usernames = [result['username'] for result in results if result['username'] != 'unknown']

            if usernames:
                for username in usernames:
                    new_users = Camera.update({"code": code, "username": username})
                    if new_users:
                        # 将new_users存入Redis
                        redis_client.set("latest_user_info", new_users)
                    usernames.pop(0)

            img = face_recognition.draw_recognition(image=img, results=results)
            # 编码为jpeg图像并返回
            yield cv2.imencode('.jpg', img)[1].tobytes()

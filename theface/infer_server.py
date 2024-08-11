import asyncio

import cv2
import numpy as np
import redis
import uvicorn
from fastapi import FastAPI, File, UploadFile, HTTPException
from starlette.middleware.cors import CORSMiddleware
from starlette.responses import StreamingResponse
from starlette.websockets import WebSocket

from camera_opencv import Camera
from utils.FaceRecognitionUtil import FaceRecognition
from utils.logger import setup_logger

logger = setup_logger('server')

redis_client = redis.StrictRedis(host='124.222.32.229', port=6379,password='Yys12300', db=0, decode_responses=True)

# 文档配置
app = FastAPI(
    title='Fastapi Demo API Docs',
    description='API接口文档',
    version='1.0.0',
    docs_url='/docs',
    redoc_url='/redocs'
)

# 允许跨越访问
# origins = [
#     "http://localhost",
#     "https://localhost"
# ]
# 允许所有来源的跨域请求
app.add_middleware(
    CORSMiddleware,
    # 允许跨域的源列表，例如 ["http://www.example.org"] 等等，["*"] 表示允许任何源
    allow_origins=["*"],
    # 跨域请求是否支持 cookie，默认是 False，如果为 True，allow_origins 必须为具体的源，不可以是 ["*"]
    allow_credentials=False,
    # 允许跨域请求的 HTTP 方法列表，默认是 ["GET"]
    allow_methods=["*"],
    # 允许跨域请求的 HTTP 请求头列表，默认是 []，可以使用 ["*"] 表示允许所有的请求头
    # 当然 Accept、Accept-Language、Content-Language 以及 Content-Type 总之被允许的
    allow_headers=["*"],
    # 可以被浏览器访问的响应头, 默认是 []，一般很少指定
    # expose_headers=["*"]
    # 设定浏览器缓存 CORS 响应的最长时间，单位是秒。默认为 600，一般也很少指定
    # max_age=1000
)

# 加载人脸识别工具
face_recognition = FaceRecognition()


def gen(camera, code):
    """视频流生成"""
    while True:
        frame = camera.get_frame(code)
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')


@app.get('/video_feed/{room_code}')
def video_feed(room_code: str):
    return StreamingResponse(gen(Camera(room_code), room_code), media_type='multipart/x-mixed-replace; boundary=frame')


clients = {}


@app.websocket("/ws/{room_code}")
async def websocket_endpoint(websocket: WebSocket, room_code: str):
    await websocket.accept()
    clients[room_code] = websocket
    try:
        while True:
            new_face_info = await get_new_face_info()
            if new_face_info:
                await broadcast_message(room_code, new_face_info)
    except Exception as e:
        print(e)
    finally:
        if room_code in clients:
            del clients[room_code]


async def get_new_face_info():
    while True:
        user_info = redis_client.get("latest_user_info")
        if user_info:
            redis_client.delete("latest_user_info")
            return user_info  # 确保数据格式正确
        await asyncio.sleep(0.1)


async def broadcast_message(room_code: str, message: {}):
    if room_code in clients:
        await clients[room_code].send_json(message)


@app.get('/deleteRoom/{code}')
def deleteRoom(code: str):
    return Camera.delete_room(code)


# 人脸注册或者添加人脸
@app.post('/register')
async def register(username: str, image: UploadFile = File(...)):
    # Logging
    logger.info('\n' + '=' * 70)

    try:
        contents = await image.read()
        nparr = np.frombuffer(contents, np.uint8)
        image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    except Exception as e:
        logger.warning(f'上传文件不存在，或是图片错误！错误信息e：{e}')
        raise HTTPException(status_code=400, detail="上传文件不存在，或是图片错误")

    # Perform face registration or addition
    result, state = face_recognition.register(image=image, user_name=username)
    if not result:
        logger.warning(f'执行人脸注册或者添加人脸失败！错误信息e：{state}')
        raise HTTPException(status_code=500, detail="执行人脸注册或者添加人脸失败")

    # Add return information
    return {"msg": "success"}


# 人脸识别
@app.post('/recognition')
async def recognition(image: UploadFile = File(...)):
    # Logging
    logger.info('\n' + '=' * 70)

    try:
        contents = await image.read()
        nparr = np.frombuffer(contents, np.uint8)
        image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    except Exception as e:
        logger.warning(f'上传文件不存在，或是图片错误！错误信息e：{e}')
        raise HTTPException(status_code=400, detail="上传文件不存在，或是图片错误")

    # Perform face recognition
    results = face_recognition.recognition(image=image)

    # Add return information
    return {"msg": "success", "results": results}


# 人脸属性识别
@app.post('/face_attribute')
async def face_attribute(image: UploadFile = File(...)):
    # Logging
    logger.info('\n' + '=' * 70)

    try:
        contents = await image.read()
        nparr = np.frombuffer(contents, np.uint8)
        image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    except Exception as e:
        logger.warning(f'上传文件不存在，或是图片错误！错误信息e：{e}')
        raise HTTPException(status_code=400, detail="上传文件不存在，或是图片错误")

    # Perform face attribute recognition
    results = face_recognition.face_attribute(image=image)

    # Add return information
    return {"msg": "success", "results": results}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000)

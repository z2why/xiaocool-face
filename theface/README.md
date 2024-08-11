# 安装CPU版本：

```shell
python -m pip install -r requirements.txt -i https://mirror.baidu.com/pypi/simple
```


# 安装GPU版本：

```shell
python -m pip install -r requirements.txt -i https://mirror.baidu.com/pypi/simple
```

```shell
python -m pip uninstall onnxruntime
```

```shell
python -m pip install onnxruntime-gpu==1.11.0 -i https://mirror.baidu.com/pypi/simple
```

```shell
conda install cudatoolkit=11.3 cudnn --channel https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/Paddle/
```

# 代码介绍

1. `infer_camera.py`为调用摄像头识别演示程序
2. `infer_path.py`为使用图片路径识别演示程序
3. `infer_server.py`为提供Web识别接口程序

import onnxruntime as ort


# 检查是否支持GPU
def check_gpu_support():
    try:
        # 尝试创建一个支持GPU的会话
        sess = ort.InferenceSession(None, providers=['CUDAExecutionProvider'])
        print("GPU support available.")
    except ort.CapiException as e:
        print(f"GPU support not available: {e}")


check_gpu_support()

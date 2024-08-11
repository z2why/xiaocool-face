package com.cool.cos.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.cool.cos.client.CosConfig;
import com.cool.cos.service.ICosFileService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.TransferManager;
import domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class ICosFileServiceImpl implements ICosFileService {

    @Resource
    private COSClient cosClient;

    @Resource
    private CosConfig cosConfig;

//    // 复用下载对象
//    private TransferManager transferManager;
//
//    // bean 加载完成后执行
//    @PostConstruct
//    public void init() {
//        System.out.println("Bean 初始化成功");
//        // 多线程并发上传下载
//        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
//        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
//        ExecutorService threadPool = Executors.newFixedThreadPool(32);
//        transferManager = new TransferManager(cosClient, threadPool);
//    }

    /**
     * 下载对象
     *
     * @param key 唯一键
     * @return
     */
    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosConfig.getBucketName(), key);
        log.info("getObjectRequest====》"+getObjectRequest);
        return cosClient.getObject(getObjectRequest);
    }

    /**
     * 下载对象到本地文件
     *
     * @param key
     * @return
     */
    public Map<String, Object> download(String key)  {
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(cosConfig.getBucketName(), key);
        req.setExpiration(expiration);
        URL url = cosClient.generatePresignedUrl(req);
        Map<String, Object> response = new HashMap<>();
        response.put("url", url.toString());
        return response;
    }






    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object upload(MultipartFile[] files, String folder) {
        String res = "";
        try {
            for (MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename();
                // 获得文件流
                InputStream inputStream = file.getInputStream();
                // 设置文件路径
                String filePath = getFilePath(originalFileName, folder);
                // 上传文件
                String bucketName = cosConfig.getBucketName();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, inputStream, objectMetadata);
                cosClient.putObject(putObjectRequest);
                cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
                String url = cosConfig.getPath() + "/" + filePath;
                res += url + ",";
            }
            return res.substring(0, res.length() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }





    // 在应用程序关闭时调用 cosClient.shutdown()
    public void shutdownCosClient() {
        if (cosClient != null) {
            cosClient.shutdown();
        }

    }

    @PreDestroy
    public void onDestroy() {
        shutdownCosClient();
    }


    @Override
    public Object delete(String folder) {
        cosConfig.cosClient();
        // 文件桶内路径
        cosClient.deleteObject(cosConfig.getBucketName(), folder);
        return "删除成功";
    }

    /**
     * 生成文件路径
     * @param originalFileName 原始文件名称
     * @param folder 存储路径
     * @return
     */
    private String getFilePath(String originalFileName, String folder) {
        // 获取后缀名
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 以文件后缀来存储在存储桶中生成文件夹方便管理
        String filePath = folder + "/";
        // 去除文件后缀 替换所有特殊字符
        String fileStr = StrUtil.removeSuffix(originalFileName, fileType).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
        filePath += new DateTime().toString("yyyyMMddHHmmss") + "_" + fileStr + fileType;
        return filePath;
    }
    /**
     * 生成文件路径
     * @param originalFileName 原始文件名称
     * @param folder 存储路径
     * @return
     */
    private String getDelFilePath(String originalFileName, String folder) {
        // 获取后缀名
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 以文件后缀来存储在存储桶中生成文件夹方便管理
        String filePath = folder + "/";
        // 去除文件后缀 替换所有特殊字符
        String fileStr = StrUtil.removeSuffix(originalFileName, fileType).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
        filePath += fileStr + fileType;
        return filePath;
    }


    /**
     * 删除目录
     *
     * @param delPrefix
     * @throws CosClientException
     * @throws CosServiceException
     */
    public Result deleteDir(String delPrefix) throws CosClientException, CosServiceException {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置 bucket 名称
        listObjectsRequest.setBucketName(cosConfig.getBucketName());
        // prefix 表示列出的对象名以 prefix 为前缀
        // 这里填要列出的目录的相对 bucket 的路径
        listObjectsRequest.setPrefix(delPrefix);
        // 设置最大遍历出多少个对象, 一次 listobject 最大支持1000
        listObjectsRequest.setMaxKeys(1000);

        // 保存每次列出的结果
        ObjectListing objectListing = null;

        do {
            objectListing = cosClient.listObjects(listObjectsRequest);

            // 这里保存列出的对象列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            if (cosObjectSummaries.isEmpty()) {
                break;
            }
            ArrayList<DeleteObjectsRequest.KeyVersion> delObjects = new ArrayList<DeleteObjectsRequest.KeyVersion>();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                delObjects.add(new DeleteObjectsRequest.KeyVersion(cosObjectSummary.getKey()));
            }

            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosConfig.getBucketName());
            deleteObjectsRequest.setKeys(delObjects);
            cosClient.deleteObjects(deleteObjectsRequest);

            // 标记下一次开始的位置
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());

        return Result.success("删除成功");
    }




//
@Override
public Map<String, Object> listFilesInBucket(String folder, String nextMarker) {
    COSClient cosClient = cosConfig.cosClient();

    // 创建一个 ListObjectsRequest 对象
    ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
    // 设置存储桶名称
    listObjectsRequest.setBucketName(cosConfig.getBucketName());
    // 设置前缀
    listObjectsRequest.setPrefix(folder);
    // delimiter 表示目录的截断符
    listObjectsRequest.setDelimiter("");
    // 设置最大遍历出多少个对象, 在此设置为100
    listObjectsRequest.setMaxKeys(100);

    // 如果 nextMarker 不为空，则设置 marker
    if (nextMarker != null && !nextMarker.isEmpty()) {
        listObjectsRequest.setMarker(nextMarker);
    }

    // 列出对象
    ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);

    // 获取对象列表
    List<COSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
    Map<String, Object> result = new HashMap<>();

    TreeNode treeRoot = buildTree(objectSummaries);
    Map<String, Object> treeMap = treeToMap(treeRoot, "");
    result.put("list", treeMap);

    // 获取下一次开始的位置
    if (objectListing.isTruncated()) {
        nextMarker = objectListing.getNextMarker();
    } else {
        nextMarker = null; // 如果没有更多数据，则设置为 null
    }
    result.put("nextMarker", nextMarker);

    return result;
}

    public static class TreeNode {
        String name;
        boolean isFile;
        Long size;
        List<TreeNode> children;
        String path; // 添加路径属性
        int id;

        public TreeNode(String name, boolean isFile, Long size, String path,int id) {
            this.name = name;
            this.isFile = isFile;
            this.size = size;
            this.children = new ArrayList<>();
            this.path = path; // 初始化路径
            this.id=id;
        }
    }

    public TreeNode buildTree(List<COSObjectSummary> objectSummaries) {
        TreeNode root = new TreeNode("", false, null, "",0); // 根节点
        int id=0;
        for (COSObjectSummary summary : objectSummaries) {
            String key = summary.getKey();
            long size = summary.getSize();
            String[] parts = key.split("/");
            id++;
            TreeNode currentNode = root;
            String currentPath = "";
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                boolean isFile = (i == parts.length - 1) && !key.endsWith("/");
                currentPath = currentPath.isEmpty() ? part : currentPath + "/" + part;

                TreeNode childNode = findChild(currentNode, part, isFile);
                if (childNode == null) {
                    childNode = new TreeNode(part, isFile, isFile ? size : null, currentPath,id);
                    currentNode.children.add(childNode);
                }

                currentNode = childNode;
            }
        }

        return root;
    }

    private TreeNode findChild(TreeNode node, String name, boolean isFile) {
        for (TreeNode child : node.children) {
            if (child.name.equals(name) && child.isFile == isFile) {
                return child;
            }
        }
        return null;
    }


    public Result uploadFileToSimulatedFolder(String folderPath) {
        String key = folderPath + "/" ; // 构造包含“文件夹”路径前缀的键名
        // 使用空字节数组创建一个空对象
        byte[] content = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
            PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucketName(), key,inputStream,null);
        // （可选）设置其他请求参数，如ACL、存储类、内容类型等
        PutObjectResult result= cosClient.putObject(putObjectRequest); // 上传对象到COS存储桶
        return Result.success(result);
    }

    public Map<String, Object> treeToMap(TreeNode node, String parentPath) {

        Map<String, Object> map = new HashMap<>();
        map.put("name", node.name);
        map.put("isFile", node.isFile);
        map.put("path", node.path); // 添加路径属性
        map.put("id",node.id);
        if (!node.isFile) {
            List<Map<String, Object>> children = new ArrayList<>();
            for (TreeNode child : node.children) {
                children.add(treeToMap(child, node.path));
            }
            map.put("children", children);
            map.put("isOpen",false);
        } else {
            // 将字节数除以1024得到KB
            Long kb = node.size / 1024;
            map.put("size", kb);
            String type = FileNameUtil.extName(node.name);
            switch (type) {
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                case "bmp":
                case "tiff":
                case "webp":
                case "svg":
                    type = "photo";
                    break;
                case "txt":
                case "log":
                case "ini":
                case "csv":
                case "md":
                    type = "txt";
                    break;
                case "pdf":
                    type = "pdf";
                    break;
                case "docx":
                case "doc":
                    type = "word";
                    break;
                case "xlsx":
                case "xls":
                    type = "xls";
                    break;
                case "pptx":
                case "ppt":
                    type = "ppt";
                    break;
                case "mp3":
                case "wav":
                case "flac":
                case "ogg":
                    type = "mp3";
                    break;
                case "mp4":
                case "avi":
                case "mov":
                case "wmv":
                    type = "mp4";
                    break;
                case "zip":
                case "rar":
                case "tar":
                case "gz":
                case "7z":
                    type = "zip";
                    break;
                case "exe":
                    type = "exe";
                    break;
                // 可以继续添加其他常见的文件类型
                default:
                    type = "unknown";
                    break;
            }
            map.put("type", type);
        }

        return map;
    }


}

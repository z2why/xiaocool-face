package com.cool.cos.service;

import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import domain.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ICosFileService {
    /**
     * 上传
     * @param files
     * @return
     */
    Object upload(MultipartFile[] files,String folder);

    /**
     * 删除
     * @param folder
     * @return
     */
    Object delete(String folder);


    Map<String, Object> listFilesInBucket(String folder, String nextMarker);


    Map<String, Object> download(String key);

    Result deleteDir(String delPrefix) throws CosClientException;

    Result uploadFileToSimulatedFolder(String folderPath);
}

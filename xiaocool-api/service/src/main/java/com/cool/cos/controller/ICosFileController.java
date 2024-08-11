package com.cool.cos.controller;

import com.cool.cos.service.ICosFileService;
import domain.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/cos")
public class ICosFileController {

    @Autowired
    private ICosFileService iCosFileService;

    @ApiOperation(value = "文件上传", httpMethod = "POST")
    @PostMapping("/upload")
    public Result upload(@RequestParam("files") MultipartFile[] files,@RequestParam String folder) {
        return Result.success(iCosFileService.upload(files, folder));
    }

    @ApiOperation(value = "文件删除", httpMethod = "GET")
    @GetMapping("/delete")
    public Result delete(@RequestParam("folder") String folder) {
        return Result.success(iCosFileService.delete(folder));
    }

    @ApiOperation(value = "查看文件列表", httpMethod = "GET")
    @GetMapping("/list")
    public Result listFiles(@RequestParam("folder") String folder,@RequestParam("nextMarker") String nextMarker) {
        return Result.success(iCosFileService.listFilesInBucket(folder,nextMarker));
    }



    /**
     * 测试文件下载
     *
     * @param key 文件名
     * @return
     */
    @ApiOperation(value = "下载文件", httpMethod = "GET")
    @GetMapping("/file/download")
    public Result download(@RequestParam("fileName")String key){
        return Result.success(iCosFileService.download(key));
    }

    @ApiOperation(value = "删除目录", httpMethod = "GET")
    @GetMapping("/deleteFolder")
    public Result deleteFolder(@RequestParam("folder") String folder) {
        return Result.success(iCosFileService.deleteDir(folder));
    }


    @ApiOperation(value = "添加文件夹", httpMethod = "GET")
    @GetMapping("/uploadFile")
    public Result uploadFile(@RequestParam("folder") String folder) {
        return Result.success(iCosFileService.uploadFileToSimulatedFolder(folder));
    }
}
package com.icplatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.entity.Assets;
import com.icplatform.service.AssetsService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icplatform.dto.FileUploadResponse; // 导入你的响应类

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetsController {

    @Value("${files.upload.path}")
    private String uploadPath;

    @Autowired
    private AssetsService assetsService;

    // 上传文件并更新数据库的方法
    @PostMapping("/upload")
    public FileUploadResponse uploadFile(@RequestHeader Map<String, String> header, @RequestParam MultipartFile file) throws IOException {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 1){

            String originalFilename = file.getOriginalFilename();
            long fileSize = file.getSize();
            String fileType = file.getContentType();  // 获取文件类型
            Date currentTime = new Date(System.currentTimeMillis());  // 获取当前时间作为 Date 对象

            File uploadParentFile = new File(uploadPath);
            if (!uploadParentFile.exists()) {
                uploadParentFile.mkdirs();  // 创建目录
            }

            File uploadFile = new File(uploadPath + File.separator + originalFilename);

            if (uploadFile.exists()) {
                uploadFile.delete();  // 如果存在则删除旧文件
            }

            file.transferTo(uploadFile);  // 保存新文件

            // 检查数据库中是否有相同的 fname
            try {
                // 如果存在相同的 fname，则更新对应记录
                assetsService.updateAssetByFname(originalFilename, fileType, fileSize, uploadFile.getAbsolutePath(), currentTime);
            } catch (IllegalArgumentException e) {
                // 如果不存在相同的 fname，则插入新记录
                assetsService.insertNewAsset(originalFilename, fileType, fileSize, uploadFile.getAbsolutePath(), currentTime);
            }

            String newToken = JWTUtil.generateToken(userType,username);

            return new FileUploadResponse("上传成功",newToken);

        }else{
            return new FileUploadResponse("上传失败",null);
        }
    }



    // 遍历文件夹并返回目录结构
    @GetMapping("/catalogue")
    public Map<String, Object> getCatalogue(@RequestHeader Map<String, String> header, @RequestParam String path) {
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 0){
            List<FileNode> folderStructure = getFolderStructure(path);
            String newToken = JWTUtil.generateToken(userType,username);

            Map<String,Object> response = new HashMap<>();
            response.put("folderStructure", folderStructure);
            response.put("status","success");
            response.put("newToken", newToken);
            return response;
        }else{
            return null;
        }
    }

    // 定义文件和文件夹的结构类
    public static class FileNode {
        private String label;  // 文件/文件夹名称
        private String type;    // "directory" or "file"
        private List<FileNode> children;  // 子节点
        private String path;    // 文件的完整路径

        // Constructors, getters and setters
        public FileNode() {}

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<FileNode> getChildren() {
            return children;
        }

        public void setChildren(List<FileNode> children) {
            this.children = children;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }


    // 获取文件夹结构
    public List<FileNode> getFolderStructure(String folderPath) {
        File folder = new File(folderPath);
        List<FileNode> nodes = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            collectFiles(folder, nodes);
        }
        return nodes;
    }

    // 递归遍历文件夹并构建目录结构
    private void collectFiles(File folder, List<FileNode> nodes) {
        for (File file : folder.listFiles()) {
            FileNode node = new FileNode();
            node.setLabel(file.getName());
            // 设置文件的路径并替换反斜杠为正斜杠
            String path = file.getPath().replace("\\", "/");
            node.setPath(path);

            node.setType(file.isDirectory() ? "directory" : "file");
            if (file.isDirectory()) {
                node.setChildren(new ArrayList<>());
                collectFiles(file, node.getChildren());
            }
            nodes.add(node);
        }
    }

    // 下载资源
    @GetMapping("/downloads")
    public ResponseEntity<Resource> downloadFile(@RequestHeader Map<String,String> header,@RequestParam String filePath) {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType ==0 ){
            System.out.println("收到下载请求，文件路径: " + filePath); // 输出接收到的文件路径

            try {
                File file = new File(filePath);

                if (!file.exists()) {
                    System.out.println("文件未找到: " + filePath); // 输出文件未找到的信息
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }

                // 获取文件的MIME类型
                String mimeType = Files.probeContentType(Paths.get(filePath));
                Resource resource = new FileSystemResource(file);
                System.out.println("准备返回文件: " + file.getName() + ", MIME类型: " + mimeType); // 输出准备返回的文件信息

                // 如果无法确定MIME类型，则设置为默认类型
                if (mimeType == null) {
                    mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }

                // 对文件名进行URL编码处理，防止非ASCII字符引发异常
                String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString()).replace("+", "%20");

                // 构建响应头
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "attachment; filename*=UTF-8''" + encodedFileName);


                // 返回文件作为下载资源
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(file.length())
                        .contentType(MediaType.parseMediaType(mimeType))
                        .body(resource);

            } catch (IOException e) {
                // 如果发生错误，返回服务器内部错误状态
                System.out.println("发生错误: " + e.getMessage()); // 输出错误信息
                System.out.println(1);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        return null;
    }

//    @GetMapping("/download")
//    public ResponseEntity<Map<String, String>> generateDownloadLink(@RequestHeader Map<String,String > header,@RequestParam String fileName) {
//
//        String token = header.get("token");
//        DecodedJWT decodedJWT;
//        try {
//            decodedJWT = JWTUtil.verifyToken(token);
//        }catch (Exception e){
//            return null;
//        }
//        String username = decodedJWT.getClaim("username").asString();
//        int userType = decodedJWT.getClaim("usertype").asInt();
//
//        if(userType ==0 ){
//            Map<String, String> response = new HashMap<>();
//
//            // 在 assets 表中根据文件名查找路径
//            String filePath = assetsService.searchTpathByFname(fileName);
//
//            if (filePath == null) {
//                response.put("status", "error");
//                response.put("message", "文件未找到");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//            }
//
//            // 生成下载链接
//            String downloadUrl = "/api/assets/download?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
//            response.put("status", "success");
//            response.put("downloadUrl", downloadUrl);
//            return ResponseEntity.ok(response);
//        }
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "error");
//        return response;
//    }
}

package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.entity.Course;
import com.icplatform.entity.Homework;
import com.icplatform.entity.SC;
import com.icplatform.entity.Teacher;
import com.icplatform.service.*;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SCService scService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("")
    public Map<String, Object> courseList(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> sno) {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "token超时");
            response.put("status", "error");
            return response;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        String sno_a = sno.get("sno");
        System.out.println(sno_a);

        if(userType == 0){
            List<SC> scList = scService.searchBySno(sno_a);
            if(!scList.isEmpty()){

                List<Map<String,Object>> courseList = new ArrayList<>();

                for(SC sc : scList){
                    String cid = sc.getCid();
                    String cno = sc.getCno();
                    Course course = courseService.findByCidAndCno(cid,cno);
                    if(course != null){
                        Map<String ,Object> response = new HashMap<>();
                        response.put("cid",cid);
                        response.put("cno",cno);
                        response.put("cname",course.getCname());

                        courseList.add(response);
                    }
                }

                String newToken = JWTUtil.generateToken(userType,username);

                // 返回结果列表
                Map<String, Object> result = new HashMap<>();
                result.put("courseList", courseList);
                result.put("newToken", newToken);
                result.put("status","success");
                return result;
            }else{
                // 没有找到任何 SC 记录
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "学生本学期无选课信息");
                errorResponse.put("status","error");
                return errorResponse;
            }
        }else{
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "用户类型错误");
            errorResponse.put("status","error");
            return errorResponse;
        }
    }
    //课程信息
    @PostMapping("/info")
    public Map<String,Object> CourseInformation(@RequestHeader Map<String,String> header,@RequestParam String cid,@RequestParam String cno){
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0) {
            // 根据cid和cno查询课程信息
            Course course = courseService.findByCidAndCno(cid, cno);
            if (course != null) {
                // 获取课程的tno
                String tno = course.getTno();

                // 根据tno查询教师信息
                Teacher teacher = teacherService.searchByTno(tno);
                if (teacher != null) {
                    // 组装返回数据
                    Map<String, Object> courseInfo = new HashMap<>();
                    courseInfo.put("cid", course.getCid());
                    courseInfo.put("cno", course.getCno());
                    courseInfo.put("cname", course.getCname());
                    courseInfo.put("description",course.getDescription());

                    // 添加教师信息
                    Map<String, Object> teacherInfo = new HashMap<>();
                    teacherInfo.put("tname", teacher.getTname());
                    teacherInfo.put("temail", teacher.getTemail());
                    teacherInfo.put("gender", teacher.getGender());
                    teacherInfo.put("major", teacher.getMajor());
                    teacherInfo.put("title", teacher.getTitle());

                    courseInfo.put("teacherInfo", teacherInfo);

                    // 生成新token
                    String newToken = JWTUtil.generateToken(userType, username);

                    // 返回结果
                    Map<String, Object> result = new HashMap<>();
                    result.put("status","success");
                    result.put("courseInfo", courseInfo);
                    result.put("newToken", newToken);

                    return result;
                } else {
                    // 没有找到教师信息
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "未找到对应教师信息");
                    return errorResponse;
                }
            } else {
                // 没有找到课程信息
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "未找到课程信息");
                return errorResponse;
            }
        } else {
            // 用户类型错误
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "用户类型错误");
            return errorResponse;
        }
    }

    //课程作业信息
    @PostMapping("/homework/display")
    public Map<String,Object> CourseHomework(@RequestHeader Map<String,String> header,@RequestBody Map<String,String> homeworkDate){//cid,sno
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        System.out.println("Received homeworkDate: " + homeworkDate);

        if (userType == 0) {
            String cid = homeworkDate.get("cid");
            String sno = homeworkDate.get("sno");

            if(cid != null && sno != null){

                int totalStudents = scService.countStudentsByCid(cid);

                List<Homework> homeworkList = homeworkService.searchByCidAndSno(cid,sno);
                if(homeworkList != null && homeworkList.size() > 0){
                    List<Map<String, Object>> homeworkInfoList = new ArrayList<>();

                    for(Homework homework : homeworkList){

                        int submitStudents = homeworkService.countSubmittedByCidAndWorkId(cid, homework.getWorkid());

                        String submitRatio = submitStudents + "/" + totalStudents;

                        Map<String, Object> homeworkInfo = new HashMap<>();
                        homeworkInfo.put("homeworkName",homework.getHomework_name());
                        homeworkInfo.put("start",homework.getStart());
                        homeworkInfo.put("end",homework.getEnd());
                        homeworkInfo.put("submitTime",homework.getSubmit_time());
                        homeworkInfo.put("submitRatio",submitRatio);
                        homeworkInfo.put("score",homework.getScore());
                        homeworkInfo.put("reviestatus",homework.getReviestatus());

                        homeworkInfoList.add(homeworkInfo);
                    }

                    // 返回作业信息和新token
                    String newToken = JWTUtil.generateToken(userType, username);
                    Map<String, Object> result = new HashMap<>();
                    result.put("homeworkInfoList", homeworkInfoList);
                    result.put("newToken", newToken);
                    result.put("status", "success");
                    return result;
                }else{
                    // 没有找到作业记录
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "未找到作业记录");
                    errorResponse.put("status", "error");
                    return errorResponse;
                }
            }
        }
        return null;
    }

    //课程作业下载
    @GetMapping("/homework/download")
    public ResponseEntity<Resource> downloadHomework(@RequestHeader Map<String,String> header, @RequestParam String cid,@RequestParam String sno,@RequestParam String workid) {//cid,sno,workid

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType ==0){


        Integer workId = Integer.valueOf(workid);

        System.out.println("收到下载请求，CID: " + cid + ", 学号: " + sno + ", 作业ID: " + workid); // 输出接收到的参数

            if(cid != null && sno != null && workid != null){
                Homework homework = homeworkService.findByCidSnoAndWorkid(cid,sno,workId);

                 if(homework != null){
                   String homeworkPath = homework.getPath();
                   Resource resource = resourceLoader.getResource(homeworkPath);

                   String newToken = JWTUtil.generateToken(userType, username);

                     HttpHeaders headers = new HttpHeaders();
                     headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + homework.getHomework_name());
                     headers.add("newToken", newToken);
                     headers.add("status", "success");

                     return ResponseEntity.ok()
                             .headers(headers)
                             .body(resource);
                 }
            }
        }
    return null;
    }
}



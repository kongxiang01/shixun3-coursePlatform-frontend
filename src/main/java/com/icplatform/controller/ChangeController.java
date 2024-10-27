package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.entity.Student;
import com.icplatform.entity.Teacher;
import com.icplatform.service.StudentService;
import com.icplatform.service.TeacherService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/change")
@CrossOrigin(origins = "*")
public class ChangeController {

    @Autowired
    public StudentService studentService;

    @Autowired
    public TeacherService teacherService;

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> changeData(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> changeData) {

        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        if(token == null){
            response.put("message", "修改密码失败，token被清空或已过期");
            response.put("status", "error");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 0){
            Student student = studentService.searchBySno(username);
            if(student != null){
                String password = changeData.get("password");
                if(password !=null){
                    student.setPassword(password);
                }
                studentService.save(student);
            }
        }else if(userType == 1){
            Teacher teacher = teacherService.searchByTno(username);
            if(teacher != null){
                String password = changeData.get("password");
                if(password !=null){
                    teacher.setPassword(password);
                }
                teacherService.save(teacher);
            }
        }

        response.put("message", "成功修改密码!");
        response.put("status", "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

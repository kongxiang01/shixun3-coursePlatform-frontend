package com.icplatform.service;

import com.icplatform.entity.Homework;
import com.icplatform.repositories.HomeworkRepositories;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class HomeworkService {
    private final HomeworkRepositories homeworkRepositories;

    public HomeworkService(HomeworkRepositories homeworkRepositories) {
        this.homeworkRepositories = homeworkRepositories;
    }

    public List<Homework> searchByCidAndSno(String cid, String sno) {
        return homeworkRepositories.findByCidAndSno(cid, sno);
    }

    public int countHomeworkByCid(String cid) {
        return homeworkRepositories.countByCid(cid);
    }

    // 根据 cid 和 workid 统计已提交的作业数量
    public int countSubmittedByCidAndWorkId(String cid, int workid) {
        return homeworkRepositories.countSubmittedByCidAndWorkId(cid, workid);
    }

    public List<Object[]> countHomeworkByWorkId(String cid) {
        return homeworkRepositories.countHomeworkByWorkId(cid);
    }

    public Homework findByCidSnoAndWorkid(String cid, String sno, int workid) {
        return homeworkRepositories.findByCidAndSnoAndWorkid(cid, sno, workid);
    }

    public List<Homework> findByCidAndWorkid(String cid, int workid) {
        return homeworkRepositories.findByCidAndWorkid(cid, workid);
    }

    public Resource getHomeworkFile(String cid, int workid, String sno){
        Homework homework = homeworkRepositories.findByCidAndSnoAndWorkid(cid,sno,workid);

        if (homework != null) {
            try {
                Path filePath = Paths.get(homework.getPath());
                return new UrlResource(filePath.toUri());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

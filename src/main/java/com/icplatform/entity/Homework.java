package com.icplatform.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Homework {

    @Id
    private int id;

    private String homework_name;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime submit_time;
    private Integer score;
    private String path;
    private String sno;
    private String cno;
    private String cid;
    private int workid;
    private String reviestatus;

    public Homework() {}

    public Homework(int id, String homework_name, LocalDateTime start, LocalDateTime end, LocalDateTime submit_time, Integer score,
                    String path, String sno, String cno, String cid, int workid, String reviestatus) {
        this.id = id;
        this.homework_name = homework_name;
        this.start = start;
        this.end = end;
        this.submit_time = submit_time;
        this.score = score;
        this.path = path;
        this.sno = sno;
        this.cno = cno;
        this.cid = cid;
        this.workid = workid;
        this.reviestatus = reviestatus;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getHomework_name() { return homework_name; }
    public void setHomework_name(String homework_name) { this.homework_name = homework_name; }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }

    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public LocalDateTime getSubmit_time() { return submit_time; }
    public void setSubmit_time(LocalDateTime submit_time) { this.submit_time = submit_time; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getSno() { return sno; }
    public void setSno(String sno) { this.sno = sno; }

    public String getCno() { return cno; }
    public void setCno(String cno) { this.cno = cno; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }

    public int getWorkid() { return workid; }
    public void setWorkid(int workid) { this.workid = workid; }

    public String getReviestatus() { return reviestatus; }
    public void setReviestatus(String reviestatus) { this.reviestatus = reviestatus; }
}

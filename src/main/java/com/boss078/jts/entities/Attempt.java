package com.boss078.jts.entities;

import javax.persistence.*;

@Entity
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    private String code;

    private String result;

    private int errorCode;

    private String log = "";

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getResult() { return result; }

    public void setResult(String result) { this.result = result; }

    public void addResult(String result) { this.result += result; }

    public void addLog(String log) { this.log += log; }

    public String getLog() { return log; }

    public void setTask(Task task) { this.task = task; }

    public Task getTask() { return task; }

    public int getErrorCode() { return errorCode; }

    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }
}
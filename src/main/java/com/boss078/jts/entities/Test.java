package com.boss078.jts.entities;

import javax.persistence.*;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    private String in;

    private String out;

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    public String getIn() { return in; }

    public void setIn(String in) { this.in = in; }

    public String getOut() { return out; }

    public void setOut(String out) { this.out = out; }
}

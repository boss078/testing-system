package com.boss078.jts.entities;

import javax.persistence.*;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column
    @Lob
    private String input;

    @Column
    @Lob
    private String output;

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    public String getInput() { return input; }

    public void setInput(String input) { this.input = input; }

    public String getOutput() { return output; }

    public void setOutput(String output) { this.output = output; }
}

package com.boss078.jts.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task", cascade={CascadeType.ALL})
    private List<Test> tests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task", cascade={CascadeType.ALL})
    private List<Attempt> attempts;

    private String label;

    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public List<Test> getTests() { return tests; }

    public void addTest(Test test) { tests.add(test); }

    public void removeTest(Test test) { tests.remove(test); }

    public List<Attempt> getAttempts() { return attempts; }

    public void addAttempt(Attempt attempt) { attempts.add(attempt); }

    public void removeAttempt(Attempt attempt) { attempts.remove(attempt); }
}

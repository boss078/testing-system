package com.boss078.jts.repositories;

import com.boss078.jts.entities.Task;
import com.boss078.jts.entities.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Integer> {
    List<Test> findAllByTask(Task task);
}

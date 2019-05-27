package com.boss078.jts.repositories;

import com.boss078.jts.entities.Attempt;
import com.boss078.jts.entities.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttemptRepository extends CrudRepository<Attempt, Integer> {
    List<Attempt> findAllByTask(Task task);
}
package com.boss078.jts.repositories;

import com.boss078.jts.entities.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Integer> {

}

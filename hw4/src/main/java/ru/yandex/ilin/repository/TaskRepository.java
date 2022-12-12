package ru.yandex.ilin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.ilin.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findAllByBoard_BoardIdAndStatusIs(String boardId, Task.TaskStatus status);

    @Modifying
    @Transactional
    @Query("update task set status = 'DONE' where taskId = :taskId")
    void markTaskDone(@Param("taskId") String taskId);
}

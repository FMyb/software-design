package ru.yandex.ilin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.ilin.dto.CreateTaskDto;
import ru.yandex.ilin.model.Board;
import ru.yandex.ilin.model.Task;
import ru.yandex.ilin.repository.BoardRepository;
import ru.yandex.ilin.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(value = "tasks")
public class TaskController {
    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;

    public TaskController(BoardRepository boardRepository, TaskRepository taskRepository) {
        this.boardRepository = boardRepository;
        this.taskRepository = taskRepository;
    }

    @RequestMapping(value = "/{board_id}", method = RequestMethod.POST)
    public String addTask(
        @PathVariable("board_id") String boardId,
        @ModelAttribute("task") CreateTaskDto createTaskDto
    ) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            throw new IllegalArgumentException("board not found");
        }
        Task task = new Task(
            UUID.randomUUID().toString(),
            createTaskDto.name(),
            createTaskDto.description(),
            Task.TaskStatus.NOT_DONE,
            board.get()
        );
        taskRepository.save(task);
        return "redirect:/tasks/" + boardId;
    }

    @RequestMapping(value = "/{board_id}", method = RequestMethod.GET)
    public String getTasks(@PathVariable("board_id") String boardId, ModelMap modelMap) {
        List<Task> tasks = taskRepository.findAllByBoard_BoardIdAndStatusIs(boardId, Task.TaskStatus.NOT_DONE);
        addTasksToModelMap(modelMap, tasks, boardId);
        return "tasks";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String markTaskDone(@PathVariable String id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            throw new IllegalArgumentException("task not found");
        }
        Task task = taskOpt.get();
        taskRepository.markTaskDone(task.getTaskId());
        return "redirect:/tasks/" + task.getBoard().getBoardId();
    }

    private void addTasksToModelMap(ModelMap map, List<Task> tasks, String boardId) {
        map.addAttribute("tasks", tasks);
        map.addAttribute("task_create", new CreateTaskDto("", ""));
        map.addAttribute("board_id", boardId);
    }
}

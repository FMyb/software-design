package ru.yandex.ilin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "board")
@Table(name = "boards")
public class Board {
    @Id
    private String boardId;

    @Column
    private String name;

    @OneToMany(mappedBy = "board")
    private Set<Task> tasks;

    public Board() {
    }

    public Board(String boardId, String name, Set<Task> tasks) {
        this.boardId = boardId;
        this.name = name;
        this.tasks = tasks;
    }

    public Board(String boardId, String name) {
        this.boardId = boardId;
        this.name = name;
        this.tasks = new HashSet<>();
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}

package ru.yandex.ilin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ilin.model.Board;

public interface BoardRepository extends JpaRepository<Board, String> {
}

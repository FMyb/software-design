package ru.yandex.ilin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.ilin.dto.CreateBoardDto;
import ru.yandex.ilin.model.Board;
import ru.yandex.ilin.repository.BoardRepository;

import java.util.List;
import java.util.UUID;

@Controller
public class BoardController {
    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {this.boardRepository = boardRepository;}

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String allBoards(ModelMap modelMap) {
        List<Board> boards = boardRepository.findAll();
        addBoardsToModelMap(modelMap, boards);
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createBoard(@ModelAttribute("board") CreateBoardDto createBoardDto) {
        Board board = new Board(UUID.randomUUID().toString(), createBoardDto.name());
        boardRepository.save(board);
        return "redirect:/";
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public String deleteBoard(@RequestParam("board_id") String boardId) {
        boardRepository.deleteById(boardId);
        return "redirect:/";
    }

    private void addBoardsToModelMap(ModelMap modelMap, List<Board> boards) {
        modelMap.addAttribute("boards", boards);
        modelMap.addAttribute("board_create", new CreateBoardDto(""));
    }
}

package server.api.Board;

import commons.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final SimpMessagingTemplate msg;

    @Autowired
    public BoardController(BoardService boardService, SimpMessagingTemplate msg) {
        this.boardService = boardService;
        this.msg = msg;
    }

    /**
     * Retrieves all boards from the repository
     */
    @GetMapping({" ","/get-all"})
    public Result<List<Board>> getAllBoards(){
        return boardService.getAllBoards();
    }


    /**
     * Retrieves specific the Board from the repository
     */
    @GetMapping({"/get/{id}"})
    public Result<Board> getBoard(@PathVariable UUID id){
        return boardService.getBoardById(id);
    }

    /**
     * Retrieves specific the Board from the repository
     */
    @PostMapping({"/create/"})
    public Result<Board> createBoard(@RequestBody Board board){
        System.out.println("Created a board with the id \t" + board.getBoardID());
        var result = boardService.addNewBoard(board);
        msg.convertAndSend("/topic/update-overview/", board.getBoardID());
        return result;
    }

    /**
     * @param board the board to update
     * @param id  the id of the board to update
     * @return the updated board
     */
    @PutMapping({"/update/{id}"})
    public Result<Board> updateBoard(@RequestBody Board board, @PathVariable UUID id){
        System.out.println("updating board");
        var result = boardService.updateBoard(board, id);
        msg.convertAndSend("/topic/update-board/", id);
        return result;
    }


    /**
     * Delete request to remove the Board with id {id} from the repository
     */
    @PutMapping("/delete/{id}")
    public Result<Object> deleteBoard(@PathVariable UUID id) {
        System.out.println("Deleted a board with the id \t" + id);
        var result = boardService.deleteBoard(id);
        msg.convertAndSend("/topic/update-overview/", id);
        return result;
    }

    /**
     * Put request to update the theme of a board with id {id}
     */
    @PutMapping("/update-theme/{id}")
    public Result<Board> updateBoardTheme(@PathVariable UUID id, @RequestBody Theme theme){
        var result = boardService.updateBoardTheme(id, theme);
        msg.convertAndSend("/topic/update-board/", id);
        return result;
    }


    /**
     * Adds the tag in the request body
     * to the card with given id
     */
    @PutMapping("/add-tag/{boardId}")
    public Result<Board> addTagToBoard(@RequestBody Tag tag, @PathVariable UUID boardId){
        Result<Board> res =  boardService.addTagToBoard(tag, boardId);
        msg.convertAndSend("/topic/update-board/", boardId);
        return res;
    }

    /**
     * Adds the given list to board with id {id}
     */
    @PutMapping("/add-list/{id}")
    public Result<Board> addListToBoard(@RequestBody CardList list, @PathVariable UUID id){
        var result = boardService.updateBoardAddList(list);
        msg.convertAndSend("/topic/update-board/", id);
        return result;
    }
}
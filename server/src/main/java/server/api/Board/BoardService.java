package server.api.Board;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.api.Tag.TagService;
import server.database.BoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    private TagService tagService;

    @Autowired
    public BoardService(BoardRepository boardRepository, TagService tagService) {
        this.boardRepository = boardRepository;
        this.tagService = tagService;
    }

    /**
     * Retrieves all the Boards from the repository
     */
    public Result<List<Board>> getAllBoards(){
        try {
            return Result.SUCCESS.of(boardRepository.findAll());
        }catch (Exception e){
            return Result.FAILED_GET_ALL_BOARDS;
        }
    }

    /**
     * Retrieves the Board with the given id
     * @param id
     */
    public Result<Board> getBoardById(UUID id){
        try {
            Optional<Board> board = boardRepository.findById(id);
            if (board.isPresent()) {
                return Result.SUCCESS.of(board.get());
            } else {
                return Result.FAILED_RETRIEVE_BOARD_BY_ID.of(null);
            }
        }catch (Exception e){
            return Result.FAILED_RETRIEVE_BOARD_BY_ID.of(null);
        }
    }

    /**
     * Adds the Board to the repository
     * @param board
     */
    public Result<Board> addNewBoard(Board board){
        if (board == null || board.boardTitle == null) {
            return Result.OBJECT_ISNULL.of(null);
        }
        try {
            boardRepository.save(board);
            return Result.SUCCESS.of(board);
        }catch (Exception e){
            return Result.FAILED_ADD_NEW_BOARD.of(null);
        }
    }



    /**
     * Deletes the Board with the given id
     * @param boardId the id of the board to delete
     */
    public Result<Object> deleteBoard (UUID boardId) {
        try {
            boardRepository.deleteById(boardId);
            return Result.SUCCESS.of(null);
        } catch (Exception e){
            return Result.FAILED_DELETE_BOARD;
        }
    }

    /**
     * Updates the theme of the board with the given id.
     */
    public Result<Board> updateBoardTheme(UUID id, Theme theme){
        try {
            return Result.SUCCESS.of(boardRepository.findById(id)
                    .map(board -> {
                        board.setBoardTheme(theme);
                        boardRepository.save(board);
                        return board;
                    }).get());
        }catch (Exception e){
            return Result.FAILED_UPDATE_BOARD_THEME;
        }
    }


    /**
     * @param board the board to update
     * @param id the id of the board to update
     * @return the updated board
     * updates the board with the given id
     */
    public Result<Board> updateBoard(Board board, UUID id){
        try {
            return Result.SUCCESS.of(boardRepository.findById(id)
                    .map(b -> {
                        for (Tag tag :
                                board.tagList) {
                            tag.board = b;
                            tagService.updateTag(tag, tag.tagID);
                        }
                        return boardRepository.save(board);
                    }).get());
        }catch (Exception e){
            return Result.FAILED_UPDATE_BOARD;
        }
    }

    /**
     * @param board the board to update
     * @return the updated board
     * updates the board with the given id
     */
    public Result<Board> updateBoard(Board board){
        try {
            return Result.SUCCESS.of(boardRepository.save(board));
        }catch (Exception e){
            return Result.FAILED_UPDATE_BOARD;
        }
    }



    /** Adds a list to a board
     * @param list
     * @return Result<board> Result object containing Board
     */
    public Result<Board> updateBoardAddList(CardList list){
        try {
            return Result.SUCCESS.of(boardRepository.findById(list.boardId)
                    .map(board -> {
                        board.getCardListList().add(list);
                        boardRepository.save(board);
                        return board;
                    }).get());
        }catch (Exception e){
            return Result.FAILED_TO_ADD_LIST_TO_BOARD;
        }
    }
    /**
     * Deletes list
     * @param cardList
     * @return
     */
    public Result<Board> deleteList(CardList cardList) {
        try {
            return Result.SUCCESS.of(boardRepository.findById(cardList.boardId)
                    .map(b -> {
                        b.cardListList.remove(cardList);
                        boardRepository.save(b);
                        return b;
                    }).get());
        } catch (Exception e) {
            return Result.FAILED_UPDATE_BOARD.of(null);
        }
    }

    /**
     * Adds the given tag to the board with Id {id}
     */
    public Result<Board> addTagToBoard(Tag tag, UUID id){


        if(tag == null || id == null) return Result.OBJECT_ISNULL.of(null);
        try{
            Board board = boardRepository.findById(id).get();
            tag.board = board;
            tagService.createTag(tag);
            board.tagList.add(tag);
            boardRepository.save(board);
            return Result.SUCCESS.of(board);
        }
        catch (Exception e){
            return Result.FAILED_ADD_TAG_TO_BOARD;
        }
    }
}

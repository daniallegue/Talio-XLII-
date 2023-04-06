package server.api.List;

import commons.*;
import commons.utils.HardcodedIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.Board.BoardService;
import server.api.Card.CardService;
import server.database.ListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
class ListControllerTest {



    @Mock
    ListRepository listRepository;
    @Mock
    CardService cardService;
    @Mock
    BoardService boardService;

    ListService listService;
    @Mock
    SimpMessagingTemplate msg;
    @InjectMocks
    ListController listController;

    Card card1;
    CardList list1;

    Board board1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listService = new ListService(listRepository, cardService, boardService);
        listController = new ListController(listService, msg);

        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        //String boardTitle,
        //                 List<CardList> cardListList,
        //                 String description,
        //                 Boolean isProtected,
        //                 String passwordHash,
        //                 Theme boardTheme

        board1 = new Board(idGenerator1.generateID(), "Test Board", new ArrayList<>(),
                "Test Description", false, "Test Password", new Theme());
        list1 = new CardList(idGenerator1.generateID(), "Test List",
                new ArrayList<>(), new Board("Test Board", new ArrayList<>(),
                            "Test Description", false, "Test Password", new Theme()));
        card1 = new Card(idGenerator1.generateID(),list1, "Test Card", "pikachu is cute",
                new ArrayList<>(), new ArrayList<>());
        list1.addCard(card1);
    }

    @Test
    void getAllLists() {
        List<CardList> allLists = new ArrayList<>();
        allLists.add(list1);

        doReturn(allLists).when(listRepository).findAll();

        Result<List<CardList>> result = listController.getAllLists();
        assertEquals(Result.SUCCESS.of(allLists), result);
    }

    @Test
    void getListById() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doReturn(Optional.of(list1)).when(listRepository).findById(idGenerator1.generateID());

        Result<CardList> result = listController.getListById(idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(list1), result);
    }

    @Test
    void createNewList() {

        doReturn(list1).when(listRepository).save(list1);
        doReturn(Result.SUCCESS.of(board1)).when(boardService).updateBoardAddList(list1);

        Result<CardList> result = listController.createNewList(list1);
        assertEquals(Result.SUCCESS.of(list1), result);
    }

    @Test
    void createNewListFail() {
        doReturn(Result.FAILED_ADD_NEW_LIST).when(listRepository).save(list1);

        Result<CardList> result = listController.createNewList(list1);
        assertEquals(Result.FAILED_ADD_NEW_LIST, result);
    }

    @Test
    void deleteList() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doReturn(Optional.of(list1)).when(listRepository).findById(idGenerator1.generateID());

        Result<Object> result = listController.deleteList(idGenerator1.generateID(), list1);
        assertEquals(Result.SUCCESS.of(true), result);
    }

    @Test
    void updateName() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");

        doReturn(list1).when(listRepository).save(list1);
        doReturn(Optional.of(list1)).when(listRepository).findById(idGenerator1.generateID());

        Result<CardList> result = listController.updateName(list1,idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(list1), result);
    }

    @Test
    void removeCardFromList() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");

        doReturn(list1).when(listRepository).save(list1);
        doReturn(Optional.of(list1)).when(listRepository).findById(idGenerator1.generateID());

        Result<CardList> result = listController.removeCardFromList(card1, idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(list1), result);
    }

    @Test
    void addCardToList() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doReturn(Optional.of(list1)).when(listRepository).findById(idGenerator1.generateID());
        doReturn(Result.SUCCESS.of(card1)).when(cardService).addNewCard(card1);

        Result<Card> result = listController.addCardToList(card1, idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(card1), result);
    }

    @Test
    void moveCard() {
        HardcodedIDGenerator idGenerator2 = new HardcodedIDGenerator();
        idGenerator2.setHardcodedID("2");
        CardList listWithEmpyCardList = new CardList(idGenerator2.generateID(), "Test List",
                new ArrayList<>(), new Board());

        doReturn(Optional.of(list1)).when(listRepository).findById(list1.cardListId);
        doReturn(Optional.of(listWithEmpyCardList)).when(listRepository).findById(idGenerator2.generateID());


        Result<Card> result = listController.moveCard(card1, list1.cardListId, idGenerator2.generateID(), 0);
        assertEquals(Result.SUCCESS.of(card1), result);
    }

    @Test
    void moveCardFAIL() {
        HardcodedIDGenerator idGenerator2 = new HardcodedIDGenerator();
        idGenerator2.setHardcodedID("2");

//        doThrow(new RuntimeException()).when(listService).getListById(idGenerator2.generateID());
        doReturn(null).when(listRepository).findById(idGenerator2.generateID());

        Result<Card> result = listController.moveCard(card1, list1.cardListId, idGenerator2.generateID(), 0);
        assertEquals(Result.FAILED_MOVE_CARD.of(null), result);
    }
}
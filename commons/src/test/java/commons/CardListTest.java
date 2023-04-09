package commons;

import commons.utils.HardcodedIDGenerator;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    Board board;

    @Test
    void emptyConstructor() {
        CardList cardList = new CardList();
        assertNull(cardList.cardListId);
        assertNull(cardList.cardListTitle);
        assertNull(cardList.cardList);
        assertNull(cardList.boardId);
        assertNull(cardList.board);
    }

    void setup(){
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");
        board = new Board(idGenerator.generateID(), "BoardTitle", new ArrayList<>(), "Description", false, "PasswordHash", new Theme());
    }
    @Test
    void constructorOne() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");
        CardList cardList = new CardList(idGenerator.generateID(), "CardListTitle", new ArrayList<>(), null, board);
        assertEquals(idGenerator.generateID(),cardList.cardListId);
        assertEquals("CardListTitle",cardList.cardListTitle);
        assertEquals(new ArrayList<>(),cardList.cardList);
        assertNull(cardList.boardId);
        assertEquals(board,cardList.board);
    }
    @Test
    void constructorTwo() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");
        CardList cardList = new CardList(idGenerator.generateID(), "CardListTitle", new ArrayList<>(), board);
        assertEquals(idGenerator.generateID(),cardList.cardListId);
        assertEquals("CardListTitle",cardList.cardListTitle);
        assertEquals(new ArrayList<>(),cardList.cardList);
        assertEquals(board,cardList.board);
    }
    @Test
    void constructorThree() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>(), board);
        assertEquals("CardListTitle",cardList.cardListTitle);
        assertEquals(new ArrayList<>(),cardList.cardList);
        assertEquals(board,cardList.board);
    }
    @Test
    void constructorFour() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        assertEquals("CardListTitle",cardList.cardListTitle);
        assertEquals(new ArrayList<>(),cardList.cardList);
    }
    @Test
    void getCardListTitle() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        assertEquals("CardListTitle",cardList.getCardListTitle());
    }
    @Test
    void getCardListId() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");
        CardList cardList = new CardList(idGenerator.generateID(), "CardListTitle", new ArrayList<>(), new Board());
        assertEquals(idGenerator.generateID(),cardList.getCardListId());
    }
    @Test
    void setCardListId() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");
        CardList cardList = new CardList(idGenerator.generateID(), "CardListTitle", new ArrayList<>(), new Board());
        idGenerator.setHardcodedID("2");
        cardList.setCardListId(idGenerator.generateID());
        assertEquals(idGenerator.generateID(),cardList.getCardListId());
    }
    @Test
    void setBoardId() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");
        CardList cardList = new CardList(idGenerator.generateID(), "CardListTitle", new ArrayList<>(), new Board());
        idGenerator.setHardcodedID("2");
        cardList.setBoardId(idGenerator.generateID());
        assertEquals(idGenerator.generateID(),cardList.boardId);
    }
    @Test
    void getCardList() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        assertEquals(new ArrayList<>(),cardList.getCardList());
    }
    @Test
    void setBoard() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        Board board = new Board();
        cardList.setBoard(board);
        assertEquals(board,cardList.board);
    }
    @Test
    void setCardListTitle() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        cardList.setCardListTitle("NewCardListTitle");
        assertEquals("NewCardListTitle",cardList.getCardListTitle());
    }
    @Test
    void addCard() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        Card card = new Card();
        cardList.addCard(card);
        assertEquals(card,cardList.getCardList().get(0));
    }
    @Test
    void equals() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        CardList cardList1 = new CardList( "CardListTitle", new ArrayList<>());
        assertEquals(cardList,cardList1);
    }
    @Test
    void notEquals() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        CardList cardList1 = new CardList( "CardListTitle1", new ArrayList<>());
        assertNotEquals(cardList,cardList1);
    }
    @Test
    void hashCodeTest() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        CardList cardList1 = new CardList( "CardListTitle", new ArrayList<>());
        assertEquals(cardList.hashCode(),cardList1.hashCode());
    }
    @Test
    void hashCodeTestNotEquals() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        CardList cardList1 = new CardList( "CardListTitle1", new ArrayList<>());
        assertNotEquals(cardList.hashCode(),cardList1.hashCode());
    }
    @Test
    void toStringTest() {
        CardList cardList = new CardList( "CardListTitle", new ArrayList<>());
        assertEquals("CardList{cardListId=null, cardListTitle='CardListTitle', cardList=[], boardId=null}",cardList.toString());
    }
}
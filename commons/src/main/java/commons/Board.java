package commons;

import javax.persistence.*;
import java.util.*;

@Entity
public class Board {

    @Id
    public UUID boardID;
    public String boardTitle;
    @OneToMany(cascade = CascadeType.PERSIST)
    public List<CardList> cardListList;

    @ManyToMany(cascade = CascadeType.PERSIST)
    public List<Tag> tagList;

    public String description;

    public Boolean isProtected;
    public String passwordHash;

//    @ManyToOne(cascade = CascadeType.PERSIST)
    @Embedded
    public Theme boardTheme;


    public Board() {

    }
    public Board(UUID boardID,
                 String boardTitle,
                 List<CardList> cardListList,
                 String description,
                 Boolean isProtected,
                 String passwordHash,
                 Theme boardTheme) {
        this.boardID = boardID;
        this.boardTitle = boardTitle;
        this.cardListList = cardListList;
        this.description = description;
        this.isProtected = isProtected;
        this.passwordHash = passwordHash;
        this.boardTheme = boardTheme;
    }
    public Board(String boardTitle,
                 List<CardList> cardListList,
                 String description,
                 Boolean isProtected,
                 String passwordHash,
                 Theme boardTheme) {
        this.boardTitle = boardTitle;
        this.cardListList = cardListList;
        this.description = description;
        this.isProtected = isProtected;
        this.passwordHash = passwordHash;
        this.boardTheme = boardTheme;
    }


    /** Adds a cardlist to the cardlist list
     * @param cardList
     */
    public void addCardList(CardList cardList){
        cardListList.add(cardList);
    }


    /** Getter for list of cardlists
     * @return List<cardList
     */
    public List<CardList> getCardListList() {
        return cardListList;
    }

    /** Getter for list of cardlists
     * @return List<cardList
     */
    public CardList getCardListByID(UUID cardListID) {
        for(CardList cardList:cardListList){
            if(cardList.getCardListId().equals(cardListID)){
                return  cardList;
            }
        }
        return null;

    }

    /** get boardID
     * @return boardID
     */
    public UUID getBoardID(){
        return boardID;
    }

    /** Setter for boardID
     * @param boardID
     */
    public void setBoardID(UUID boardID){
        this.boardID = boardID;
    }

    /**
     * Sets the board theme
     */
    public void setBoardTheme(Theme boardTheme) {
        this.boardTheme = boardTheme;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return boardID.equals(board.boardID) && Objects.equals(boardTitle, board.boardTitle)
                && Objects.equals(cardListList, board.cardListList)
                && Objects.equals(description, board.description)
                && Objects.equals(isProtected, board.isProtected)
                && Objects.equals(passwordHash, board.passwordHash)
                && Objects.equals(boardTheme, board.boardTheme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardID, boardTitle, cardListList, description, isProtected, passwordHash, boardTheme);
    }

    @Override
    public String toString() {
        return "Board{" +
                "boardID=" + boardID +
                ", boardTitle='" + boardTitle + '\'' +
                ", cardListList=" + cardListList +
                ", description='" + description + '\'' +
                ", isProtected=" + isProtected +
                ", passwordHash='" + passwordHash + '\'' +
                ", boardTheme=" + boardTheme +
                '}';
    }
}



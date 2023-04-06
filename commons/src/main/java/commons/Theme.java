package commons;


import javax.persistence.*;
import java.util.*;
@Entity
public class Theme{


    @Id
    public UUID themeID;
    public String boardBackgroundColor;
    public String boardFont;
    public String listBackgroundColor;
    public String listFont;
    public String cardBackgroundColor;
    public String cardFont;

    public Theme() {}

    public Theme(UUID themeID, String boardBackgroundColor, String boardFont, String listBackgroundColor, String listFont, String cardBackgroundColor, String cardFont) {
        this.themeID = themeID;
        this.boardBackgroundColor = boardBackgroundColor;
        this.boardFont = boardFont;
        this.listBackgroundColor = listBackgroundColor;
        this.listFont = listFont;
        this.cardBackgroundColor = cardBackgroundColor;
        this.cardFont = cardFont;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(themeID, theme.themeID) && Objects.equals(boardBackgroundColor, theme.boardBackgroundColor) && Objects.equals(boardFont, theme.boardFont) && Objects.equals(listBackgroundColor, theme.listBackgroundColor) && Objects.equals(listFont, theme.listFont) && Objects.equals(cardBackgroundColor, theme.cardBackgroundColor) && Objects.equals(cardFont, theme.cardFont);
    }

    @Override
    public int hashCode() {
        return Objects.hash(themeID, boardBackgroundColor, boardFont, listBackgroundColor, listFont, cardBackgroundColor, cardFont);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "themeID=" + themeID +
                ", boardBackgroundColor='" + boardBackgroundColor + '\'' +
                ", boardFont='" + boardFont + '\'' +
                ", listBackgroundColor='" + listBackgroundColor + '\'' +
                ", listFont='" + listFont + '\'' +
                ", cardBackgroundColor='" + cardBackgroundColor + '\'' +
                ", cardFont='" + cardFont + '\'' +
                '}';
    }


    public UUID getThemeID() {
        return themeID;
    }

    public void setThemeID(UUID themeID) {
        this.themeID = themeID;
    }

    public String getBoardBackgroundColor() {
        return boardBackgroundColor;
    }

    public void setBoardBackgroundColor(String boardBackgroundColor) {
        this.boardBackgroundColor = boardBackgroundColor;
    }

    public String getBoardFont() {
        return boardFont;
    }

    public void setBoardFont(String boardFont) {
        this.boardFont = boardFont;
    }

    public String getListBackgroundColor() {
        return listBackgroundColor;
    }

    public void setListBackgroundColor(String listBackgroundColor) {
        this.listBackgroundColor = listBackgroundColor;
    }

    public String getListFont() {
        return listFont;
    }

    public void setListFont(String listFont) {
        this.listFont = listFont;
    }

    public String getCardBackgroundColor() {
        return cardBackgroundColor;
    }

    public void setCardBackgroundColor(String cardBackgroundColor) {
        this.cardBackgroundColor = cardBackgroundColor;
    }

    public String getCardFont() {
        return cardFont;
    }

    public void setCardFont(String cardFont) {
        this.cardFont = cardFont;
    }
}

package commons;


import javax.persistence.*;
import java.util.*;
@Embeddable
public class Theme{


    public String themeName;
    public String boardBackgroundColor;
    public String boardFont;
    public String listBackgroundColor;
    public String listFont;
    public String cardBackgroundColorHighlighted;
    public String cardFontHighlighted;

    public String cardBackgroundColorNormal;
    public String cardFontNormal;

    public String cardBackgroundColorLow;
    public String cardFontLow;

    public Theme() {}

    public Theme(String themeName, String boardBackgroundColor, String boardFont, String listBackgroundColor,
                 String listFont, String cardBackgroundColorHighlighted, String cardFontHighlighted,
                 String cardBackgroundColorNormal, String cardFontNormal, String cardBackgroundColorLow,
                 String cardFontLow) {
        this.themeName = themeName;
        this.boardBackgroundColor = boardBackgroundColor;
        this.boardFont = boardFont;
        this.listBackgroundColor = listBackgroundColor;
        this.listFont = listFont;
        this.cardBackgroundColorHighlighted = cardBackgroundColorHighlighted;
        this.cardFontHighlighted = cardFontHighlighted;
        this.cardBackgroundColorNormal = cardBackgroundColorNormal;
        this.cardFontNormal = cardFontNormal;
        this.cardBackgroundColorLow = cardBackgroundColorLow;
        this.cardFontLow = cardFontLow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(themeName, theme.themeName) && Objects.equals(boardBackgroundColor, theme.boardBackgroundColor) && Objects.equals(boardFont, theme.boardFont) && Objects.equals(listBackgroundColor, theme.listBackgroundColor) && Objects.equals(listFont, theme.listFont) && Objects.equals(cardBackgroundColorHighlighted, theme.cardBackgroundColorHighlighted) && Objects.equals(cardFontHighlighted, theme.cardFontHighlighted) && Objects.equals(cardBackgroundColorNormal, theme.cardBackgroundColorNormal) && Objects.equals(cardFontNormal, theme.cardFontNormal) && Objects.equals(cardBackgroundColorLow, theme.cardBackgroundColorLow) && Objects.equals(cardFontLow, theme.cardFontLow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(themeName, boardBackgroundColor, boardFont, listBackgroundColor, listFont, cardBackgroundColorHighlighted, cardFontHighlighted, cardBackgroundColorNormal, cardFontNormal, cardBackgroundColorLow, cardFontLow);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "themeName='" + themeName + '\'' +
                ", boardBackgroundColor='" + boardBackgroundColor + '\'' +
                ", boardFont='" + boardFont + '\'' +
                ", listBackgroundColor='" + listBackgroundColor + '\'' +
                ", listFont='" + listFont + '\'' +
                ", cardBackgroundColorHighlighted='" + cardBackgroundColorHighlighted + '\'' +
                ", cardFontHighlighted='" + cardFontHighlighted + '\'' +
                ", cardBackgroundColorNormal='" + cardBackgroundColorNormal + '\'' +
                ", cardFontNormal='" + cardFontNormal + '\'' +
                ", cardBackgroundColorLow='" + cardBackgroundColorLow + '\'' +
                ", cardFontLow='" + cardFontLow + '\'' +
                '}';
    }

}

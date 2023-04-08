package commons;

import commons.utils.HardcodedIDGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ThemeTest {


    @Test
    void testEquals() {
        Theme themeA = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF");
        Theme themeB = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF");
        Theme themeC = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2B", "#00ffd1",
                "#2A2A2A","#FF00FF");

        assertEquals(themeA,themeB);
        assertNotEquals(themeB,themeC);
    }

    @Test
    void testHashCode() {
        Theme themeA = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF");
        Theme themeB = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF");
        Theme themeC = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#othercolor");
        assertEquals(themeA.hashCode(),themeB.hashCode());
        assertNotEquals(themeB.hashCode(),themeC.hashCode());
    }

    @Test
    void testToString() {
        Theme theme = new Theme("base",
                "#2A2A2A", "#2A2A2A","#2A2A2A",
                "#2A2A2A","#2A2A2A","#2A2A2A",
                "#2A2A2A","#2A2A2A",
                "#2A2A2A","#2A2A2A");

        String actualString = theme.toString();
        String string = "Theme{" +
                "themeName='" + "base" + '\'' +
                ", boardBackgroundColor='" + "#2A2A2A" + '\'' +
                ", boardFont='" + "#2A2A2A" + '\'' +
                ", listBackgroundColor='" + "#2A2A2A" + '\'' +
                ", listFont='" + "#2A2A2A" + '\'' +
                ", cardBackgroundColorHighlighted='" + "#2A2A2A" + '\'' +
                ", cardFontHighlighted='" + "#2A2A2A" + '\'' +
                ", cardBackgroundColorNormal='" + "#2A2A2A" + '\'' +
                ", cardFontNormal='" + "#2A2A2A" + '\'' +
                ", cardBackgroundColorLow='" + "#2A2A2A" + '\'' +
                ", cardFontLow='" + "#2A2A2A" + '\'' +
                '}';
        assertEquals(string,actualString);
    }


}
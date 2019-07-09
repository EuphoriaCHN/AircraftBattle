package pers.euphoria.aircraftbattle.ui;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class PersonalFont {
    public Font getPersonalFont(String fontPath) {
        InputStream inputStream = this.getClass().getResourceAsStream(fontPath);
        Font actionJson = null;
        try {
            actionJson = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        assert actionJson != null;
        return actionJson.deriveFont(Font.BOLD, 30);
    }
}

package utils;

import javax.swing.*;

public class ControllerHelper {

    public static void updateTextArea(JTextArea textArea, String str, int newLineCount) {
        textArea.append(str);
        for (int i = 1; i <= newLineCount; i++) {
            textArea.append("\n");
        }
    }

}

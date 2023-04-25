package lab_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Zad3 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            LetterThreadSynchornized letterThreadSynchornized = new LetterThreadSynchornized(i);
            letterThreadSynchornized.start();
        }
    }
}

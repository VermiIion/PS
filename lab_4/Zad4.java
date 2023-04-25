package lab_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Zad4 implements ActionListener {
    JFrame frame;
    ArrayList<JButton> buttons;
    JPanel panel;
    ArrayList<JLabel> labels;

    ArrayList<AlphabetSpeller> spellers;

    ArrayList<Void> tasks;

    public Zad4() throws InterruptedException {
        spellers = new ArrayList<AlphabetSpeller>();
        for (int i = 0; i < 10; i++) {
            AlphabetSpeller speller = new AlphabetSpeller(i);
            spellers.add(speller);
        }

        frame = new JFrame();
        buttons = new ArrayList<JButton>();
        labels = new ArrayList<JLabel>();
        for (int i = 0; i < 10; i++) {
            JButton button = new JButton("Task " + (i + 1));
            JLabel label = new JLabel("Abort");
            button.addActionListener(this);
            buttons.add(button);
            labels.add(label);
        }


        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        for (int i = 0; i < buttons.size(); i++) {
            panel.add(buttons.get(i));
            panel.add(labels.get(i));
        }


        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Zad 4");
        frame.pack();
        frame.setVisible(true);

        for (AlphabetSpeller s : spellers) {
            run(s);
        }
    }

    public void run(AlphabetSpeller speller) throws InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            try {
                return speller.execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = buttons.indexOf(e.getSource());
        spellers.get(buttons.indexOf(e.getSource())).running = 0;

        labels.get(index).setText("Task Aborted");
    }

    public static void main(String[] args) throws InterruptedException {
        new Zad4();

    }
}

package lab_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Zad2 implements ActionListener {
    //int count = 0;
    JFrame frame;
    JButton button;
    ArrayList<JButton> buttons;
    JPanel panel;
    ArrayList<JLabel> labels;

    ArrayList<LetterThread> threads;

    public Zad2() {
       threads = new ArrayList<LetterThread>();
        for (int i = 0; i < 10; i++) {
            LetterThread letterThread = new LetterThread(i);
            letterThread.start();
            letterThread.suspend();
            letterThread.isSuspended = 1;
            threads.add(letterThread);
        }

        frame = new JFrame();
        buttons = new ArrayList<JButton>();
        labels = new ArrayList<JLabel>();
        for(int i = 0; i<10; i++){
            JButton button = new JButton("Thread "+(i+1));
            JLabel label = new JLabel("Thread " + (i+1) + " Suspended");
            button.addActionListener(this);
            buttons.add(button);
            labels.add(label);
        }


        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        for(int i = 0; i < buttons.size(); i++){
            panel.add(buttons.get(i));
            panel.add(labels.get(i));
        }


        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Zad 2");
        frame.pack();
        frame.setVisible(true);
    }

    public void threadDriver (ArrayList<LetterThread>threads, int id) {
        LetterThread thread = threads.get(id);
        if (thread.isAlive()) {
            if (thread.isSuspended == 1) {
                System.out.println("sleep -> start");
                thread.resume();
                thread.isSuspended = 0;
            } else {
                thread.suspend();
                thread.isSuspended = 1;
                System.out.println("start -> sleep");
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int index = buttons.indexOf(e.getSource());
        threadDriver(threads, index);
        if(threads.get(index).isSuspended == 1){
            labels.get(index).setText("Thread " + (index+1) + " Suspended");
        }else labels.get(index).setText("Thread " + (index+1) + " Running");
    }

    public static void main(String[] args) {
        new Zad2();

    }
}

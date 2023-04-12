package lab_4;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static class Zad1{
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Zad 1");
            ThreadHelloWorld helloThread = new ThreadHelloWorld();
            helloThread.start();
            helloThread.join();
            System.out.println("Hello Thread");
        }
    }

    private static class Zad2{
        public static synchronized void main(String[] args) throws InterruptedException {
            System.out.println("Zad 2");
            ArrayList<Thread>threads = new ArrayList<Thread>();
            for(int i = 0; i < 10; i++){
                LetterThread letterThread = new LetterThread(i);
                letterThread.start();
                letterThread.suspend();
                threads.add(letterThread);
            }
            Scanner input = new Scanner(System.in);
            while(true){
                System.out.println("Type a number to start or stop a thread(type -1 to quit):");
                int threadnumber = input.nextInt();
                if(threadnumber < 1 || threadnumber > 10){
                    if(threadnumber == -1) break;
                    System.out.println("No such thread");
                    continue;
                }
                if(threads.get(threadnumber-1).isInterrupted())threads.get(threadnumber-1).interrupt();
                else threads.get(threadnumber).resume();
            }
            for(Thread t : threads) {
                t.resume();
            }
            for(Thread t : threads) {
                t.join();
            }
            System.out.println("done");
        }
    }

    public static class Zad3{


        public static synchronized void increment(char c){
            c++;
        }

        public static void main(String[] args) {
            char c = 'A';

            for(int i = 0; i < 10; i++){
                Thread sThread = new Thread(() -> {
                    while (c < 'Z')
                    increment(c);
                });
                sThread.start();
            }
        }
    }

    private static class Zad4{

    }

}

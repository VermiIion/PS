package lab_4;

public class Zad1 {
    public static void main(String[] args) throws InterruptedException {
            System.out.println("Zad 1");
            ThreadHelloWorld helloThread = new ThreadHelloWorld();
            helloThread.start();
            helloThread.join();
            System.out.println("Hello Thread");
        }
}

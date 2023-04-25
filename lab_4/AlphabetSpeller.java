package lab_4;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlphabetSpeller {
    int id;
    int running;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public AlphabetSpeller(int id) {
        this.id = id;
        this.running = 1;
    }

    public String execute() throws InterruptedException {
        for (char c = 'A'; c <= 'Z'; ++c) {
            System.out.println(c + "" + (this.id) + " ");
            Thread.sleep(1000);
            if(running != 1) break;
        }
        if (running == 1) {
            return "Task " + id + " done";
        }
        return "Task " + id + " aborted";
    }
}

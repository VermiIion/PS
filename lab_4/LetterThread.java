package lab_4;

public class LetterThread extends Thread{
    int threadNumber = -1;
    public LetterThread(int i){
        threadNumber = i;
        if(threadNumber == 10) threadNumber = 0;
    }
    @Override
    public void run() {
        for(char c = 'A'; c <= 'Z'; ++c) {
            try {
                System.out.println(c + "" + threadNumber);
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


package lab_4;

public class LetterThreadSynchornized extends Thread{
    int threadNumber = -1;
    public LetterThreadSynchornized(int i){
        threadNumber = i;
        //if(threadNumber == 10) threadNumber = 0;
    }
    @Override
    public synchronized void run() {
        for(char c = 'A'; c <= 'Z'; ++c) {
            System.out.println(c + "" + (threadNumber) + " ");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


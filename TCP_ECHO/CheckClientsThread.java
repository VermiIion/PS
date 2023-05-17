package TCP_ECHO;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CheckClientsThread extends Thread{
    ArrayList<ClientContainer> clients;

    public CheckClientsThread(ArrayList<ClientContainer> clients){
        this.clients = clients;
    }

    @Override
    public void run(){
        while (true) {
            for (ClientContainer c : clients) {
                if (!c.running) {
                    try {
                        c.clientSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    clients.remove(c);
                }
            }
        }
    }
}

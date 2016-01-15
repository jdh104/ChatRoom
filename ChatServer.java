
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer{
    
    private int port;
    private String myIp;
    private ArrayList<Socket> clients;
    private ServerSocket server;
    private Listener listener;
    
    public ChatServer(int p){
        port = p;
    }
    
    public void startListener(){
        listener = new Listener(server,clients);
    }
    
    private class Listener extends Thread{
        
        private ServerSocket server;
        private ArrayList<Socket> clients;
        
        public Listener(ServerSocket s, ArrayList<Socket> c){
            server = s;
            clients = c;
        }
        
        public void run(){
            clients.add(server.accept());
        }
    }
}

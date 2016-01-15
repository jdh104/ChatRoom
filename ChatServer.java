
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatHost{
    
    private int port;
    private String myIp, myName;
    private ArrayList<Socket> clients;
    private ArrayList<PrintWriter> outs;
    private ServerSocket host;
    private Connector connector;
    
    public ChatHost(int p){
        port = p;
    }
    
    public void setName(String nn){
        myName = nn;
    }
    
    public void startConnector(){
        connector = new Connector(host,clients);
    }
    
    public void broadcast(String message){
        for (int i=0, i<outs.size(), i++){
            outs.get(i).println(message);
        }
    }
    
    private class Connector extends Thread{
        
        private ServerSocket host;
        private ArrayList<Socket> clients;
        
        public Connector(ServerSocket s, ArrayList<Socket> c){
            host = s;
            clients = c;
        }
        
        public void run(){
            try{
                Socket cli = host.accept();
                clients.add(cli);
                new Handler(host,cli).start();
            } catch(Exception e){
                //Do Nothing
            }
        }
    }
    
    private class Handler extends Thread{
        
        private ServerSocket host;
        private Socket client;
        private PrintWriter out;
        
        public Handler(Serversocket h, Socket c){
            host = h;
            client = c;
            out = new PrintWriter(c.getOutputStream(), true);
            outs.add(out);
        }
        
        public void run(){
            
        }
    }
}

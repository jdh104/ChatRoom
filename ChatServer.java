
import java.io.BufferedReader;
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
    private ArrayList<BufferedReader> ins;
    private ArrayList<String> chatQ;
    private ServerSocket host;
    private Connector connector;
    private Socket mySocket;
    
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
        private Socket lastCli;
        
        public Connector(ServerSocket s, ArrayList<Socket> c){
            host = s;
            clients = c;
        }
        
        public void run(){
            while (true){
                try{
                    lastCli = host.accept();
                    clients.add(lastCli);
                    new Handler(host,lastCli).start();
                } catch(Exception e){
                    //Do Nothing
                }
            }
        }
    }
    
    private class Handler extends Thread{
        
        private ServerSocket host;
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private int id;
        private String lastChat;
        
        public Handler(Serversocket h, Socket c){
            host = h;
            client = c;
            out = new PrintWriter(c.getOutputStream(), true);
            outs.add(out);
            in = new BufferedReader(new InputStreamReader(c.getInputStream));
            ins.add(in);
            id = outs.size()-1;
        }
        
        public void run(){
            while (true){
                try{
                    lastChat = in.readLine();
                    chatQ.add(lastChat);
                } catch(Exception e){
                    out.println("\tFrom: HOST, Error in sending message...");
                }
            }
        }
    }
}

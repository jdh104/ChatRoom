
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ChatHost{
    
    private ServerSocket host;
    private int port;
    private Connector connector;
    
    public ChatHost(int p){
        port = p;
        try{
            host = new ServerSocket(p);
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error starting up ServerSocket");
            host = null;
        }
        
        if (host != null){
            connector = new Connector(host, this);
            connector.start();
        }
    }
    
    public void process(String incoming){
        if (!(incoming.substring(0,1).equals("/"))){
            broadcast(incoming);
        } else {
            processCommand(incoming);
        }
    }
    
    public void broadcast(String message){
        for (int i=0; i<connector.clients.size(); i++){
            connector.outs.get(i).println(message);
        }
    }
    
    public void processCommand(String cmd){
        //IMPLEMENT LATER
    }
    
    private class Connector extends Thread{
        
        public ArrayList<Socket> clients;
        public ArrayList<PrintWriter> outs;
        private ServerSocket server;
        private ChatHost HOST;
        
        public Connector(ServerSocket host, ChatHost HOST){
            clients = new ArrayList<Socket>();
            outs = new ArrayList<PrintWriter>();
            server = host;
            this.HOST = HOST;
        }
        
        public void run(){
            while (true){
                try{
                    clients.add(server.accept());
                    outs.add(new PrintWriter(clients.get(clients.size()-1).getOutputStream(),true));
                    new Listener(clients.get(clients.size()-1),HOST).start();
                } catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Error recieving client");
                    while (clients.size() > outs.size()){
                        clients.remove(clients.size()-1);
                    }
                }
            }
        }
        
        private class Listener extends Thread{
            
            private Socket client;
            private BufferedReader bufferedReader;
            private ChatHost HOST;
            
            public Listener(Socket cli, ChatHost HOST){
                client = cli;
                this.HOST = HOST;
                try{
                    bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                } catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Error setting up buffered reader");
                }
            }
            
            public void run(){
                while (true){
                    try{
                        HOST.process(bufferedReader.readLine());
                    } catch(Exception e){
                        //REQUIRED
                    }
                }
            }
        }
    }
}

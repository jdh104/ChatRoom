
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
            connector = new Connector(host);
            connector.start();
        }
    }
    
    private class Connector extends Thread{
        
        private ArrayList<Socket> clients;
        private ServerSocket server;
        
        public Connector(ServerSocket host){
            clients = new ArrayList<Socket>();
            server = host;
        }
        
        public void run(){
            while (true){
                try{
                    clients.add(server.accept());
                } catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Error recieving client");
                }
            }
        }
        
        private class Listener extends Thread{
            
            private Socket client;
            private BufferedReader bufferedReader;
            
            public Listener(Socket cli){
                client = cli;
                try{
                    bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                } catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Error setting up buffered reader");
                }
            }
            
            public void run(){
                while (true){
                    
                }
            }
        }
    }
}

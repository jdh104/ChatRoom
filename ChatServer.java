
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class ChatServer{
    
    Server server;
    
    public static void main(String[] args){
        try {
            ChatServer cs = new ChatServer();
        } catch (IOException i){
            //DONT WORRY BOUT IT
        }
    }
    
    public ChatServer() throws IOException{
        server = new Server();
        server.start();
        
        while (true){
            pingClients();
            broadcastData();
            try{
                Thread.sleep(3000);
            } catch (Exception e){
                //CARRY ON
            }
        }
    }
    
    private void pingClients(){
        for (int j=0; j<server.hostData.size(); j++){
            try{
                if (!server.hostData.get(j).isConnected()){
                    server.hostData.remove(j);
                }
            } catch (IndexOutOfBoundsException e){
                //CONTINUE WITH LOOP
            } catch (Exception e){
                server.hostData.remove(j);
            }
        }
    }
    
    private void broadcastData(){
        for (int k=0; k<server.hostData.size(); k++){
            server.hostData.get(k).getPrintWriter().println(server.DATA);
        }
    }
    
    private class Server extends Thread{
        
        public ArrayList<HostData> hostData;
        public ServerSocket listener;
        public String DATA;
        
        public Server() throws IOException{
            super();
            hostData = new ArrayList<HostData>();
            listener = new ServerSocket();
            DATA = new String("");
        }
        
        public void run(){
            while (true){
                try{
                    hostData.add(new HostData(listener.accept()));
                } catch(Exception e) {
                    //DO NOTHING
                }
                DATA = new String("");
                for (int m=0; m<hostData.size(); m++){
                    DATA =  DATA + 
                            "Name=" + hostData.get(m).getName()
                             + "\nIP=" + hostData.get(m).getIp()
                             + "\nPort=" + hostData.get(m).getPort()
                             + "\n";
                }
            }
        }
        
        private class HostData{
            
            private String ip, name;
            private int port;
            private Socket clientSocket;
            private PrintWriter out;
            private BufferedReader in;
            
            public HostData(Socket socket){
                try{
                    clientSocket = socket;
                    ip = socket.getInetAddress().toString();
                    port = socket.getPort();
                    out = new PrintWriter(socket.getOutputStream());
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    name = in.readLine();
                } catch (Exception e){
                    //Errors in connection will not cause outside errors
                }
            }
            
            public void setPort(int newPort){
                port = newPort;
            }
            
            public String getIp(){
                return ip;
            }
            
            public int getPort(){
                return port;
            }
            
            public String getName(){
                return name;
            }
            
            public boolean isConnected(){
                return clientSocket.isConnected();
            }
            
            public PrintWriter getPrintWriter(){
                return out;
            }
        }
    }
}

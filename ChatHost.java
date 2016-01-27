
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
    
    public void process(ClientInfo info, String incoming){
        try{
            for (int i=0; i<incoming.length(); i++){
                if (incoming.substring(i,i+8).equals(":::CMD=/")){
                    processCommand(info,incoming,i);
                    return;
                }
            }
        } catch (Exception e){}   
        broadcast(info.getName() + ":" + incoming);
    }
    
    public void broadcast(String message){
        for (int i=0; i<connector.clients.size(); i++){
            connector.outs.get(i).println(message);
        }
    }
    
    public void broadcastColor(String message, String fg, String bg){
        for (int i=0; i<connector.clients.size(); i++){
            connector.outs.get(i).println("/color" + fg+bg + message);
        }
    }
    
    public void sendMessage(ClientInfo info, String message){
        info.getPrintWriter().println(message);
    }
    
    public void sendColorMessage(ClientInfo info, String message, String fg, String bg){
        info.getPrintWriter().println("/color" + fg+bg + message);
    }
    
    public void processCommand(ClientInfo senderInfo, String cmd, int i){
        Command command = new Command(senderInfo,cmd.substring(i+7,cmd.length()));
        if (command.isValid() && command.isAllowed()){
            if (command.getCommand().equals("newname")){
                String newName = command.getArgs().get(1);
                if (!clientExists(newName)){
                    String oldName = senderInfo.getName();
                    senderInfo.setName(newName);
                    broadcastColor(oldName + "\'s name has been changed to " + newName,"BLU","WHI");
                } else {
                    sendColorMessage(senderInfo,"Could not change name to " + newName,"RED","WHI");
                }
            }
        } else {
                sendMessage(senderInfo,command.getError());
        }
    }
    
    public void changeClientPermission(String client, int newPermission){
        ClientInfo info = connector.getClientInfo(client);
        if (!info.equals(null)){
            info.setPermission(newPermission);
            sendMessage(info,"Your permissions have been changed to " + info.getPermissionString());
        }
    }
    
    public boolean clientExists(String name){
        return (connector.getClientInfo(name) != null);
    }
    
    private class Connector extends Thread{
        
        public ArrayList<Socket> clients;
        public ArrayList<PrintWriter> outs;
        public ArrayList<ClientInfo> clientInfo;
        private ServerSocket server;
        private ChatHost HOST;
        
        public Connector(ServerSocket host, ChatHost HOST){
            clients = new ArrayList<Socket>();
            outs = new ArrayList<PrintWriter>();
            clientInfo = new ArrayList<ClientInfo>();
            server = host;
            this.HOST = HOST;
        }
        
        public ClientInfo getClientInfo(String name){
            for (ClientInfo i : clientInfo){
                if (i.getName().equals(name))
                    return i;
            }
            return null;
        }
        
        public void run(){
            while (true){
                try{
                    clients.add(server.accept());
                    outs.add(new PrintWriter(clients.get(clients.size()-1).getOutputStream(),true));
                    clientInfo.add(new ClientInfo());
                    if (clients.size() == 1){
                        clientInfo.get(clients.size()-1).setName("HOST");
                        clientInfo.get(clients.size()-1).setPermission(3);
                    } else {
                        clientInfo.get(clients.size()-1).setName("user" + clients.size());
                        clientInfo.get(clients.size()-1).setPermission(1);
                    }
                    clientInfo.get(clients.size()-1).setPrintWriter(outs.get(clients.size()-1));
                    new Listener(clients.get(clients.size()-1),clientInfo.get(clients.size()-1),HOST).start();
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
            private ClientInfo clientInfo;
            
            public Listener(Socket cli, ClientInfo info, ChatHost HOST){
                client = cli;
                this.HOST = HOST;
                clientInfo = info;
                try{
                    bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                } catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Error setting up buffered reader");
                }
            }
            
            public void run(){
                while (true){
                    try{
                        HOST.process(clientInfo,bufferedReader.readLine());
                    } catch(Exception e){
                        //REQUIRED
                    }
                }
            }
        }
    }
}

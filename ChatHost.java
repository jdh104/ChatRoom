
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
        if (info.getPermission() > 0){
            try{
                for (int i=0; i<incoming.length(); i++){
                    if (incoming.substring(i,i+8).equals(":::CMD=/")){
                        processCommand(info,incoming,i);
                        return;
                    }
                }
            } catch (Exception e){}   
            broadcast(info.getName() + ":" + incoming);
        } else {
            sendColorMessage(info,"HOST:You are currently muted","RED","WHI");
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
        try{
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
                } else if (command.getCommand().equals("promote")){
                    if (command.getArgs().size() < 2){
                        throw new IndexOutOfBoundsException();
                    } else if (connector.getClientInfo(command.getArgs().get(1)) == null){
                        throw new IndexOutOfBoundsException();
                    } else {
                        ClientInfo operand = connector.getClientInfo(command.getArgs().get(1));
                        if (operand.getPermission() == 1){
                            operand.setPermission(2);
                            broadcastColor(operand.getName() + "\'s permission has been changed to " +
                                           operand.getPermissionString(), "BLU","WHI");
                        }
                    }
                } else if (command.getCommand().equals("demote")){
                    if (command.getArgs().size() < 2){
                        throw new IndexOutOfBoundsException();
                    } else if (connector.getClientInfo(command.getArgs().get(1)) == null){
                        throw new IndexOutOfBoundsException();
                    } else {
                        ClientInfo operand = connector.getClientInfo(command.getArgs().get(1));
                        if (operand.getPermission() == 2){
                            operand.setPermission(1);
                            broadcastColor(operand.getName() + "\'s permission has been changed to " +
                                           operand.getPermissionString(), "BLU","WHI");
                        }
                    }
                } else if (command.getCommand().equals("mute")){
                    if (command.getArgs().size() < 3){
                        throw new IndexOutOfBoundsException();
                    } else if (connector.getClientInfo(command.getArgs().get(1)) == null){
                        throw new IndexOutOfBoundsException();
                    } else {
                        ClientInfo operand = connector.getClientInfo(command.getArgs().get(1));
                        long time = Long.parseLong(command.getArgs().get(2));
                        if (operand.getPermission()!=0){
                            operand.setPermission(0);
                            new MuteHandler(operand,time);
                            broadcastColor(operand.getName() + " has been muted for " + time +
                                           " minutes","GRE","WHI");
                        }
                    }
                }
            } else {
                    throw new Exception();
            }
        } catch (IndexOutOfBoundsException e){
            sendColorMessage(senderInfo,"Syntax Error: Wrong/missing argument(s)","RED","BLA");
        } catch (Exception e){
            sendColorMessage(senderInfo,command.getError(),"RED","BLA");
        }
    }
    
    public void changeClientPermission(String client, int newPermission){
        ClientInfo info = connector.getClientInfo(client);
        if (!info.equals(null)){
            info.setPermission(newPermission);
            sendColorMessage(info,"Your permissions have been changed to " + info.getPermissionString(),"GRE","WHI");
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
    
    private class MuteHandler extends Thread{
        private long time;
        private boolean unmutable;
        private ClientInfo client;
        public MuteHandler(ClientInfo cli, long t){
            time = t;
            unmutable = (t==0);
            client = cli;
        }
        public void run(){
            if (unmutable){
                try{
                    Thread.sleep(time*60000);
                } catch (Exception e){
                    run();
                }
                client.setPermission(1);
            }
        }
    }
}

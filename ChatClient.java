
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

public class ChatClient{
    
    private String name, host;
    private int port, permission;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    
    private ChatRoom ROOM;
    private ChatReciever reciever;
    
    public ChatClient(){
        //DO NOTHING
    }
    
    public ChatClient(String h, int p){
        permission=1;
        if (connectTo(h,p)){
            try{
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(),true);
                host = h;
                port = p;
                ROOM = new ChatRoom("Testing");
                ROOM.build();
                ROOM.attachClient(this);
                ROOM.startInterpreter();
                ROOM.setVisible(true);
                
                reciever = new ChatReciever(this,in,ROOM);
                reciever.start();
            } catch(Exception e) {
                in = null;
                out = null;
                JOptionPane.showMessageDialog(null,"Error establishing input/output with server");
            }
        } else {
            JOptionPane.showMessageDialog(null,"Error connecting");
        }
    }
    
    public boolean connectTo(String h, int p){
        try {
            client = new Socket(h,p);
        } catch(Exception e){
            return false;
        }
        return true;
    }
    
    public void setScreenName(String n){
        name = n;
    }
    
    public void bindRoom(ChatRoom room){
        ROOM = room;
    }
    
    public void sendPlainMessage(String text){
        Message message = new Message(name,text);
        out.println(message.getMessage());
    }
    
    public void sendCommand(String cmd){
        Command command = new Command(name,cmd);
        if (command.isValid())
            out.println(command.getOutput());
        else
            ROOM.println("Error Sending Command:\n\t" + command.getError());
    }
    
    public String getName(){
        return name;
    }
    
    public int getPermission(){
        return permission;
    }
    
    public void setPermission(int newPermission){
        permission = newPermission;
    }
    
    private class ChatReciever extends Thread{
        
        private BufferedReader in;
        private ChatRoom ROOM;
        private ChatClient client;
        
        public ChatReciever(ChatClient cli, BufferedReader in, ChatRoom room){
            this.in = in;
            ROOM = room;
            client = cli;
        }
        
        public void run(){
            while (true){
                try{
                    String incoming = in.readLine();
                    ROOM.println(incoming);
                } catch(Exception e){
                    //REQUIRED
                }
            }
        }
    }
                
}

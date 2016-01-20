
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

public class ChatClient{
    
    private String name, host;
    private int port;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    
    private ChatRoom ROOM;
    private ChatReciever reciever;
    
    public ChatClient(){
        //DO NOTHING
    }
    
    public ChatClient(String h, int p){
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
                
                reciever = new ChatReciever(in,ROOM);
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
        out.println(message.getOutput());
    }
    
    public void sendCommand(String cmd){
        Command command = new Command(name,cmd);
        if (command.isValid()){
            out.println(command.getOutput());
        } else {
            ROOM.println("Command failed because:");
            ROOM.println(command.getError());
        }
    }
    
    private class ChatReciever extends Thread{
        
        private BufferedReader in;
        private ChatRoom ROOM;
        
        public ChatReciever(BufferedReader in, ChatRoom room){
            this.in = in;
            ROOM = room;
        }
        
        public void run(){
            while (true){
                try{
                    ROOM.println(in.readLine());
                } catch(Exception e){
                    //REQUIRED
                }
            }
        }
    }
                
}

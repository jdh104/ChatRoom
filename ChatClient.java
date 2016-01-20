
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class ChatClient{
    
    private String name;
    private int host;
    private int port;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    
    private ChatRoom ROOM;
    
    public ChatClient(){
        
    }
    
    public ChatClient(String h, int p){
        if (connectTo(h,p)){
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(),true);
            host = h;
            port = p;
        }
    }
    
    public void connectTo(String h, int p){
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
}

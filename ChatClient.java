
import java.net.Socket;

public class ChatClient{
    
    private String name;
    private int host;
    private int port;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    
    private UserInterface GUI;
    
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
    
    public void setScreenName(n){
        name = n;
    }
    
    public void bindGUI(UserInterface gui){
        GUI = gui;
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
            gui.println("Command failed because:");
            gui.println(command.getError());
        }
    }
}

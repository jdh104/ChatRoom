
import java.net.Socket;

public class ChatClient{
    
    private String name;
    private int host;
    private int port;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    
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
    
    public void sendPlainMessage(String text){
        Message message = new Message(name,text);
        out.println(message.getOutput());
    }
}

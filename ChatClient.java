
import java.awt.Color;
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
            ROOM.println("Error Sending Command:\n\t" + command.getError(), Color.RED, Color.BLACK, true);
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
                    if (incoming.length() < 12 || !incoming.substring(0,6).equals("/color")){
                        ROOM.println(incoming, Color.BLACK, Color.WHITE, false);
                    } else {
                        String f = incoming.substring(6,9).toUpperCase();
                        String b = incoming.substring(9,12).toUpperCase();
                        Color fg = Color.BLACK;
                        Color bg = Color.WHITE;
                             if (f.equals("RED")) fg = Color.RED;
                        else if (f.equals("BLU")) fg = Color.BLUE;
                        else if (f.equals("GRE")) fg = Color.GREEN;
                        else if (f.equals("MAG")) fg = Color.MAGENTA;
                        else if (f.equals("YEL")) fg = Color.YELLOW;
                        else if (f.equals("CYA")) fg = Color.CYAN;
                        else if (f.equals("GRA")) fg = Color.GRAY;
                        else if (f.equals("WHI")) fg = Color.WHITE;
                             if (b.equals("RED") && !f.equals("RED")) bg = Color.RED;
                        else if (b.equals("BLU") && !f.equals("BLU")) bg = Color.BLUE;
                        else if (b.equals("GRE") && !f.equals("GRE")) bg = Color.GREEN;
                        else if (b.equals("MAG") && !f.equals("MAG")) bg = Color.MAGENTA;
                        else if (b.equals("YEL") && !f.equals("YEL")) bg = Color.YELLOW;
                        else if (b.equals("CYA") && !f.equals("CYA")) bg = Color.CYAN;
                        else if (b.equals("GRA") && !f.equals("GRA")) bg = Color.GRAY;
                        else if (b.equals("WHI") && !f.equals("WHI")) bg = Color.WHITE;
                        else if (f.equals("WHI")                    ) bg = Color.BLACK;
                        
                        ROOM.println(incoming.substring(12,incoming.length()),fg,bg,true);
                    }
                        
                } catch(Exception e){
                    //REQUIRED
                }
            }
        }
    }
                
}

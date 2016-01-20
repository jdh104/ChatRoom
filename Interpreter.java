
import java.util.ArrayList;

public class Interpreter extends Thread {
    
    private ArrayList<String> commands;
    private ChatClient client;
    
    public Interpreter(){
        commands = new ArrayList<String>();
    }
    
    public void add(String newCommand){
        commands.add(newCommand);
    }
    
    public void attachClient(ChatClient cli){
        client = cli;
    }
    
    private void interpret(String cmd){
        if (!(cmd.substring(0,1).equals("/"))){
            client.sendPlainMessage(cmd);
        } else {
            client.sendCommand(cmd.substring(1,cmd.length()));
        }
    }
    
    public void run(){
        while (client != null){
            try{
                Thread.sleep(10);
            } catch(Exception e){
                //REQUIRED BY JAVA
            }
            if (commands.size() != 0){
                interpret(commands.get(0));
                commands.remove(0);
            }
        }
    }
}

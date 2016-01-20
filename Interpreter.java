
import java.util.ArrayList;

public class Interpreter extends Thread {
    
    private ArrayList<String> commands;
    private ChatClient client;
    
    public Interpreter(ChatClient cli){
        commands = new ArrayList<String>();
        client = cli;
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
            client.sendCommand(cmd);
        }
    }
    
    public void run(){
        while (true){
            try{
                Thread.sleep(10);
            } catch(Exception ex){
                //REQUIRED
            }
            while (client != null){
                try{
                    Thread.sleep(10);
                } catch(Exception e){
                    //REQUIRED
                }
                if (commands.size() != 0){
                    interpret(commands.get(0));
                    commands.remove(0);
                }
            }
        }
    }
}

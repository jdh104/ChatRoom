
import java.util.ArrayList;

public class Command{
    
    private String sender, command;
    private ArrayList<String> args;
    private ArrayList<String> validCommands;
    private ClientInfo clientInfo;
    
    public Command(String s, String c){
        sender = s;
        command = c + " ";
        args = new ArrayList<String>();
        addValidCommands();
        
        int n = 1;
        for (int i=1; i<command.length(); i++){
            if (command.substring(i,i+1).equals(" ") || (i+1 == command.length())){
                args.add(command.substring(n,i));
                n = i+1;
            }
        }
    }
    
    public Command(ClientInfo info, String c){
        clientInfo = info;
        sender = clientInfo.getName();
        command = c + " ";
        args = new ArrayList<String>();
        addValidCommands();
        
        int n = 1;
        for (int i=1; i<command.length(); i++){
            if (command.substring(i,i+1).equals(" ") || (i+1 == command.length())){
                args.add(command.substring(n,i));
                n = i+1;
            }
        }
    }
    
    public void addValidCommands(){
        validCommands = new ArrayList<String>();
        validCommands.add("mute");
        validCommands.add("unmute");
        validCommands.add("kick");
        validCommands.add("promote");
        validCommands.add("demote");
        validCommands.add("lock");
        validCommands.add("unlock");
        validCommands.add("newname");
    }
    
    public String getCommand(){
        return args.get(0);
    }
    
    public ArrayList<String> getArgs(){
        return args;
    }
    
    public boolean isValid(){
        return (validCommands.contains(getCommand()));
    }
    
    public boolean isAllowed(){
        if (clientInfo == null)
            return false;
        if (getCommand().equals("newname"))
            return (!(clientInfo.getPermission() == 3));
        return (clientInfo.getPermission() == 3);
    }
    
    public String getError(){
        if (!isValid()){
            return "Command not recognized";
        } else if (!isAllowed()) {
            return "You do not have permission to execute this command";
        } else {
            return "Internal error, blame the programmer I guess";
        }
    }
    
    public String getOutput(){
        return sender + ":::CMD=" + command;
    }
}

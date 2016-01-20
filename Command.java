
import java.util.ArrayList;

public class Command{
    
    private String sender, command;
    private ArrayList<String> args;
    
    public Command(String s, String c){
        sender = s;
        command = c;
        args = new ArrayList<String>();
        
        int n = 0;
        for (int i=0; i<command.length()-1; i++){
            if (command.substring(i,i+1).equals(" ") || (i+1 == command.length())){
                args.add(command.substring(n,i+1));
                n = i+1;
            }
        }
    }
    
    public boolean isValid(){
        return false;
    }
    
    public String getError(){
        if (false){
            return "You do not have permission to execute this command";
        } else if (false) {
            return "Syntax error";
        } else {
            return "Internal error, blame the programmer I guess";
        }
    }
    
    public ArrayList<String> getOutput(){
        return args;
    }
}

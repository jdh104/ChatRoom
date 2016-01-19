

public class Message{
    private String sender, message, destip;
    
    public Message(String s, String m){
        sender = s;
        message = m;
    }
    
    public Message(String s, String m, String ip){
        sender = s;
        message = m;
        destip = ip;
    }
    
    public void setDestination(String ip){
        destip = ip;
    }
    
    public String getDestination(){
        return destip
    }
    
    public String getSender(){
        return sender;
    }
    
    public String getMessage(){
        return message;
    }
}

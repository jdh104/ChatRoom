

public class Message{
    private String sender, message, destip;
    
    public Messsage(String s,String m){
        sender = s;
        message = m;
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

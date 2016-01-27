public class Controller{
    
    private ChatHost host;
    private ChatClient cli;
    private UserInterface GUI;
    
    public Controller(){
        GUI = new UserInterface();
        GUI.build();
        GUI.bindController(this);
        GUI.setVisible(true);
    }
    
    public void setUpChatRoom(int port){
        host = new ChatHost(port);
        cli = new ChatClient("127.0.0.1",port);
        cli.setScreenName("HOST");
        cli.setPermission(3);
    }
    
    public void connectToHost(String ip, int port){
        cli = new ChatClient(ip,port);
    }
}

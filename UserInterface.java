import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class UserInterface extends JFrame implements ActionListener {
    
    private JPanel contentPane, hostPane, connectPane;
    private JTextField hostPortInput, connectPortInput, connectIpInput;
    private JButton hostButton, connectButton;
    
    private Controller controller;
    
    public UserInterface(){
        super();
    }
    
    public void build(){
        contentPane = ((JPanel) getContentPane());
        contentPane.setLayout(new FlowLayout());
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        
        hostPane = new JPanel();
        connectPane = new JPanel();
        
        hostPortInput = new JTextField(5);
        connectPortInput = new JTextField(15);
        connectIpInput = new JTextField(15);
        hostButton = new JButton("Host");
        connectButton = new JButton("Connect");
        
        hostButton.addActionListener(this);
        connectButton.addActionListener(this);
        
        hostPane.add(new JLabel("Port:"));
        hostPane.add(hostPortInput);
        hostPane.add(hostButton);
        
        connectPane.add(new JLabel("IP:"));
        connectPane.add(connectIpInput);
        connectPane.add(new JLabel("Port:"));
        connectPane.add(connectPortInput);
        connectPane.add(connectButton);
        
        contentPane.add(connectPane);
        contentPane.add(hostPane);
        
        setPreferredSize(new Dimension(600,120));
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        pack();
    }
    
    public void bindController(Controller c){
        controller = c;
    }
    
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("Connect")){
            try{
                controller.connectToHost(connectIpInput.getText(),Integer.parseInt(connectPortInput.getText()));
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Invalid port number");
            }
        } else if (e.getActionCommand().equals("Host")){
            try{
                controller.setUpChatRoom(Integer.parseInt(hostPortInput.getText()));
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Invalid port number");
            }
        }
    }
}

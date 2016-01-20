import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class UserInterface extends JFrame implements ActionListener {
    
    JPanel contentPane;
    JTextField hostPortInput, connectPortInput, connectIpInput;
    JButton hostButton, connectButton;
    
    public UserInterface(){
        super();
    }
    
    public void build(){
        contentPane = ((JFrame) getContentPane());
        contentPane.setLayout(new GridLayout(2,5));
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        
        hostPortInput = new JTextField(5);
        connectPortInput = new JTextField(15);
        connectIpInput = new JTextField(15);
        hostButton = new JButton(" Host  ");
        connectButton = new JButton("Connect");
        
        hostButton.addActionListener(this);
        connectButton.addActionListener(this);
        
        contentPane.add(new JLabel("Port:"));
        contentPane.add(hostPortInput);
        contentPane.add(hostButton);
        contentPane.add(new JLabel("IP:"));
        contentPane.add(connectIpInput);
        contentPane.add(new JLabel(" Port:"));
        contentPane.add(connectPortInput);
        contentPane.add(connectButton);
        
        pack();
    }
    
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("Connect")){
            MainClass.connectToHost(connectIpInput.getText(),connectPortInput.getText());
        } else if (e.getActionCommand().equals(" Host  ")){
            MainClass.setUpChatRoom(hostPortInput.getText());
        }
    }
}

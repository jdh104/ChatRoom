
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class UserInterface extends JFrame implements ActionListener {
    
    private JPanel contentPane, userPane;
    private JScrollPane chatScroller;
    private JTextArea chatRoom;
    private JTextField chatField;
    private JButton sendButton;
    
    public UserInterface(String title){
        super(title);
    }
    
    public void build(){
        contentPane = ((JFrame) getContentPane());
        contentPane.setLayout(new GridLayout(2,3));
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        
        userPane = new JPanel;
        chatRoom = new JTextArea(10,30);
        chatScroller = new JScrollPane(chatRoom);
        chatField = new JTextField();
        sendButton = new JButton(" > ");
        
        sendButton.addActionListener(this);
        
        add(userPane);
        add(chatScroller);
        add(chatField);
        add(sendButton);
        
        pack();
    }
    
    public void actionPerformed(ActionEvent e){
        
    }
    
}

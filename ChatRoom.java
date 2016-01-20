import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChatRoom extends JFrame implements ActionListener {
    
    private JPanel contentPane, userPane, topPane, bottomPane;
    private JScrollPane chatScroller;
    private JScrollBar hs, vs;
    private JTextArea chatRoom;
    private JTextField chatField;
    private JButton sendButton;
    
    private ChatClient user;
    private Interpreter interpreter;
    
    public ChatRoom(String title){
        super(title);
    }
    
    public void build(){
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        
        topPane = new JPanel();
        bottomPane = new JPanel();
        
        userPane = new JPanel();
        chatRoom = new JTextArea(20,80);
        chatScroller = new JScrollPane(chatRoom);
        chatField = new JTextField(70);
        sendButton = new JButton(">");
        hs = chatScroller.getHorizontalScrollBar();
        vs = chatScroller.getVerticalScrollBar();
        
        sendButton.addActionListener(this);
        chatRoom.setEditable(false);
        
        topPane.add(userPane);
        topPane.add(chatScroller);
        bottomPane.add(chatField);
        bottomPane.add(sendButton);
        
        contentPane.add(topPane);
        contentPane.add(bottomPane);
        
        setPreferredSize(new Dimension(1000,400));
        setResizable(false);
        
        pack();
    }
    
    public void attachClient(ChatClient cli){
        user = cli;
    }
    
    public void startInterpreter(){
        interpreter = new Interpreter(user);
        interpreter.start();
    }
    
    public void println(String output){
        chatRoom.append(output + "\n");
        vs.setValue(vs.getMaximum());
        hs.setValue(hs.getMinimum());
    }
    
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals(">")){
            if (chatField.getText().length() != 0){
                String text = chatField.getText();
                chatField.setText("");
                interpreter.add(text);
            }
            chatField.grabFocus();
        }
    }
}

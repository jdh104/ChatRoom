import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class ChatRoom extends JFrame implements ActionListener {
    
    private JPanel contentPane, userPane, topPane, bottomPane;
    private JScrollPane chatScroller;
    private JScrollBar hs, vs;
    private JTextPane chatRoom;
    private JTextField chatField;
    private JButton sendButton;
    private StyledDocument chatRoomDoc;
    
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
        chatRoom = new JTextPane();
        chatRoomDoc = chatRoom.getStyledDocument();
        chatScroller = new JScrollPane(chatRoom);
        chatScroller.setPreferredSize(new Dimension(800,300));
        chatRoom.setSize(800,300);
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
        
        setMinimumSize(new Dimension(1000,300));
        setPreferredSize(new Dimension(1000,400));
        setResizable(true);
        
        pack();
    }
    
    public void attachClient(ChatClient cli){
        user = cli;
    }
    
    public void startInterpreter(){
        interpreter = new Interpreter(user);
        interpreter.start();
    }
    
    public void println(String output, Color foreground, Color background, boolean bold){
        append(output + "\n", foreground, background, bold);
        vs.setValue(vs.getMaximum());
        hs.setValue(hs.getMinimum());
    }
    
    public void print(String output, Color foreground, Color background, boolean bold){
        append(output,foreground,background,bold);
    }
    
    private void append(String msg, Color f, Color b, boolean bold)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        SimpleAttributeSet aset = new SimpleAttributeSet();
        StyleConstants.setForeground(aset,f);
        StyleConstants.setBackground(aset,b);
        StyleConstants.setBold(aset,bold);
        try{
            chatRoomDoc.insertString(chatRoomDoc.getLength(), msg, aset);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error appending text to room");
        }
    }
    
    public void setVisible(boolean b){
        super.setVisible(b);
    }
    
    public void setChatBackground(Color c){
        chatRoom.setBackground(c);
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

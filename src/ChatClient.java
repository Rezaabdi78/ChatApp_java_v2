import com.sun.nio.sctp.SendFailedNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ChatClient {
    static JFrame chatWindow = new JFrame("Chat Application");
    static JTextArea chatArea = new JTextArea(22,40);
    static JTextField textField = new JTextField(40);
    static JLabel blankLabel = new JLabel("");
    static JButton sendbutton = new JButton("Send" );
    static BufferedReader in;
    static PrintWriter out;

    ChatClient(){
        chatWindow.setLayout(new FlowLayout());
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendbutton);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(475,500);
        chatWindow.setVisible(true);

        textField.setEditable(false);
        chatArea.setEditable(false);

        sendbutton.addActionListener(new Listener());
        textField.addActionListener(new Listener());
    }

    void startChat() throws Exception{
        String ipAddress =  JOptionPane.showInputDialog(chatWindow,
                                        "Enter IP Address:",
                                        "IP Address Required!!",
                                          JOptionPane.PLAIN_MESSAGE);

        Socket socket = new Socket(ipAddress, 8888);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true){
            String str = in.readLine();
            if (str.equals("NAME_REQUIRED")){
                String name = JOptionPane.showInputDialog(chatWindow,
                                                        "Enter a unique name:",
                                                            "Name required!!",
                                                            JOptionPane.PLAIN_MESSAGE);
                out.println(name);
            }else if (str.equals("NAME_ALREADY_EXISTS")){
                String name = JOptionPane.showInputDialog(chatWindow, "Enter another Name:",
                                                            "Name Already Exists!!", JOptionPane.WARNING_MESSAGE);
                out.println(name);
            }else if (str.startsWith("NAME_ACCEPTED")){

                textField.setEditable(true);
                blankLabel.setText("You are logged in as : " + str.substring(13));

            }else {
                chatArea.append(str + "\n");
            }

        }
    }
    class Listener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            ChatClient.out.println(ChatClient.textField.getText());
            ChatClient.textField.setText("");
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.startChat();
    }
}

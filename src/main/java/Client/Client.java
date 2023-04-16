package Client;

import Server.Server;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    JFrame frame = new JFrame("chatroom");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);

    public Client() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        textField.addActionListener(actionEvent -> {
            printWriter.println(textField.getText());
            textField.setText("");
        });
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Adress of Server:",
                "Welcome to chatroom",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Your name is: ",
                "Input your name",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void run() throws IOException {
        //与Server建立连接并获取资源
        String serverAdress = getServerAddress();
        Socket socket = new Socket(serverAdress, Server.PORT);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = bufferedReader.readLine();
            System.out.println(line);
            if (line.startsWith("YOURNAME")) {
                printWriter.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }
}

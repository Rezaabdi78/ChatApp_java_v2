import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    static ArrayList<String> userNames = new ArrayList<String>();
    static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();

    public static void main(String[] args) throws Exception {
        System.out.println("Waiting for Clients...");
        ServerSocket serverSocket = new ServerSocket(8888);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connection Established");
            ConversationHandler handler = new ConversationHandler(socket);
            handler.start();

        }
    }
}

class ConversationHandler extends Thread{
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String name;
    PrintWriter pw;
    static FileWriter fw;
    static BufferedWriter bw;


    public ConversationHandler(Socket socket)throws IOException {
        this.socket = socket;
        fw = new FileWriter("D:\\JavaExamples\\ChatApplication\\logs\\log.txt", true);
        bw = new BufferedWriter(fw);
        pw = new PrintWriter(bw, true);
    }
    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            int count = 0;
            while (true){
                if (count >0){
                    out.println("NAME_ALREADY_EXISTS");
                }else {
                    out.println("NAME_REQUIRED");
                }
                name = in.readLine();
                if (name == null){
                    return;
                }
                if (!ChatServer.userNames.contains(name)){
                    ChatServer.userNames.add(name);
                    break;
                }
                count++;
            }
            out.println("NAME_ACCEPTED" + name);
            ChatServer.writers.add(out);

            while (true){
                String message = in.readLine();

                if (message == null){
                    return;
                }
                pw.println(name + " : " + message);
                for (PrintWriter writer : ChatServer.writers){
                    writer.println(name + " : " +  message);
                }
            }

        }catch (Exception e){e.printStackTrace();}
    }
}
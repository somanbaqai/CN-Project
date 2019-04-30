/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnproject;

/**
 *
 * @author somanbaqai
 */
import java.net.*;
import java.io.*;
import java.util.*;
import javafx.collections.ObservableList;

public class GroupChat implements Runnable {

    private static final String TERMINATE = "Exit";
    static String name;
    static volatile boolean finished = false;
    public static boolean checkFile = false;
    public static String message = "";
    static ObservableList<String> chatMessages;
    static String portStr;

    public GroupChat(ObservableList<String> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public GroupChat() {
    }

    public static void setTXT(String TXTs) {
        message = TXTs;
    }

    public void StartChat(String[] args) {
        args = new String[2];
        args[0] = "localhost";
        args[1] = portStr;
        ff file = new ff("E:/filee.txt");
        System.out.println("port: " + args[1]);

        if (args.length != 2) {
            System.out.println("Two arguments required: <multicast-host> <port-number>");
        } else {
            try {
//				InetAddress group = InetAddress.getLocalHost(); 
                InetAddress group = InetAddress.getByName("230.0.0.0");
                int port = Integer.parseInt(args[1]);
                Scanner sc = new Scanner(System.in);
//                System.out.print("Enter your name: ");
//                name = sc.nextLine();
                MulticastSocket socket = new MulticastSocket(port);

                // Since we are deploying 
                socket.setTimeToLive(0);
                //this on localhost only (For a subnet set it as 1) 

                socket.joinGroup(group);
                Thread t = new Thread(new ReadThread(socket, group, port));

                // Spawn a thread for reading messages 
                t.start();

                // sent to the current group 
                System.out.println("Start typing messages...\n");
                while (true) {

//                    message = sc.nextLine();
                    System.out.println("mess: " + message);
                    if (!message.equalsIgnoreCase("")) {
                        System.out.println("in iffff");
                        if (message.equalsIgnoreCase(GroupChat.TERMINATE)) {
                            finished = true;
                            socket.leaveGroup(group);
                            socket.close();
                            break;
                        }
                        if (message.toLowerCase().contains("file path")) {
                            System.out.println("in send file");
                            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                            ObjectOutput oo = new ObjectOutputStream(bStream);
                            oo.writeObject(file);
                            oo.close();
                            message = name + ":";
                            byte[] buffer = message.getBytes();

                            byte[] serializedMessage = bStream.toByteArray();

//                        System.out.println("ser: " + serializedMessage);
                            DatagramPacket datagram = new DatagramPacket(serializedMessage, serializedMessage.length, group, port);
                            socket.send(datagram);
                            checkFile = true;
                            System.out.println("in send cehck: " + checkFile);
                            message = "";
                        } else {
                            message = name + ":~ " + message;
                            byte[] buffer = message.getBytes();
                            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
                            socket.send(datagram);
                            System.out.println("mess: " + message);
                            message = "";
                        }
                    }
                }
            } catch (SocketException se) {
                System.out.println("Error creating socket");
                se.printStackTrace();
            } catch (IOException ie) {
                System.out.println("Error reading/writing from/to socket");
                ie.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        String[] strr = new String[2];
        StartChat(strr);
    }

}

class ReadThread implements Runnable {

    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;

    ReadThread(MulticastSocket socket, InetAddress group, int port) {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        while (!GroupChat.finished) {
            byte[] buffer = new byte[ReadThread.MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
            String message = null;
            try {
                socket.receive(datagram);
                message = new String(buffer, 0, datagram.getLength(), "UTF-8");
                System.out.println("echk = " + GroupChat.checkFile);
                System.out.println("mes rec: " + message);
                if (!message.startsWith(GroupChat.name)) {
                    System.out.println(message);
                    System.out.println("haha");
                    if (message.contains("~")) {
                        GroupChat.chatMessages.add(message);
                    }
                    if (GroupChat.checkFile == false) {
                        if (!message.contains("~")) {
                            System.out.println("in rec file");
                            try {

                                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
                                ff file = (ff) iStream.readObject();
                                File f = file;
                                System.out.println("file name: " + f.getName());
                                System.out.println("file: " + file);
                                GroupChat.chatMessages.add(file.getAbsolutePath());
                                FileReader fr = new FileReader(f);
                                int xx;
                                String strr = "";
                                while ((xx = fr.read()) != -1) {
                                    System.out.print((char) xx);
                                    char chr = (char) xx;
                                    strr += chr;
                                }
                                fr.close();
                                System.out.println("strr: " + strr);
                                File f1 = new File("E:/filee2.txt");
                                FileWriter fw = new FileWriter(f1);
                                fw.write(strr);
                                fw.close();
                                GroupChat.chatMessages.add(strr);
                                iStream.close();
                            } catch (IOException e) {
                                System.out.println(e);
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                System.out.println("class: " + e);
                            }
                            GroupChat.checkFile = false;
                        }
                    }

                }

            } catch (IOException e) {
                System.out.println("Socket closed!");
            }

        }
    }
}

class ff extends File implements Serializable {

    public ff(String string) {
        super(string);
    }

}

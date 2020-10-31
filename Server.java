import java.net.*;
import java.io.*;
import java.time.*;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Server implements Runnable
{
    private ServerThread[] client = new ServerThread[100];
    private int clientNum = 0, port;
    private volatile boolean running = true;
    private String serverName;
    private Thread thread;
    private ServerSocket server;
    
    public Server(){}

    public Server(int port, String serverName)
    { 	
        this.port = port;
        this.serverName = serverName;
        thread = new Thread (this, serverName);
        thread.start();
    }

    public void run()
    {
        try
        {   
            //get ip and start listening
            InetAddress ip = InetAddress.getLocalHost();
            server = new ServerSocket(port);
            System.out.println("Server is listening on " + ip.getHostAddress() + ":" + port  + " ...");
            while (running)
            {   
                //create a ServerThread on request
                client[clientNum] = new ServerThread(server.accept(), "client" + clientNum, this);
                System.out.println("Client " + clientNum + " joined Server: " + serverName);
                clientNum++;
            }
        }
        catch(IOException e) 
        {
        }
        System.out.println("Server " + serverName + " shuts down!");
    }

    public void reportMsg (String msg, String name, String threadName)
    {   
        //save it on the file
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        saveData( "@ " + time.format(format) + "\t" + name + ": " + msg);

        for (int i = 0 ; i < clientNum ; i++)
        {
            if(!(client[i].getThreadName().equals(threadName))) //send to everyone but the sender
                client[i].send(name + ": " + msg);
        }
    }

    public void sendKillThreadMsg (String threadName)
    {
        for (int i = 0 ; i < clientNum ; i++)
        {   
            if((client[i].getThreadName().equals(threadName)))
            {   
                System.out.println("Client " + i + " left server: " + serverName + " !"); //show on server console
                reportMsg(client[i].getClientName() + " left the chat!", "Server", "server"); //tell others
            }
        }
    }

    public String getName()
    {
        return serverName;
    }

    public void kill()
    {
        for (int i = 0 ; i < clientNum ; i++)
        {   
            client[i].send("Server shuts down ...");
            client[i].kill();
        }
        running = false;
        
        try
        {
            server.close();
        }
        catch (IOException ex)
        {
         System.out.println("done!");
        }
    }

    private void saveData (String data)
    {   
        try
        {
            String full = "";
            File chatLog = new File(serverName + "ChatLog.txt");
            chatLog.createNewFile();
            Scanner myReader = new Scanner(chatLog);
            while (myReader.hasNextLine())
            {
                full += ( myReader.nextLine() + "\n" );
            }
            full += data;
            myReader.close();
            FileWriter myWriter = new FileWriter(serverName +"ChatLog.txt");
            myWriter.write(full);
            myWriter.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
    }

    public boolean isAlive()
    {
        if (running)
            return true;
        return false;
    }
} 
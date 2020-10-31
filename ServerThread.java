import java.util.Scanner;
import java.net.*;
import java.io.*;

public class ServerThread implements Runnable
{   
    private String clientName;
    private PrintWriter clientOut;
    private Scanner clientIn;
    private String threadName;
    private Server server;
    private Socket client;
    private volatile boolean running = true;

    public ServerThread (){}

    public ServerThread (Socket client, String threadName, Server server)
    {   
        //create the thread and point the writer and scanner to the client
        this.client = client;
        this.threadName = threadName;
        this.server = server;
        Thread thread = new Thread (this, threadName);
        thread.start();

    }

    public void run()
    {   
        //connect
        try
        {
            clientOut = new PrintWriter(client.getOutputStream(), true);
            clientIn = new Scanner(client.getInputStream());

            //get username
            clientOut.println("You are connected to the chat server!");
            clientOut.println("Please choose a username: ");
            clientName = clientIn.nextLine();
            clientOut.println("Thanks! You are all set! Type \"Exit\" to you know ... exit!\n");

            //tell others you joined
            server.reportMsg(clientName + " joined the chat!", "Server",threadName);

            //listen
            String inputLine;
            while (clientIn.hasNextLine() && running) 
            {      
                inputLine = clientIn.nextLine();
                if(inputLine.equalsIgnoreCase("exit"))
                    break;
                server.reportMsg(inputLine, clientName, threadName);
            }
            server.sendKillThreadMsg(threadName);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public void kill()
    {
        send("terminate");
        running = false;
        try
        {
            clientOut.close();
            clientIn.close();
            client.close();
        }
        catch(Exception ex)
        {}
    }

    public String getThreadName ()
    {
        return threadName;
    }

    public String getClientName()
    {
        return clientName;
    }

    public void send (String msg)
    {
        clientOut.println(msg);
    }
} 
import java.io.*;
import java.util.Scanner;

public class ClientThread implements Runnable
{   
    private Scanner serverInput;
    private boolean running = true;

    public ClientThread (InputStream instream)
    {   
        serverInput = new Scanner(instream);
        Thread thread = new Thread (this);
        thread.start();
    }

    public void run() 
    {   
        //listen to server and print everything
        String inputLine;
        while (serverInput.hasNextLine() && running)
        {   
            inputLine = serverInput.nextLine();
            if (inputLine.equals("terminate"))
            {   
                System.out.println("Press enter to exit the app!");
                Client.running = false;
                break;
            }
            System.out.println(inputLine);
        }
    }

    public void kill()
    {   
        running = false;
    }
}
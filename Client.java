import java.net.*; 
import java.io.*; 
import java.util.Scanner;

public class Client
{
    public static volatile boolean running = true;

	public static void main(String args[]) 
	{	
        String userInput ="", ipAddress;
        int port;
        Scanner scan = new Scanner(System.in);

        //read server ip and port
		System.out.print("Enter the server's IP address: ");
		ipAddress = scan.nextLine();
		System.out.print("Enter the server's port: ");
		port = Integer.parseInt(scan.nextLine());

		try 
		{   
            //connect
            Socket socket = new Socket(ipAddress, port);
            ClientThread listener = new ClientThread (socket.getInputStream());
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);

            //send user input to server
            while (scan.hasNextLine() && running)
            {   
                userInput = scan.nextLine();
                if (!userInput.isEmpty())
                    toServer.println(userInput);
                if (userInput.equalsIgnoreCase("exit"))
                {	
                	listener.kill();
                    break;
                }
            }
        }
        catch (IOException e)
        {   
            System.out.println( "Caught: " + e);
        }
	}
}
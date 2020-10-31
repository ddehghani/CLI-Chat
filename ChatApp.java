import java.util.Scanner;

public class ChatApp
{   
    public static void main(String args[])
    { 	
    	Server[] server = new Server[50];
    	int serverNum = 0;
    	String input;
    	Scanner sysScan = new Scanner(System.in);
    	String[] splitetInput = new String[10];
    	System.out.println("Enter \"Server serverName port\" to start a new server;");
    	System.out.println("Enter \"Kill serverName\" to start a stop a server;");
    	System.out.println("Enter \"Exit\" to exit the program!");

    	while (sysScan.hasNextLine())
    	{	
    		input = sysScan.nextLine();
    		splitetInput = input.split(" ");
    		if (splitetInput[0].equalsIgnoreCase("server"))
    		{
    			server[serverNum] = new Server(Integer.parseInt(splitetInput[2]), splitetInput[1]);
    			serverNum++;
    		} 
    		else if (splitetInput[0].equalsIgnoreCase("kill"))
    		{
    			for (int i = 0 ; i < serverNum ; i++)
        		{   
            		if(server[i].getName().equals(splitetInput[1]))  
               			server[i].kill();
        		}
    		}
    		else if (splitetInput[0].equalsIgnoreCase("exit"))
    		{	
    			for (int i = 0 ; i < serverNum ; i++)
        		{   
                    if (server[i].isAlive())
               		   server[i].kill();
        		}
    			break;
    		}
    		else
    		{
    			System.out.println("Wrong syntax!");
    			System.out.println("Enter \"Server serverName port\" to start a new server;");
    			System.out.println("Enter \"Kill serverName\" to start a stop a server;");
    			System.out.println("Enter \"Exit\" to exit the program!");
    		}
    	}
    }
} 
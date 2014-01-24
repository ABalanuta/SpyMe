package server.cli;

import java.util.ArrayList;

public class ServerCli {

	ArrayList<Command> _commands = new ArrayList<Command>();
	
	public ServerCli(){
		_commands.add(new Command("Show Computers"));
		_commands.add(new Command("Show Time"));
		_commands.add(new Command("Show Version"));
		_commands.add(new Command("Exit"));
	}
	
	public void showCommands(){
		int index = 1;
		System.out.println("### showCommands ###");
		for(Command c : _commands){
			System.out.print(index++);
			System.out.println(" "+c._name);
		}
		System.out.println("### ############ ###");
	}
	
	public static void clearConsole()
	{
	    try
	    {
	        String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (Exception exception)
	    {
	    	System.out.println("Error");
	    }
	}
}

package server.cli;

public class Command {

	public String _name;
	
	
	public Command(String name){
		_name = name;
	}

	public void println(){
		System.out.println(_name);
	}
}

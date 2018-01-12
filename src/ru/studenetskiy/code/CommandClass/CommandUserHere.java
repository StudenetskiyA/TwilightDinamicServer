package ru.studenetskiy.code.CommandClass;

import java.util.ArrayList;

public class CommandUserHere extends CommonCommand {
public Double latitude;
public Double longitude;
	
	
	public CommandUserHere(String _userName,String _password,Double _latitude,Double _longitude){
		super(_userName,_password);
		latitude=_latitude;
		longitude=_longitude;
	}
	
	public static CommandUserHere createCommandFromString(ArrayList<String> parameter){
		return new CommandUserHere(parameter.get(0),parameter.get(1),Double.parseDouble(parameter.get(2)),Double.parseDouble(parameter.get(3)));
		
	}
}

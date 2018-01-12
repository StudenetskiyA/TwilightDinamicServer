package ru.studenetskiy.code;

public class User {
	Double latitude=0.0;
	Double longitude=0.0;
	String name="";
	String password="";
	int powerSide=0;
	String achievement="";
	int priority=0;
	int level=8;
	String lastConnected="";
	String curse="0";
	int superuser=0;

	User(String name, String password, int powerSide, int level) {
		this.name = name;
		this.password=password;
		this.powerSide=powerSide;
		this.level=level;
	}
	User(String name,String password, int powerSide, int level, Double lat,Double lon,String lastconnected,String achievements,int superuser,String curse) {
		this.name = name;
		this.password=password;
		this.powerSide=powerSide;
		this.level=level;
		this.latitude=lat;
		this.longitude=lon;
		this.lastConnected=lastconnected;
		this.achievement=achievements;
		this.curse=curse;
		this.superuser=superuser;
	}
	
	User(String name, int powerSide, Double lat,Double lon,String lastconnected) {
		this.name = name;
		//this.password=password;
		this.powerSide=powerSide;
		//this.level=level;
		this.latitude=lat;
		this.longitude=lon;
		this.lastConnected=lastconnected;
	}
	
	String formatForSearchUser(){
		return this.name+Commons.COMMA+this.latitude+Commons.COMMA+this.longitude+Commons.COMMA+this.powerSide+Commons.COMMA+this.lastConnected;
	}
}

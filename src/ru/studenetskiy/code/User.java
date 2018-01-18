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
	String vampireCall="";
	int superuser=0;
	int hidden=0;

	User(String name, String password, int powerSide, int su) {
		this.name = name;
		this.password=password;
		this.powerSide=powerSide;
		this.superuser=su;
	}
	User(String name,String password, int powerSide, int level, Double lat,Double lon,String lastconnected,String achievements,int superuser,String curse, int hidden) {
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
		this.hidden = hidden;
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
	
	String formatForSearchUser(int power){
		String result;
		result = this.name+Commons.COMMA+this.latitude+Commons.COMMA+this.longitude+Commons.COMMA+this.powerSide+Commons.COMMA+this.lastConnected; 
		if (power>1) result+=Commons.COMMA+this.curse;
		else result+=Commons.COMMA+Commons.NOT_DATA_ABOUT_CURSE_FOR_SEARCH;
		return result;
	}
	
	int rangeBetweenUsers(Double lati, Double longi) {
		int l = Commons.rangeBetweenPoints(lati, longi, this.latitude, this.longitude);
		return l;
	}
}

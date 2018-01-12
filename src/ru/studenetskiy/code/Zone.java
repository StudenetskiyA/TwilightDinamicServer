package ru.studenetskiy.code;

public class Zone {

	Double latitude;
	Double longitude;
	Double radius;
	String name;
	String textForHuman;
	String textForLight;
	String textForDark;
	String achievement;
	int priority;
	int system;

	Zone(String name, Double latitude, Double longitude, Double radius, String textForHuman, String textForLight,
			String textForDark,int priority,String achievement,int system) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.name = name;
		this.textForHuman = textForHuman;
		this.textForLight = textForLight;
		this.textForDark = textForDark;
		this.priority=priority;
		this.achievement=achievement;
		this.system=system;
	}

	Boolean isInZone(Double lati, Double longi) {
		Double l = Math.sqrt(Math.pow(Math.abs(lati - latitude), 2.0) + Math.pow(Math.abs(longi - longitude), 2.0));
		System.out.println("Radius from "+lati+" to "+latitude+"= "+l);
		return l <= radius;
	}

	public String toString() {
		return name + "," + latitude + "," + longitude + "," + radius + "," + textForHuman + "," + textForLight + ","
				+ textForDark;
	}
	
	String formatForSearchUser(){
		return this.name+Commons.COMMA+this.latitude+Commons.COMMA+this.longitude+Commons.COMMA+this.radius+Commons.COMMA+this.priority+Commons.COMMA+this.textForHuman;
	}
}

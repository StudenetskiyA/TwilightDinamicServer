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
	int observable;

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

	int rangeBetweenUserAndZone(Double lati, Double longi) {
		return Commons.rangeBetweenPoints(lati, longi, this.latitude, this.longitude);
	}
	
	Boolean isInZone(Double lati, Double longi) {
		int l = Commons.rangeBetweenPoints(lati, longi, this.latitude, this.longitude);
		System.out.println("Radius from "+lati+" to "+latitude+"= "+l);
		return l <= radius;
	}

	public String toString() {
		return name + "," + latitude + "," + longitude + "," + radius + "," + textForHuman + "," + textForLight + ","
				+ textForDark;
	}
	
	String formatForSearchUser(int power){
		String result;
		result = this.name+Commons.COMMA+this.latitude+Commons.COMMA+this.longitude+Commons.COMMA+this.radius+Commons.COMMA+this.priority;
	    if (power>2) result+=	Commons.COMMA+this.textForHuman;
	    else result+=Commons.COMMA+Commons.NOT_DATA_ABOUT_ZONE_FOR_SEARCH;
		return result;
	}
}

package ru.studenetskiy.code;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

enum PowerSide {
	Human, Light, Dark;

	PowerSide toPowerside(int n) {
		if (n == 0)
			return Human;
		else if (n == 1)
			return Light;
		else
			return Dark;
	}
	
	int toInt() {
		if (this==Light) return 1;
		else if (this==Dark) return 2;
		else return 0;
	}
}

public class Commons {
	public static final String SERVER_VERSION = "0.53 - client";
	public static final String DISPELL_CAST = "Снять заклинание ";
	public static final String HIDE_CAST = "Защита от обнаружения";
	public static final String FREE_ZONE_TEXT = "Всё нормально";
	public static final String NOT_DATA_ABOUT_CURSE_FOR_SEARCH= "Нет данных о проклятии";
	public static final String NOT_DATA_ABOUT_ZONE_FOR_SEARCH= "Нет данных о свойствах зоны";
	public static final int MINUTES_FOR_SEARCH_IN_PAST = 20;
	public static final int RADIUS_SEARCH_ONE_LEVEL = 1000; 
	public static final int RADIUS_SEARCH_TWO_LEVEL = 2000;
	public static final int RADIUS_SEARCH_THREE_LEVEL = 5000;
	public static final int RADIUS_SCAN_ONE_LEVEL = 500; 
	public static final int RADIUS_SCAN_TWO_LEVEL = 1000;
	public static final int RADIUS_SCAN_THREE_LEVEL = 3000;
	public static final int PROTECTION_LONG_ONE_LEVEL = 1; 
	public static final int PROTECTION_LONG_TWO_LEVEL = 3; 
	public static final int PROTECTION_LONG_THREE_LEVEL = 8; 
	
	private static String database = "jerry";
//	private static String url = "jdbc:mysql://localhost:3306/jerry?useUnicode=yes&characterEncoding=utf8";
//	private static String rootLogin = "jerry";
	private static String url = "jdbc:mysql://localhost:3306/users?useUnicode=yes&characterEncoding=utf8";
	private static String rootLogin = "root";
	private static String password = "QJaVlKVV";
	public static final String COMMA="|";


	static SQLUserHelper sql = new SQLUserHelper(database, url, rootLogin, password);
	static SQLZoneHelper sqlZone = new SQLZoneHelper(database, url, rootLogin, password);
	static SQLLogingHelper sqlLogs = new SQLLogingHelper(database, url, rootLogin, password);

	static int getProtectLongFromPower(int power) {
		if (power == 3)
			return Commons.PROTECTION_LONG_THREE_LEVEL;
		else if (power == 2)
			return Commons.PROTECTION_LONG_TWO_LEVEL;
		else if (power==1)
			return Commons.PROTECTION_LONG_ONE_LEVEL;
		else return 5000;
	}
	
	static int getScanRadiusFromPower(int power) {
		if (power == 3)
			return Commons.RADIUS_SCAN_THREE_LEVEL;
		else if (power == 2)
			return Commons.RADIUS_SCAN_TWO_LEVEL;
		else if (power==1)
			return Commons.RADIUS_SCAN_ONE_LEVEL;
		else return 500000;
	}
	
	static int getSearchRadiusFromPower(int power) {
		if (power == 3)
			return Commons.RADIUS_SEARCH_THREE_LEVEL;
		else if (power == 2)
			return Commons.RADIUS_SEARCH_TWO_LEVEL;
		else if (power==1)
			return Commons.RADIUS_SEARCH_ONE_LEVEL;
		else return 500000;
	}
	
	static int gradusToMetter(Double gradus){
		return (int) (gradus*111197);
	}
	
	static String getTextCommand(String txt){
		return txt.substring(0, txt.indexOf("("));
	}
	
	static int rangeBetweenPoints(Double lati, Double longi, Double lati2, Double longi2) {
		int r = 6371000;
		int d =(int)(r*Math.acos(Math.sin(Math.toRadians(lati2))*Math.sin(Math.toRadians(lati))+Math.cos(Math.toRadians(lati2))*
				Math.cos(Math.toRadians(lati))*Math.cos(Math.toRadians(longi2)-Math.toRadians(longi)))); 
		System.out.println("Range in meter="+d);
		return d;
	}
	
	static String now() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	static ArrayList<String> getTextBetween(String txt) {
		String fromText = txt;
		ArrayList<String> rtrn = new ArrayList<String>();
		String beforeText = "(";
		fromText = fromText.substring(fromText.indexOf(beforeText) + 1, fromText.indexOf(")"));
		String[] par = fromText.split(Pattern.quote(COMMA));
		for (int i = 0; i < par.length; i++) {
			//System.out.println("Par : " + par[i]);
			rtrn.add(par[i]);
		}
		return rtrn;
	}
}

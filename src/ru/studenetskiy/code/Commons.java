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
	public static final String SERVER_VERSION = "0.116client";
	private static String database = "jerry";
//	private static String url = "jdbc:mysql://localhost:3306/jerry?useUnicode=yes&characterEncoding=utf8";
//	private static String rootLogin = "jerry";
	private static String url = "jdbc:mysql://localhost:3306/users?useUnicode=yes&characterEncoding=utf8";
	private static String rootLogin = "root";
	private static String password = "QJaVlKVV";
	public static final String COMMA="|";
	//private static String address = "ws://localhost:8080/BHServer/serverendpointdemo";// "ws://test1.uralgufk.ru:8080/BHServer/serverendpointdemo";


	static SQLUserHelper sql = new SQLUserHelper(database, url, rootLogin, password);
	static SQLZoneHelper sqlZone = new SQLZoneHelper(database, url, rootLogin, password);
	static SQLLogingHelper sqlLogs = new SQLLogingHelper(database, url, rootLogin, password);

	static String getTextCommand(String txt){
		return txt.substring(0, txt.indexOf("("));
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

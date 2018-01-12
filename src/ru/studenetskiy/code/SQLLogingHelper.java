package ru.studenetskiy.code;

import java.sql.SQLException;

public class SQLLogingHelper extends SQLHelper {
	
	SQLLogingHelper(String databaseName, String url, String login, String password) {
		super(databaseName, url, login, password);
	}
	
	void addRecord(LogingRecord z) {
		String query = "INSERT INTO logs VALUES(";
		query += "'" + z.when + "','" + z.what + "','"  + z.name + "'," + z.latitude + "," + z.longitude + "," + z.radius + ");";
		System.out.println("Query : " + query);
		try {
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
	static class LogingRecord {
		String when="";
		String what="user";
		String name="";
		Double latitude=0.0;
		Double longitude=0.0;
		Double radius=0.0;
		
		LogingRecord(String when,String what,String name,Double latitude,Double longitude,Double radius) {
			this.when=when;
			this.what=what;
			this.name=name;
			this.latitude=latitude;
			this.longitude=longitude;
			this.radius=radius;
		}
	}
}

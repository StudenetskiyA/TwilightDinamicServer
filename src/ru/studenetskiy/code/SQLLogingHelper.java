package ru.studenetskiy.code;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLLogingHelper extends SQLHelper {
	
	SQLLogingHelper(String databaseName, String url, String login, String password) {
		super(databaseName, url, login, password);
	}
	
	void firstInitZonesTable() {
		String query = "CREATE TABLE `logs` (`Datetime` DATETIME,`Whattodo` varchar(30),`name` varchar(30),"+
	"`latitude` DOUBLE,`longitude` DOUBLE,`radius` DOUBLE DEFAULT 0) DEFAULT CHARSET=utf8;";
		try {
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}

	}
	
	Boolean isTableExist() {
		String query = "SHOW TABLES LIKE 'logs';";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Table logs NOT exist");
		return false;
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

package ru.studenetskiy.code;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLUserHelper extends SQLHelper {

	SQLUserHelper(String databaseName, String url, String login, String password) {
		super(databaseName, url, login, password);
	}

	void firstInitUsersTable() {
		String query = "CREATE TABLE `users` (`name` varchar(50) NOT NULL,`password` varchar(20) NOT NULL,`powerside` int(1) NOT NULL,"+
"`level` int(3) NOT NULL,`latitude` DOUBLE,`longitude` DOUBLE,`lastconnected` datetime,`achievements` varchar(1000) DEFAULT '',"+
"`superuser` int(1) DEFAULT 0,`curse` varchar(30) DEFAULT '0',`hidden` int(1) DEFAULT 0,`hiddenuntil` DATETIME,`vampirecall` varchar(50) DEFAULT '0',"+
"`vampiresend` varchar(50) DEFAULT '0',PRIMARY KEY (`name`)) DEFAULT CHARSET=utf8;";
		try {
			// executing SELECT query
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	addUser(new User("admin","twilight",0,9));
	}
	
	void addUser(User z) {
		String query = "INSERT INTO users (name,password,powerside,level,superuser,curse) VALUES(";
		query += "'" + z.name + "','" + z.password + "'," + z.powerSide + "," + z.level + "," + z.superuser + ",'"
				+ z.curse + "');";
		System.out.println("Query : " + query);
		try {
			// executing SELECT query
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	void clearVampireCall(String name) {
		String query = "UPDATE USERS SET vampirecall='0' WHERE vampirecall='" + name + "';";
		try {
			// executing SELECT query
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
		query = "UPDATE USERS SET vampiresend='0' WHERE vampiresend='" + name + "';";
		try {
			// executing SELECT query
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
	void deleteUser(String name) {
		String query = "DELETE FROM users WHERE name='" + name + "';";
		System.out.println("Query : " + query);
		try {
			// executing SELECT query
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	User getNonHiddenUser(String _userName, int power) {
		if (isUserExist(_userName)) {
			String query = "SELECT * FROM users WHERE hidden<" + power + " AND name='";
			query += _userName + "';";
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return new User(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5),
								rs.getDouble(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), 0);
					}
				}
			} catch (SQLException e) {
				return null;
			}
		}
		return null;
	}

	User getUser(String _userName) {
		if (isUserExist(_userName)) {
			String query = "SELECT * FROM users WHERE name='";
			query += _userName + "';";
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return new User(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5),
								rs.getDouble(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), 0);
					}
				}
			} catch (SQLException e) {
				return null;
			}
		}
		return null;
	}

	ArrayList<User> getAllNonHiddenUsersForTimes(int minutes, int power) {
		ArrayList<User> returnList = new ArrayList<User>();
		String query = "SELECT * FROM users where hidden<" + power + " AND lastconnected>=NOW()-INTERVAL " + minutes
				+ " MINUTE;";
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					// TODO Change to constant
					returnList.add(
							new User(rs.getString(1), rs.getInt(3), rs.getDouble(5), rs.getDouble(6), rs.getString(7)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return returnList;
	}

	ArrayList<User> getAllNonHiddenUsersForTimesInRange(int minutes, int power, Double latitude, Double longitude) {
		int radius = Commons.getScanRadiusFromPower(power);
		ArrayList<User> returnList = new ArrayList<User>();
		String query = "SELECT * FROM users where hidden<" + power + " AND lastconnected>=NOW()-INTERVAL " + minutes
				+ " MINUTE;";
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					User user = new User(rs.getString(1), rs.getInt(3), rs.getDouble(5), rs.getDouble(6),
							rs.getString(7));
					if (user.rangeBetweenUsers(latitude, longitude) < radius)
						returnList.add(user);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return returnList;
	}

	ArrayList<User> getAllUsers() {
		ArrayList<User> returnList = new ArrayList<User>();
		String query = "SELECT * FROM users;";
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					// TODO Change to constant
					returnList.add(
							new User(rs.getString(1), rs.getInt(3), rs.getDouble(5), rs.getDouble(6), rs.getString(7)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return returnList;
	}

	String getUserCurse(String _userName) {
		String r = getUserSomething(_userName,"curse");
		if (r!=null) return r;
		return "0";
	}
	
	String getUserVampireCaller(String _userName) {
		String r = getUserSomething(_userName,"vampirecall");
		if (r!=null) return r;
		return "0";
	}
	
	private String getUserSomething(String _userName, String _something) {
		if (isUserExist(_userName)) {
			String query = "SELECT " + _something+ " FROM users WHERE name='";
			query += _userName + "';";

			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return rs.getString(1);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	String getUserVampireSender(String _userName) {
		String r = getUserSomething(_userName,"vampiresend");
		if (r!=null) return r;
		return "0"; 
	}
	
	PowerSide getUserPowerside(String _userName) {
		String r = getUserSomething(_userName,"powerside");
		if (r!=null) return PowerSide.Human.toPowerside(Integer.parseInt(r));
		return PowerSide.Human; 
	}

	Double getUserLatitude(String _userName) {
		String r = getUserSomething(_userName,"latitude");
		if (r!=null) return Double.parseDouble(r);
		return 0.0;
	}

	Double getUserLongitude(String _userName) {
		String r = getUserSomething(_userName,"longitude");
		if (r!=null) return Double.parseDouble(r);
		return 0.0;
	}

	String getUserLastconnect(String _userName) {
		String r = getUserSomething(_userName,"lastconnected");
		if (r!=null) return r;
		return "Never";
	}

	String getUserAchievements(String _userName) {
		String r = getUserSomething(_userName,"achievements");
		if (r!=null) return r;
		return "";
	}

	void addUserAchievement(String _userName, String _achievement) {
		String currentAchievement = getUserAchievements(_userName);
		if (currentAchievement.equals(""))
			currentAchievement = _achievement;
		else
			currentAchievement += "," + _achievement;
		if (isUserExist(_userName)) {
			String query = "UPDATE users SET achievements=";
			query += "'" + currentAchievement + "' WHERE name=" + "'" + _userName + "';";
			try {
				con.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	Boolean isTableExist() {
		String query = "SHOW TABLES LIKE 'users';";
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

		System.out.println("Table users NOT exist");
		return false;
	}
	
	Boolean isUserExist(String _userName) {
		String query = "SELECT count(*) FROM users WHERE name='";
		query += _userName + "'" + ";";

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					return rs.getInt(1) == 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	Boolean isUserPasswordCorrect(String _userName, String _password) {
		String r = getUserSomething(_userName,"password");
		if (r!=null) return r.equals(_password);
		return false;
	}

	int getSuperUser(String _userName) {
		String r = getUserSomething(_userName,"superuser");
		if (r!=null) return Integer.parseInt(r);
		return 0;
	}

	void writeUserCoordinates(String userName, Double latitude, Double longitude) {
		if (isUserExist(userName)) {
			String query = "UPDATE users SET latitude=";
			query += latitude + ",longitude=" + longitude + " WHERE name=" + "'" + userName + "';";
			try {
				// executing SELECT query
				con.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	void writeUserCurse(String userName, String curse) {
		if (isUserExist(userName)) {
			String query = "UPDATE users SET curse='" + curse + "' WHERE name=" + "'" + userName + "';";
			try {
				// executing SELECT query
				con.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	void writeUserVampireCaller(String userName, String vampireName) {
		if (isUserExist(userName)) {
			String query = "UPDATE users SET vampirecall='" + vampireName + "' WHERE name=" + "'" + userName + "';";
			try {
				// executing SELECT query
				System.out.println("Query : " + query);
				con.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	void writeUserVampireSender(String userName, String vampireName) {
		if (isUserExist(userName)) {
			String query = "UPDATE users SET vampiresend='" + vampireName + "' WHERE name=" + "'" + userName + "';";
			try {
				// executing SELECT query
				System.out.println("Query : " + query);
				con.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	void writeUserHidden(String userName, int hidden) {
		if (isUserExist(userName)) {
			String howLong = Integer.toString(Commons.getProtectLongFromPower(hidden));
			String query = "UPDATE users SET hidden=" + hidden +", hiddenuntil = NOW()+INTERVAL " + howLong + " HOUR WHERE name=" + "'" + userName + "';";
			try {
				// executing SELECT query
				con.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	void endUserProtection(String _userName) {
		if (isUserExist(_userName)) {
			String query = "UPDATE users SET hidden=0 WHERE name='" + _userName + "' AND hiddenuntil<NOW();";
			try {
				// executing SELECT query
				con.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	void writeUserLastConneted(String userName) {
		if (isUserExist(userName)) {
			String query = "UPDATE users SET lastconnected=NOW() WHERE name=" + "'" + userName + "';";
			try {
				// executing SELECT query
				con.createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

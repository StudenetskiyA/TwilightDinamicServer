package ru.studenetskiy.code;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLUserHelper extends SQLHelper {

	SQLUserHelper(String databaseName, String url, String login, String password) {
		super(databaseName, url, login, password);
	}

	void addUser(User z) {
		String query = "INSERT INTO users (name,password,powerside,level,superuser,curse) VALUES(";
		query += "'" + z.name + "','" + z.password + "'," + z.powerSide + "," + z.level + "," + z.superuser + "','" + z.curse + "');";
		System.out.println("Query : " + query);
		try {
			// executing SELECT query
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
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
								rs.getDouble(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10));
					}
				}
			} catch (SQLException e) {
				return null;
			}
		}
		return null;
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
		if (isUserExist(_userName)) {
			String query = "SELECT curse FROM users WHERE name='";
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
		return "0";
	}

	
	PowerSide getUserPowerside(String _userName) {
		if (isUserExist(_userName)) {
			String query = "SELECT powerside FROM users WHERE name='";
			query += _userName + "'" + ";";

			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return PowerSide.Human.toPowerside(rs.getInt(1));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return PowerSide.Human;
	}

	Double getUserLatitude(String _userName) {
		if (isUserExist(_userName)) {
			String query = "SELECT latitude FROM users WHERE name='";
			query += _userName + "';";

			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return rs.getDouble(1);
					}
				}
			} catch (SQLException e) {
				return 0.0;
			}
		}
		return 0.0;
	}

	Double getUserLongitude(String _userName) {
		if (isUserExist(_userName)) {
			String query = "SELECT longitude FROM users WHERE name='";
			query += _userName + "';";
			System.out.println("GetLatitude " + query);
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return rs.getDouble(1);
					}
				}
			} catch (SQLException e) {
				return 0.0;
			}
		}
		return 0.0;
	}

	String getUserLastconnect(String _userName) {
		if (isUserExist(_userName)) {
			String query = "SELECT lastconnected FROM users WHERE name='";
			query += _userName + "'" + ";";

			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return rs.getString(1);
					}
				}
			} catch (SQLException e) {
				return "Never";
			}
		}
		return "Never";
	}

	String getUserAchievements(String _userName) {
		if (isUserExist(_userName)) {
			String query = "SELECT achievements FROM users WHERE name='";
			query += _userName + "'" + ";";
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null && !rs.getString(1).equals("null")) {
					while (rs.next()) {
						return rs.getString(1);
					}
				}
			} catch (SQLException e) {
				return "";
			}
		}
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
		if (isUserExist(_userName)) {
			String query = "SELECT password FROM users WHERE name='";
			query += _userName + "'" + ";";
			// System.out.println("Query : " + query);
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return rs.getString(1).equals(_password);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	Boolean isSuperUser(String _userName) {
		if (isUserExist(_userName)) {
			String query = "SELECT superuser FROM users WHERE name='";
			query += _userName + "'" + ";";
			// System.out.println("Query : " + query);
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					while (rs.next()) {
						return rs.getString(1).equals("1");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
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

	void writeUserCurse(String userName,String curse) {
		if (isUserExist(userName)) {
			String query = "UPDATE users SET curse='"+curse
					+ "' WHERE name=" + "'" + userName + "';";
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

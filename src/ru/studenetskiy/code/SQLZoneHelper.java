package ru.studenetskiy.code;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLZoneHelper extends SQLHelper {
	SQLZoneHelper(String databaseName, String url, String login, String password) {
		super(databaseName, url, login, password);
	}
	
	void deleteZone(String z) {
		String query = "DELETE FROM zones WHERE name='";
		query += z+"';";
	try {
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
	void updateZone(Zone z) {
		String query = "UPDATE zones SET name='" + z.name + "',textforhuman='"+z.textForHuman + "',textforlight='"+ z.textForLight+"',textfordark='"+z.textForDark+ "',latitude="+z.latitude+ ",longitude="+z.longitude+ ",radius="+z.radius+ ",priority="+z.priority+ ",achievement='"+z.achievement+"',system="+z.system+" WHERE name='"+z.name+"';";
		System.out.println("Query : " + query);
		try {
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	void addZone(Zone z) {
		String query = "INSERT INTO zones (name,textforhuman,textforlight,textfordark,latitude,longitude,radius,priority,achievement,system) VALUES(";
		query += "'" + z.name + "','" + z.textForHuman + "','" + z.textForLight + "','" + z.textForDark + "'," + z.latitude
				+ "," + z.longitude + "," + z.radius + "," +z.priority+",'"+z.achievement+ "',"+z.system+");";
		System.out.println("Query : " + query);
		try {
			con.createStatement().executeUpdate(query);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
	ArrayList<Zone> getZonesWithAcievementsList() {
		ArrayList<Zone> returnList = new ArrayList<Zone>();
		String query = "SELECT * FROM zones WHERE achievement!='' and system=0;";
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					// TODO Change to constant
					returnList.add(new Zone(rs.getString(1), rs.getDouble(6), rs.getDouble(7), rs.getDouble(5), rs.getString(2),
							rs.getString(3), rs.getString(4), rs.getInt(8), rs.getString(9),rs.getInt(10)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return returnList;
	}	
	
	Boolean isZoneExist(String _zoneName) {
		String query = "SELECT count(*) FROM zones WHERE name='";
		query += _zoneName + "'" + ";";

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

	Boolean isSystemZoneExist(String _zoneName) {
		String query = "SELECT count(*) FROM zones WHERE system=1 and name='";
		query += _zoneName + "'" + ";";

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
	
	ArrayList<Zone> getZonesListSortByPriority() {
		ArrayList<Zone> returnList = new ArrayList<Zone>();
		String query = "SELECT * FROM zones WHERE priority>=0 and system=0 ORDER BY priority DESC;";
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					// TODO Change to constant
					returnList.add(new Zone(rs.getString(1), rs.getDouble(6), rs.getDouble(7), rs.getDouble(5), rs.getString(2),
							rs.getString(3), rs.getString(4), rs.getInt(8), rs.getString(9),rs.getInt(10)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return returnList;
	}

	int getZonePriority(String name) {
		String query = "SELECT priority FROM zones WHERE name='"+name+"';";
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					// TODO Change to constant
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	Zone getZone(String name) {
		String query = "SELECT * FROM zones WHERE name='"+name+"';";
		try {
			ResultSet rs = con.createStatement().executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					// TODO Change to constant
					return new Zone(rs.getString(1), rs.getDouble(6), rs.getDouble(7), rs.getDouble(5), rs.getString(2),
							rs.getString(3), rs.getString(4), rs.getInt(8), rs.getString(9),rs.getInt(10));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	Zone getFreeZone() {
		//Zone table must have 'free zone'!
		return getZone("free zone");	
		}
	
	Zone isInZoneOrFreeZone(Double latitude, Double longitude) {
		ArrayList<Zone> zonesList = getZonesListSortByPriority();
		for (int i = 0; i < zonesList.size(); i++) {
			if (zonesList.get(i).isInZone(latitude, longitude))
				return zonesList.get(i);
		}
		//If you are in no zone - return free-zone
		return getFreeZone();
	}

}

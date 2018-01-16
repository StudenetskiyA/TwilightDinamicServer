package ru.studenetskiy.code;

import java.io.IOException;
import java.util.ArrayList;

import ru.studenetskiy.code.SQLLogingHelper.LogingRecord;
import ru.studenetskiy.code.CommandClass.CommandUserHere;

public class ClientMessageHandler {
	ArrayList<String> parameter = new ArrayList<String>();
	String command;
	TwilightServerEndpoint server;

	ClientMessageHandler(TwilightServerEndpoint server, String message) {
		this.server = server;
		parameter = Commons.getTextBetween(message);
		command = Commons.getTextCommand(message);
	}

	void proceed() throws IOException {
		// TODO Move this
		if (!Commons.sql.connect()) {
			server.sendMessage("MESSAGE(server database damaged - cant access.)");
			return;
		}
		if (!Commons.sqlZone.connect()) {
			server.sendMessage("MESSAGE(server database damaged - cant access.)");
			return;
		}
		if (!Commons.sqlLogs.connect()) {
			server.sendMessage("MESSAGE(server database damaged - cant access.)");
			return;
		}

		// If user exist and password correct
		if (Commons.sql.isUserPasswordCorrect(parameter.get(0), parameter.get(1))) {
			server.sendMessage("Password correct.");
			// Change coordinates and last connect
			if (command.equals("ADDNEWZONE")) {
				// TODO Command class
				if (parameter.get(10).equals("none"))
					parameter.set(10, "");
				if (!Commons.sqlZone.isZoneExist(parameter.get(2))) {
					Commons.sqlZone.addZone(new Zone(parameter.get(2), Double.parseDouble(parameter.get(3)),
							Double.parseDouble(parameter.get(4)), Double.parseDouble(parameter.get(5)),
							parameter.get(6), parameter.get(7), parameter.get(8), Integer.parseInt(parameter.get(9)),
							parameter.get(10), Integer.parseInt(parameter.get(11))));
					server.sendMessage("MESSAGE(Зона добавлена корректно.)");
					Commons.sqlLogs.addRecord(new LogingRecord(Commons.now(), "ADDZONE", parameter.get(2),
							Double.parseDouble(parameter.get(3)), Double.parseDouble(parameter.get(4)),
							Double.parseDouble(parameter.get(5))));
				} else {
					Commons.sqlZone.updateZone(new Zone(parameter.get(2), Double.parseDouble(parameter.get(3)),
							Double.parseDouble(parameter.get(4)), Double.parseDouble(parameter.get(5)),
							parameter.get(6), parameter.get(7), parameter.get(8), Integer.parseInt(parameter.get(9)),
							parameter.get(10), Integer.parseInt(parameter.get(11))));
					server.sendMessage("MESSAGE(Зона обновлена корректно.)");
					Commons.sqlLogs.addRecord(new LogingRecord(Commons.now(), "UPDZONE", parameter.get(2),
							Double.parseDouble(parameter.get(3)), Double.parseDouble(parameter.get(4)),
							Double.parseDouble(parameter.get(5))));
				}
			} else if (command.equals("DELETEZONE")) {
				// TODO Command class
				if (Commons.sqlZone.isZoneExist(parameter.get(2))) {
					Commons.sqlZone.deleteZone(parameter.get(2));
					server.sendMessage("MESSAGE(Зона удалена корректно.)");
					Commons.sqlLogs.addRecord(new LogingRecord(Commons.now(), "DELZONE", parameter.get(2),
							Double.parseDouble(parameter.get(3)), Double.parseDouble(parameter.get(4)),
							Double.parseDouble(parameter.get(5))));
				} else {
					server.sendMessage("MESSAGE(Зона '" + parameter.get(2) + "' не существует.)");
				}
			} else if (command.equals("ADDUSER")) {
				// TODO Command class?
				if (!Commons.sql.isUserExist(parameter.get(2))) {
					User u = new User(parameter.get(2), parameter.get(3), Integer.parseInt(parameter.get(4)),
							Integer.parseInt(parameter.get(5)));
					Commons.sql.addUser(u);
					server.sendMessage("MESSAGE(Игрок добавлен корректно.)");
				} else {
					server.sendMessage("MESSAGE(Игрок '" + parameter.get(2) + "' уже существует.)");
				}
			} else if (command.equals("MAKECURSE")) {
				// TODO Optimize sql query
				if (Commons.sql.isUserExist(parameter.get(2))) {
					if (Commons.sqlZone.isSystemZoneExist(parameter.get(3))) {
						if (!Commons.sql.getUserCurse(parameter.get(2)).equals("0")) {
							// If player already have curse.
							int cc = Commons.sqlZone.getZonePriority(Commons.sql.getUserCurse(parameter.get(2)));
							int nc = Commons.sqlZone.getZonePriority(parameter.get(3));
							if (nc > cc) {
								if (parameter.get(3).contains("Снять заклятие ")) {
									Commons.sql.writeUserCurse(parameter.get(2), "0");
									server.sendMessage("MESSAGE(Заклятие успешно снято.)");
								} else {
									Commons.sql.writeUserCurse(parameter.get(2), parameter.get(3));
									server.sendMessage("MESSAGE(Проклятие успешно наложено.)");
								}
							} else {
								server.sendMessage("MESSAGE(На игрока действует что-то более сильное.)");
							}
						} else {
							Commons.sql.writeUserCurse(parameter.get(2), parameter.get(3));
							server.sendMessage("MESSAGE(Проклятие успешно наложено.)");
						}
					} else {
						server.sendMessage("MESSAGE(Проклятие '" + parameter.get(3) + "' не найдено.)");
					}
				} else {
					server.sendMessage("MESSAGE(Игрок '" + parameter.get(2) + "' не найден.)");
				}
			} else if (command.equals("SEARCHALL")) {
				// Users
				ArrayList<User> result = new ArrayList<User>();
				result = Commons.sql.getAllUsers();
				for (User user : result) {
					server.sendMessage("SEARCHUSER(" + user.formatForSearchUser() + ")");
				}
				// Zones
				ArrayList<Zone> resultZ = new ArrayList<Zone>();
				resultZ = Commons.sqlZone.getZonesListSortByPriority();
				for (Zone zone : resultZ) {
					server.sendMessage("SEARCHZONE(" + zone.formatForSearchUser() + ")");
				}
			} else if (command.equals("SEARCHUSER")) {
				if (Commons.sql.isUserExist(parameter.get(2))) {
					User user = Commons.sql.getUser(parameter.get(2));
					server.sendMessage("SEARCHUSER(" + user.formatForSearchUser() + ")");
				} else {
					server.sendMessage("MESSAGE(Игрок с именем '" + parameter.get(2) + "' не найден.)");
				}
			} else if (command.equals("CONNECT")) {
				CommandUserHere command = CommandUserHere.createCommandFromString(parameter);
				Commons.sql.writeUserLastConneted(command.userName);
				server.sendMessage("SUPERUSER(+"+Commons.sql.getSuperUser(command.userName)+")");
			} else {
				// USER(...)
				CommandUserHere command = CommandUserHere.createCommandFromString(parameter);
				if (command.latitude != 0.0 && command.longitude != 0.0) {
					Commons.sql.writeUserCoordinates(command.userName, command.latitude, command.longitude);
					Commons.sqlLogs.addRecord(new LogingRecord(Commons.now(), "USER", command.userName,
							command.latitude, command.longitude, 0.0));
				}
				Commons.sql.writeUserLastConneted(command.userName);
				// and check enter to zone
				Zone currentZone = Commons.sqlZone.isInZoneOrFreeZone(command.latitude, command.longitude);
				if (currentZone == null) {
					server.sendMessage("MESSAGE(server database damaged - no free zone.)");
					return;
				}
				if (currentZone.name.equals(command.userName)) {
					// Player can't see you own mobile zone, like curse etc.
				} else {
					// check powerside
					PowerSide side = Commons.sql.getUserPowerside(command.userName);
					if (side == PowerSide.Human && !currentZone.textForHuman.equals("")) {
						server.sendMessage("ZONE(" + currentZone.textForHuman + ")");
					} else if (side == PowerSide.Light && !currentZone.textForLight.equals("")) {
						server.sendMessage("ZONE(" + currentZone.textForLight + ")");
					} else if (side == PowerSide.Dark && !currentZone.textForDark.equals("")) {
						server.sendMessage("ZONE(" + currentZone.textForDark + ")");
					} else {
						server.sendMessage("ZONE(" + Commons.sqlZone.getFreeZone().textForHuman + ")");
					}
				}
				// Achievements
				// You must see all zones, never mind priority!!!
				ArrayList<Zone> allZones = Commons.sqlZone.getZonesWithAcievementsList();

				ArrayList<Zone> hereZones = new ArrayList<Zone>();
				for (Zone zone : allZones) {
					if (zone.isInZone(command.latitude, command.longitude))
						hereZones.add(zone);
				}
				String achievements = Commons.sql.getUserAchievements(command.userName);
				for (Zone zone : hereZones) {
					if (!achievements.contains(zone.achievement)) {
						// Add new achievement to user.
						Commons.sql.addUserAchievement(command.userName, zone.achievement);
					}
				}
				// Mobile Curse
				if (command.latitude != 0 && command.longitude != 0) {
					String curse = Commons.sql.getUserCurse(command.userName);
					if (!curse.equals("0")) {
						if (Commons.sqlZone.isZoneExist(curse)) {
							// System zone for this curse must be exist
							if (!Commons.sqlZone.isZoneExist(command.userName)) {
								Zone z = Commons.sqlZone.getZone(curse);
								// Copy zone from system
								z.latitude = command.latitude;
								z.longitude = command.longitude;
								z.name = command.userName;
								z.system = 0;
								Commons.sqlZone.addZone(z);
							} else {
								Zone z = Commons.sqlZone.getZone(command.userName);
								// Copy zone from past one
								z.latitude = command.latitude;
								z.longitude = command.longitude;
								z.system = 0;
								Commons.sqlZone.updateZone(z);
							}
						}
					}
				}
			}
		} else
			server.sendMessage("MESSAGE(Неправильный пароль.)");

		// TODO Move this
		Commons.sql.disconnect();
		Commons.sqlZone.disconnect();
		Commons.sqlLogs.disconnect();
	}
}

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
			server.sendMessage("MESSAGE(#damage)");
			return;
		}
		if (!Commons.sqlZone.connect()) {
			server.sendMessage("MESSAGE(#damage)");
			return;
		}
		if (!Commons.sqlLogs.connect()) {
			server.sendMessage("MESSAGE(#damage)");
			return;
		}

		//Check empty server and fill if it needs!
		if (!Commons.sql.isTableExist()) Commons.sql.firstInitUsersTable();
		if (!Commons.sqlZone.isTableExist()) Commons.sqlZone.firstInitZonesTable();
		if (!Commons.sqlLogs.isTableExist()) Commons.sqlLogs.firstInitZonesTable();
		
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
					Commons.sqlLogs.addRecord(new LogingRecord(Commons.now(), "ADDZONE", parameter.get(2),
							Double.parseDouble(parameter.get(3)), Double.parseDouble(parameter.get(4)),
							Double.parseDouble(parameter.get(5))));
					server.sendMessage("MESSAGE(#newzonecorrect)");
				} else {
					Commons.sqlZone.updateZone(new Zone(parameter.get(2), Double.parseDouble(parameter.get(3)),
							Double.parseDouble(parameter.get(4)), Double.parseDouble(parameter.get(5)),
							parameter.get(6), parameter.get(7), parameter.get(8), Integer.parseInt(parameter.get(9)),
							parameter.get(10), Integer.parseInt(parameter.get(11))));
					server.sendMessage("MESSAGE(#updatezonecorrect)");
					Commons.sqlLogs.addRecord(new LogingRecord(Commons.now(), "UPDZONE", parameter.get(2),
							Double.parseDouble(parameter.get(3)), Double.parseDouble(parameter.get(4)),
							Double.parseDouble(parameter.get(5))));
				}
			} else if (command.equals("DELETEZONE")) {
				// TODO Command class
				if (Commons.sqlZone.isZoneExist(parameter.get(2))) {
					Commons.sqlZone.deleteZone(parameter.get(2));
					server.sendMessage("MESSAGE(#updatezonecorrect)");
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
			} else if (command.equals("DIE")) {
				Commons.sql.deleteUser(parameter.get(0));
				//Clear vampire call
				Commons.sql.clearVampireCall(parameter.get(0));
				server.sendMessage("MESSAGE(Игрок умер корректно.)");
			} else if (command.equals("MAKECURSE")) {
				// TODO Optimize sql query
				if (Commons.sql.isUserExist(parameter.get(2)) && Commons.sqlZone.isSystemZoneExist(parameter.get(3))) {
					if (parameter.get(3).contains(Commons.DISPELL_CAST)) {
						if (!Commons.sql.getUserCurse(parameter.get(2)).equals("0")) {
							// If player already have curse.
							int cc = Commons.sqlZone.getZonePriority(Commons.sql.getUserCurse(parameter.get(2)));
							int nc = Commons.sqlZone.getZonePriority(parameter.get(3));
							if (nc > cc) {
								Commons.sql.writeUserCurse(parameter.get(2), "0");
								server.sendMessage("MESSAGE(Заклятие успешно снято.)");
							} else {
								server.sendMessage("MESSAGE(На игрока действует что-то более сильное.)");
							}
						} else {
							server.sendMessage("MESSAGE(Игрок не проклят.)");
						}
					} else if (parameter.get(3).contains(Commons.HIDE_CAST)) {
						//Power
						System.out.println("Power = '"+ parameter.get(3).substring(parameter.get(3).indexOf(Commons.HIDE_CAST)+Commons.HIDE_CAST.length()+1, parameter.get(3).length())+"'");
						int power = Integer.parseInt(parameter.get(3).substring(parameter.get(3).indexOf(Commons.HIDE_CAST)+Commons.HIDE_CAST.length()+1, parameter.get(3).length()));
						Commons.sql.writeUserHidden(parameter.get(2), power);
						server.sendMessage("MESSAGE(Заклятие защиты от обнаружения успешно наложено.)");
					} else { // Just curse
						if (!Commons.sql.getUserCurse(parameter.get(2)).equals("0")) {
							// If player already have curse.
							int cc = Commons.sqlZone.getZonePriority(Commons.sql.getUserCurse(parameter.get(2)));
							int nc = Commons.sqlZone.getZonePriority(parameter.get(3));
							if (nc > cc) {
								Commons.sql.writeUserCurse(parameter.get(2), parameter.get(3));
								server.sendMessage("MESSAGE(Проклятие успешно наложено.)");
							} else {
								server.sendMessage(
										"MESSAGE(На игрока действует что-то более сильное или такой же силы.)");
							}
						} else {
							Commons.sql.writeUserCurse(parameter.get(2), parameter.get(3));
							server.sendMessage("MESSAGE(Проклятие успешно наложено.)");
						}
					}
				} else {
					server.sendMessage("MESSAGE(Игрок или проклятие не найдено.)");
				}
			} else if (command.equals("VAMPIRESEND")) {
				if (Commons.sql.getUserVampireSender(parameter.get(0)).equals("0")) {
					if (Commons.sql.isUserExist(parameter.get(2))) {
						if (!Commons.sql.getUserVampireCaller(parameter.get(2)).equals("0")) {
							server.sendMessage("MESSAGE(На игрока уже действует зов.)");
						} else {
							Commons.sql.writeUserVampireCaller(parameter.get(2), parameter.get(0));
							Commons.sql.writeUserVampireSender(parameter.get(0), parameter.get(2));
							server.sendMessage("MESSAGE(Зов успешно послан.)");
							server.sendMessage("VAMPIRESEND(" + parameter.get(2) + ")");
						}
					} else {
						server.sendMessage("MESSAGE(Игрок с именем '" + parameter.get(2) + "' не найден.)");
					}
				} else {
					if (Commons.sql.getUserVampireCaller(parameter.get(2)).equals(parameter.get(0))) {
						Commons.sql.writeUserVampireCaller(parameter.get(2), "0");
						Commons.sql.writeUserVampireSender(parameter.get(0), "0");
						server.sendMessage("MESSAGE(Зов успешно снят.)");
						server.sendMessage("VAMPIRESEND(0)");
					} else {
						server.sendMessage("MESSAGE(Одновременно можно звать только одного игрока.)");
					}
				}
			} else if (command.equals("SEARCHUSER")) {
				if (Commons.sql.isUserExist(parameter.get(2))) {
					int power = Integer.parseInt(parameter.get(3));
					User user = Commons.sql.getNonHiddenUser(parameter.get(2), power);
					if (user != null) {
						int radius = Commons.getSearchRadiusFromPower(power);
						if (user.rangeBetweenUsers(Commons.sql.getUserLatitude(parameter.get(0)),
								Commons.sql.getUserLongitude(parameter.get(0))) < radius || power == 9)
							server.sendMessage("SEARCHUSER(" + user.formatForSearchUser(power) + ")");
						else
							server.sendMessage("MESSAGE(Игрок вне радиуса поиска или под защитой от обнаружения.)");
					} else
						server.sendMessage("MESSAGE(Игрок вне радиуса поиска или под защитой от обнаружения.)");
				} else {
					server.sendMessage("MESSAGE(Игрок с именем '" + parameter.get(2) + "' не найден.)");
				}
			} else if (command.equals("SCANUSER")) {
				int power = Integer.parseInt(parameter.get(2));
				// Users
				ArrayList<User> result = new ArrayList<User>();
				result = Commons.sql.getAllNonHiddenUsersForTimesInRange(Commons.MINUTES_FOR_SEARCH_IN_PAST, power,
						Commons.sql.getUserLatitude(parameter.get(0)), Commons.sql.getUserLongitude(parameter.get(0)));
				for (User user : result) {
					server.sendMessage("SEARCHUSER(" + user.formatForSearchUser(power) + ")");
				}
				if (power > 1) {
					// Zones
					ArrayList<Zone> resultZ = new ArrayList<Zone>();
					resultZ = Commons.sqlZone.getZonesListInRange(power, Commons.sql.getUserLatitude(parameter.get(0)),
							Commons.sql.getUserLongitude(parameter.get(0)));
					for (Zone zone : resultZ) {
						server.sendMessage("SEARCHZONE(" + zone.formatForSearchUser(power) + ")");
					}
				}
			} else if (command.equals("CONNECT")) {
				Commons.sql.writeUserLastConneted(parameter.get(0));
				server.sendMessage("SUPERUSER(" + Commons.sql.getSuperUser(parameter.get(0)) + ")");
			} else if (command.equals("USER")) {
				CommandUserHere command = CommandUserHere.createCommandFromString(parameter);
				if (command.latitude != 0.0 && command.longitude != 0.0) {
					Commons.sql.writeUserCoordinates(command.userName, command.latitude, command.longitude);
					Commons.sqlLogs.addRecord(new LogingRecord(Commons.now(), "USER", command.userName,
							command.latitude, command.longitude, 0.0));
				}
				Commons.sql.writeUserLastConneted(command.userName);
				// and check enter to zone
				Zone currentZone = Commons.sqlZone.isInZoneOrFreeZone(command.latitude, command.longitude,
						command.userName);
				if (currentZone == null) {
					server.sendMessage("MESSAGE(server database damaged - no free zone.)");
					return;
				}
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
				// Vampire call and send
				String vc = Commons.sql.getUserVampireCaller(command.userName);
				if (!vc.equals("0")) {
					server.sendMessage("VAMPIRECALL(" + vc + ")");
				}
				String vs = Commons.sql.getUserVampireSender(command.userName);
				if (!vs.equals("0")) {
					server.sendMessage("VAMPIRESEND(" + vs + ")");
				}
				//End of protection
				Commons.sql.endUserProtection(command.userName);
			}
		} else {
			server.sendMessage("MESSAGE(Неправильный логин или пароль.)");
		}

		// TODO Move this
		Commons.sql.disconnect();
		Commons.sqlZone.disconnect();
		Commons.sqlLogs.disconnect();
	}
}
/*
* Aoba Hacked Client
* Copyright (C) 2019-2023 coltonk9043
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.aoba.cmd.commands;

import net.aoba.Aoba;
import net.aoba.cmd.Command;
import net.aoba.cmd.CommandManager;
import net.aoba.module.modules.combat.Aimbot;

public class CmdAimbot extends Command {

	public CmdAimbot() {
		super("aimbot", "Allows the player to see chest locations through ESP");
	}

	@Override
	public void runCommand(String[] parameters) {
		Aimbot module = (Aimbot) Aoba.getInstance().moduleManager.aimbot;
		if (parameters.length == 2) {
			switch (parameters[0]) {
			case "toggle":
				String state = parameters[1].toLowerCase();
				if (state.equals("on")) {
					module.setState(true);
					CommandManager.sendChatMessage("Aimbot toggled ON");
				} else if (state.equals("off")) {
					module.setState(false);
					CommandManager.sendChatMessage("Aimbot toggled OFF");
				} else {
					CommandManager.sendChatMessage("Invalid value. [ON/OFF]");
				}
				break;
			default:
				CommandManager.sendChatMessage("Invalid Usage! Usage: .aoba aimbot [toggle/mode] [value]");
				break;
			}
		} else {
			CommandManager.sendChatMessage("Invalid Usage! Usage: .aoba aimbot [toggle/mode] [value]");
		}
	}

	@Override
	public String[] getAutocorrect(String previousParameter) {
		// TODO Auto-generated method stub
		return null;
	}
}

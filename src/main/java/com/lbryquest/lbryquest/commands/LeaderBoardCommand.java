package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.LBRYQuest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.text.*;


public class LeaderBoardCommand extends CommandAction {
    private LBRYQuest lbryQuest;

    public LeaderBoardCommand(LBRYQuest plugin) {
        this.lbryQuest = plugin;
    }
    public boolean run(CommandSender sender, Command cmd, String label, String[] args, Player player) {
	Set<String> ownerList = LBRYQuest.REDIS.keys("LeaderBoard *");
			int iter=1;
				for (String tempOwnerList : ownerList) {
		if ((LBRYQuest.REDIS.get("LeaderBoard "+iter)) != null) {					
			String tempString =  LBRYQuest.REDIS.get("LeaderBoard "+iter);
			String lastWord = tempString.substring(tempString.lastIndexOf(" ")+1);
		double amtUSD = (double)(lbryQuest.exRate * (Long.parseLong(lastWord) * 0.00000001));
		DecimalFormat df = new DecimalFormat("#.##");
					sender.sendMessage(ChatColor.GREEN +" "+iter+") "+ LBRYQuest.REDIS.get("LeaderBoard "+iter) + " now $" + df.format(amtUSD));
		}
					iter++;
				}
	return true;	
    }
}

package com.john4096.zLOBBY.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public final class CommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("zlobby")) {
            if (args.length == 1) {
                completions.add("reload");
                completions.add("info");
                completions.add("debugger");
            }else if(args.length==2){
                if(args[0].equalsIgnoreCase("debugger")){
                    completions.add("on");
                    completions.add("off");
                }
            }
        }
        return completions;
    }
}

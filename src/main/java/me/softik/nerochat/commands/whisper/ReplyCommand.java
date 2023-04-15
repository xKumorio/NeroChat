package me.softik.nerochat.commands.whisper;

import lombok.RequiredArgsConstructor;
import me.softik.nerochat.NeroChat;
import me.softik.nerochat.utils.CommonTool;
import me.softik.nerochat.utils.LanguageTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReplyCommand implements CommandExecutor, TabExecutor {
    private final NeroChat plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Optional<CommandSender> lastMessagedOf = plugin.getCacheTool().getLastMessagedOf(sender);

        if (lastMessagedOf.isPresent()) {
            if (plugin.getIgnoreTool().isIgnored(sender, lastMessagedOf.get())) {
                if (plugin.getConfig().getBoolean("onlyhidepms")) {
                    CommonTool.sendSender(sender, CommonTool.mergeArgs(args, 0), lastMessagedOf.get());
                } else {
                    sender.sendMessage(CommonTool.getPrefix() + "This person ignores you!");
                }
            } else if (plugin.getIgnoreTool().isIgnored(lastMessagedOf.get(), sender)) {
                sender.sendMessage(CommonTool.getPrefix() + "You ignore this person!");
            } else {
                if (args.length > 0) {
                    CommonTool.sendWhisperTo(sender, CommonTool.mergeArgs(args, 0), lastMessagedOf.get());
                } else {
                    return false;
                }
            }
        } else {
            sender.sendMessage(LanguageTool.getMessage("notonline"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}

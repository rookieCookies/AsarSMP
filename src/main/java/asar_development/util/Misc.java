package asar_development.util;

import asar_development.asarsmp.AsarSMP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Misc {
    private static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
    private static final TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>();
    private static List<String> emojiList = Config.getStringList("emojis");

    static {
        ROMAN_NUMERALS.put(1000, "M");
        ROMAN_NUMERALS.put(900, "CM");
        ROMAN_NUMERALS.put(500, "D");
        ROMAN_NUMERALS.put(400, "CD");
        ROMAN_NUMERALS.put(100, "C");
        ROMAN_NUMERALS.put(90, "XC");
        ROMAN_NUMERALS.put(50, "L");
        ROMAN_NUMERALS.put(40, "XL");
        ROMAN_NUMERALS.put(10, "X");
        ROMAN_NUMERALS.put(9, "IX");
        ROMAN_NUMERALS.put(5, "V");
        ROMAN_NUMERALS.put(4, "IV");
        ROMAN_NUMERALS.put(1, "I");
        ROMAN_NUMERALS.put(0, "0");
    }

    Misc() { throw new IllegalStateException("Utility class"); }

    public static double fixDouble(double i) {
        i *= Math.pow(10, 2);
        i = Math.round(i);
        i /= Math.pow(10, 2);
        return i;
    }
    @SuppressWarnings("unused")
    public static String getFormattedNumber(double number) {
        var nf = NumberFormat.getInstance(new Locale("en", "US"));
        return nf.format(fixDouble(number));
    }
    @SuppressWarnings("unused")
    public static String getFormattedNumber(float number) {
        var nf = NumberFormat.getInstance(new Locale("en", "US"));
        return nf.format(fixDouble(number));
    }
    @SuppressWarnings("unused")
    public static String toRoman(int number) {
        int l = ROMAN_NUMERALS.floorKey(number);
        if (number == l) {
            return ROMAN_NUMERALS.get(number);
        }
        return ROMAN_NUMERALS.get(l) + toRoman(number - l);
    }
    public static String coloured(String msg) {
        return hex(addEmoji(msg));
    }
    public static String colouredNoEmoji(String msg) {
        return hex(msg);
    }

    public static String hex(String message) {
        if (message == null) {
            return null;
        }
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            ChatColor color = ChatColor.of(hexCode);
            message = message.replace(hexCode, color.toString());
            matcher = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String getMessage(String path) {
        path = "messages." + path;
        String message = AsarSMP.getInstance().getConfig().getString(path);
        if (message == null) {
            AsarSMP.getInstance().getLogger().log(Level.WARNING,
                    "There is a message missing in config file! Path: {0}",
                    path.replace(".", " > "));
            return "";
        }
        return coloured(message);
    }

    public static void reloadEmojis() {
        emojiList = Config.getStringList("emojis");
    }
    public static String addEmoji(String str) {
        return addEmoji(null, str, false);
    }
    public static String addEmoji(Player p, String str) {
        return addEmoji(p, str, true);
    }
    private static String addEmoji(Player p, String str, boolean requirePermission) {
        for (String i : emojiList) {
            List<String> list = List.of(i.split("\\|"));
            if (requirePermission &&
                    (!p.hasPermission("server.chat.emoji." + list.get(2)) && !p.hasPermission("server.chat.emoji.*"))) {
                continue;
            }
            String syntax = list.get(1);
            String replacement = list.get(0);
            str = str
                    .replaceAll("!" + syntax, ChatColor.stripColor(Misc.colouredNoEmoji(replacement + "&f")))
                    .replaceAll(syntax, Misc.colouredNoEmoji(replacement));
        }
        return str;
    }
    public static String locationToString(Location loc) {
        return String.format(Locale.ROOT, "%d/%d/%d/%s", (int) loc.getX(), (int) loc.getY(), (int) loc.getZ(), Objects.requireNonNull(loc.getWorld()).getName());
    }
    public static Location stringToLocation(String str) {
        List<String> list = Arrays.asList(str.split("/"));
        if (list.size() != 4) {
            throw new IllegalArgumentException(String.format("Invalid string regex while converting to location [%s]", str));
        }
        return new Location(Bukkit.getWorld(list.get(3)), Double.parseDouble(list.get(0)), Double.parseDouble(list.get(1)), Double.parseDouble(list.get(2)));
    }
}

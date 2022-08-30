package com.sappyoak.soulbound;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Debugger {
    private final Map<UUID, Player> playersEnabledFor = new HashMap<>(); 
    private final Logger logger = Logger.getAnonymousLogger();
    private final SoulBound plugin;

    public Debugger(SoulBound plugin) {
        this.plugin = plugin;

    }

    public void init() {
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

        for (final Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        try {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            final File debugFolder = new File(plugin.getDataFolder() + "/debug");

            if (!debugFolder.exists() && !debugFolder.mkdirs()) {
                plugin.getLogger().info("Could not create the debug log folder: " + debugFolder);
            }

            final File logFile = new File(debugFolder, format.format(new Date()) + "debug.log");
            logFile.createNewFile();

            final FileHandler handler = new FileHandler(logFile.getAbsolutePath());
            handler.setFormatter(LogFormatter.create());
            logger.addHandler(handler);
            
        } catch (final IOException | SecurityException err) {
            plugin.getLogger().log(Level.SEVERE, "Error initializing debugger", err);
        }
    }

    public void addPlayerDebug(Player player) {
        playersEnabledFor.put(player.getUniqueId(), player);
    }

    public void removePlayerDebug(Player player) {
        playersEnabledFor.remove(player.getUniqueId());
    }

    public void log(Level level, String msg) {
        if (!plugin.getSettings().isDebug()) {
            return;
        }

        logger.log(level, msg);
        for (Player player : playersEnabledFor.values()) {
            player.sendMessage(plugin.getMessages().applyPrefix("[Debug]: " + msg));
        }
    }

    static class LogFormatter extends Formatter {
        private final SimpleDateFormat date;

        public static LogFormatter create() {
            return new LogFormatter();
        }

        private LogFormatter() {
            super();
            date = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
        }

        @Override
        public String format(final LogRecord record) {
            final StringBuilder builder = new StringBuilder();
            final Throwable exception = record.getThrown();

            builder.append(date.format(record.getMillis()));
            builder.append(" [");
            builder.append(record.getLevel().getLocalizedName().toUpperCase());
            builder.append("] ");
            builder.append(record.getMessage());
            builder.append('\n');

            if (exception != null) {
                final StringWriter writer = new StringWriter();
                exception.printStackTrace(new PrintWriter(writer));
                builder.append(writer);
            }

            return builder.toString();
        }
    }
}

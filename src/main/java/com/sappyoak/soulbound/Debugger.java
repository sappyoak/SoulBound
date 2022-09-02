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
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class Debugger {
    private final static Map<UUID, Player> playersEnabledFor = new HashMap<>(); 
    private final SoulBound plugin;
    private Logger logger;

    private boolean enabled = false;
    
    public Debugger(SoulBound plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        this.enabled = true;
        this.init();
    }

    public void disable() {
        this.enabled = false;

        if (logger != null) {
            final Handler[] handlers = logger.getHandlers().clone();
            for (final Handler handler : handlers) {
                logger.removeHandler(handler);
                handler.close();
            }
            
            logger = null;
        }

        for (final Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        playersEnabledFor.clear();
    }

    public void init() {
        if (logger == null) {
            logger = Logger.getAnonymousLogger();
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);

            for (final Handler handler : logger.getHandlers()) {
                logger.removeHandler(handler);
                handler.close();
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
    }

    public void addPlayerDebug(Player player) {
        playersEnabledFor.put(player.getUniqueId(), player);
    }

    public boolean hasPlayerDebug(Player player) {
        return playersEnabledFor.containsKey(player.getUniqueId());
    }

    public void removePlayerDebug(Player player) {
        playersEnabledFor.remove(player.getUniqueId());
    }

    public void log(String msg) {
        if (!enabled) {
            return;
        }

        log(Level.INFO, msg);
    }

    public void log(String msg, Throwable exception) {
        if (!enabled) {
            return;
        }

        logger.log(Level.SEVERE, msg, exception);
        plugin.getLogger().log(Level.SEVERE, msg, exception);
        logForPlayers(msg);
    }

    public void log(Level level, String msg) {
        if (!enabled) {
            return;
        }

        logger.log(level, msg);
        plugin.getLogger().log(level, msg);
        logForPlayers(msg);
    }

    public void log(Supplier<String> supplier) {
        if (!enabled) {
            return;
        }

        log(supplier.get());
    }

    public void log(Level level, Supplier<String> supplier) {
        if (!enabled) {
            return;
        }

        log(level, supplier.get());
    }

    public void log(Supplier<String> supplier, Throwable exception) {
        if (!enabled) {
            return;
        }

        log(supplier.get(), exception);
    }

    private void logForPlayers(String msg) {
        for (Player player : playersEnabledFor.values()) {
            player.sendMessage(plugin.messages.applyPrefix("<blue>[Debug]:</blue> " + msg));
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

package com.sappyoak.soulbound.util;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedBarChart;
import org.bstats.charts.CustomChart;
import org.bstats.charts.MultiLineChart;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.HashMap;

// @TODO create own custom metrics wrapper to parse this data... BStats really just doesn't offer enough 
// to be that great.
public class MetricsWrapper {
    private final JavaPlugin plugin;
    private final Metrics metrics;
    private final Map<String, Boolean> commands = new HashMap<>();

    public MetricsWrapper(final JavaPlugin plugin, final int pluginId) {
        this.plugin = plugin;
        this.metrics = new Metrics(plugin, pluginId);

        addVersionHistoryChart();
        addCommandChart();
    }

    public void markCommand(final String command, final boolean state) {
        commands.put(command, state);
    }

    public void addCustomChart(final CustomChart chart) {
        metrics.addCustomChart(chart);
    }

    private void addVersionHistoryChart() {
        metrics.addCustomChart(new MultiLineChart("versionHistory", () -> {
            final Map<String, Integer> result = new HashMap<>();
            result.put(plugin.getDescription().getVersion(), 1);
            return result;
        }));
    }

    private void addCommandChart() {
        for (final String command : plugin.getDescription().getCommands().keySet()) {
            markCommand(command, false);
        }

        metrics.addCustomChart(new AdvancedBarChart("commands", () -> {
            final Map<String, int[]> result = new HashMap<>();
            for (final Map.Entry<String, Boolean> entry : commands.entrySet()) {
                if (entry.getValue()) {
                    result.put(entry.getKey(), new int[]{1, 0});
                } else {
                    result.put(entry.getKey(), new int[]{0, 1});
                }
            }

            return result;
        }));
    }
}

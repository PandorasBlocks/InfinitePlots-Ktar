package uk.co.jacekk.bukkit.infiniteplots;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.permissions.PluginPermission;

/**
 * The {@link PluginPermission} used to control access to various features.
 */
public class Permission {
	
	public static final PluginPermission PLOT_CLAIM			= new PluginPermission("ktarplots.plot.claim",                  PermissionDefault.TRUE,		"Allows the player to claim plots");
public static final PluginPermission PLOT_UNCLAIM			= new PluginPermission("ktarplots.plot.unclaim",                PermissionDefault.TRUE,		"Allows the player to unclaim plots");
public static final PluginPermission PLOT_UNCLAIM_OTHERS		= new PluginPermission("ktarplots.plot.unclaim.others",         PermissionDefault.OP,		"Allows the player to unclaim other players plots");
public static final PluginPermission PLOT_ADD_BUILDER			= new PluginPermission("ktarplots.plot.add-builder",            PermissionDefault.TRUE,		"Allows the player to add builders to their plots");
public static final PluginPermission PLOT_REMOVE_BUILDER		= new PluginPermission("ktarplots.plot.remove-builder",         PermissionDefault.TRUE,		"Allows the player to remove builders from their plots");
public static final PluginPermission PLOT_FLAG				= new PluginPermission("ktarplots.plot.flag",                   PermissionDefault.OP,		"Allows the player to modify their plots flags");
public static final PluginPermission PLOT_FLAG_OTHER			= new PluginPermission("ktarplots.plot.flag.others",            PermissionDefault.OP,		"Allows the player to modify other players plot flags");
public static final PluginPermission PLOT_SET_BIOME			= new PluginPermission("ktarplots.plot.set-biome",              PermissionDefault.OP,		"Allows the player to set their plots biome");
public static final PluginPermission PLOT_SET_BIOME_OTHERS		= new PluginPermission("ktarplots.plot.set-biome.others",       PermissionDefault.OP,		"Allows the player to set biome of other player plots");
public static final PluginPermission PLOT_INFO				= new PluginPermission("ktarplots.plot.info",                   PermissionDefault.TRUE,		"Allows the player to view plot info");
public static final PluginPermission PLOT_RESET				= new PluginPermission("ktarplots.plot.reset",                  PermissionDefault.TRUE,		"Allows the player to regenerate their plots");
public static final PluginPermission PLOT_RESET_OTHER			= new PluginPermission("ktarplots.plot.reset.others",           PermissionDefault.OP,		"Allows the player to regenerate other players plots");
public static final PluginPermission PLOT_LIST				= new PluginPermission("ktarplots.plot.list",                   PermissionDefault.TRUE,		"Allows the player to list their plots");
public static final PluginPermission PLOT_LIST_OTHER			= new PluginPermission("ktarplots.plot.list.others",            PermissionDefault.OP,		"Allows the player to list another players plots");
public static final PluginPermission PLOT_CHANGE_WEATHER		= new PluginPermission("ktarplots.plot.weather",                PermissionDefault.OP,		"Allows the player to change the weather in their plot");
public static final PluginPermission PLOT_CHANGE_WEATHER_OTHERS         = new PluginPermission("ktarplots.plot.weather.others",         PermissionDefault.OP,		"Allows the player to change the weather inside others plot");
public static final PluginPermission PLOT_CHANGE_TIME			= new PluginPermission("ktarplots.plot.time",                   PermissionDefault.OP,		"Allows the player to change time in their plot");
public static final PluginPermission PLOT_CHANGE_TIME_OTHERS            = new PluginPermission("ktarplots.plot.time.others",            PermissionDefault.OP,		"Allows the player to change time inside others plot");
public static final PluginPermission PLOT_MERGE                         = new PluginPermission("ktarplots.plot.merge",                  PermissionDefault.OP,		"Allows the player to merge plots");
public static final PluginPermission PLOT_TELEPORT			= new PluginPermission("ktarplots.plot.teleport",               PermissionDefault.TRUE,		"Allows the player to teleport to their plots");
public static final PluginPermission PLOT_TELEPORT_OTHER		= new PluginPermission("ktarplots.plot.teleport.others",        PermissionDefault.OP,		"Allows the player to teleport to another players plots");
public static final PluginPermission PLOT_DECORATE			= new PluginPermission("ktarplots.plot.decorate",               PermissionDefault.OP,		"Allows the player to decorate plots");
public static final PluginPermission PLOT_DECORATE_OTHER		= new PluginPermission("ktarplots.plot.decorate.others",        PermissionDefault.OP,		"Allows the player to decorate another players plots");
public static final PluginPermission PLOT_PROTECTION			= new PluginPermission("ktarplots.plot.protection",             PermissionDefault.TRUE,		"Allows the player to modify a plots protection");
public static final PluginPermission PLOT_PROTECTION_OTHER		= new PluginPermission("ktarplots.plot.protection.others",      PermissionDefault.OP,		"Allows the player to modify another players plot protection");
public static final PluginPermission PLOT_BUILD_ALL			= new PluginPermission("ktarplots.plot.build-all",              PermissionDefault.OP,		"Allows the player to build in plots they do not own");
public static final PluginPermission PLOT_BYPASS_CLAIM_LIMIT            = new PluginPermission("ktarplots.plot.bypass-claim-limit",     PermissionDefault.OP,		"Allows the player to claim more plots than the limit");
public static final PluginPermission PLOT_PURGE				= new PluginPermission("ktarplots.plot.purge",                  PermissionDefault.OP,		"Allows the player to remove dead plots");
}

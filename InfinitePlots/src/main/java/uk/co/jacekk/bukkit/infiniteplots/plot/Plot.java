package uk.co.jacekk.bukkit.infiniteplots.plot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.BaseObject;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.infiniteplots.Config;
import uk.co.jacekk.bukkit.infiniteplots.InfinitePlots;
import uk.co.jacekk.bukkit.infiniteplots.Permission;
import uk.co.jacekk.bukkit.infiniteplots.flag.PlotFlag;
import uk.co.jacekk.bukkit.infiniteplots.generation.PlotsGenerator;
import uk.co.jacekk.bukkit.infiniteplots.plot.PlotLocation.Direction;
import uk.co.jacekk.bukkit.infiniteplots.plot.decorator.FlatPlotDecorator;

/**
 * Represents a plot in the world.
 */
public class Plot extends BaseObject<InfinitePlots> {

    private final File configFile;
    private final PluginConfig config;
    private final PlotLocation location;
    private final int size;
    private final PlotStats stats;
    private int[] plotLimits;
    private int[] buildLimits;

    public Plot(InfinitePlots plugin, File configFile, PluginConfig config) {
        super(plugin);

        this.configFile = configFile;
        this.config = config;
        this.location = new PlotLocation(config.getString(PlotConfig.LOCATION_WORLD_NAME), config.getInt(PlotConfig.LOCATION_X), config.getInt(PlotConfig.LOCATION_Z));
        this.size = ((PlotsGenerator) this.location.getWorld().getGenerator()).getGridSize();
        this.stats = new PlotStats(this, this.config);

        int x1 = (int) Math.floor(((this.location.getX() * this.size) / this.size) * this.size);
        int z1 = (int) Math.floor(((this.location.getZ() * this.size) / this.size) * this.size);
        int x2 = x1 + this.size;
        int z2 = z1 + this.size;

        this.plotLimits = new int[]{x1, z1, x2, z2};
        this.buildLimits = new int[]{x1 + 4, z1 + 4, x2 - 4, z2 - 4};
    }

    /**
     * Gets the file used to store this plots config.
     *
     * @return The {@link File}
     */
    public File getConfigFile() {
        return this.configFile;
    }

    /**
     * Gets the location of this plot.
     *
     * @return The {@link PlotLocation} of this plot.
     */
    public PlotLocation getLocation() {
        return this.location;
    }

    /**
     * Gets the {@link PlotStats} for this plot.
     *
     * @return The stats
     */
    public PlotStats getStats() {
        return this.stats;
    }

    /**
     * Gets the name of this plot
     *
     * @return The plot name
     */
    public String getName() {
        return this.config.getString(PlotConfig.INFO_NAME);
    }

    /**
     * Sets the name of this plot
     *
     * @param name The new name for the plot
     */
    public void setName(String name) {
        this.config.set(PlotConfig.INFO_NAME, name);
    }

    /**
     * Gets the limits of the plot.
     *
     * @return An array of X,Z coordinates [x1, z1, x2, z2]
     */
    public int[] getBuildLimits() {
        return this.buildLimits;
    }

    /**
     * Gets the player that is the admin of this plot.
     *
     * @return The player name.
     */
    public String getAdmin() {
        return this.config.getString(PlotConfig.AUTH_ADMIN_NAME);
    }

    /**
     * Sets the admin of this plot.
     *
     * @param admin The name of the player.
     */
    public void setAdmin(String admin) {
        this.config.set(PlotConfig.AUTH_ADMIN_NAME, admin);
    }

    public void setTime(long time) {
        this.config.set(PlotConfig.INFO_TIME, time);
    }

    public Long getTime() {
        return this.config.getLong(PlotConfig.INFO_TIME);
    }

    public void setWeather(boolean weather) {
        this.config.set(PlotConfig.INFO_WEATHER, weather);
    }

    public boolean getWeather() {
        return this.config.getBoolean(PlotConfig.INFO_WEATHER);
    }

    /**
     * Gets all of the players (not including the plot admin) that are allowed
     * to build in this plot.
     *
     * @return The list of player names.
     */
    public List<String> getBuilders() {
        return this.config.getStringList(PlotConfig.AUTH_BUILDER_NAMES);
    }

    /**
     * Checks to see if a player can build in this plot.
     *
     * <p>
     * A player that is not able to build should not be able to interact with
     * the environment in any way at all inside the plot area.
     * <p>
     *
     * @param playerName The name of the player to test.
     * @return True if the player can build, false if not.
     */
    public boolean canBuild(String playerName) {
        return (!this.isBuildProtected() || this.getAdmin().equalsIgnoreCase(playerName) || this.getBuilders().contains(playerName.toLowerCase()));
    }

    /**
     * Checks to see if a player can enter the plot.
     *
     * Note that this does not perform a permissions check.
     *
     * @param playerName The name of the player
     * @return True if they can enter false if not.
     */
    public boolean canEnter(String playerName) {
        return (!this.isEnterProtected() || this.canBuild(playerName));
    }

    /**
     * Sets if the plot is protected from people building in it or not
     *
     * @param enable The build protection state.
     */
    public void setBuildProtection(boolean enable) {
        this.config.set(PlotConfig.PROTECTION_BUILD, enable);
    }

    /**
     * Sets if the plot is protected from people entering it or not.
     *
     * @param enable The enter protection state.
     */
    public void setEnterProtection(boolean enable) {
        this.config.set(PlotConfig.PROTECTION_ENTER, enable);
    }

    /**
     * Gets the build protection state of the plot.
     *
     * If this is true then only the owner or players listed as a builder will
     * be allowed to modify the world inside the plot.
     *
     * @return The state.
     */
    public boolean isBuildProtected() {
        return this.config.getBoolean(PlotConfig.PROTECTION_BUILD);
    }

    /**
     * Gets the enter protection state of the plot.
     *
     * If this is true then only the owner or players listed as a builder will
     * be allowed to move into the plot area.
     *
     * @return The state.
     */
    public boolean isEnterProtected() {
        return this.config.getBoolean(PlotConfig.PROTECTION_ENTER);
    }

    /**
     * Adds a builder to this plot.
     *
     * @param playerName The name of the player to add.
     */
    public void addBuilder(String playerName) {
        List<String> builders = this.getBuilders();
        builders.add(playerName.toLowerCase());
        this.config.set(PlotConfig.AUTH_BUILDER_NAMES, builders);
    }

    /**
     * Removes a builder from this plot.
     *
     * @param playerName The name of the player to remove.
     */
    public void removeBuilder(String playerName) {
        List<String> builders = this.getBuilders();
        builders.remove(playerName.toLowerCase());
        this.config.set(PlotConfig.AUTH_BUILDER_NAMES, builders);
    }

    /**
     * Removes all builders from this plot.
     */
    public void removeAllBuilders() {
        this.config.set(PlotConfig.AUTH_BUILDER_NAMES, Arrays.asList(new String[0]));
    }

    /**
     * Checks to see if a player is within the buildable area of a plot.
     *
     * @param player The {@link Player} to check.
     * @param location The {@link Location} to check.
     * @return True if the player is in the area false if not.
     */
    public boolean withinBuildableArea(Player player, Location location) {
        String playerName = player.getName();
        int x = location.getBlockX();
        int z = location.getBlockZ();

        if (plugin.config.getBoolean(Config.CLAIM_PROTECT_PATHS)) {
            return (x >= this.buildLimits[0] && z >= this.buildLimits[1] && x <= this.buildLimits[2] && z <= this.buildLimits[3]);
        }

        if (x < this.plotLimits[0] || z < this.plotLimits[1] || x > this.plotLimits[2] || z > this.plotLimits[3]) {
            return false;
        }

        if (!Permission.PLOT_MERGE.has(player)) {
            return false;
        } else if (Permission.PLOT_MERGE.has(player)) {
            Plot plot0 = plugin.getPlotManager().getPlotAt(this.location.getRelative(Direction.WEST));
            Plot plot1 = plugin.getPlotManager().getPlotAt(this.location.getRelative(Direction.NORTH));
            Plot plot2 = plugin.getPlotManager().getPlotAt(this.location.getRelative(Direction.EAST));
            Plot plot3 = plugin.getPlotManager().getPlotAt(this.location.getRelative(Direction.SOUTH));

            if ((plot0 == null || !plot0.canBuild(playerName)) && x < this.buildLimits[0]) {
                return false;
            }

            if ((plot1 == null || !plot1.canBuild(playerName)) && z < this.buildLimits[1]) {
                return false;
            }

            if ((plot2 == null || !plot2.canBuild(playerName)) && x > this.buildLimits[2]) {
                return false;
            }

            if ((plot3 == null || !plot3.canBuild(playerName)) && z > this.buildLimits[3]) {
                return false;
            }

            return true;
        }
        return true;
    }

    /**
     * Checks to see if a specific flag is enabled for this plot.
     *
     * <p>
     * An enabled flag will allow the event it describes to take place,
     * </p>
     *
     * @param flag The flag to check
     * @return The value of the flag for this plot
     */
    public boolean isFlagEnabled(PlotFlag flag) {
        return this.config.getBoolean(flag.getConfigKey());
    }

    /**
     * Sets a flag for this plot.
     *
     * <p>
     * An enabled flag will allow the event it describes to take place,
     * </p>
     *
     * @param flag The flag to set.
     * @param value The value to set this flag to.
     */
    public void setFlag(PlotFlag flag, boolean value) {
        this.config.set(flag.getConfigKey(), value);
    }

    /**
     * Sets the biome for the plot.
     *
     * @param biome The biome to set.
     */
    public void setBiome(Biome biome) {
        World world = plugin.server.getWorld(this.location.getWorldName());

        for (int x = this.buildLimits[0]; x <= this.buildLimits[2]; ++x) {
            for (int z = this.buildLimits[1]; z <= this.buildLimits[3]; ++z) {
                world.setBiome(x, z, biome);
            }
        }
    }

    /**
     * Gets the biome for this plot
     *
     * @return The {@link Biome} used
     */
    public Biome getBiome() {
        World world = plugin.server.getWorld(this.location.getWorldName());
        return world.getBiome(this.buildLimits[0], this.buildLimits[1]);
    }

    /**
     * Regenerates the buildable region of this plot.
     */
    public void regenerate() {
        (new FlatPlotDecorator(plugin, Material.getMaterial(plugin.config.getInt(Config.BLOCKS_GROUND)), Material.getMaterial(plugin.config.getInt(Config.BLOCKS_SURFACE)), (byte) 0, (byte) 0)).decorate(this);
    }

    public HashMap<String, Plot> getAdjacentPlots(Plot plot) {
        HashMap<String, Plot> plots = new HashMap<String, Plot>();

        PlotLocation location = plot.getLocation();

        Plot plot0 = plugin.getPlotManager().getPlotAt(location.getRelative(PlotLocation.Direction.SOUTH));
        Plot plot1 = plugin.getPlotManager().getPlotAt(location.getRelative(PlotLocation.Direction.WEST));
        Plot plot2 = plugin.getPlotManager().getPlotAt(location.getRelative(PlotLocation.Direction.NORTH));
        Plot plot3 = plugin.getPlotManager().getPlotAt(location.getRelative(PlotLocation.Direction.EAST));

        if (plot0 != null && plot0.getAdmin().equals(plot.getAdmin())) {
            plots.put("South", plot0);
        }

        if (plot1 != null && plot1.getAdmin().equals(plot.getAdmin())) {
            plots.put("West", plot1);
        }

        if (plot2 != null && plot2.getAdmin().equals(plot.getAdmin())) {
            plots.put("North", plot2);
        }

        if (plot3 != null && plot3.getAdmin().equals(plot.getAdmin())) {
            plots.put("East", plot3);
        }


        return plots;
    }

    public Location[] getCorners(Plot plot) {
        PlotLocation plocation = plot.getLocation();

        int psize = ((PlotsGenerator) plocation.getWorld().getGenerator()).getGridSize();

        int x1 = (int) Math.floor(((plocation.getX() * psize) / psize) * psize);
        int z1 = (int) Math.floor(((plocation.getZ() * psize) / psize) * psize);
        int x2 = x1 + psize;
        int z2 = z1 + psize;

        int[] pbuildLimits = new int[]{x1 + 4, z1 + 4, x2 - 4, z2 - 4};

        int x3 = pbuildLimits[0];
        int z3 = pbuildLimits[1] + (psize - 7);
        int x4 = pbuildLimits[2];
        int z4 = pbuildLimits[3] - (psize - 7);

        int y = plugin.config.getInt(Config.GRID_HEIGHT);
        World world = plugin.getServer().getWorld(plot.getLocation().getWorldName());

        //if (plugin.config.getInt(Config.BLOCKS_UPPER_WALL) == 0){
        //      y += 1;
        //}else{
        //        y += 2;
        //}

        Block cornerOne = world.getBlockAt(pbuildLimits[0] - 1, y, pbuildLimits[1] - 1);
        Block cornerTwo = world.getBlockAt(pbuildLimits[2] + 1, y, pbuildLimits[3] + 1);
        Block cornerThree = world.getBlockAt(x3 - 1, y, z3);
        Block cornerFour = world.getBlockAt(x4 + 1, y, z4);

        Location[] corners = new Location[4];

        //0 = north west, 3 = north east, 1 = south east, 2 = south west
        
        
        //north west
        corners[0] = cornerOne.getLocation();
        
        //south east
        corners[1] = cornerTwo.getLocation();
        
        //south west
        corners[2] = cornerThree.getLocation();
        
        //north east
        corners[3] = cornerFour.getLocation();
        return corners;
    }

    public HashMap<String, Location[]> getCordsOfAdjacentPlots(Plot plot) {
        //First get plot, return all plots that are owned by owner.Check
        //Then, get the direction of the plot from first plot.Check
        //Get the corners of the plot, retrieve directs of plots from eachother. Check
        //Figure out which plots corners are next to eachother. Check
        //REGEN!
        PlotLocation plocation = plot.getLocation();

        HashMap<String, Location[]> locations = new HashMap<String, Location[]>();

        Plot plot0 = plugin.getPlotManager().getPlotAt(plocation.getRelative(PlotLocation.Direction.WEST));
        Plot plot1 = plugin.getPlotManager().getPlotAt(plocation.getRelative(PlotLocation.Direction.NORTH));
        Plot plot2 = plugin.getPlotManager().getPlotAt(plocation.getRelative(PlotLocation.Direction.EAST));
        Plot plot3 = plugin.getPlotManager().getPlotAt(plocation.getRelative(PlotLocation.Direction.SOUTH));

        Location[] north = new Location[4];
        Location[] east = new Location[4];
        Location[] south = new Location[4];
        Location[] west = new Location[4];

        Location[] north1 = new Location[4];
        Location[] east1 = new Location[4];
        Location[] south1 = new Location[4];
        Location[] west1 = new Location[4];

        locations.put("main", plot.getCorners(plot));

        if (plot0 != null && plot0.getAdmin().equals(plot.getAdmin())) {
            west = plot.getCorners(plot0);
            west1[0] = west[3];
            locations.put("west", west1);
        }
        if (plot1 != null && plot1.getAdmin().equals(plot.getAdmin())) {
            north = plot.getCorners(plot1);
            north1[0] = north[2];
            locations.put("north", north1);
        }
        if (plot2 != null && plot2.getAdmin().equals(plot.getAdmin())) {
            east = plot.getCorners(plot2);
            east1[0] = east[0];
            locations.put("east", east1);
        }
        if (plot3 != null && plot3.getAdmin().equals(plot.getAdmin())) {
            south = plot.getCorners(plot3);
            south1[0] = south[0];
            locations.put("south", south1);
        }

        return locations;

    }

    public void fillRoads(HashMap<String, Location[]> plots, World world) {
        //North = -z   South = +Z
        //East  = +x    West = -x
        if (plots.containsKey("north")) {
            Location[] north = plots.get("north");
            Location[] main = plots.get("main");
            int startX = north[0].getBlockX();
            int startZ = north[0].getBlockZ();
            int endX = main[3].getBlockX();
            int endZ = main[3].getBlockZ();

            //0 = north west, 3 = north east, 1 = south east, 2 = south west
            
            
            
         for (int y = 0; y <= 254; y++){
            for (int z = startZ; z <= endZ; z++) {
                for (int x = startX; x <= endX; x++) {
                    if(y == 0){
                        world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    }else if(y < main[3].getBlockY()){
                        world.getBlockAt(x, y, z).setType(Material.DIRT);
                    }else if(y == main[3].getBlockY() && (z == (startZ) || z == (endZ))){
                        world.getBlockAt(x, y, z).setType(Material.OBSIDIAN);
                    }else if(y == main[3].getBlockY() && !(z == (startZ) || z == (endZ))){
                        world.getBlockAt(x, y, z).setType(Material.STEP);
                        world.getBlockAt(x, y, z).setData((byte) 0);
                    }else if(y == main[3].getBlockY() + 1 && (z == (startZ) || z == (endZ))){
                        world.getBlockAt(x, y, z).setType(Material.STEP);
                        world.getBlockAt(x, y, z).setData((byte) 0);
                    }else if(y == main[3].getBlockY() + 1 && !(z == (startZ) || z == (endZ))){
                        world.getBlockAt(x, y, z).setType(Material.AIR);
                    }else if(y > main[3].getBlockY() + 1){
                        world.getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }

            }
         }
        }
        
        
        
        
        
        if (plots.containsKey("east")) {
            Location[] east = plots.get("east");
            Location[] main = plots.get("main");
            int startX = east[0].getBlockX();
            int startZ = east[0].getBlockZ();
            int endX = main[1].getBlockX();
            int endZ = main[1].getBlockZ();

            //0 = north west, 3 = north east, 1 = south east, 2 = south west

         for (int y = 0; y <= 254; y++){
            for (int x = startX; x >= endX; x--) {
                for (int z = startZ; z <= endZ; z++) {
                    if(y == 0){
                        world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    }else if(y < main[3].getBlockY()){
                        world.getBlockAt(x, y, z).setType(Material.DIRT);
                    }else if(y == main[3].getBlockY() && (x == (startX) || x == (endX))){
                        world.getBlockAt(x, y, z).setType(Material.OBSIDIAN);
                    }else if(y == main[3].getBlockY() && !(x == (startX) || x == (endX))){
                        world.getBlockAt(x, y, z).setType(Material.STEP);
                        world.getBlockAt(x, y, z).setData((byte) 0);
                    }else if(y == main[3].getBlockY() + 1 && (x == (startX) || x == (endX))){
                        world.getBlockAt(x, y, z).setType(Material.STEP);
                        world.getBlockAt(x, y, z).setData((byte) 0);
                    }else if(y == main[3].getBlockY() + 1 && !(x == (startX) || x == (endX))){
                        world.getBlockAt(x, y, z).setType(Material.AIR);    
                    }else if(y > main[3].getBlockY() + 1){
                        world.getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }

            }
         }
        }
        
        
        
        
        if (plots.containsKey("south")) {
            Location[] south = plots.get("south");
            Location[] main = plots.get("main");
            int startX = south[0].getBlockX();
            int startZ = south[0].getBlockZ();
            int endX = main[1].getBlockX();
            int endZ = main[1].getBlockZ();

            //0 = north west, 3 = north east, 1 = south east, 2 = south west
            
         for (int y = 0; y <= 254; y++){
            for (int z = startZ; z >= endZ; z--) {
                for (int x = startX; x <= endX; x++) {
                    if(y == 0){
                        world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    }else if(y < main[3].getBlockY()){
                        world.getBlockAt(x, y, z).setType(Material.DIRT);
                    }else if(y == main[3].getBlockY() && (z == (startZ) || z == (endZ))){
                        world.getBlockAt(x, y, z).setType(Material.OBSIDIAN);
                    }else if(y == main[3].getBlockY() && !(z == (startZ) || z == (endZ))){
                        world.getBlockAt(x, y, z).setType(Material.STEP);
                        world.getBlockAt(x, y, z).setData((byte) 0);
                    }else if(y == main[3].getBlockY() + 1 && (z == (startZ) || z == (endZ))){
                        world.getBlockAt(x, y, z).setType(Material.STEP);
                        world.getBlockAt(x, y, z).setData((byte) 0);
                    }else if(y == main[3].getBlockY() + 1 && !(z == (startZ) || z == (endZ))){
                        world.getBlockAt(x, y, z).setType(Material.AIR);
                    }else if(y > main[3].getBlockY() + 1){
                        world.getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }

            }
         }
        }
        
        
        
        
        if (plots.containsKey("west")) {
            Location[] west = plots.get("west");
            Location[] main = plots.get("main");
            int startX = west[0].getBlockX();
            int startZ = west[0].getBlockZ();
            int endX = main[2].getBlockX();
            int endZ = main[2].getBlockZ();

            plugin.getServer().broadcastMessage("West: " + startX + ", " + startZ );
            plugin.getServer().broadcastMessage("0: " + main[0].getBlockX() + ", " + main[0].getBlockZ());
            plugin.getServer().broadcastMessage("1: " + main[1].getBlockX() + ", " + main[1].getBlockZ());
            plugin.getServer().broadcastMessage("2: " + main[2].getBlockX() + ", " + main[2].getBlockZ());
            plugin.getServer().broadcastMessage("3: " + main[3].getBlockX() + ", " + main[3].getBlockZ());
            
            
            //0 = north west, 3 = north east, 1 = south east, 2 = south west
            
        for (int y = 0; y <= 254; y++){
            for (int x = startX; x <= endX; x++) {
                for (int z = startZ; z <= endZ; z++) {
                    if(y == 0){
                        world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    }else if(y < main[3].getBlockY()){
                        world.getBlockAt(x, y, z).setType(Material.DIRT);
                    }else if(y == main[3].getBlockY() && (x == (startX) || x == (endX))){
                        world.getBlockAt(x, y, z).setType(Material.OBSIDIAN);
                    }else if(y == main[3].getBlockY() && !(x == (startX) || x == (endX))){
                        world.getBlockAt(x, y, z).setType(Material.STEP);
                        world.getBlockAt(x, y, z).setData((byte) 0);
                    }else if(y == main[3].getBlockY() + 1 && (x == (startX) || x == (endX))){
                        world.getBlockAt(x, y, z).setType(Material.STEP);
                        world.getBlockAt(x, y, z).setData((byte) 0);
                    }else if(y == main[3].getBlockY() + 1 && !(x == (startX) || x == (endX))){
                        world.getBlockAt(x, y, z).setType(Material.AIR);
                    }else if(y > main[3].getBlockY() + 1){
                        world.getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }

            }
         }
        }

    }

    /**
     * Places a sign at all corners of the plot with the owners name.
     */
    public void createSigns() {
        int[] buildLimits = this.getBuildLimits();

        int x3 = buildLimits[0];
        int z3 = buildLimits[1] + (this.size - 7);
        int x4 = buildLimits[2];
        int z4 = buildLimits[3] - (this.size - 7);
        int y = plugin.config.getInt(Config.GRID_HEIGHT);
        World world = plugin.getServer().getWorld(this.getLocation().getWorldName());

        if (plugin.config.getInt(Config.BLOCKS_UPPER_WALL) == 0) {
            y += 1;
        } else {
            y += 2;
        }

        Block cornerOne = world.getBlockAt(buildLimits[0] - 1, y, buildLimits[1] - 1);
        Block cornerTwo = world.getBlockAt(buildLimits[2] + 1, y, buildLimits[3] + 1);
        Block cornerThree = world.getBlockAt(x3 - 1, y, z3);
        Block cornerFour = world.getBlockAt(x4 + 1, y, z4);

        cornerOne.setType(Material.SIGN_POST);
        cornerTwo.setType(Material.SIGN_POST);
        cornerThree.setType(Material.SIGN_POST);
        cornerFour.setType(Material.SIGN_POST);

        // north west
        Sign signOne = (Sign) cornerOne.getState();
        // south east
        Sign signTwo = (Sign) cornerTwo.getState();
        // north east
        Sign signThree = (Sign) cornerThree.getState();
        // south west
        Sign signFour = (Sign) cornerFour.getState();

        signOne.setRawData((byte) 0x6);
        signTwo.setRawData((byte) 0xE);
        signThree.setRawData((byte) 0x2);
        signFour.setRawData((byte) 0xA);

        signOne.setLine(1, plugin.config.getString(Config.OWNER_PREFIX));
        signOne.setLine(2, this.getAdmin());
        signTwo.setLine(1, plugin.config.getString(Config.OWNER_PREFIX));
        signTwo.setLine(2, this.getAdmin());
        signThree.setLine(1, plugin.config.getString(Config.OWNER_PREFIX));
        signThree.setLine(2, this.getAdmin());
        signFour.setLine(1, plugin.config.getString(Config.OWNER_PREFIX));
        signFour.setLine(2, this.getAdmin());

        signOne.update();
        signTwo.update();
        signThree.update();
        signFour.update();
    }

    /**
     * Removes the signs from the plot corners.
     */
    public void removeSigns() {
        int[] buildLimits = this.getBuildLimits();
        int x3 = buildLimits[0];
        int z3 = buildLimits[1] + (this.size - 7);
        int x4 = buildLimits[2];
        int z4 = buildLimits[3] - (this.size - 7);
        int y = plugin.config.getInt(Config.GRID_HEIGHT);
        World world = plugin.getServer().getWorld(this.getLocation().getWorldName());

        if (plugin.config.getInt(Config.BLOCKS_UPPER_WALL) == 0) {
            y += 1;
        } else {
            y += 2;
        }

        Block cornerOne = world.getBlockAt(buildLimits[0] - 1, y, buildLimits[1] - 1);
        Block cornerTwo = world.getBlockAt(buildLimits[2] + 1, y, buildLimits[3] + 1);
        Block cornerThree = world.getBlockAt(x3 - 1, y, z3);
        Block cornerFour = world.getBlockAt(x4 + 1, y, z4);

        cornerOne.setType(Material.AIR);
        cornerTwo.setType(Material.AIR);
        cornerThree.setType(Material.AIR);
        cornerFour.setType(Material.AIR);
    }
}

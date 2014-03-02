package uk.co.jacekk.bukkit.infiniteplots.protection;

import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.infiniteplots.InfinitePlots;
import uk.co.jacekk.bukkit.infiniteplots.Permission;
import uk.co.jacekk.bukkit.infiniteplots.generation.PlotsGenerator;
import uk.co.jacekk.bukkit.infiniteplots.plot.Plot;
import uk.co.jacekk.bukkit.infiniteplots.plot.PlotLocation;

public class EnterListener extends BaseListener<InfinitePlots> {
	
	public EnterListener(InfinitePlots plugin){
		super(plugin);
	}
	
        @SuppressWarnings("null")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event){
		Location from = event.getFrom();
		Location to = event.getTo();
		
                
                
		if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()){
			return;
		}
		
		if (!(to.getWorld().getGenerator() instanceof PlotsGenerator)){
			return;
		}
		
		Player player = event.getPlayer();
		
		Plot plot = plugin.getPlotManager().getPlotAt(PlotLocation.fromWorldLocation(to));
		Plot plot2 = null;
                
		if (plot != null && !plot.canEnter(player.getName()) && plot.withinBuildableArea(player, to)){
                    event.setCancelled(true);
		}else if(plot != null && plot.canEnter(player.getName()) && plot.withinBuildableArea(player, to) && (plot.getTime() != 0L || plot.getWeather() != true) && !uk.co.jacekk.bukkit.infiniteplots.InfinitePlots.getInstance().isInside.contains(player.getName())){
                    uk.co.jacekk.bukkit.infiniteplots.InfinitePlots.getInstance().isInside.add(player.getName());
                   
                   
                    
                    if(plot.getTime() != 0L){
                    player.setPlayerTime(plot.getTime(), false);
                    }
                    
                    if(plot.getWeather() == true){
                        player.setPlayerWeather(WeatherType.CLEAR);
                    }
                    else if(plot.getWeather() == false){
                        player.setPlayerWeather(WeatherType.DOWNFALL);
                    }
                    
                    if (Permission.PLOT_BUILD_ALL.has(player)){
			return;
                    }
                   
                    
                }
                
               else if(uk.co.jacekk.bukkit.infiniteplots.InfinitePlots.getInstance().isInside.contains(player.getName())){
                    
                   if(plugin.getPlotManager().getPlotAt(PlotLocation.fromWorldLocation(from)) != null){
                   plot2 = plugin.getPlotManager().getPlotAt(PlotLocation.fromWorldLocation(from));
                        if(!plot2.withinBuildableArea(player, from)){
                       
                            uk.co.jacekk.bukkit.infiniteplots.InfinitePlots.getInstance().isInside.remove(player.getName());
                            player.resetPlayerTime();
                            player.resetPlayerWeather();

                            if (Permission.PLOT_BUILD_ALL.has(player)) {
                                return;
                            }
                        }
                   }  
                }
                
	}
	
}

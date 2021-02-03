package me.dank.mega;
// all of the imports
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	// config
	FileConfiguration config = getConfig();
	
	@Override
	//start and stop of the plugin
	public void onEnable() {	
        config.addDefault("BlocksPerHit", 30);
        config.options().copyDefaults(true);
        saveConfig();
		
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {

	}

	
	public void doBlockEater(Player player, Block startingBlock, int amount) {
		// the code to destroy things
		if (startingBlock.getType() == Material.AIR) return;
		Material targetMaterial = startingBlock.getType();
		
		ArrayList<Block> blocksToCheck = new ArrayList<>();
		blocksToCheck.add(startingBlock);
		
		for (int i = 0; i <= amount; i++) {

			ArrayList<Block> preClonedList = new ArrayList<>(blocksToCheck);
			for (Block block : preClonedList) {
				blocksToCheck.remove(block);
				Block upperBlock = block.getRelative(BlockFace.UP);
				Block lowerBlock = block.getRelative(BlockFace.DOWN);
				Block northBlock = block.getRelative(BlockFace.NORTH);
				Block eastBlock = block.getRelative(BlockFace.EAST);
				Block southBlock = block.getRelative(BlockFace.SOUTH);
				Block westBlock = block.getRelative(BlockFace.WEST);
				for (Block nearbyBlock : new ArrayList<Block>(Arrays.asList(upperBlock, lowerBlock, northBlock, eastBlock, southBlock, westBlock))) {
					if (nearbyBlock.getType() == targetMaterial) {
						nearbyBlock.breakNaturally();
						nearbyBlock.getWorld().playSound(nearbyBlock.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.3f, 2f);
						blocksToCheck.add(nearbyBlock);
					}
				}
			}
		}
		
		
	}
// detect if the player destroyed a block
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = (Player) event.getPlayer();
		Block block = (Block) event.getBlock();
		doBlockEater(player, block, config.getInt("BlocksPerHit"));
		

	}

				
}
package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.*;
import com.random.captureTheFlag.util.ItemBuilder;
import com.random.captureTheFlag.util.Scoreboard;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.List;

public class GameStartListener implements Listener {
    public static Map<UUID, KitType> redKits = new HashMap<>();
    public static Map<UUID, KitType> blueKits = new HashMap<>();
    public static final Inventory RED_KIT_INV = Bukkit.createInventory(null, 27, "§6Choose Kit");
    public static final Inventory BLUE_KIT_INV = Bukkit.createInventory(null, 27, "§6Choose Kit");

    static {
        for (int i = 0; i <= 26; i++) {
            RED_KIT_INV.setItem(i, new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData())).setDisplayName("§c").getItem());
            BLUE_KIT_INV.setItem(i, new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData())).setDisplayName("§c").getItem());
        }

        RED_KIT_INV.setItem(KitType.MID_FEILD.getIndex(), KitType.MID_FEILD.toItem(KitType.MID_FEILD));
        RED_KIT_INV.setItem(KitType.DEFENSE.getIndex(), KitType.DEFENSE.toItem(KitType.DEFENSE));
        RED_KIT_INV.setItem(KitType.FLAG_STEALER.getIndex(), KitType.FLAG_STEALER.toItem(KitType.FLAG_STEALER));
        RED_KIT_INV.setItem(KitType.LONG_RANGE.getIndex(), KitType.LONG_RANGE.toItem(KitType.LONG_RANGE));
        BLUE_KIT_INV.setItem(KitType.MID_FEILD.getIndex(), KitType.MID_FEILD.toItem(KitType.MID_FEILD));
        BLUE_KIT_INV.setItem(KitType.DEFENSE.getIndex(), KitType.DEFENSE.toItem(KitType.DEFENSE));
        BLUE_KIT_INV.setItem(KitType.FLAG_STEALER.getIndex(), KitType.FLAG_STEALER.toItem(KitType.FLAG_STEALER));
        BLUE_KIT_INV.setItem(KitType.LONG_RANGE.getIndex(), KitType.LONG_RANGE.toItem(KitType.LONG_RANGE));
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent ev) {
        if (ev.getWhoClicked().getGameMode() != GameMode.SURVIVAL) return;
        if (ev.getCurrentItem() == null) return;
        if (ev.getCurrentItem().getItemMeta().getDisplayName().equals("asdf")) {
            if (Capture.getInstance().getPlayers().containsKey(ev.getWhoClicked().getUniqueId())) return;
            if (Capture.getInstance().getPlayers().size() != 8) {
                Capture.getInstance().getPlayers().put(ev.getWhoClicked().getUniqueId(), new CapturePlayer(ev.getWhoClicked().getUniqueId()));
                if (Capture.getInstance().getPlayers().size() == 2 /* Change to 8 once done testing */) {
                    Team.RED.setSpawn(Capture.getInstance().getLocation("redSpawn"));
                    Team.BLUE.setSpawn(Capture.getInstance().getLocation("blueSpawn"));
                    List<CapturePlayer> cps = new ArrayList<>();
                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                        cp.setScoreboard(new Scoreboard(cp, true));
                        cps.add(cp);
                    }
                    for (int i = 0; i != 1/* Change to 3 once done testing */; i++) {
                        cps.get(i).setTeam(Team.RED);
                    }
                    for (int i = 1/* Change to 3 once done testing */; i != 2/* Change to 7 once done testing */; i++) {
                        cps.get(i).setTeam(Team.BLUE);
                    }
                    for (CapturePlayer cp : cps) {
                        cp.getHandle().teleport(Capture.getInstance().getWaitingLoc());
                        cp.getHandle().getInventory().addItem(new ItemBuilder().setMaterial(Material.CHEST).setDisplayName("§6Kit Selection").getItem());
                        cp.getHandle().sendMessage("Hallo"); // Game start event

                        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.GREEN + "Select Your Kit\"}");

                        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                        PacketPlayOutTitle length = new PacketPlayOutTitle(0, 60, 30);

                        ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(title);
                        ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(length);
                    }
                    new BukkitRunnable() {
                        int timer = 20;
                        @Override
                        public void run() {
                            for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                cp.getHandle().setLevel(timer);
                            }
                            if (timer <= 5 && timer != 0) {
                                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                    IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.YELLOW + "Starting in " + ChatColor.RED + timer + ChatColor.YELLOW + "...\"}");

                                    PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                                    PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 0);

                                    ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(title);
                                    ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(length);
                                }
                            }
                            if (timer == 0) {
                                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                    IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.GREEN + "Capture the Flag\"}");

                                    PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                                    PacketPlayOutTitle length = new PacketPlayOutTitle(0, 60, 20);

                                    ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(title);
                                    ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(length);
                                    Capture.getInstance().setState(GameState.INGAME);
                                    Capture.getInstance().setRound(Round.ONE);
                                    if (cp.getKit() == null) {
                                        cp.setKit(getRandomKit(cp.getTeam() == Team.RED ? new ArrayList<>(redKits.values()) : new ArrayList<>(blueKits.values())));
                                    } else {
                                        cp.setKit(cp.getTeam() == Team.RED ? redKits.get(cp.getHandle().getUniqueId()) : blueKits.get(cp.getHandle().getUniqueId()));
                                    }
                                    cp.setScoreboard(new Scoreboard(cp, true));
                                    cp.getHandle().teleport(cp.getTeam().getSpawn());
                                }
                                redKits.clear();
                                blueKits.clear();
                                cancel();
                                return;
                            }
                            timer--;

                        }
                    }.runTaskTimer(Capture.getInstance(), 20, 20);
                } else {
                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                        cp.setScoreboard(new Scoreboard(cp, false));
                    }
                }

            } else {
                ev.getWhoClicked().sendMessage(ChatColor.YELLOW + "Game currently in progress, please wait for it to be over.");
            }

        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent ev) {
        if (ev.getCurrentItem() == null) return;
        if (Capture.getInstance().getPlayers().containsKey(ev.getWhoClicked().getUniqueId())) {
            CapturePlayer cp = Capture.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId());
            if (!(ev.getClickedInventory().getName().equals("§6Choose Kit"))) {
                return;
            } else {
                ev.setCancelled(true);
            }
            if (ev.getCurrentItem().getItemMeta().getDisplayName().equals("§eMid-Field Kit")) {
                if (cp.getKit() == KitType.MID_FEILD) {
                    ev.getWhoClicked().sendMessage("§cYou already have this kit selected!");
                    return;
                }
                if (cp.getKit() != null) {
                    KitType kit = cp.getKit();
                    if (cp.getTeam() == Team.RED) {
                        RED_KIT_INV.setItem(kit.getIndex(), kit.toItem(kit));
                        redKits.remove(ev.getWhoClicked().getUniqueId());
                    } else {
                        BLUE_KIT_INV.setItem(kit.getIndex(), kit.toItem(kit));
                        blueKits.remove(ev.getWhoClicked().getUniqueId());
                    }

                }
                if (cp.getTeam() == Team.RED) {
                    if (wasKitChosen(KitType.MID_FEILD, redKits)) {
                        ev.getWhoClicked().sendMessage("§cThis kit has already been chosen!");
                    } else {
                        redKits.put(ev.getWhoClicked().getUniqueId(), KitType.MID_FEILD);
                        cp.setKit(KitType.MID_FEILD);
                        RED_KIT_INV.setItem(KitType.MID_FEILD.getIndex(), new ItemBuilder(KitType.MID_FEILD.toItem(KitType.MID_FEILD))
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§cSelected!").getItem());
                    }
                } else {
                    if (wasKitChosen(KitType.MID_FEILD, blueKits)) {
                        ev.getWhoClicked().sendMessage("§cThis kit has already been chosen!");
                    } else {
                        blueKits.put(ev.getWhoClicked().getUniqueId(), KitType.MID_FEILD);
                        cp.setKit(KitType.MID_FEILD);
                        BLUE_KIT_INV.setItem(KitType.MID_FEILD.getIndex(), new ItemBuilder(KitType.MID_FEILD.toItem(KitType.MID_FEILD))
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§cSelected!").getItem());
                    }
                }
            }
            if (ev.getCurrentItem().getItemMeta().getDisplayName().equals("§eDefense Kit")) {
                if (cp.getKit() == KitType.DEFENSE) {
                    ev.getWhoClicked().sendMessage("§cYou already have this kit selected!");
                    return;
                }
                if (cp.getKit() != null) {
                    KitType kit = cp.getKit();
                    if (cp.getTeam() == Team.RED) {
                        RED_KIT_INV.setItem(kit.getIndex(), kit.toItem(kit));
                        redKits.remove(ev.getWhoClicked().getUniqueId());
                    } else {
                        BLUE_KIT_INV.setItem(kit.getIndex(), kit.toItem(kit));
                        blueKits.remove(ev.getWhoClicked().getUniqueId());
                    }

                }
                if (cp.getTeam() == Team.RED) {
                    if (wasKitChosen(KitType.DEFENSE, redKits)) {
                        ev.getWhoClicked().sendMessage("§cThis kit has already been chosen!");
                    } else {
                        cp.setKit(KitType.DEFENSE);
                        redKits.put(ev.getWhoClicked().getUniqueId(), KitType.DEFENSE);
                        RED_KIT_INV.setItem(KitType.DEFENSE.getIndex(), new ItemBuilder(KitType.DEFENSE.toItem(KitType.DEFENSE))
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§cSelected!").getItem());
                    }
                } else {
                    if (wasKitChosen(KitType.DEFENSE, blueKits)) {
                        ev.getWhoClicked().sendMessage("§cThis kit has already been chosen!");
                    } else {
                        cp.setKit(KitType.DEFENSE);
                        blueKits.put(ev.getWhoClicked().getUniqueId(), KitType.DEFENSE);
                        BLUE_KIT_INV.setItem(KitType.DEFENSE.getIndex(), new ItemBuilder(KitType.DEFENSE.toItem(KitType.DEFENSE))
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§cSelected!").getItem());
                    }
                }
            }
            if (ev.getCurrentItem().getItemMeta().getDisplayName().equals("§eFlag-Stealer Kit")) {
                if (cp.getKit() == KitType.FLAG_STEALER) {
                    ev.getWhoClicked().sendMessage("§cYou already have this kit selected!");
                    return;
                }
                if (cp.getKit() != null) {
                    KitType kit = cp.getKit();
                    if (cp.getTeam() == Team.RED) {
                        RED_KIT_INV.setItem(kit.getIndex(), kit.toItem(kit));
                        redKits.remove(ev.getWhoClicked().getUniqueId());
                    } else {
                        BLUE_KIT_INV.setItem(kit.getIndex(), kit.toItem(kit));
                        blueKits.remove(ev.getWhoClicked().getUniqueId());
                    }

                }
                if (cp.getTeam() == Team.RED) {
                    if (wasKitChosen(KitType.FLAG_STEALER, redKits)) {
                        ev.getWhoClicked().sendMessage("§cThis kit has already been chosen!");
                    } else {
                        redKits.put(ev.getWhoClicked().getUniqueId(), KitType.FLAG_STEALER);
                        cp.setKit(KitType.FLAG_STEALER);
                        RED_KIT_INV.setItem(KitType.FLAG_STEALER.getIndex(), new ItemBuilder(KitType.FLAG_STEALER.toItem(KitType.FLAG_STEALER))
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§cSelected!").getItem());
                    }
                } else {
                    if (wasKitChosen(KitType.FLAG_STEALER, blueKits)) {
                        ev.getWhoClicked().sendMessage("§cThis kit has already been chosen!");
                    } else {
                        blueKits.put(ev.getWhoClicked().getUniqueId(), KitType.FLAG_STEALER);
                        cp.setKit(KitType.FLAG_STEALER);
                        BLUE_KIT_INV.setItem(KitType.FLAG_STEALER.getIndex(), new ItemBuilder(KitType.FLAG_STEALER.toItem(KitType.FLAG_STEALER))
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§cSelected!").getItem());
                    }
                }
            }
            if (ev.getCurrentItem().getItemMeta().getDisplayName().equals("§eLong-Range Kit")) {
                if (cp.getKit() == KitType.LONG_RANGE) {
                    ev.getWhoClicked().sendMessage("§cYou already have this kit selected!");
                    return;
                }
                if (cp.getKit() != null) {
                    KitType kit = cp.getKit();
                    if (cp.getTeam() == Team.RED) {
                        RED_KIT_INV.setItem(kit.getIndex(), kit.toItem(kit));
                        redKits.remove(ev.getWhoClicked().getUniqueId());
                    } else {
                        BLUE_KIT_INV.setItem(kit.getIndex(), kit.toItem(kit));
                        blueKits.remove(ev.getWhoClicked().getUniqueId());
                    }

                }
                if (cp.getTeam() == Team.RED) {
                    if (wasKitChosen(KitType.LONG_RANGE, redKits)) {
                        ev.getWhoClicked().sendMessage("§cThis kit has already been chosen!");
                    } else {
                        redKits.put(ev.getWhoClicked().getUniqueId(), KitType.LONG_RANGE);
                        cp.setKit(KitType.LONG_RANGE);
                        RED_KIT_INV.setItem(KitType.LONG_RANGE.getIndex(), new ItemBuilder(KitType.LONG_RANGE.toItem(KitType.LONG_RANGE))
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§cSelected!").getItem());
                    }
                } else {
                    if (wasKitChosen(KitType.LONG_RANGE, blueKits)) {
                        ev.getWhoClicked().sendMessage("§cThis kit has already been chosen!");
                    } else {
                        blueKits.put(ev.getWhoClicked().getUniqueId(), KitType.LONG_RANGE);
                        cp.setKit(KitType.LONG_RANGE);
                        BLUE_KIT_INV.setItem(KitType.LONG_RANGE.getIndex(), new ItemBuilder(KitType.LONG_RANGE.toItem(KitType.LONG_RANGE))
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§cSelected!").getItem());
                    }
                }
            }

        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent ev) {
        if (!Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) return;
        if (ev.getPlayer().getItemInHand() == null) return;
        if (ev.getPlayer().getItemInHand().getType() == Material.CHEST) {
            if (ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ev.getPlayer().openInventory(Capture.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() == Team.RED ? RED_KIT_INV : BLUE_KIT_INV);

            }
        }
    }

    private boolean wasKitChosen(KitType kitType, Map<UUID, KitType> kits) {
        for(KitType all : kits.values()) {
            if(all.equals(kitType)) {
                return true;
            }
        }
        return false;
    }

    public static KitType getRandomKit(List<KitType> chosen) {
        KitType kit = KitType.values()[new Random().nextInt(KitType.values().length)];
        while (chosen.contains(kit)) {
            kit = KitType.values()[new Random().nextInt(KitType.values().length)];
        }
        return kit;
    }

}

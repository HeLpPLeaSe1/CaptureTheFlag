package com.random.captureTheFlag.player;

import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum KitType {
    MID_FEILD(10, new ItemStack[] { new ItemBuilder().setMaterial(Material.IRON_LEGGINGS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).getItem()
        , new ItemBuilder().setMaterial(Material.IRON_BOOTS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).getItem() },
        new ItemBuilder().setMaterial(Material.STONE_SWORD).setUnbreakable(true).addEnchantment(Enchantment.DAMAGE_ALL, 1).getItem(),
            new ItemBuilder().setMaterial(Material.SHEARS).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.POTION).setPotion(new PotionEffect(PotionEffectType.JUMP, 120 * 20, 4)).getItem()
    ),
    DEFENSE(12, new ItemStack[] { new ItemBuilder().setMaterial(Material.IRON_LEGGINGS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).getItem()
            , new ItemBuilder().setMaterial(Material.IRON_BOOTS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).getItem() },
            new ItemBuilder().setMaterial(Material.STONE_SWORD).setUnbreakable(true).addEnchantment(Enchantment.DAMAGE_ALL, 1).getItem(),
            new ItemBuilder().setMaterial(Material.SHEARS).setUnbreakable(true).getItem()
    ),
    FLAG_STEALER(14, new ItemStack[] { new ItemBuilder().setMaterial(Material.IRON_LEGGINGS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).getItem()
            , new ItemBuilder().setMaterial(Material.IRON_BOOTS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).getItem() },
            new ItemBuilder().setMaterial(Material.WOOD_SWORD).setUnbreakable(true).addEnchantment(Enchantment.DAMAGE_ALL, 2).getItem(),
            new ItemBuilder().setMaterial(Material.SHEARS).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.POTION).setPotion(new PotionEffect(PotionEffectType.SPEED, 120 * 20, 2)).getItem()
    ),
    LONG_RANGE(16, new ItemStack[] { new ItemBuilder().setMaterial(Material.IRON_LEGGINGS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).getItem()
            , new ItemBuilder().setMaterial(Material.IRON_BOOTS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).getItem() },
            new ItemBuilder().setMaterial(Material.WOOD_SWORD).setUnbreakable(true).addEnchantment(Enchantment.DAMAGE_ALL, 1).getItem(),
            new ItemBuilder().setMaterial(Material.SHEARS).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.POTION).setPotion(new PotionEffect(PotionEffectType.JUMP, 120 * 20, 2)).getItem(),
            new ItemBuilder().setMaterial(Material.POTION).setPotion(new PotionEffect(PotionEffectType.SPEED, 120 * 20, 1)).getItem(),
            new ItemBuilder().setMaterial(Material.BOW).getItem(),
            new ItemBuilder().setMaterial(Material.ARROW).setAmount(16).getItem()
    );

    private final int index;
    private final ItemStack[] armor;
    private final ItemStack[] items;

    KitType(int index, ItemStack[] armor, ItemStack... items) {
        this.index = index;
        this.armor = armor;
        this.items = items;
    }

    public void apply(CapturePlayer capturePlayer) {
        Player player = capturePlayer.getHandle();
        player.getInventory().clear();
        ItemStack chestPlate = new ItemBuilder().setMaterial(Material.LEATHER_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .getItem();
        LeatherArmorMeta leatherArmorMeta = ((LeatherArmorMeta) chestPlate.getItemMeta());
        leatherArmorMeta.setColor(capturePlayer.getTeam() == Team.RED ? Color.RED : Color.BLUE);
        leatherArmorMeta.spigot().setUnbreakable(true);
        chestPlate.setItemMeta(leatherArmorMeta);
        player.getInventory().setChestplate(chestPlate);
        ItemStack helmet = new ItemBuilder().setMaterial(Material.LEATHER_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .getItem();
        LeatherArmorMeta leatherArmorHelmetMeta = ((LeatherArmorMeta) helmet.getItemMeta());
        leatherArmorHelmetMeta.setColor(capturePlayer.getTeam() == Team.RED ? Color.RED : Color.BLUE);
        leatherArmorHelmetMeta.spigot().setUnbreakable(true);
        helmet.setItemMeta(leatherArmorHelmetMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().addItem(items);
        player.getInventory().addItem(new ItemStack(Material.WOOL, 128, DyeColor.valueOf(capturePlayer.getTeam().name()).getWoolData()));
        player.getInventory().setLeggings(armor[0]);
        player.getInventory().setBoots(armor[1]);
    }

    public int getIndex() {
        return index;
    }

    public ItemStack toItem(KitType kit) {
        if (kit == KitType.MID_FEILD) {
            return new ItemBuilder(new ItemStack(Material.POTION, 1, DyeColor.GREEN.getData()))
                    .setDisplayName("§eMid-Feild Kit")
                    .setLore("§aAvailable!", "§7Leather Helmet §8(Protection I)", "§7Leather Chestplate §8(Protection I)", "§7Iron Leggings §8(Protection I)",
                            "§7Iron Boots §8(Protection I)", "§7Stone Sword (Sharpness I)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS).getItem();
        }
        if (kit == KitType.DEFENSE) {
            return new ItemBuilder(new ItemStack(Material.IRON_CHESTPLATE, 1, DyeColor.GRAY.getDyeData()))
                    .setDisplayName("§eDefense Kit")
                    .setLore("§aAvailable!", "§7Leather Helmet §8(Protection I)", "§7Leather Chestplate §8(Protection I)", "§7Iron Leggings §8(Protection III)",
                            "§7Iron Boots §8(Protection II)", "§7Stone Sword §8(Sharpness I)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS).getItem();

        }
        if (kit == KitType.FLAG_STEALER) {
            return new ItemBuilder(new ItemStack(Material.BANNER, 1, DyeColor.GRAY.getDyeData()))
                    .setDisplayName("§eFlag-Stealer Kit")
                    .setLore("§aAvailable!", "§7Leather Helmet §8(Protection I)", "§7Leather Chestplate §8(Protection I)", "§7Iron Leggings §8(Protection II)",
                            "§7Iron Boots §8(Protection II)", "§7Speed Potion §8(Level III, 120 Seconds)", "§7Wooden Sword §8(Sharpness II)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS).getItem();
        }
        return new ItemBuilder().setMaterial(Material.BOW).setDisplayName("§eLong-Range Kit")
                .setLore("§aAvailable!", "§7Leather Helmet §8(Protection I)", "§7Leather Chestplate §8(Protection I)", "§7Iron Leggings §8(Protection I)",
                        "§7Iron Boots §8(Protection I)", "§7Wood Sword §8(Sharpness I)")
                .addEnchantment(Enchantment.DURABILITY, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).getItem();
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getItems() {
        return items;
    }
}

package com.random.captureTheFlag.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;

public class ItemBuilder {

	private final ItemStack item;

	public ItemBuilder() {
		item = new ItemStack(Material.APPLE, 1);
	}

	public ItemBuilder(ItemStack is) {
		item = is;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		item.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemStack getItem() {
		return item;
	}

	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemBuilder setPotion(PotionEffect eff) {
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.addCustomEffect(eff, true);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setDisplayName(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setUnbreakable(boolean b) {
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(b);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setFirework(FireworkEffect effect) {
		FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
		meta.setEffect(effect);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(String... lores) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lores));
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setMaterial(Material mat) {
		item.setType(mat);
		return this;
	}

	public ItemBuilder addItemFlag(ItemFlag... flag) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(flag);
		item.setItemMeta(meta);
		return this;
	}

}

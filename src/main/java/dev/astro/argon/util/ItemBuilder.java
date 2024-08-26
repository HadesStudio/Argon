package dev.astro.argon.util;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.astro.argon.util.reflect.Reflection;
import dev.astro.argon.util.version.ServerVersion;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ItemBuilder {

	private ItemStack itemStack;

	public ItemBuilder(Material material) {
		this(material, 1);
	}

	public ItemBuilder(Material material, int amount) {
		this(material, amount, (short) 0);
	}

	public ItemBuilder(Material material, int amount, short damage) {
		this(new ItemStack(material, amount, damage));
	}

	public ItemBuilder setType(Material material) {
		itemStack.setType(material);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder setDurability(int durability) {
		itemStack.setDurability((short) durability);
		return this;
	}

	public ItemBuilder setData(MaterialData data) {
		itemStack.setData(data);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment) {
		addEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		itemStack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		itemStack.removeEnchantment(enchantment);
		return this;
	}

	public ItemBuilder clearEnchantments() {
		for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
			itemStack.removeEnchantment(enchantment);
		}
		
		return this;
	}

	public ItemBuilder setName(String name) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		
		itemStack.setItemMeta(itemMeta);
		return this;
	}

	public ItemBuilder addLore(String lore) {
		ItemMeta itemMeta = itemStack.getItemMeta();

		List<String> lores = itemMeta.getLore();
		if (lores == null) {
			lores = Lists.newArrayList();
		}
			
		lores.add(ColorUtil.colorize(lore));

		itemMeta.setLore(lores);

		itemStack.setItemMeta(itemMeta);
		return this;
	}

	public ItemBuilder setLore(String... lores) {
		clearLore();

		for (String lore : lores) {
			addLore(lore);
		}

		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		clearLore();

		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setLore(ColorUtil.colorize(lore));
		itemStack.setItemMeta(itemMeta);

		return this;
	}

	public ItemBuilder clearLore() {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setLore(Lists.newArrayList());
		
		itemStack.setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder setColor(Color color) {
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		leatherArmorMeta.setColor(color);
		
		itemStack.setItemMeta(leatherArmorMeta);
		return this;
	}

	public ItemBuilder setPotionEffect(PotionEffect effect) {
		PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
		
		potionMeta.setMainEffect(effect.getType());
		potionMeta.addCustomEffect(effect, false);
		
		itemStack.setItemMeta(potionMeta);
		return this;
	}

	public ItemBuilder setPotionEffects(PotionEffect... effects) {
		PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
		potionMeta.setMainEffect(effects[0].getType());

		for (PotionEffect effect : effects) {
			potionMeta.addCustomEffect(effect, false);
		}
			
		itemStack.setItemMeta(potionMeta);
		return this;
	}

	public ItemBuilder setSkullOwner(String owner) {
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		skullMeta.setOwner(owner);

		itemStack.setItemMeta(skullMeta);
		return this;
	}

	public ItemBuilder setSkullTexture(String urlHead, String texture) {
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

		UUID id = UUID.randomUUID();
		if (ReflectionUtil.getServerVersion().isAfter(ServerVersion.MINECRAFT_1_16_4)) {
			Class<?> PLAYER_PROFILE_CLASS = Reflection.getClass("org.bukkit.profile.PlayerProfile");
			Class<?> PLAYER_TEXTURES_CLASS = Reflection.getClass("org.bukkit.profile.PlayerTextures");

			Object playerProfile = Reflection.getMethod(Bukkit.class, "createPlayerProfile", UUID.class).invoke(null, id);
			Object playerTextures = Reflection.getMethod(PLAYER_PROFILE_CLASS, "getTextures").invoke(playerProfile);

			try {
				URL url = new URL("http://textures.minecraft.net/texture/" + urlHead);
				Reflection.getMethod(PLAYER_TEXTURES_CLASS, "setSkin", URL.class).invoke(playerTextures, url);
			} catch (MalformedURLException e) {e.printStackTrace();}

			Reflection.getMethod(PLAYER_PROFILE_CLASS, "setTextures", PLAYER_TEXTURES_CLASS).invoke(playerProfile, playerTextures);
			Reflection.getMethod(skullMeta.getClass(), "setOwnerProfile", PLAYER_PROFILE_CLASS).invoke(skullMeta, playerProfile);
		} else {
			GameProfile profile = new GameProfile(id, null);
			profile.getProperties().put("textures", new Property("textures", texture));

			Reflection.getField(skullMeta.getClass(), "profile", GameProfile.class).set(skullMeta, profile);
		}

		itemStack.setItemMeta(skullMeta);
		return this;
	}

	public ItemStack build() {
		return itemStack;
	}
	
	public static String toBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(stream);
            
            bukkitStream.writeInt(items.length);

            for (int i = 0; i < items.length; ++i) {
                bukkitStream.writeObject(items[i]);
            }
            
            bukkitStream.close();
            
            return Base64Coder.encodeLines(stream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    
    public static ItemStack[] fromBase64(String string) throws IOException {
        try {
        	ByteArrayInputStream stream = new ByteArrayInputStream(Base64Coder.decodeLines(string));
            BukkitObjectInputStream bukkitStream = new BukkitObjectInputStream(stream);
            
            ItemStack[] items = new ItemStack[bukkitStream.readInt()];
            for (int i = 0; i < items.length; ++i) {
                items[i] = (ItemStack) bukkitStream.readObject();
            }
            
            bukkitStream.close();
            
            return items;
        }
        catch (ClassNotFoundException ex) {
            throw new IOException("Unable to decode class type.", ex);
        }
    }
}
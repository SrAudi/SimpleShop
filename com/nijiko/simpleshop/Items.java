package com.nijiko.simpleshop;

import com.nijiko.simpleshop.util.Misc;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author Nijiko
 */
public class Items {

    public Items() {
    }

    /**
     * Returns the name of the item stored in the hashmap or the item name stored in the items.txt file in the hMod folder.
     *
     * @param id
     * @return
     */
    public static String name(int id) {
        if (SimpleShop.items.containsKey(Misc.string(id))) {
            return SimpleShop.items.get(Misc.string(id));
        }

        for (Material item : Material.values()) {
            if (item.getId() == id) {
                return item.toString();
            }
        }

        return Misc.string(id);
    }

    /**
     * Sets the item name inside of the items array and stores it inside our database.
     *
     * @param id
     * @param name
     */
    public static void setName(String id, String name) {
        SimpleShop.items.put(id, name);
        SimpleShop.Items.setString(id, name);
    }

    /**
     * Checks whether a player has the required amount of items or not.
     *
     * @param player
     * @param itemId
     * @param amount
     * @return true / false
     */
    public static boolean has(Player player, int itemId, int amount) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int total = 0;

        for (ItemStack item : items) {
            if ((item != null) && (item.getTypeId() == itemId) && (item.getAmount() > 0)) {
                total += item.getAmount();
            }
        }

        return (total >= amount) ? true : false;
    }

    /**
     * Checks whether a player has the required amount of items or not also with itemType
     *
     * @param player
     * @param itemId
     * @param amount
     * @param itemType
     * @return true / false
     */
    public static boolean has(Player player, int itemId, int itemType, int amount) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int total = 0;

        for (ItemStack item : items) {
            if ((item != null) && (item.getTypeId() == itemId) && (item.getData().getData() == (byte) itemType) && (item.getAmount() > 0)) {
                total += item.getAmount();
            }
        }

        return (total >= amount) ? true : false;
    }

    /**
     * Checks the getArray() for the amount total of items
     *
     * @param itemId The item we are looking for
     * @return integer - Total amount of items
     */
    public static int hasAmount(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int amount = 0;

        for (ItemStack item : items) {
            if ((item != null)) {
                amount += item.getAmount();
            }
        }

        return amount;
    }

    /**
     * Checks the getArray() for the amount total of this item id.
     *
     * @param itemId The item we are looking for
     * @return integer - Total amount of itemId in the array
     */
    public static int hasAmount(Player player, int itemId) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int amount = 0;

        for (ItemStack item : items) {
            if ((item != null) && (item.getTypeId() == itemId)) {
                amount += item.getAmount();
            }
        }

        return amount;
    }

    /**
     * Checks the getArray() for the amount total of this item id.
     *
     * @param itemId The item we are looking for
     * @return integer - Total amount of itemId in the array
     */
    public static int hasAmount(Player player, int itemId, int incrementsOf) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int amount = 0;

        for (ItemStack item : items) {
            if ((item != null) && (item.getTypeId() == itemId) && (item.getAmount() == incrementsOf)) {
                amount += item.getAmount();
            }
        }

        return amount;
    }

    /**
     * Removes the amount of items from a player
     *
     * @param player
     * @param item
     * @param amount
     */
    public static void remove(Player player, int item, int amount) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int counter = amount;
        int leftover = (hasAmount(player, item) - amount);
        inventory.remove(item);

        if (leftover > 0) {
            inventory.addItem(new ItemStack(item, leftover));
        }
    }

    /**
     * Validate the string for an item
     *
     * @param item
     * @return -1 if false, id if true.
     */
    public static int validate(String item) {
        int itemId = -1;

        if (item.contains(":")) {
            item.replace(':', ',');
        }

        try {
            itemId = Integer.valueOf(item);
        } catch (NumberFormatException e) {
            for (String id : SimpleShop.items.keySet()) {
                if (SimpleShop.items.get(id).equalsIgnoreCase(item)) {
                    if (id.contains(",")) {
                        if (id.split(",")[0].equals(item.split(",")[0]) && id.split(",")[1].equals(item.split(",")[1])) {
                            itemId = Integer.valueOf(id.split(",")[0]);
                        }
                    } else {
                        itemId = Integer.valueOf(id);
                    }
                }
            }

            if (itemId == -1) {
                return -1;
            }
        }

        if (!checkID(itemId)) {
            return -1;
        } else {
            return itemId;
        }
    }

    /**
     * Validate the string for an item
     *
     * @param item
     * @return -1 if false, type if true.
     */
    public static int validateGrabType(String item) {
        int itemId = -1;
        int itemType = -1;


        try {
            itemId = Integer.valueOf(item);
        } catch (NumberFormatException e) {
            for (String id : SimpleShop.items.keySet()) {
                if (SimpleShop.items.get(id).equalsIgnoreCase(item)) {
                    if (id.contains(",")) {
                        itemId = Integer.valueOf(id.split(",")[0]);
                        itemType = Integer.valueOf(id.split(",")[1]);
                    }
                }
            }

            if (itemId == -1) {
                return -1;
            }
        }

        if (!checkID(itemId)) {
            return -1;
        } else if (!validateType(itemId, itemType)) {
            return -1;
        } else {
            return itemType;
        }
    }

    public static boolean validateType(int id, int type) {
        if (type == -1) {
            return true;
        }

        int itemId = -1;


        if (id == 35 || id == 351 || id == 63) {
            if (type >= 0 && type <= 15) {
                return true;
            }
        }

        if (id == 17) {
            if (type >= 0 && type <= 2) {
                return true;
            }
        }

        if (id == 91 || id == 86 || id == 67 || id == 53 || id == 77 || id == 71 || id == 64) {
            if (type >= 0 && type <= 3) {
                return true;
            }
        }

        if (id == 66) {
            if (type >= 0 && type <= 9) {
                return true;
            }
        }

        if (id == 68) {
            if (type >= 2 && type <= 5) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkID(int id) {
        for (Material item : Material.values()) {
            if (item.getId() == id) {
                return true;
            }
        }

        return false;
    }
}

package com.kitx.mystic;

import com.samjakob.spigui.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Getter
public class MysticItem implements Serializable {
    private final int tier;
    private final Random random = new Random(); // Yay this is serializable
    private final List<MysticEnchant> enchants = new ArrayList<>();
    private final String material;
    public MysticItem(int tier, Material material) {
        this.tier = tier;
        this.material = material.name();
    }

    public String serialize() {
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(io);
            os.writeObject(this);
            os.flush();
            byte[] serializedObject = io.toByteArray();
            return Base64.getEncoder().encodeToString(serializedObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MysticItem deserialize(String encodedObject) {
        try {
            byte[] serializedObject = Base64.getDecoder().decode(encodedObject);

            ByteArrayInputStream in = new ByteArrayInputStream(serializedObject);
            ObjectInputStream is = new ObjectInputStream(in);

            return (MysticItem) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void giveItem(ItemStack stack) {
        
    }


}

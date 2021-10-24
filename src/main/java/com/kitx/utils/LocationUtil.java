package com.kitx.utils;

import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class LocationUtil {
    public static String parseToString(Location location) {
        return location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch() + "," +
                location.getWorld().getName();
    }

    //Translate a String to a Location
    public static Location parseToLocation(String string) {
        if (string == null) return null;
        String[] data = string.split(",");
        try {
            double x = Double.parseDouble(data[0]);
            double y = Double.parseDouble(data[1]);
            double z = Double.parseDouble(data[2]);
            float pitch = Float.parseFloat(data[4]);
            float yaw = Float.parseFloat(data[3]);
            org.bukkit.World world = Bukkit.getWorld(data[5]);
            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static JsonObject parseToJson(Location location) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", location.getX());
        jsonObject.addProperty("y", location.getY());
        jsonObject.addProperty("z", location.getZ());
        jsonObject.addProperty("yaw", location.getYaw());
        jsonObject.addProperty("pitch", location.getPitch());
        jsonObject.addProperty("world", location.getWorld().getName());
        return jsonObject;
    }

    public static Location parseToLocation(JsonObject jsonObject) {
        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        double z = jsonObject.get("z").getAsDouble();
        float yaw = jsonObject.get("yaw").getAsFloat();
        float pitch = jsonObject.get("pitch").getAsFloat();
        World world = Bukkit.getWorld(jsonObject.get("world").getAsString());
        return new Location(world, x, y, z, yaw, pitch);
    }
}
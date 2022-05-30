package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.dungeons.DungeonMap;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.dungeons.DungeonScore;
import com.kevinthegreat.skyblockmod.dungeons.LividColor;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;

import java.io.*;
import java.util.Arrays;

public class Config {

    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(new File(MinecraftClient.getInstance().runDirectory, "config"), SkyblockMod.MOD_ID + ".txt")));
            String line;
            LividColor lividColor = SkyblockMod.skyblockMod.lividColor;
            DungeonMap dungeonMap = SkyblockMod.skyblockMod.dungeonMap;
            DungeonScore dungeonScore = SkyblockMod.skyblockMod.dungeonScore;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(":");
                try {
                    switch (args[0]) {
                        case "apiKey" -> SkyblockMod.skyblockMod.api.apiKey = args[1];
                        case "fishing" -> SkyblockMod.skyblockMod.fishing.on = Boolean.parseBoolean(args[1]);
                        case "lividColor" -> lividColor.on = Boolean.parseBoolean(args[1]);
                        case "lividColorText" -> {
                            lividColor.text = Arrays.copyOf(args[1].split("\\[color]"), 2);
                            if (lividColor.text[0] == null) {
                                lividColor.text[0] = "";
                            }
                            if (lividColor.text[1] == null) {
                                lividColor.text[1] = "";
                            }
                        }
                        case "map" -> dungeonMap.on = Boolean.parseBoolean(args[1]);
                        case "mapScale" -> dungeonMap.scale = Float.parseFloat(args[1]);
                        case "mapOffsetX" -> dungeonMap.offsetX = Integer.parseInt(args[1]);
                        case "mapOffsetY" -> dungeonMap.offsetY = Integer.parseInt(args[1]);
                        case "quiverWarning" -> SkyblockMod.skyblockMod.quiverWarning.on = Boolean.parseBoolean(args[1]);
                        case "reparty" -> SkyblockMod.skyblockMod.reparty.on = Boolean.parseBoolean(args[1]);
                        case "score270" -> dungeonScore.on270 = Boolean.parseBoolean(args[1]);
                        case "score270Text" -> dungeonScore.text270 = args[1];
                        case "score300" -> dungeonScore.on300 = Boolean.parseBoolean(args[1]);
                        case "score300Text" -> dungeonScore.text300 = args[1];
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    SkyblockMod.skyblockMod.LOGGER.error("Unable to parse configuration \"" + args[0] + "\".");
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            SkyblockMod.skyblockMod.LOGGER.info("Configuration file not found.");
        } catch (IOException e) {
            SkyblockMod.skyblockMod.LOGGER.error("Error while reading configuration file.");
        }
    }

    public void save() {
        DungeonMap dungeonMap = SkyblockMod.skyblockMod.dungeonMap;
        DungeonScore dungeonScore = SkyblockMod.skyblockMod.dungeonScore;
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(new File(MinecraftClient.getInstance().runDirectory, "config"), SkyblockMod.MOD_ID + ".txt"))));
            writer.println("apiKey:" + SkyblockMod.skyblockMod.api.apiKey);
            writer.println("fishing:" + SkyblockMod.skyblockMod.fishing.on);
            writer.println("lividColor:" + SkyblockMod.skyblockMod.lividColor.on);
            writer.println("lividColorText:" + SkyblockMod.skyblockMod.lividColor.text[0] + "[color]" + SkyblockMod.skyblockMod.lividColor.text[1]);
            writer.println("map:" + dungeonMap.on);
            writer.println("mapScale:" + dungeonMap.scale);
            writer.println("mapOffsetX:" + dungeonMap.offsetX);
            writer.println("mapOffsetY:" + dungeonMap.offsetY);
            writer.println("quiverWarning:" + SkyblockMod.skyblockMod.quiverWarning.on);
            writer.println("reparty:" + SkyblockMod.skyblockMod.reparty.on);
            writer.println("score270:" + dungeonScore.on270);
            writer.println("score270Text:" + dungeonScore.text270);
            writer.println("score300:" + dungeonScore.on300);
            writer.println("score300Text:" + dungeonScore.text300);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger logger = SkyblockMod.skyblockMod.LOGGER;
            logger.error("Error while writing configuration file. Logging configuration.");
            logger.info("apiKey:" + SkyblockMod.skyblockMod.api.apiKey);
            logger.info("fishing:" + SkyblockMod.skyblockMod.fishing.on);
            logger.info("lividColor:" + SkyblockMod.skyblockMod.lividColor.on);
            logger.info("lividColorText:" + SkyblockMod.skyblockMod.lividColor.text[0] + "[color]" + SkyblockMod.skyblockMod.lividColor.text[1]);
            logger.info("map:" + dungeonMap.on);
            logger.info("mapScale:" + dungeonMap.scale);
            logger.info("mapOffsetX:" + dungeonMap.offsetX);
            logger.info("mapOffsetY:" + dungeonMap.offsetY);
            logger.info("quiverWarning:" + SkyblockMod.skyblockMod.quiverWarning.on);
            logger.info("reparty:" + SkyblockMod.skyblockMod.reparty.on);
            logger.info("score270:" + dungeonScore.on270);
            logger.info("score270Text:" + dungeonScore.text270);
            logger.info("score300:" + dungeonScore.on300);
            logger.info("score300Text:" + dungeonScore.text300);
        }
    }
}
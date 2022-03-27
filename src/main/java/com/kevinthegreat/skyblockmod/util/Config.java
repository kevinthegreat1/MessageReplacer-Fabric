package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.dungeons.DungeonMap;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.dungeons.DungeonScore;
import com.kevinthegreat.skyblockmod.dungeons.LividColor;
import org.slf4j.Logger;

import java.io.*;
import java.util.Arrays;

public class Config {

    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SkyblockMod.MOD_ID + ".txt"));
            String line;
            LividColor lividColor = SkyblockMod.skyblockMod.lividColor;
            DungeonMap dungeonMap = SkyblockMod.skyblockMod.dungeonMap;
            DungeonScore dungeonScore = SkyblockMod.skyblockMod.dungeonScore;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(":");
                try {
                    switch (args[0]) {
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
                        case "mapScale" -> dungeonMap.mapScale = Float.parseFloat(args[1]);
                        case "mapOffsetX" -> dungeonMap.mapOffsetx = Integer.parseInt(args[1]);
                        case "mapOffsetY" -> dungeonMap.mapOffsety = Integer.parseInt(args[1]);
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
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(SkyblockMod.MOD_ID + ".txt")));
            writer.println("lividColor:" + SkyblockMod.skyblockMod.lividColor.on);
            writer.println("lividColorText:" + SkyblockMod.skyblockMod.lividColor.text[0] + "[color]" + SkyblockMod.skyblockMod.lividColor.text[1]);
            writer.println("map:" + dungeonMap.on);
            writer.println("mapScale:" + dungeonMap.mapScale);
            writer.println("mapOffsetX:" + dungeonMap.mapOffsetx);
            writer.println("mapOffsetY:" + dungeonMap.mapOffsety);
            writer.println("quiverWarning" + SkyblockMod.skyblockMod.quiverWarning.on);
            writer.println("reparty:" + SkyblockMod.skyblockMod.reparty.on);
            writer.println("score270:" + dungeonScore.on270);
            writer.println("score270Text:" + dungeonScore.text270);
            writer.println("score300:" + dungeonScore.on300);
            writer.println("score300Text:" + dungeonScore.text300);
            writer.close();
        } catch (IOException e) {
            Logger logger = SkyblockMod.skyblockMod.LOGGER;
            logger.error("Error while writing configuration file. Logging configuration.");
            logger.info("lividColor:" + SkyblockMod.skyblockMod.lividColor.on);
            logger.info("lividColorText:" + SkyblockMod.skyblockMod.lividColor.text[0] + "[color]" + SkyblockMod.skyblockMod.lividColor.text[1]);
            logger.info("map:" + dungeonMap.on);
            logger.info("mapScale:" + dungeonMap.mapScale);
            logger.info("mapOffsetX:" + dungeonMap.mapOffsetx);
            logger.info("mapOffsetY:" + dungeonMap.mapOffsety);
            logger.info("quiverWarning" + SkyblockMod.skyblockMod.quiverWarning.on);
            logger.info("reparty:" + SkyblockMod.skyblockMod.reparty.on);
            logger.info("score270:" + dungeonScore.on270);
            logger.info("score270Text:" + dungeonScore.text270);
            logger.info("score300:" + dungeonScore.on300);
            logger.info("score300Text:" + dungeonScore.text300);
        }
    }
}
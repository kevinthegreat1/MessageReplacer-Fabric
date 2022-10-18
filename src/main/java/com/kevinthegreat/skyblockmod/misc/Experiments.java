package com.kevinthegreat.skyblockmod.misc;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.*;

public class Experiments {
    public boolean toggleChronomatron = true;
    public boolean toggleSuperpairs = true;
    public boolean toggleUltrasequencer = true;

    public enum Type {
        NONE, CHRONOMATRON, SUPERPAIRS, ULTRASEQUENCER
    }

    public enum State {
        REMEMBER, WAIT, SHOW, END
    }

    public Type type = Type.NONE;
    public State state = State.REMEMBER;

    public final List<Item> chronomatronSlots = new ArrayList<>();
    public int chronomatronChainLengthCount;
    public int chronomatronCurrentSlot;
    public int chronomatronCurrentOrdinal;
    public final Map<Integer, ItemStack> superpairsSlots = new HashMap<>();
    public int superpairsPrevClickedSlot;
    public ItemStack superpairsCurrentSlot;
    public final Set<Integer> superpairsDuplicatedSlots = new HashSet<>();
    public final Map<Integer, ItemStack> ultrasequencerSlots = new HashMap<>();
    public int ultrasequencerNextSlot;

    public static final Map<Item, Item> terracottaToGlass = Map.of(
            Items.RED_TERRACOTTA, Items.RED_STAINED_GLASS,
            Items.ORANGE_TERRACOTTA, Items.ORANGE_STAINED_GLASS,
            Items.YELLOW_TERRACOTTA, Items.YELLOW_STAINED_GLASS,
            Items.LIME_TERRACOTTA, Items.LIME_STAINED_GLASS,
            Items.GREEN_TERRACOTTA, Items.GREEN_STAINED_GLASS,
            Items.CYAN_TERRACOTTA, Items.CYAN_STAINED_GLASS,
            Items.LIGHT_BLUE_TERRACOTTA, Items.LIGHT_BLUE_STAINED_GLASS,
            Items.BLUE_TERRACOTTA, Items.BLUE_STAINED_GLASS,
            Items.PURPLE_TERRACOTTA, Items.PURPLE_STAINED_GLASS,
            Items.PINK_TERRACOTTA, Items.PINK_STAINED_GLASS
    );

    public Experiments() {
        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, scaledWidth, scaledHeight) -> {
            if ((toggleChronomatron || toggleSuperpairs || toggleUltrasequencer) && screen instanceof GenericContainerScreen genericContainerScreen) {
                String title = genericContainerScreen.getTitle().getString();
                int index = title.indexOf('(');
                if (11 <= index && index <= 15) {
                    switch (title.substring(0, index)) {
                        case "Chronomatron " -> {
                            if (toggleChronomatron) {
                                type = Type.CHRONOMATRON;
                                state = State.REMEMBER;
                                chronomatron(genericContainerScreen);
                            }
                        }
                        case "Superpairs " -> {
                            if (toggleSuperpairs) {
                                type = Type.SUPERPAIRS;
                                state = State.SHOW;
                                superpairs(genericContainerScreen);
                            }
                        }
                        case "Ultrasequencer " -> {
                            if (toggleUltrasequencer) {
                                type = Type.ULTRASEQUENCER;
                                state = State.REMEMBER;
                                ultrasequencer(genericContainerScreen);
                            }
                        }
                    }
                }
            }
        });
    }

    public void tick() {
        if (type != Type.NONE) {
            switch (type) {
                case CHRONOMATRON -> {
                    if (toggleChronomatron && MinecraftClient.getInstance().currentScreen instanceof GenericContainerScreen genericContainerScreen && genericContainerScreen.getTitle().getString().startsWith("Chronomatron (")) {
                        chronomatron(genericContainerScreen);
                    } else {
                        resetChronomatron();
                    }
                }
                case SUPERPAIRS -> {
                    if (toggleSuperpairs && MinecraftClient.getInstance().currentScreen instanceof GenericContainerScreen genericContainerScreen && genericContainerScreen.getTitle().getString().startsWith("Superpairs (")) {
                        superpairs(genericContainerScreen);
                    } else {
                        resetSuperpairs();
                    }
                }
                case ULTRASEQUENCER -> {
                    if (toggleUltrasequencer && MinecraftClient.getInstance().currentScreen instanceof GenericContainerScreen genericContainerScreen && genericContainerScreen.getTitle().getString().startsWith("Ultrasequencer (")) {
                        ultrasequencer(genericContainerScreen);
                    } else {
                        resetUltrasequencer();
                    }
                }
            }
        }
    }

    private void resetChronomatron() {
        chronomatronSlots.clear();
        chronomatronChainLengthCount = 0;
        chronomatronCurrentSlot = 0;
        chronomatronCurrentOrdinal = 0;
        reset();
    }

    private void resetSuperpairs() {
        superpairsSlots.clear();
        reset();
    }

    private void resetUltrasequencer() {
        ultrasequencerSlots.clear();
        reset();
    }

    private void reset() {
        type = Type.NONE;
        state = State.REMEMBER;
    }

    private void chronomatron(GenericContainerScreen screen) {
        switch (state) {
            case REMEMBER -> {
                Inventory inventory = screen.getScreenHandler().getInventory();
                if (chronomatronCurrentSlot == 0) {
                    for (int index = 10; index < 43; index++) {
                        if (inventory.getStack(index).hasEnchantments()) {
                            if (chronomatronSlots.size() <= chronomatronChainLengthCount) {
                                chronomatronSlots.add(terracottaToGlass.get(inventory.getStack(index).getItem()));
                                state = State.WAIT;
                            } else {
                                chronomatronChainLengthCount++;
                            }
                            chronomatronCurrentSlot = index;
                            return;
                        }
                    }
                } else if (!inventory.getStack(chronomatronCurrentSlot).hasEnchantments()) {
                    chronomatronCurrentSlot = 0;
                }
            }
            case WAIT -> {
                if (screen.getScreenHandler().getInventory().getStack(49).getName().getString().startsWith("Timer: ")) {
                    state = State.SHOW;
                }
            }
            case END -> {
                String name = screen.getScreenHandler().getInventory().getStack(49).getName().getString();
                if (!name.startsWith("Timer: ")) {
                    if (name.equals("Remember the pattern!")) {
                        chronomatronChainLengthCount = 0;
                        chronomatronCurrentOrdinal = 0;
                        state = State.REMEMBER;
                    } else {
                        resetChronomatron();
                    }
                }
            }
        }
    }

    private void superpairs(GenericContainerScreen screen) {
        if (state == State.SHOW) {
            if (screen.getScreenHandler().getInventory().getStack(4).isOf(Items.CAULDRON)) {
                resetSuperpairs();
            } else if (superpairsSlots.get(superpairsPrevClickedSlot) == null) {
                ItemStack itemStack = screen.getScreenHandler().getInventory().getStack(superpairsPrevClickedSlot);
                if (!(itemStack.isOf(Items.CYAN_STAINED_GLASS) || itemStack.isOf(Items.BLACK_STAINED_GLASS_PANE) || itemStack.isOf(Items.AIR))) {
                    superpairsSlots.entrySet().stream().filter((entry -> ItemStack.areEqual(entry.getValue(), itemStack))).findAny().ifPresent(entry -> superpairsDuplicatedSlots.add(entry.getKey()));
                    superpairsSlots.put(superpairsPrevClickedSlot, itemStack);
                    superpairsCurrentSlot = itemStack;
                }
            }
        }
    }

    private void ultrasequencer(GenericContainerScreen screen) {
        switch (state) {
            case REMEMBER -> {
                Inventory inventory = screen.getScreenHandler().getInventory();
                if (inventory.getStack(49).getName().getString().equals("Remember the pattern!")) {
                    for (int index = 9; index < 45; index++) {
                        ItemStack itemStack = inventory.getStack(index);
                        String name = itemStack.getName().getString();
                        if (name.matches("\\d+")) {
                            if (name.equals("1")) {
                                ultrasequencerNextSlot = index;
                            }
                            ultrasequencerSlots.put(index, itemStack);
                        }
                    }
                    state = State.WAIT;
                }
            }
            case WAIT -> {
                if (screen.getScreenHandler().getInventory().getStack(49).getName().getString().startsWith("Timer: ")) {
                    state = State.SHOW;
                }
            }
            case END -> {
                String name = screen.getScreenHandler().getInventory().getStack(49).getName().getString();
                if (!name.startsWith("Timer: ")) {
                    if (name.equals("Remember the pattern!")) {
                        ultrasequencerSlots.clear();
                        state = State.REMEMBER;
                    } else {
                        resetUltrasequencer();
                    }
                }
            }
        }
    }
}

package com.github.volbot.technolougy.tileentity.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class SmelterContainerScreen extends AbstractContainerScreen<SmelterContainer>{
    public SmelterContainerScreen(AbstractRhizomaticMachineContainer container, PlayerInventory inventory, ITextComponent text) {
        super(container, inventory, text);
    }
}

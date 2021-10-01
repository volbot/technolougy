package com.github.volbot.technolougy.tileentity.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SmelterContainerScreen extends AbstractContainerScreen<SmelterContainer>{
    public SmelterContainerScreen(AbstractRhizomaticMachineContainer container, PlayerInventory inventory, ITextComponent text) {
        super(container, inventory, text);
        this.texture=new ResourceLocation("technolougy:textures/gui/container/smelter.png");
    }

    @Override
    protected void renderBg(MatrixStack stack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        super.renderBg(stack, p_230450_2_, p_230450_3_, p_230450_4_);
        int leftPos = this.leftPos;
        int topPos = this.topPos;
        this.blit(stack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int burnProgress = this.menu.getBurnProgress();
        this.blit(stack, leftPos + 83, topPos + 35, 176, 0, burnProgress + 1, 16);
        int fluidQuantity = this.menu.getFluidQuantity();
        this.blit(stack, leftPos + 10, topPos + 49 - fluidQuantity, 176, 17, 18, fluidQuantity);
    }
}

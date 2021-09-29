package com.github.volbot.technolougy.tileentity.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbstractContainerScreen<T extends AbstractRhizomaticMachineContainer> extends ContainerScreen<T> {
    protected ResourceLocation texture;

    public AbstractContainerScreen(AbstractRhizomaticMachineContainer container, PlayerInventory inventory, ITextComponent text) {
        super((T)container, inventory, text);
    }

    @Override
    protected void renderBg(MatrixStack stack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(texture);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(stack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int burnProgress = this.menu.getBurnProgress();
        this.blit(stack, i + 83, j + 35, 176, 0, burnProgress + 1, 16);
        int fluidQuantity = this.menu.getFluidQuantity();
        this.blit(stack, i + 10, j + 49 - fluidQuantity, 176, 17, 18, fluidQuantity);
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
}

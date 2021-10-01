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
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
}

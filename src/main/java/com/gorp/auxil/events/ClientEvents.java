package com.gorp.auxil.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ClientEvents {
    
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        MatrixStack ms = event.getMatrixStack();
        IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        int light = 0xF000F0;
        int overlay = OverlayTexture.NO_OVERLAY;
        float pt = event.getPartialTicks();
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            onRenderHotbar(ms, buffers, light, overlay, pt);
        }
    }
    
    public static void onRenderHotbar(MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay, float partialTicks) {
        //DebugOverlayRenderer.renderOverlay(ms, buffer, light, overlay, partialTicks);
    }
}

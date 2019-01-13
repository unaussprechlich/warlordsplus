package net.unaussprechlich.warlordsplus;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



@Mod(modid = WarlordsPlus.MODID, version = WarlordsPlus.VERSION, clientSideOnly = true)
public class WarlordsPlus {

    static final String MODID = "warlordsplus";
    static final String VERSION = "0.1";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLLog.info("", "Invite loaded.");
    }



    @SubscribeEvent
    public void onClientTick(ClientChatReceivedEvent event){
        try {

            if(event.message.getUnformattedText().startsWith("You killed ")){
                Minecraft.getMinecraft().thePlayer.playSound(MODID + ":SUCTION" , 10, 1);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


}
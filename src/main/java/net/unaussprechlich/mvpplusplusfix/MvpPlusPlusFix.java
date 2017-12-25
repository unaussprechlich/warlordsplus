package net.unaussprechlich.mvpplusplusfix;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = MvpPlusPlusFix.MODID, version = MvpPlusPlusFix.VERSION, clientSideOnly = true)
public class MvpPlusPlusFix {

    static final String MODID = "mvpplusplusfix";
    static final String VERSION = "0.1";

    private static final String MVPppREGEX = ".*(\u00a7r)?\u00a76\\[MVP(\u00a7r)?\u00a7[0-9|a-f]\\+\\+(\u00a7r)?\u00a76].*";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLLog.info("", "[MVP++]-Fix loaded.");
    }


    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGH)
    public void onChatMessage(ClientChatReceivedEvent e) {
        try {

            if(e.message.getFormattedText().matches(MVPppREGEX)){

                IChatComponent newMessage = new ChatComponentText(e.message.getUnformattedTextForChat()).setChatStyle(e.message.getChatStyle());

                for(IChatComponent sib : e.message.getSiblings()){
                    if(sib.getFormattedText().matches(MVPppREGEX)) {
                        int indexOfMvpPP = sib.getFormattedText().indexOf("\u00a76[MVP\u00a7");

                        if(sib.getFormattedText().charAt(indexOfMvpPP + 7) == 'r'){
                            String newTag = "\u00a7b[MVP\u00a7r\u00a7" + sib.getFormattedText().charAt(indexOfMvpPP + 9) + "++\u00a7r\u00a7b]";
                            newMessage.appendSibling(new ChatComponentText(
                                    sib.getFormattedText().substring(0, indexOfMvpPP)
                                            + newTag
                                            + sib.getFormattedText().substring(indexOfMvpPP + 17))
                                    .setChatStyle(sib.getChatStyle()));

                        } else {
                            String newTag = "\u00a7b[MVP\u00a7" + sib.getFormattedText().charAt(indexOfMvpPP + 7) + "++\u00a7b]";
                            newMessage.appendSibling(new ChatComponentText(
                                    sib.getFormattedText().substring(0, indexOfMvpPP)
                                            + newTag
                                            + sib.getFormattedText().substring(indexOfMvpPP + 13))
                                    .setChatStyle(sib.getChatStyle()));
                        }
                    } else if((sib.getUnformattedText().equals("[MVP") || sib.getUnformattedText().contains("] ")) && sib.getChatStyle().getColor() == EnumChatFormatting.GOLD){
                        sib.getChatStyle().setColor(EnumChatFormatting.AQUA);
                        newMessage.appendSibling(sib);
                    } else newMessage.appendSibling(sib);
                }
                e.message = newMessage;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onPlayerName(PlayerEvent.NameFormat e) {
        try{
            ScorePlayerTeam team = (ScorePlayerTeam) e.entityPlayer.getTeam();
            if(team.getColorPrefix().matches(MVPppREGEX)){
                team.setNamePrefix("\u00a7b[MVP\u00a7" + team.getColorPrefix().charAt(7) + "++\u00a7b]");
                e.entityPlayer.refreshDisplayName();
            }

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
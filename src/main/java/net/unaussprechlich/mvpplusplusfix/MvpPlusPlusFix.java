package net.unaussprechlich.mvpplusplusfix;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod(modid = MvpPlusPlusFix.MODID, version = MvpPlusPlusFix.VERSION, acceptedMinecraftVersions = "1.8.9")
public class MvpPlusPlusFix {

    static final String MODID = "mvpppfix";
    static final String VERSION = "0.1";

    private static final String MVPppREGEX = ".*(§r)?§6\\[MVP(§r)?§[0-9|a-f]\\+\\+(§r)?§6].*";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent e) {
        try {
            System.out.println(e.message.getFormattedText().matches(MVPppREGEX) || e.message.getUnformattedText().matches(MVPppREGEX));
            if(e.message.getFormattedText().matches(MVPppREGEX) || e.message.getUnformattedText().matches(MVPppREGEX)){

                IChatComponent newMessage = new ChatComponentText(e.message.getUnformattedTextForChat()).setChatStyle(e.message.getChatStyle());

                for(IChatComponent sib : e.message.getSiblings()){
                    if(sib.getFormattedText().matches(MVPppREGEX)) {
                        int indexOfMvpPP = sib.getFormattedText().indexOf("§6[MVP§");

                        if(sib.getFormattedText().charAt(indexOfMvpPP + 7) == 'r'){
                            String newTag = "§b[MVP§r§" + sib.getFormattedText().charAt(indexOfMvpPP + 9) + "++§r§b]";
                            newMessage.appendSibling(new ChatComponentText(
                                    sib.getFormattedText().substring(0, indexOfMvpPP)
                                            + newTag
                                            + sib.getFormattedText().substring(indexOfMvpPP + 17))
                                    .setChatStyle(sib.getChatStyle()));

                        } else {
                            String newTag = "§b[MVP§" + sib.getFormattedText().charAt(indexOfMvpPP + 7) + "++§b]";
                            newMessage.appendSibling(new ChatComponentText(
                                    sib.getFormattedText().substring(0, indexOfMvpPP)
                                            + newTag
                                            + sib.getFormattedText().substring(indexOfMvpPP + 13))
                                    .setChatStyle(sib.getChatStyle()));
                        }
                    } else if((sib.getUnformattedText().equals("[MVP") || sib.getUnformattedText().contains("] ")) && sib.getChatStyle().getColor() == EnumChatFormatting.GOLD){
                        sib.getChatStyle().setColor(EnumChatFormatting.AQUA);
                        newMessage.appendSibling(sib);
                    } else {
                        newMessage.appendSibling(sib);
                    }
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
            if(team.getColorPrefix().matches(MVPppREGEX))
                team.setNamePrefix("§b[MVP§" + team.getColorPrefix().charAt(7) + "++§b]");
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
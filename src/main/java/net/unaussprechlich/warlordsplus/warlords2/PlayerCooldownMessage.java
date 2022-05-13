package net.unaussprechlich.warlordsplus.warlords2;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.unaussprechlich.warlordsplus.OtherPlayers;
import net.unaussprechlich.warlordsplus.Player;
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager;
import org.jetbrains.annotations.NotNull;

public class PlayerCooldownMessage implements IMessage {

    private String playerName;
    private int currentEnergy;
    private int maxEnergy;
    private int redCooldown;
    private int purpleCooldown;
    private int blueCooldown;
    private int orangeCooldown;

    public PlayerCooldownMessage() {
    }

    public PlayerCooldownMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerName = ByteBufUtils.readUTF8String(buf);
        this.currentEnergy = buf.readInt();
        this.maxEnergy = buf.readInt();
        this.redCooldown = buf.readInt();
        this.purpleCooldown = buf.readInt();
        this.blueCooldown = buf.readInt();
        this.orangeCooldown = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }


    public String getPlayerName() {
        return playerName;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getRedCooldown() {
        return redCooldown;
    }

    public int getPurpleCooldown() {
        return purpleCooldown;
    }

    public int getBlueCooldown() {
        return blueCooldown;
    }

    public int getOrangeCooldown() {
        return orangeCooldown;
    }

    public static class Handler implements IMessageHandler<PlayerCooldownMessage, IMessage> {

        @Override
        public IMessage onMessage(PlayerCooldownMessage message, @NotNull MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(() -> {
                if (GameStateManager.INSTANCE.isWarlords2() && GameStateManager.INSTANCE.isIngame()) {
                    Player player = OtherPlayers.INSTANCE.getPlayerForName(message.getPlayerName());
                    if (player != null) {
                        player.setCurrentEnergy(message.getCurrentEnergy());
                        player.setMaxEnergy(message.getMaxEnergy());
                        player.setRedCooldown(message.getRedCooldown());
                        player.setPurpleCooldown(message.getPurpleCooldown());
                        player.setBlueCooldown(message.getBlueCooldown());
                        player.setOrangeCooldown(message.getOrangeCooldown());
                    }
                }
            });
            return null; // no response in this case
        }
    }
}

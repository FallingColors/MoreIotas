package ram.talia.moreiotas.forge.xplat;

import at.petrak.hexcasting.common.msgs.IMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import ram.talia.moreiotas.forge.eventhandlers.ChatEventHandler;
import ram.talia.moreiotas.forge.network.ForgePacketHandler;
import ram.talia.moreiotas.xplat.IXplatAbstractions;

public class ForgeXplatImpl implements IXplatAbstractions {

	@Override
	public boolean isPhysicalClient() {
		return FMLLoader.getDist() == Dist.CLIENT;
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer target, IMessage packet) {
		ForgePacketHandler.getNetwork().send(PacketDistributor.PLAYER.with(() -> target), packet);
	}
	
	@Override
	public void sendPacketNear(Vec3 pos, double radius, ServerLevel dimension, IMessage packet) {
		ForgePacketHandler.getNetwork().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
						pos.x, pos.y, pos.z, radius * radius, dimension.dimension()
		)), packet);
	}
	
	@Override
	public Packet<?> toVanillaClientboundPacket(IMessage message) {
		return ForgePacketHandler.getNetwork().toVanillaPacket(message, NetworkDirection.PLAY_TO_CLIENT);
	}

	@Override
	public boolean isBreakingAllowed (Level level, BlockPos pos, BlockState state, Player player) {
		return !MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player));
	}

	@Override
	public @Nullable String lastMessage(@Nullable Player player) {
		return ChatEventHandler.getLastMessage(player);
	}

	@Override
	public void setChatPrefix(Player player, @Nullable String prefix) {
		ChatEventHandler.setPrefix(player, prefix);
	}

	@Override
	public @Nullable String getChatPrefix(Player player) {
		return ChatEventHandler.getPrefix(player);
	}
}

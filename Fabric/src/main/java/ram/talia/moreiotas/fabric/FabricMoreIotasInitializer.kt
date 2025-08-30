package ram.talia.moreiotas.fabric

import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import ram.talia.moreiotas.api.MoreIotasAPI
import ram.talia.moreiotas.common.lib.hex.MoreIotasActions
import ram.talia.moreiotas.common.lib.hex.MoreIotasArithmetics
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes
import ram.talia.moreiotas.fabric.eventhandlers.ChatEventHandler
import ram.talia.moreiotas.fabric.network.FabricPacketHandler
import java.util.function.BiConsumer

@Suppress("unused")
object FabricMoreIotasInitializer : ModInitializer {
    override fun onInitialize() {
        MoreIotasAPI.LOGGER.info("Hello Fabric World!")

        FabricMoreIotasConfig.setup()
        FabricPacketHandler.initServerBound()

        initListeners()

        initRegistries()
    }

    private fun initListeners() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatEventHandler::receiveChat)
    }

    private fun initRegistries() {
        MoreIotasIotaTypes.registerTypes(bind(IXplatAbstractions.INSTANCE.iotaTypeRegistry))
        MoreIotasActions.register(bind(IXplatAbstractions.INSTANCE.actionRegistry))
        MoreIotasArithmetics.register(bind(IXplatAbstractions.INSTANCE.arithmeticRegistry))
    }


    private fun <T> bind(registry: Registry<in T>): BiConsumer<T, ResourceLocation> =
        BiConsumer<T, ResourceLocation> { t, id -> Registry.register(registry, id, t) }
}
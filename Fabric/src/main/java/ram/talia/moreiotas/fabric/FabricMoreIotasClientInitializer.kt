package ram.talia.moreiotas.fabric

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import ram.talia.moreiotas.fabric.network.FabricPacketHandler
import ram.talia.moreiotas.client.RegisterClientStuff

object FabricMoreIotasClientInitializer : ClientModInitializer {
    override fun onInitializeClient() {
        FabricPacketHandler.initClientBound()

        RegisterClientStuff.init()

        RegisterClientStuff.registerBlockEntityRenderers(object : RegisterClientStuff.BlockEntityRendererRegisterer {
            override fun <T : BlockEntity> registerBlockEntityRenderer(type: BlockEntityType<T>, berp: BlockEntityRendererProvider<in T>) {
                BlockEntityRenderers.register(type, berp)
            }
        })
    }
}
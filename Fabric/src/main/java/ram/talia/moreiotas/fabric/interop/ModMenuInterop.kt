package ram.talia.moreiotas.fabric.interop

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screens.Screen
import ram.talia.moreiotas.fabric.FabricMoreIotasConfig

@Environment(EnvType.CLIENT)
class ModMenuInterop : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent: Screen -> AutoConfig.getConfigScreen(FabricMoreIotasConfig::class.java, parent).get() }
    }
}
package ram.talia.moreiotas.forge

import net.minecraftforge.event.RegisterGameTestsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import ram.talia.moreiotas.api.MoreIotasAPI

@EventBusSubscriber(modid = MoreIotasAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object MoreIotasGameTests {
	@SubscribeEvent
	fun registerTests(event: RegisterGameTestsEvent) {
		MoreIotasAPI.LOGGER.debug("registering tests")
//		event.register(WispTests::class.java)
	}
}
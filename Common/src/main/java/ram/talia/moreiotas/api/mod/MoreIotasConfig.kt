package ram.talia.moreiotas.api.mod

import net.minecraft.resources.ResourceLocation
import ram.talia.moreiotas.api.MoreIotasAPI

object MoreIotasConfig {
    interface CommonConfigAccess { }

    interface ClientConfigAccess { }

    interface ServerConfigAccess {
        val maxMatrixSize: Int
        val maxStringLength: Int

        val setBlockStringCost: Long
        val nameCost: Long

        companion object {
            const val DEF_MIN_COST = 0.0001
            const val DEF_MAX_COST = 10_000.0

            const val DEFAULT_MAX_MATRIX_SIZE: Int = 144
            const val MIN_MAX_MATRIX_SIZE: Int = 3
            const val MAX_MAX_MATRIX_SIZE: Int = 512

            const val DEFAULT_MAX_STRING_LENGTH: Int = 1728
            const val MIN_MAX_STRING_LENGTH: Int = 1
            const val MAX_MAX_STRING_LENGTH: Int = 32768

            const val DEFAULT_SET_BLOCK_STRING_COST = 0.01
            const val DEFAULT_NAME_COST = 0.01
        }
    }

    // Simple extensions for resource location configs
    fun anyMatch(keys: List<String>, key: ResourceLocation): Boolean {
        for (s in keys) {
            if (ResourceLocation.isValidResourceLocation(s)) {
                val rl = ResourceLocation(s)
                if (rl == key) {
                    return true
                }
            }
        }
        return false
    }

    fun noneMatch(keys: List<String>, key: ResourceLocation): Boolean {
        return !anyMatch(keys, key)
    }

    private object DummyCommon : CommonConfigAccess {  }
    private object DummyClient : ClientConfigAccess {  }
    private object DummyServer : ServerConfigAccess {
        override val maxMatrixSize: Int
            get() = throw IllegalStateException("Attempted to access property of Dummy Config Object")
        override val maxStringLength: Int
            get() = throw IllegalStateException("Attempted to access property of Dummy Config Object")
        override val setBlockStringCost: Long
            get() = throw IllegalStateException("Attempted to access property of Dummy Config Object")
        override val nameCost: Long
            get() = throw IllegalStateException("Attempted to access property of Dummy Config Object")
    }

    @JvmStatic
    var common: CommonConfigAccess = DummyCommon
        set(access) {
            if (field != DummyCommon) {
                MoreIotasAPI.LOGGER.warn("CommonConfigAccess was replaced! Old {} New {}",
                        field.javaClass.name, access.javaClass.name)
            }
            field = access
        }

    @JvmStatic
    var client: ClientConfigAccess = DummyClient
        set(access) {
            if (field != DummyClient) {
                MoreIotasAPI.LOGGER.warn("ClientConfigAccess was replaced! Old {} New {}",
                        field.javaClass.name, access.javaClass.name)
            }
            field = access
        }

    @JvmStatic
    var server: ServerConfigAccess = DummyServer
        set(access) {
            if (field != DummyServer) {
                MoreIotasAPI.LOGGER.warn("ServerConfigAccess was replaced! Old {} New {}",
                        field.javaClass.name, access.javaClass.name)
            }
            field = access
        }
}
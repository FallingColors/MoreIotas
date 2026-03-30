package ram.talia.moreiotas.fabric;

import at.petrak.hexcasting.api.misc.MediaConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import ram.talia.moreiotas.api.MoreIotasAPI;
import ram.talia.moreiotas.api.mod.MoreIotasConfig;
import ram.talia.moreiotas.xplat.IXplatAbstractions;

@Config(name = MoreIotasAPI.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/calcite.png")
public class FabricMoreIotasConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    public final Common common = new Common();
    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public final Client client = new Client();
    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.TransitiveObject
    public final Server server = new Server();

    public static FabricMoreIotasConfig setup() {
        AutoConfig.register(FabricMoreIotasConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        var instance = AutoConfig.getConfigHolder(FabricMoreIotasConfig.class).getConfig();

        MoreIotasConfig.setCommon(instance.common);
        // We care about the client only on the *physical* client ...
        if (IXplatAbstractions.INSTANCE.isPhysicalClient()) {
            MoreIotasConfig.setClient(instance.client);
        }
        // but we care about the server on the *logical* server
        // i believe this should Just Work without a guard? assuming we don't access it from the client ever
        MoreIotasConfig.setServer(instance.server);

        return instance;
    }


    @Config(name = "common")
    private static class Common implements ConfigData, MoreIotasConfig.CommonConfigAccess { }

    @Config(name = "client")
    private static class Client implements ConfigData, MoreIotasConfig.ClientConfigAccess { }


    @Config(name = "server")
    private static class Server implements ConfigData, MoreIotasConfig.ServerConfigAccess {
        @ConfigEntry.BoundedDiscrete(min = MIN_MAX_MATRIX_SIZE, max = MAX_MAX_MATRIX_SIZE)
        @ConfigEntry.Gui.Tooltip
        private int maxMatrixSize = DEFAULT_MAX_MATRIX_SIZE;

        @ConfigEntry.BoundedDiscrete(min = MIN_MAX_STRING_LENGTH, max = MAX_MAX_STRING_LENGTH)
        @ConfigEntry.Gui.Tooltip
        private int maxStringLength = DEFAULT_MAX_STRING_LENGTH;

        private double setBlockStringCost = DEFAULT_SET_BLOCK_STRING_COST;
        private double nameCost = DEFAULT_NAME_COST;

        private int maxChatLog = DEFAULT_MAX_CHAT_LOG;

        @Override
        public void validatePostLoad() throws ValidationException {
            this.maxMatrixSize = bound(this.maxMatrixSize, MIN_MAX_MATRIX_SIZE, MAX_MAX_MATRIX_SIZE);
            this.maxStringLength = bound(this.maxStringLength, MIN_MAX_STRING_LENGTH, MAX_MAX_STRING_LENGTH);
            this.maxChatLog = bound(this.maxChatLog, MIN_MAX_CHAT_LOG, MAX_MAX_CHAT_LOG);

            this.setBlockStringCost = bound(this.setBlockStringCost, DEF_MIN_COST, DEF_MAX_COST);
            this.nameCost = bound(this.nameCost, DEF_MIN_COST, DEF_MAX_COST);
        }

        private int bound(int toBind, int lower, int upper) {
            return Math.min(Math.max(toBind, lower), upper);
        }
        private double bound(double toBind, double lower, double upper) {
            return Math.min(Math.max(toBind, lower), upper);
        }

        @Override
        public int getMaxMatrixSize() {
            return maxMatrixSize;
        }

        @Override
        public int getMaxStringLength() {
            return maxStringLength;
        }

        @Override
        public long getSetBlockStringCost() {
            return (long) (this.setBlockStringCost * MediaConstants.DUST_UNIT);
        }

        @Override
        public long getNameCost() {
            return (long) (this.nameCost * MediaConstants.DUST_UNIT);
        }

        @Override
        public int getMaxChatLog() {
            return this.maxChatLog;
        }
    }
}

package ram.talia.moreiotas.forge;


import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraftforge.common.ForgeConfigSpec;
import ram.talia.moreiotas.api.mod.MoreIotasConfig;

public class ForgeMoreIotasConfig implements MoreIotasConfig.CommonConfigAccess {
    public ForgeMoreIotasConfig(ForgeConfigSpec.Builder builder) {

    }

    public static class Client implements MoreIotasConfig.ClientConfigAccess {
        public Client(ForgeConfigSpec.Builder builder) {

        }
    }

    public static class Server implements MoreIotasConfig.ServerConfigAccess {
        private static ForgeConfigSpec.IntValue maxMatrixSize;
        private static ForgeConfigSpec.IntValue maxStringLength;

        private static ForgeConfigSpec.DoubleValue setBlockStringCost;
        private static ForgeConfigSpec.DoubleValue nameCost;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Spells");
            maxMatrixSize = builder.comment("How large can matrices be")
                    .defineInRange("maxMatrixSize", DEFAULT_MAX_MATRIX_SIZE, MIN_MAX_MATRIX_SIZE, MAX_MAX_MATRIX_SIZE);
            maxStringLength = builder.comment("How long can strings be")
                    .defineInRange("maxStringLength", DEFAULT_MAX_STRING_LENGTH, MIN_MAX_STRING_LENGTH, MAX_MAX_STRING_LENGTH);

            setBlockStringCost = builder.comment("How much dust should string/block/set cost?")
                    .defineInRange("setBlockStringCost", DEFAULT_SET_BLOCK_STRING_COST, DEF_MIN_COST, DEF_MAX_COST);
            nameCost = builder.comment("How much dust should string/name cost?")
                    .defineInRange("nameCost", DEFAULT_NAME_COST, DEF_MIN_COST, DEF_MAX_COST);
        }

            @Override
        public int getMaxMatrixSize() {
            return maxMatrixSize.get();
        }

        @Override
        public int getMaxStringLength() {
            return maxStringLength.get();
        }

        @Override
        public long getSetBlockStringCost() {
            return (long) (setBlockStringCost.get() * MediaConstants.DUST_UNIT);
        }

        @Override
        public long getNameCost() {
            return (long) (nameCost.get() * MediaConstants.DUST_UNIT);
        }
    }
}

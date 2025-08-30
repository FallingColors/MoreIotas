package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.utils.HexUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.api.mod.MoreIotasConfig;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

public class StringIota extends Iota {
    private StringIota(@NotNull String string) {
        super(MoreIotasIotaTypes.STRING, string);
    }

    public static StringIota make(@NotNull String string) throws MishapInvalidIota {
        if (string.length() > MoreIotasConfig.getServer().getMaxStringLength())
            throw MishapInvalidIota.of(new StringIota(string), 0, "string.max_size", MoreIotasConfig.getServer().getMaxStringLength(), string.length());
        return new StringIota(string);
    }

    public static StringIota makeUnchecked(@NotNull String string) {
        return new StringIota(string);
    }

    public String getString() {
        return (String) this.payload;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
                && that instanceof StringIota sthat
                && this.getString().equals(sthat.getString());
    }

    @Override
    public boolean isTruthy() {
        return !this.getString().isEmpty();
    }

    @Override
    public @NotNull Tag serialize() {
        return StringTag.valueOf(this.getString());
    }

    public static IotaType<StringIota> TYPE = new IotaType<>() {
        @Override
        public StringIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            var stag = HexUtils.downcast(tag, StringTag.TYPE);

            try {
                return StringIota.make(stag.getAsString());
            } catch (MishapInvalidIota e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public Component display(Tag tag) {
            if (!(tag instanceof StringTag stag)) {
                return Component.translatable("hexcasting.spelldata.unknown");
            }
            return Component.translatable("moreiotas.tooltip.string", stag.getAsString())
                    .withStyle(ChatFormatting.LIGHT_PURPLE);
        }

        @Override
        public int color() {
            return 0xff_ff55ff;
        }
    };
}

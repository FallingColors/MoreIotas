package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Optional;

import static ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.ITEM_STACK;

public class ItemStackIota extends Iota {
    private ItemStackIota(ItemStack stack) {
        super(ITEM_STACK, stack);
    }

    public ItemStack getItemStack() {
        return (ItemStack) this.payload;
    }


    @Override
    protected boolean toleratesOther(Iota iota) {
        return iota instanceof ItemStackIota iiota && ItemStack.matches(this.getItemStack(), iiota.getItemStack());
    }

    @Override
    public boolean isTruthy() {
        return !this.getItemStack().isEmpty();
    }

    @Override
    public @NotNull Tag serialize() {
        var tag = new CompoundTag();
        var stack = this.getItemStack();
        ResourceLocation item = BuiltInRegistries.ITEM.getKey(stack.getItem());
        tag.putString(TAG_STACK_ID, item.toString());
        tag.putInt(TAG_STACK_COUNT, stack.getCount());
        if (stack.getTag() != null) {
            tag.put(TAG_STACK_TAG, stack.getTag().copy());
        }
        return tag;
    }

    public static ItemStackIota createFiltered(ItemStack originalStack) {
        var stack = originalStack.copy();

        var workQueue = new ArrayDeque<Tag>();

        if (stack.hasTag()) {
            assert stack.getTag() != null;
            workQueue.addLast(stack.getTag());
        }

        while (!workQueue.isEmpty()) {
            var next = workQueue.removeFirst();

            if (next instanceof ListTag list) {
                workQueue.addAll(list);
            }

            if (next instanceof CompoundTag ctag) {
                if (ctag.contains(TAG_STACK_ID)) {
                    ctag.remove(TAG_STACK_ID);
                    ctag.remove(TAG_STACK_COUNT);
                    ctag.remove(TAG_STACK_TAG);
                }

                for (var key : ctag.getAllKeys()) {
                    workQueue.add(Objects.requireNonNull(ctag.get(key)));
                }
            }
        }

        return new ItemStackIota(stack);
    }

    public static IotaType<ItemStackIota> TYPE = new IotaType<>() {
        @Override
        public ItemStackIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            var ctag = HexUtils.downcast(tag, CompoundTag.TYPE);

            if (ctag.isEmpty())
                return new ItemStackIota(ItemStack.EMPTY);

            var item = BuiltInRegistries.ITEM.get(new ResourceLocation(ctag.getString(TAG_STACK_ID)));
            var count = ctag.getInt(TAG_STACK_COUNT);
            Optional<CompoundTag> stackTag = Optional.empty();
            if (ctag.contains(TAG_STACK_TAG, Tag.TAG_COMPOUND)) {
                stackTag = Optional.of(ctag.getCompound(TAG_STACK_TAG));
                item.verifyTagAfterLoad(stackTag.get());
            }

            ItemStack stack = new ItemStack(item, count);

            stackTag.ifPresent(stack::setTag);

            return new ItemStackIota(stack);
        }

        @Override
        public Component display(Tag tag) {
            if (!(tag instanceof CompoundTag)) {
                return Component.translatable("hexcasting.spelldata.unknown");
            }

            var stack = deserialize(tag, null).getItemStack();

            if (stack.isEmpty())
                return Component.translatable("moreiotas.tooltip.stack.empty").withStyle(Style.EMPTY.withColor(0x1E90FF));

            return Component.translatable("moreiotas.tooltip.stack.format", stack.getCount(), stack.getDisplayName()).withStyle(Style.EMPTY.withColor(0x1E90FF));
        }

        @Override
        public int color() {
            return 0xFF_1E90FF;
        }
    };

    private static final String TAG_STACK_ID = "moreiotas:stack_id";
    private static final String TAG_STACK_COUNT = "moreiotas:stack_count";
    private static final String TAG_STACK_TAG = "moreiotas:stack_tag";
}

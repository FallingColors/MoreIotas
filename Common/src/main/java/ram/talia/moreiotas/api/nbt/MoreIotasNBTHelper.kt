package ram.talia.moreiotas.api.nbt

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asInt
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import java.util.*

fun ListTag.toIotaList(level: ServerLevel): MutableList<Iota> {
	val out = ArrayList<Iota>()
	for (patTag in this) {
		val tag = patTag.asCompound
		if (tag.size() != 1) {
			out.add(PatternIota(HexPattern.fromNBT(tag)))
		} else {
			out.add(IotaType.deserialize(tag, level))
		}
	}
	return out
}

@JvmName("toNbtListSpellDatum")
fun List<Iota>.toNbtList(): ListTag {
	val patsTag = ListTag()
	for (pat in this) {
		patsTag.add(IotaType.serialize(pat))
	}
	return patsTag
}

inline fun <reified T : Entity> ListTag.toEntityList(level: ServerLevel): MutableList<T> {
	val out = ArrayList<T>()

	for (eTag in this) {
		val uuid = NbtUtils.loadUUID(eTag)
		val entity = level.getEntity(uuid)

		if (entity != null && entity.isAlive && entity is T)
			out.add(entity)
	}

	return out
}

@JvmName("toNbtListEntity")
fun List<Entity>.toNbtList(): ListTag {
	val listTag = ListTag()

	for (entity in this) {
		listTag.add(NbtUtils.createUUID(entity.uuid))
	}

	return listTag
}

fun ListTag.toIntList(): MutableList<Int> {
	val out = mutableListOf<Int>()

	for (idTag in this) {
		out.add(idTag.asInt)
	}

	return out
}

@JvmName("toNbtListInt")

fun List<Int>.toNbtList(): ListTag {
	val listTag = ListTag()

	this.forEach { listTag.add(IntTag.valueOf(it)) }

	return listTag
}

@JvmName("toNbtListTag")
fun List<Tag>.toNbtList(): ListTag {
	val listTag = ListTag()

	this.forEach { listTag.add(it) }

	return listTag
}

fun ListTag.toCompoundTagList(): MutableList<CompoundTag> {
	val tags = mutableListOf<Tag>()
	tags.addAll(this)
	return tags.map { it as CompoundTag } as MutableList<CompoundTag>
}
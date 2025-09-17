@file:Suppress("unused")

package ram.talia.moreiotas.common.lib.hex

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.OperationAction
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexDir.*
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.math.HexPattern.Companion.fromAngles
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import ram.talia.moreiotas.api.MoreIotasAPI.modLoc
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.common.casting.actions.items.OpGetHeldItem
import ram.talia.moreiotas.common.casting.actions.items.OpGetInventoryContents
import ram.talia.moreiotas.common.casting.actions.items.OpSetItemCount
import ram.talia.moreiotas.common.casting.actions.matrices.*
import ram.talia.moreiotas.common.casting.actions.strings.*
import ram.talia.moreiotas.common.casting.actions.types.*
import java.util.function.BiConsumer

object MoreIotasActions {

	private val ACTIONS: MutableMap<ResourceLocation, ActionRegistryEntry> = mutableMapOf()


	@JvmStatic
	fun register(r: BiConsumer<ActionRegistryEntry, ResourceLocation>) {
		val var1: Iterator<*> = ACTIONS.entries.iterator()
		while (var1.hasNext()) {
			val (key, value) = var1.next() as Map.Entry<*, *>
			r.accept(value as ActionRegistryEntry, key as ResourceLocation)
		}
	}

	// ================================ Strings =======================================
	@JvmField
	val STRING_EMPTY = make("string/empty",
			fromAngles("awdwa", SOUTH_EAST),
			Action.makeConstantOp(StringIota.makeUnchecked("")))
	@JvmField
	val STRING_SPACE = make("string/space",
			fromAngles("awdwaaww", SOUTH_EAST),
			Action.makeConstantOp(StringIota.makeUnchecked(" ")))
	@JvmField
	val STRING_COMMA = make("string/comma",
			fromAngles("qa", EAST),
			Action.makeConstantOp(StringIota.makeUnchecked(",")))
	@JvmField
	val STRING_NEWLINE = make("string/newline",
			fromAngles("waawaw", EAST),
			Action.makeConstantOp(StringIota.makeUnchecked("\n")))
	@JvmField
	val STRING_BLOCK_GET = make("string/block/get", fromAngles("awqwawqe", EAST), OpGetBlockString)
	@JvmField
	val STRING_BLOCK_SET = make("string/block/set", fromAngles("dwewdweq", WEST), OpSetBlockString)
	@JvmField
	val STRING_CHAT_CASTER = make("string/chat/caster", fromAngles("waqa", EAST), OpChatString(false))
	@JvmField
	val STRING_CHAT_ALL = make("string/chat/all", fromAngles("wded", EAST), OpChatString(true))
	@JvmField
	val STRING_CHAT_PREFIX_GET = make("string/chat/prefix/get", fromAngles("ewded", NORTH_EAST), OpGetChatPrefix)
	@JvmField
	val STRING_CHAT_PREFIX_SET = make("string/chat/prefix/set", fromAngles("qwaqa", SOUTH_EAST), OpSetChatPrefix)
	@JvmField
	val STRING_IOTA = make("string/iota", fromAngles("wawqwawaw", EAST), OpIotaString)
	@JvmField
	val STRING_ACTION = make("string/action", fromAngles("wdwewdwdw", HexDir.NORTH_WEST), OpActionString)
	@JvmField
	val STRING_NAME_GET = make("string/name/get", fromAngles("deqqeddqwqqqwq", SOUTH_EAST), OpNameGet)
	@JvmField
	val STRING_NAME_SET = make("string/name/set", fromAngles("aqeeqaaeweeewe", SOUTH_WEST), OpNameSet)

	@JvmField
	val STRING_SPLIT = make("string/split", fromAngles("aqwaqa", EAST), OpSplitString)
	@JvmField
	val STRING_PARSE = make("string/parse", fromAngles("aqwaq", EAST), OpParseString)
	@JvmField
	val STRING_CASE = make("string/case", fromAngles("dwwdwwdwdd", WEST), OpCaseString)

	// ================================ Matrices =======================================
	// Operator Actions
	@JvmField
	val ALTADD = make("altadd", OperationAction(fromAngles("waawawaeawwaea", EAST)))
	@JvmField
	val ALTMUL = make("altmul", OperationAction(fromAngles("waqawawwaeaww", SOUTH_EAST)))
	@JvmField
	val ALTDIV = make("altdiv", OperationAction(fromAngles("wdedwdwwdqdww", NORTH_EAST)))
	@JvmField
	val ALTPOW = make("altpow", OperationAction(fromAngles("wedewqawwawqwa", NORTH_EAST)))

	@JvmField
	val MATRIX_MAKE = make("matrix/make", fromAngles("awwaeawwaadwa", SOUTH_WEST), OpMakeMatrix)
	@JvmField
	val MATRIX_UNMAKE = make("matrix/unmake", fromAngles("dwwdqdwwddawd", SOUTH_EAST), OpUnmakeMatrix(false))
	@JvmField
	val MATRIX_UNMAKE_DIRECT = make("matrix/unmake/direct", fromAngles("dwwdqdwwdwdwa", SOUTH_EAST), OpUnmakeMatrix(true))
	@JvmField
	val MATRIX_IDENTITY = make("matrix/identity", fromAngles("awwaeawwaqw", SOUTH_WEST), OpIdentityMatrix)
	@JvmField
	val MATRIX_ZERO = make("matrix/zero", fromAngles("awwaeawwa", SOUTH_WEST), OpZeroMatrix)
	@JvmField
	val MATRIX_ROTATION = make("matrix/rotation", fromAngles("awwaeawwawawddw", SOUTH_WEST), OpRotationMatrix)
	@JvmField
	val MATRIX_INVERSE = make("matrix/inverse", fromAngles("wwdqdwwdqaq", WEST), OpInverseMatrix)
	@JvmField
	val MATRIX_DETERMINANT = make("matrix/determinant", fromAngles("aeawwaeawaw", WEST), OpDeterminantMatrix)
	@JvmField
	val MATRIX_CONCAT_VERT = make(
			"matrix/concat/vert",
			fromAngles("awwaeawwawawdedwa", SOUTH_WEST),
			OpConcatMatrix(true))
	@JvmField
	val MATRIX_CONCAT_HORI = make(
			"matrix/concat/hori",
			fromAngles("dwwdqdwwdwdwaqawd", SOUTH_EAST),
			OpConcatMatrix(false))
	@JvmField
	val MATRIX_SPLIT_VERT = make(
			"matrix/split/vert",
			fromAngles("awdedwawawwaeawwa", SOUTH_EAST),
			OpSplitMatrix(true))
	@JvmField
	val MATRIX_SPLIT_HORI = make(
			"matrix/split/hori",
			fromAngles("dwaqawdwdwwdqdwwd", SOUTH_WEST),
			OpSplitMatrix(false))

	// ================================ Types =======================================

	@JvmField
	val TYPE_TO_ITEM = make("type/to_item", OperationAction(fromAngles("qaqqaea", EAST)))
	@JvmField
	val TYPE_ENTITY = make("type/entity", fromAngles("qawde", SOUTH_WEST), OpTypeEntity)
	@JvmField
	val TYPE_IOTA = make("type/iota", fromAngles("awd", SOUTH_WEST), OpTypeIota)
	@JvmField
	val TYPE_ITEM_HELD = make("type/item_held", fromAngles("edeedqd", SOUTH_WEST), OpTypeItemHeld)

	@JvmField
	val GET_ENTITY_TYPE = make("get_entity/type", fromAngles("dadqqqqqdad", NORTH_EAST), OpGetEntityAtDyn)
	@JvmField
	val ZONE_ENTITY_TYPE = make("zone_entity/type", fromAngles("waweeeeewaw", SOUTH_EAST), OpGetEntitiesByDyn(false))
	@JvmField
	val ZONE_ENTITY_NOT_TYPE = make("zone_entity/not_type", fromAngles("wdwqqqqqwdw", NORTH_EAST), OpGetEntitiesByDyn(true))

	// ================================ Item Stacks =======================================

	@JvmField
	val ITEM_GET_MAIN_HAND = make("item/main_hand", fromAngles("adeq", EAST), OpGetHeldItem(InteractionHand.MAIN_HAND))
	@JvmField
	val ITEM_GET_OFF_HAND = make("item/off_hand", fromAngles("qeda", EAST), OpGetHeldItem(InteractionHand.OFF_HAND))
	@JvmField
	val ITEM_GET_INVENTORY_STACKS = make("item/inventory/stacks", fromAngles("aqwed", NORTH_EAST), OpGetInventoryContents(returnStacks = true))
	@JvmField
	val ITEM_GET_INVENTORY_ITEMS = make("item/inventory/items", fromAngles("dewqa", NORTH_EAST), OpGetInventoryContents(returnStacks = false))
	@JvmField
	val ITEM_PROP_SIZE_SET = make("item/prop/size/set", fromAngles("adeeedew", EAST), OpSetItemCount)


	private fun make(name: String, pattern: HexPattern, action: Action): ActionRegistryEntry = make(name, ActionRegistryEntry(pattern, action))

	private fun make(name: String, are: ActionRegistryEntry): ActionRegistryEntry {
		return if (ACTIONS.put(modLoc(name), are) != null) {
			throw IllegalArgumentException("Typo? Duplicate id $name")
		} else {
			are
		}
	}

	private fun make(name: String, oa: OperationAction): ActionRegistryEntry {
		val are = ActionRegistryEntry(oa.pattern, oa)
		return if (ACTIONS.put(modLoc(name), are) != null) {
			throw IllegalArgumentException("Typo? Duplicate id $name")
		} else {
			are
		}
	}
}
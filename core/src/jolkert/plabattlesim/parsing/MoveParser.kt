package jolkert.plabattlesim.parsing

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.moves.*
import jolkert.plabattlesim.data.pokemon.Position

object MoveParser
{
	fun deserialize(file: FileHandle) = deserialize(file.nameWithoutExtension(), file.readString())

	fun deserialize(name: String, data: String): Move
	{
		val json = JsonReader().parse(data)
		val move = Move(
			name = name,
			type = Type.Registry[json["type"].asString()],
			category = Category.fromString(json["type"].asString()),
			pp = json["pp"].asInt(),
			power = json["power"].asStyleTriad(),
			accuracy = json["accuracy"].asStyleTriad(),
			userActionTime = json["userActionTime"].asStyleTriad(),
			targetActionTime = json["targetActionTime"].asStyleTriad(),
			critStage = json["critRate"].asStyleTriad()
		)
		val effectList = move.effects as MutableList<MoveEffect>

		for (effect: JsonValue in json["effects"])
		{
			val condition = effect.getOrNull("condition") { MoveEffect.Condition.fromJson(it) } ?: MoveEffect.Condition.None
			val effectValue: MoveEffect = when (effect["effectType"].asString())
			{
				"heal" -> MoveEffect.Heal(
					percentageOf = MoveEffect.HealRecoilPercentage.fromString(effect.getString("percentageOf")),
					percentage = effect.getInt("percentage"),
					condition = condition
				)
				"recoil" -> MoveEffect.Recoil(
					percentageOf = MoveEffect.HealRecoilPercentage.fromString(effect.getString("percentageOf")),
					percentage = effect.getInt("percentage"),
					condition = condition
				)
				"applyStatus" -> MoveEffect.ApplyStatus(
					to = Position.fromString(effect.getString("to")),
					duration = effect["duration"].asStyleTriad(),
					chance = effect["chance"].asStyleTriad(),
					statusOptions = effect["statuses"].asStringArray(),
					condition = condition
				)
				"cureStatus" -> MoveEffect.CureStatus(
					of = Position.fromString(effect.getString("to")),
					statuses = effect["statuses"].asStringArray(),
					condition = condition
				)
				"multiplyPower" -> MoveEffect.MultiplyPower(
					multiplier = effect.getInt("multiplier"),
					condition = condition
				)
				"modifyMoveData" -> MoveEffect.ModifyMoveData(
					power = effect.getOrNull("power") { it.asStyleTriad() },
					accuracy = effect.getOrNull("accuracy") { it.asStyleTriad() },
					userActionTime = effect.getOrNull("userActionTime") { it.asStyleTriad() },
					targetActionTime = effect.getOrNull("targetActionTime") { it.asStyleTriad() },
					critStage = effect.getOrNull("critStage") { it.asStyleTriad() }
				)
				"swapOffenseAndDefense" -> MoveEffect.SwapOffenseAndDefense(
					of = Position.fromString(effect.getString("of")),
					condition = condition
				)

				else -> throw UnsupportedOperationException("Could not parse effectType ${effect["effectType"].asString()}")
			}

			effectList.add(effectValue)
		}

		return move
	}

	private fun JsonValue.getOrNull(key: String): JsonValue? = if (has(key)) this[key] else null
	private fun <T> JsonValue.getOrNull(key: String, transform: (JsonValue) -> T): T? = getOrNull(key)?.let(transform)
	private fun JsonValue.asStyleTriad(): StyleTriad<Int> =
		StyleTriad(this["regular"].asInt(), this["agile"].asInt(), this["strong"].asInt())
}
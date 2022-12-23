package jolkert.plabattlesim.data.moves

import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import jolkert.plabattlesim.data.asStyleTriad
import jolkert.plabattlesim.data.getOrNull
import jolkert.plabattlesim.data.getStringTransform
import jolkert.plabattlesim.data.pokemon.Position

abstract class MoveEffect
{
	companion object
	{
		@JvmStatic fun fromJson(data: JsonValue): MoveEffect
		{
			val condition = data.getOrNull("condition") { Condition.fromJson(it) } ?: Condition.None
			val effect: MoveEffect = when (data["effectType"].asString())
			{
				"heal" -> Heal(
					percentageOf = HealRecoilPercentage.fromString(data.getString("percentageOf")),
					percentage = data.getInt("percentage"),
					condition = condition
				)
				"recoil" -> Recoil(
					percentageOf = HealRecoilPercentage.fromString(data.getString("percentageOf")),
					percentage = data.getInt("percentage"),
					condition = condition
				)
				"applyStatus" -> ApplyStatus(
					to = data.getStringTransform("to") { Position.fromString(it) },
					duration = data["duration"].asStyleTriad(),
					chance = data["chance"].asStyleTriad(),
					statusOptions = data["statuses"].asStringArray(),
					condition = condition
				)
				"cureStatus" -> CureStatus(
					of = data.getStringTransform("of") { Position.fromString(it) },
					statuses = data["statuses"].asStringArray(),
					condition = condition
				)
				"multiplyPower" -> MultiplyPower(
					multiplier = data.getInt("multiplier"),
					condition = condition
				)
				"modifyMoveData" -> ModifyMoveData(
					power = data.getOrNull("power") { it.asStyleTriad() },
					accuracy = data.getOrNull("accuracy") { it.asStyleTriad() },
					userActionTime = data.getOrNull("userActionTime") { it.asStyleTriad() },
					targetActionTime = data.getOrNull("targetActionTime") { it.asStyleTriad() },
					critStage = data.getOrNull("critStage") { it.asStyleTriad() }
				)
				"swapOffenseAndDefense" -> SwapOffenseAndDefense(
					of = data.getStringTransform("of") { Position.fromString(it) },
					condition = condition
				)

				else -> throw UnsupportedOperationException("Could not parse effectType ${data["effectType"].asString()}")
			}

			return effect
		}

		@JvmStatic fun fromJson(data: String) = fromJson(JsonReader().parse(data))
	}

	open val condition: Condition = Condition.None

	// subclasses
	data class Heal(val percentageOf: HealRecoilPercentage = HealRecoilPercentage.DamageDealt,
					val percentage: Int = 0,
					override val condition: Condition = Condition.None) : MoveEffect()
	data class Recoil(val percentageOf: HealRecoilPercentage,
					  val percentage: Int,
					  override val condition: Condition = Condition.None): MoveEffect()
	data class ApplyStatus(val to: Position,
						   val statusOptions: Array<String>,
						   val duration: StyleTriad<Int>,
						   val chance: StyleTriad<Int>,
						   override val condition: Condition = Condition.None) : MoveEffect()
	{
		override fun equals(other: Any?): Boolean
		{
			if (this === other) return true
			if (javaClass != other?.javaClass) return false

			other as ApplyStatus

			if (to != other.to) return false
			if (!statusOptions.contentEquals(other.statusOptions)) return false
			if (duration != other.duration) return false
			if (chance != other.chance) return false

			return true
		}

		override fun hashCode(): Int
		{
			var result = to.hashCode()
			result = 31 * result + statusOptions.contentHashCode()
			result = 31 * result + duration.hashCode()
			result = 31 * result + chance.hashCode()
			return result
		}
	}
	data class CureStatus(val of: Position,
						  val statuses: Array<String>,
						  override val condition: Condition = Condition.None) : MoveEffect()
	{
		override fun equals(other: Any?): Boolean
		{
			if (this === other) return true
			if (javaClass != other?.javaClass) return false

			other as CureStatus

			if (of != other.of) return false
			if (!statuses.contentEquals(other.statuses)) return false

			return true
		}

		override fun hashCode(): Int
		{
			var result = of.hashCode()
			result = 31 * result + statuses.contentHashCode()
			return result
		}
	}
	data class MultiplyPower(val multiplier: Int,
							 override val condition: Condition = Condition.None) : MoveEffect()
	data class ModifyMoveData(val power: StyleTriad<Int>? = null,
							  val accuracy: StyleTriad<Int>? = null,
							  val userActionTime: StyleTriad<Int>? = null,
							  val targetActionTime: StyleTriad<Int>? = null,
							  val critStage: StyleTriad<Int>? = null,
							  override val condition: Condition = Condition.None
	) : MoveEffect()
	data class SwapOffenseAndDefense(val of: Position,
									 override val condition: Condition = Condition.None) : MoveEffect()

	data class Condition(val user: PokemonConditionData? = null, val target: PokemonConditionData? = null)
	{
		companion object
		{
			@JvmStatic val None: Condition = Condition(null, null)
			@JvmStatic fun fromJson(json: JsonValue): Condition
			{
				fun createData(dataJson: JsonValue): PokemonConditionData
				{
					val species: String? = if (dataJson.has("species")) dataJson["species"].asString() else null
					val status: Array<String>? = if (dataJson.has("status")) dataJson["status"].asStringArray() else null

					return PokemonConditionData(species, status)
				}

				var user: PokemonConditionData? = if (json.has("user")) createData(json["user"]) else null
				var target: PokemonConditionData? = if (json.has("target")) createData(json["target"]) else null

				return Condition(user, target)
			}
		}

		class PokemonConditionData(val species: String? = null, val status: Array<String>? = null)
	}

	enum class HealRecoilPercentage
	{
		DamageDealt,
		MaxHp;

		companion object
		{
			@JvmStatic fun fromString(string: String): HealRecoilPercentage = when (string.toLowerCase().replace(" ", ""))
			{
				"damagedealt" -> DamageDealt
				"maxhp" -> MaxHp
				else -> throw IllegalArgumentException("Could not convert $string to ${HealRecoilPercentage::class.qualifiedName}")
			}
		}
	}
}
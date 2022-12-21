package jolkert.plabattlesim.data.moves

import com.badlogic.gdx.utils.JsonValue
import jolkert.plabattlesim.data.pokemon.Position

abstract class MoveEffect
{
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
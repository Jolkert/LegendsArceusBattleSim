package jolkert.plabattlesim.data.pokemon

import jolkert.plabattlesim.data.Nature
import jolkert.plabattlesim.data.moves.Moveset
import jolkert.plabattlesim.data.stats.Stat
import jolkert.plabattlesim.data.stats.Stats
import kotlin.math.roundToInt
import kotlin.math.sqrt

open class Pokemon(val species: PokemonSpecies)
{
	var nickname: String = ""
		get() = if (field == "") species.name else field
	val hasNickname: Boolean
		get() = nickname != species.name
	var isShiny: Boolean = false

	var level: Int = 1
	var nature: Nature = Nature.Default
	var effortLevels: Stats = Stats()
	val stats: Stats
		get()
		{
			val statArray = IntArray(6)
			for (i in statArray.indices)
				statArray[i] = calculateStat(Stat.fromInt(i))

			return Stats.fromArray(statArray)
		}

	var moveset: Moveset = Moveset(4)

	constructor(species: PokemonSpecies,
				nickname: String = "",
				isShiny: Boolean = false,
				level: Int = 1,
				effortLevels: Stats = Stats(),
				nature: Nature = Nature.Default): this(species)
	{
		this.nickname = nickname
		this.isShiny = isShiny
		this.level = level
		this.effortLevels = effortLevels
		this.nature = nature
	}

	fun resetNickname()
	{
		nickname = ""
	}

	private fun calculateStat(stat: Stat): Int
	{
		val base = species.baseStats[stat]

		return if (stat == Stat.Hp)
			((level/100f + 1) * base + level + effortLevelBonus(stat)).toInt()
		else
		{
			var natureMod = 1f
			if (nature.boostedStat != nature.loweredStat)
			{
				if (nature.boostedStat == stat)
					natureMod = 1.1f
				else if (nature.loweredStat == stat)
					natureMod = 0.9f
			}

			((((level/50f + 1) * base)/1.5f).toInt() * natureMod + effortLevelBonus(stat)).toInt()
		}
	}
	private fun effortLevelBonus(stat: Stat): Int =
		((sqrt(species.baseStats[stat].toFloat()) * effortLevelMultiplier(effortLevels[stat]) + level) / 2.5f).roundToInt()

	companion object
	{
		private val effortMultiplier = intArrayOf(0, 2, 3, 4, 7, 8, 9, 14, 15, 16, 25)

		@JvmStatic fun effortLevelMultiplier(level: Int): Int
		{
			if (level !in effortMultiplier.indices)
				throw IllegalArgumentException("Attempted to get multiplier for unsupported effort level $level!")

			return effortMultiplier[level]
		}
	}
}
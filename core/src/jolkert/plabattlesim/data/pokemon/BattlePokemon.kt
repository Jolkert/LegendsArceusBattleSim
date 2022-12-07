package jolkert.plabattlesim.data.pokemon

import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.damageMultiplierFrom
import jolkert.plabattlesim.data.moves.Category
import jolkert.plabattlesim.data.moves.Move
import jolkert.plabattlesim.data.moves.Style
import jolkert.plabattlesim.data.stats.Stat
import jolkert.plabattlesim.data.stats.Stats
import kotlin.random.Random
import kotlin.random.nextInt

class BattlePokemon(species: PokemonSpecies) : Pokemon(species)
{
	var currentHp: Int = stats[Stat.Hp]; private set
	var actionTime: Int = -1; private set // TODO: add this later lol
	val effectiveStats: Stats get() = stats // TODO: add modifiers later

	constructor(pokemon: Pokemon) : this(pokemon.species)

	fun useMove(index: Int, target: BattlePokemon, style: Style)
	{
		val move = moveset[index]

		if (move.category.isDamaging())
		{
			val damage = calculateDamage(this, target, move, style)
			target.takeDamage(damage)
		}
	}

	fun takeDamage(damage: Int)
	{
		currentHp = (currentHp - damage).coerceAtLeast(0)
	}

	fun isType(type: Type): Boolean
	{
		val types = species.types
		return types.first == type || types.second == type
	}

	companion object
	{
		@JvmStatic fun calculateDamage(attacker: BattlePokemon, target: BattlePokemon, move: Move, style: Style,
									   ignoreRandom: Boolean = false): Int
		{
			val attackStat  = if (move.category == Category.Physical) Stat.Attack  else Stat.SpecialAttack
			val defenseStat = if (move.category == Category.Physical) Stat.Defense else Stat.SpecialDefense

			// base
			var damage: Int = ((attacker.effectiveStats[attackStat] + (15 * attacker.level) + 100) * move.power[style] /
					(5f * (target.effectiveStats[defenseStat] + 50))
					).toInt()

			// type effectiveness
			damage = (damage * target.species.types.damageMultiplierFrom(move.type)).toInt()

			// stab
			if (attacker.isType(move.type))
				damage = (damage * 1.25f).toInt()

			// random factor
			if (!ignoreRandom)
				damage = (damage.toFloat() * Random.Default.nextInt(85..100) / 100f).toInt()

			// critical
			if (Random.Default.nextInt(24) == 0)
				damage = damage * 2/3 // should be equivalent to * 1.5f without needing to cast -jolk 2022-12-05

			// TODO: burn/frostbite, drowsy, fixation, primed, atk/def mods, rain?? -jolk 2022-12-05
			return damage
		}
	}
}
package jolkert.plabattlesim.data.pokemon

import jolkert.plabattlesim.data.StatusCondition
import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.damageMultiplierFrom
import jolkert.plabattlesim.data.moves.Category
import jolkert.plabattlesim.data.moves.Move
import jolkert.plabattlesim.data.stats.Stat
import jolkert.plabattlesim.data.stats.Stats

class BattlePokemon(val pokemon: Pokemon)
{
	var currentHp: Int = pokemon.stats[Stat.Hp]
	val effectiveStats: Stats
		get() = pokemon.stats // TODO: add modifiers later


	fun useMove(index: Int, target: BattlePokemon)
	{
		val move = pokemon.moveset[index]

		if (move.category.isDamaging())
		{
			val damage = calculateDamage(this, target, move)
			target.takeDamage(damage)
		}
	}

	fun takeDamage(damage: Int)
	{
		currentHp = (currentHp - damage).coerceAtLeast(0)
	}

	fun isType(type: Type): Boolean
	{
		val types = pokemon.species.types
		return types.first == type || types.second == type
	}

	companion object
	{
		@JvmStatic fun calculateDamage(attacker: BattlePokemon, target: BattlePokemon, move: Move): Int
		{
			val attackStat  = if (move.category == Category.Physical) Stat.Attack  else Stat.SpecialAttack
			val defenseStat = if (move.category == Category.Physical) Stat.Defense else Stat.SpecialDefense

			var damage: Int = ((100 + attacker.effectiveStats[attackStat] + (15 * attacker.pokemon.level)) /
					     (5f * (target.effectiveStats[defenseStat] + 50)))
						 .toInt() // base
			damage = (damage * target.pokemon.species.types.damageMultiplierFrom(move.type)).toInt() // weakness
			if (attacker.isType(move.type)) // STAB
				damage = (damage * 1.25f).toInt()

			// TODO: modifiers before return
			return damage
		}
	}
}
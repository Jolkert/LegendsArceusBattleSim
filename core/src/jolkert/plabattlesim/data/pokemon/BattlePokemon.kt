package jolkert.plabattlesim.data.pokemon

import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.damageMultiplierFrom
import jolkert.plabattlesim.data.moves.Category
import jolkert.plabattlesim.data.moves.Move
import jolkert.plabattlesim.data.moves.Style
import jolkert.plabattlesim.data.stats.Stat
import jolkert.plabattlesim.data.stats.Stats
import jolkert.plabattlesim.data.status.Effect
import jolkert.plabattlesim.data.status.StatusData
import jolkert.plabattlesim.data.status.StatusCondition
import kotlin.random.Random
import kotlin.random.nextInt

class BattlePokemon(species: PokemonSpecies) : Pokemon(species)
{
	var currentHp: Int = stats[Stat.Hp]; private set
	var actionTime: Int = getBaseActionTime()
	val effectiveStats: Stats
		get() = stats // TODO: add modifiers later

	var primaryStatus: StatusCondition = StatusCondition.None
	val secondaryStatuses: MutableList<StatusCondition> = emptyList<StatusCondition>() as MutableList<StatusCondition>

	fun useMove(index: Int, target: BattlePokemon, style: Style)
	{
		val stunChance = getStunChance()
		if (100 - stunChance < Random.Default.nextInt(100))
			return

		val move = moveset[index]
		if (move.category.isDamaging())
		{
			val damage = calculateDamage(this, target, move, style)
			target.takeDamage(damage)
		}
	}
	private fun getStunChance(): Int
	{
		for (effect in primaryStatus.data.effects)
			if (effect is Effect.TurnCancel)
				return effect.chance

		for (status in secondaryStatuses)
			for (effect in status.data.effects)
				if (effect is Effect.TurnCancel)
					return effect.chance

		return 0
	}

	fun takeDamage(damage: Int)
	{
		currentHp = (currentHp - damage).coerceAtLeast(0)
	}
	fun acquireStatus(status: StatusData, turnCount: Int)
	{
		val condition = StatusCondition(status, turnCount)
		if (status.isPrimary)
			primaryStatus = condition
		else
			secondaryStatuses.add(condition)
	}
	fun tickStatuses()
	{
		if (primaryStatus.data != StatusData.Healthy && primaryStatus.tickDown() < 1)
			primaryStatus = StatusCondition.None

		for (condition in secondaryStatuses)
			if (condition.tickDown() < 1)
				secondaryStatuses.remove(condition)
	}

	fun isType(type: Type): Boolean
	{
		val types = species.types
		return types.first == type || types.second == type
	}
	fun getBaseActionTime(): Int = when (effectiveStats.speed)
	{// TODO: make this not shit
		in 0..5 -> 14
		in 16..31 -> 13
		in 32..55 -> 12
		in 56..88 -> 11
		in 89..129 -> 10
		in 130..181 -> 9
		in 182..242 -> 8
		in 243..316 -> 7
		in 317..401 -> 6
		else -> 5
	}

	companion object
	{
		@JvmStatic fun calculateDamage(attacker: BattlePokemon, target: BattlePokemon, move: Move, style: Style,
									   ignoreRandom: Boolean = false): Int
		{
			val attackStat  = if (move.category == Category.Physical) Stat.Attack  else Stat.SpecialAttack
			val defenseStat = if (move.category == Category.Physical) Stat.Defense else Stat.SpecialDefense

			// base
			var damage: Int = (
					move.power[style] * (attacker.effectiveStats[attackStat] + (15 * attacker.level) + 100) /
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
package jolkert.plabattlesim.data.pokemon

import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.damageMultiplierFrom
import jolkert.plabattlesim.data.moves.Category
import jolkert.plabattlesim.data.moves.Move
import jolkert.plabattlesim.data.moves.MoveResult
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
	var actionTime: Int = baseActionTime
	val baseActionTime: Int
		get() = when (effectiveStats.speed)
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
	val effectiveStats: Stats
		get()
		{
			val effectiveStats = stats
			for (effect in effects)
				if (effect is Effect.StatModifier)
					stats[effect.stat] = (stats[effect.stat] * effect.multiplier).toInt()

			return effectiveStats
		}

	var primaryStatus: StatusCondition = StatusCondition.None
	val secondaryStatuses: MutableList<StatusCondition> = emptyList<StatusCondition>() as MutableList<StatusCondition>
	val statuses: Sequence<StatusCondition>
		get() = sequence()
		{
			yield(primaryStatus)
			for (status in secondaryStatuses)
				yield(status)
		}
	val effects: Sequence<Effect>
		get() = sequence()
		{
			for (status in statuses)
				for (effect in status.data.effects)
					yield(effect)
		}

	@JvmName("getEffectsOfType")
	inline fun <reified T> getEffects(): Sequence<T> = sequence()
	{
		for (effect in effects)
			if (effect is T)
				yield(effect as T)
	}
	private fun getStunChance(): Int = getEffects<Effect.CancelTurn>().firstOrNull()?.chance ?: 0


	fun useMove(index: Int, target: BattlePokemon, style: Style): MoveResult
	{
		// try para/drowsy
		val stunChance = getStunChance()
		if (100 - stunChance < Random.Default.nextInt(100))
			return MoveResult.Stunned


		val move = moveset[index]
		if (move.accuracy[style] < 101 && move.accuracy[style] < Random.Default.nextInt(100))
			return MoveResult.Miss

		if (move.category.isDamaging())
		{
			val damage = calculateDamage(this, target, move, style)
			target.takeDamage(damage)
		}

		// TODO: add non-damaging effects -jolk 2022-12-08
		return MoveResult.Success
	}
	fun endTurn()
	{
		tickStatuses()
		actionTime = baseActionTime
	}
	fun takeDamage(damage: Int)
	{
		currentHp = (currentHp - damage).coerceAtLeast(0)
	}

	fun acquireStatus(status: StatusData, turnCount: Int)
	{
		for (type in status.immuneTypes)
			if (isType(type))
				return

		val condition = StatusCondition(status, turnCount)
		if (status.isPrimary)
			primaryStatus = condition
		else
			secondaryStatuses.add(condition)
	}
	fun tickStatuses()
	{
		for (effect in effects)
		{
			if (effect is Effect.TurnEndDamageFraction)
				takeDamage((1f / effect.fraction * stats.hp).toInt())
			// TODO: add splinters effect -jolk 2022-12-08
		}

		for (status in statuses)
		{
			if (status.data != StatusData.Healthy && status.tickDown() < 1)
			{
				if (status.data.isPrimary)
					primaryStatus = StatusCondition.None
				else
					secondaryStatuses.remove(status)
			}
		}
	}

	fun isType(type: Type): Boolean
	{
		val types = species.types
		return types.first == type || types.second == type
	}


	companion object
	{
		@JvmStatic val None: BattlePokemon = BattlePokemon(PokemonSpecies.None)

		@JvmStatic fun calculateDamage(attacker: BattlePokemon, target: BattlePokemon, move: Move, style: Style,
									   ignoreRandom: Boolean = false): Int
		{
			val attackStat = if (move.category == Category.Physical) Stat.Attack else Stat.SpecialAttack
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
				damage = damage * 2 / 3 // should be equivalent to * 1.5f without needing to cast -jolk 2022-12-05

			// attacker effects
			for (effect in attacker.getEffects<Effect.DamageMultiplier>()
				.takeWhile { it.position == Position.Attacker })
				if (effect.category == Category.None || effect.category == move.category)
					damage = (damage * effect.multiplier).toInt()

			// target effects
			for (effect in target.getEffects<Effect.DamageMultiplier>().takeWhile { it.position == Position.Target })
				if (effect.category == Category.None || effect.category == move.category)
					damage = (damage * effect.multiplier).toInt()

			return damage
		}
	}
}
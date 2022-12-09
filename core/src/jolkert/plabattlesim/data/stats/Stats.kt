package jolkert.plabattlesim.data.stats

data class Stats(var hp: Int, var attack: Int, var defense: Int, var specialAttack: Int, var specialDefense: Int, var speed: Int)
{
	constructor() : this(0, 0, 0, 0, 0, 0)

	operator fun get(stat: Stat): Int = when (stat)
	{
		Stat.Hp -> hp
		Stat.Attack -> attack
		Stat.Defense -> defense
		Stat.SpecialAttack -> specialAttack
		Stat.SpecialDefense -> specialDefense
		Stat.Speed -> speed
	}
	operator fun set(stat: Stat, value: Int) = when (stat)
	{
		Stat.Hp -> hp = value
		Stat.Attack -> attack = value
		Stat.Defense -> defense = value
		Stat.SpecialAttack -> specialAttack = value
		Stat.SpecialDefense -> specialDefense = value
		Stat.Speed -> speed = value
	}

	companion object
	{
		@JvmStatic fun fromArray(array: IntArray): Stats
		{
			if (array.size != 6)
				throw IllegalArgumentException("Stats.fromArray expected 6 values, got ${array.size}!")

			return Stats(array[0], array[1], array[2], array[3], array[4], array[5])
		}
	}
}
package jolkert.plabattlesim.data.stats

class Stats(hp: Int, atk: Int, def: Int, spatk: Int, spdef: Int, spe: Int)
{
	private val statArray: IntArray

	init
	{
		statArray = intArrayOf(hp, atk, def, spatk, spdef, spe)
	}

	constructor() : this(0, 0, 0, 0, 0, 0)

	operator fun get(stat: Stat): Int = statArray[stat.ordinal]
	operator fun set(stat: Stat, value: Int)
	{
		statArray[stat.ordinal] = value
	}

	fun asArray(): IntArray = statArray

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
package jolkert.plabattlesim.data

import jolkert.plabattlesim.data.stats.Stat
import jolkert.plabattlesim.registry.Registry

data class Nature(val name: String, val boostedStat: Stat, val loweredStat: Stat)
{
	override fun toString() = "$name (+${boostedStat.toString(true)} -${loweredStat.toString(true)})"

	companion object
	{
		@JvmStatic val Registry = Registry(Nature::class.java)
		@JvmStatic val Default = Nature("serious", Stat.Speed, Stat.Speed)
	}
}
package jolkert.plabattlesim.data.pokemon

import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.stats.Stats
import jolkert.plabattlesim.registry.Registry

data class PokemonSpecies(val name: String, val baseStats: Stats, val types: Pair<Type, Type>)
{
	override fun toString(): String
	{
		return "$name:\n" +
				"\tType: ${types.first.name}${if (types.second != Type.None) "/${types.second.name}" else ""}\n" +
				"\tBase Stats: $baseStats"
	}

	companion object
	{
		@JvmStatic val Registry = Registry(PokemonSpecies::class.java)
		@JvmStatic val None = PokemonSpecies("none", Stats.fromArray(IntArray(6)), Pair(Type.None, Type.None))
	}
}
package jolkert.plabattlesim.data.pokemon

import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.stats.Stats

data class PokemonSpecies(val name: String, val baseStats: Stats, val types: Pair<Type, Type>)
{
	init
	{
		Type.None
	}
}
package jolkert.plabattlesim.parsing

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.pokemon.PokemonSpecies
import jolkert.plabattlesim.data.stats.Stats

object SpeciesParser
{
	fun deserialize(file: FileHandle) = deserialize(file.nameWithoutExtension(), file.readString())

	fun deserialize(name: String, data: String): PokemonSpecies
	{
		val json = JsonReader().parse(data)

		val baseStats: Stats = Stats.fromArray(json["baseStats"].asIntArray())

		val type1: Type; val type2: Type
		json["types"].asStringArray(). let {
			type1 = Type.Registry[it[0]]
			type2 = if (it.size > 1)
				Type.Registry[it[1]]
			else
				Type.None
		}

		return PokemonSpecies(name, baseStats, Pair(type1, type2))
	}
}
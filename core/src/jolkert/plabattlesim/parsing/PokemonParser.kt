package jolkert.plabattlesim.parsing

import jolkert.plabattlesim.data.Nature
import jolkert.plabattlesim.data.pokemon.Pokemon
import jolkert.plabattlesim.data.pokemon.PokemonSpecies
import jolkert.plabattlesim.data.stats.Stat
import jolkert.plabattlesim.data.stats.Stats

object PokemonParser
{
	fun deserialize(input: String): Pokemon
	{
		val lines = input.split("\n").map { it.toLowerCase().trim() }

		val firstLine = lines[0]
		val speciesName: String; var nickname = ""
		if (firstLine.contains("\\([a-z]+\\)"))
		{
			nickname = firstLine.let { it.slice(0 until it.indexOf('(')) }
			speciesName = firstLine.let { it.slice(it.firstIndexAfter("(") until it.indexOf(')')) }
		}
		else
			speciesName = lines[0]

		val species = PokemonSpecies.Registry[speciesName]

		var level = 100; var effortLevels = Stats(10); var nature = Nature(Stat.Speed, Stat.Speed); var isShiny = false
		for (line in lines.drop(1))
		{
			if (line.startsWith("level: "))
			{
				val valueStart = line.firstIndexAfter("level: ")
				if (valueStart >= line.length)
					throw IndexOutOfBoundsException()

				level = line.substring(valueStart).toInt()
			}
			else if (line.startsWith("effort levels: "))
			{
				val valueStart = line.firstIndexAfter("effort levels: ")
				if (valueStart >= line.length)
					throw IndexOutOfBoundsException()

				effortLevels = parseStats(line.substring(valueStart), 10)
			}
		}

		return Pokemon(species,
			nickname = nickname,
			level = level,
			effortLevels = effortLevels,
			nature = nature,
			isShiny = isShiny
		)
	}

	fun serialize(pokemon: Pokemon): String
	{
		val lines: ArrayList<String> = ArrayList()
		lines.add(
			if (pokemon.hasNickname)
				"${pokemon.nickname} (${pokemon.species.name})"
			else
				pokemon.species.name
		)

		if (pokemon.isShiny)
			lines.add("Shiny: yes")

		if (pokemon.level != 100)
			lines.add("Level: ${pokemon.level}")

		if (pokemon.effortLevels != Stats(10))
			lines.add("Effort Levels: ${pokemon.effortLevels.toString(10)}")

		lines.add("${pokemon.nature} Nature")

		for (move in pokemon.moveset)
			lines.add("- ${move.name}")
		
		return lines.joinToString("\n")
	}

	private fun parseStats(string: String, defaultValue: Int): Stats
	{
		val array: Array<Int?> = arrayOfNulls(6)

		val values = string.split('/').map { it.toLowerCase().trim() }
		for (value in values)
		{
			val indexOfFirstLetter = value.indexOfFirst { it.isLetter() }

			val stat = Stat.fromString(value.substring(indexOfFirstLetter))
			array[stat.ordinal] = value.substring(0, indexOfFirstLetter).toInt()
		}

		return Stats.fromArray(array.map { it ?: defaultValue }.toIntArray())
	}

	private fun String.firstIndexAfter(string: String, startIndex: Int = 0, ignoreCase: Boolean = false): Int =
		indexOf(string, startIndex, ignoreCase).let { if (it != -1) it + startIndex else -1 }
}
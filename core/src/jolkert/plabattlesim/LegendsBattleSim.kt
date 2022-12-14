package jolkert.plabattlesim

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.pokemon.Pokemon
import jolkert.plabattlesim.data.pokemon.PokemonSpecies
import jolkert.plabattlesim.parsing.SpeciesParser
import jolkert.plabattlesim.parsing.TypeParser

class LegendsBattleSim : ApplicationAdapter()
{
	override fun create()
	{
		registerTypes()
		registerSpecies()

		for ((_, value) in Type.Registry)
			println("$value")

		println(PokemonSpecies.Registry["pikachu"])
	}

	private fun registerTypes()
	{
		val folder: FileHandle = Gdx.files.internal("assets/data/types")
		if (!folder.isDirectory)
			throw IllegalStateException("data/types is not a folder! (${folder.path()})")

		for (file in folder.list())
			TypeParser.deserialize(file).let { Type.Registry.register(it.name, it) }
	}
	private fun registerSpecies()
	{
		val folder: FileHandle = Gdx.files.internal("assets/data/species")
		if (!folder.isDirectory)
			throw IllegalStateException("data/species is not a folder!")

		for (file in folder.list())
			SpeciesParser.deserialize(file).let { PokemonSpecies.Registry.register(it.name, it) }
	}

	override fun render()
	{
	}

	override fun dispose()
	{
	}
}
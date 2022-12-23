package jolkert.plabattlesim

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import jolkert.plabattlesim.data.Nature
import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.moves.Move
import jolkert.plabattlesim.data.pokemon.PokemonSpecies
import jolkert.plabattlesim.parsing.MoveParser
import jolkert.plabattlesim.parsing.NatureParser
import jolkert.plabattlesim.parsing.SpeciesParser
import jolkert.plabattlesim.parsing.TypeParser

class LegendsBattleSim : ApplicationAdapter()
{
	override fun create()
	{
		registerNatures()
		registerTypes()
		registerSpecies()
		registerMoves()
	}

	// TODO: make this less repetitive
	private fun registerNatures()
	{
		val file: FileHandle = Gdx.files.internal("assets/data/natures.json")
		val natures = NatureParser.deserialize(file)

		for (nature in natures)
			Nature.Registry.register(nature.name, nature)
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
	private fun registerMoves()
	{
		val folder: FileHandle = Gdx.files.internal("assets/data/moves")
		if (!folder.isDirectory)
			throw IllegalStateException("data/moves is not a folder!")

		for (file in folder.list())
			MoveParser.deserialize(file).let { Move.Registry.register(it.name, it) }
	}

	override fun render()
	{
	}

	override fun dispose()
	{
	}
}
package jolkert.plabattlesim

import jolkert.plabattlesim.data.pokemon.BattlePokemon

class Player
{
	val party: List<BattlePokemon> = ArrayList<BattlePokemon>(6)
	var activePokemon: BattlePokemon = BattlePokemon.None; private set

	fun switchTo(index: Int)
	{
		if (index !in party.indices)
			throw IndexOutOfBoundsException()

		activePokemon = party[index]
	}
}
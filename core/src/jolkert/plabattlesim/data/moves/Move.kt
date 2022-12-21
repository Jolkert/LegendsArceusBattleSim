package jolkert.plabattlesim.data.moves

import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.registry.Registry

data class Move(val name: String,
				val type: Type,
				val category: Category,
				val pp: Int,
				val power: StyleTriad<Int>,
				val accuracy: StyleTriad<Int>,
				val userActionTime: StyleTriad<Int>,
				val targetActionTime: StyleTriad<Int>,
				val critStage: StyleTriad<Int>)
{
	val effects: List<MoveEffect> = emptyList()

	companion object
	{
		@JvmStatic val Registry = Registry(Move::class.java)
	}
}
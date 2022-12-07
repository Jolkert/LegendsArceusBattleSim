package jolkert.plabattlesim.data.moves

import jolkert.plabattlesim.data.Type

data class Move(val name: String,
		   val type: Type,
		   val category: Category,
		   val power: StyleTriad<Int>,
		   val accuracy: StyleTriad<Int>,
		   val pp: Int,
		   val selfTarget: Boolean = false)
{
}
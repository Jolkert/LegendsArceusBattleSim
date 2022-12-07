package jolkert.plabattlesim.data.moves

import jolkert.plabattlesim.data.Type

class Moveset(val capacity: Int = 4, vararg moves: Move) : Iterable<Move>
{
	private val moves = Array<Move>(capacity) { i -> if (i in moves.indices) moves[i] else EmptyMove }
	var count: Int = 0; private set

	fun add(vararg moves: Move)
	{
		for (move in moves)
		{
			if (count >= capacity)
				break

			this.moves[count++] = move
		}
	}
	fun clear()
	{
		count = 0
		for (i in moves.indices)
			moves[i] = EmptyMove
	}

	infix operator fun contains(move: Move): Boolean = moves.contains(move)
	operator fun get(index: Int): Move
	{
		if (index >= count)
			throw IndexOutOfBoundsException()

		return moves[index] as Move
	}

	// Iterable implementations
	override fun iterator(): Iterator<Move>
	{
		return MovesetIterator()
	}
	inner class MovesetIterator : Iterator<Move>
	{
		private var index: Int = 0

		override fun hasNext(): Boolean = index < count
		override fun next(): Move = get(index++)
	}

	companion object
	{
		@JvmStatic
		private val EmptyMove = Move(
			"empty",
			Type.None,
			Category.Status,
			StyleTriad(0),
			StyleTriad(0),
			0
		)
	}
}
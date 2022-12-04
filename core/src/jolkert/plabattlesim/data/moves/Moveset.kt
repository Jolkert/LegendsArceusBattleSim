package jolkert.plabattlesim.data.moves

class Moveset(val capacity: Int = 4, vararg moves: Move) : Iterable<Move>
{
	private val moveset: Array<Move?> = arrayOfNulls(capacity)
	var count: Int = 0
		private set

	init
	{
		for (i in moves.indices)
		{
			if (i >= capacity)
				break

			moveset[i] = moves[i]
		}
	}

	fun add(vararg moves: Move)
	{
		for (move in moves)
		{
			if (count >= capacity)
				break

			moveset[count++] = move
		}
	}
	fun clear()
	{
		count = 0
		for (i in moveset.indices)
			moveset[i] = null
	}

	operator fun get(index: Int): Move
	{
		if (index >= count)
			throw IndexOutOfBoundsException()

		return moveset[index] as Move
	}

	override fun iterator(): Iterator<Move>
	{
		return MovesetIterator(this)
	}

	class MovesetIterator(val moveset: Moveset) : Iterator<Move>
	{
		private var index: Int = 0

		override fun hasNext(): Boolean = index < moveset.count
		override fun next(): Move = moveset[index++]
	}
}
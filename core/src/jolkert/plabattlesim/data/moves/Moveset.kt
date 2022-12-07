package jolkert.plabattlesim.data.moves

class Moveset(val capacity: Int = 4, vararg moves: Move) : Iterable<Move>
{
	private val moveset: Array<Move?> = arrayOfNulls(capacity)
	var count: Int = 0; private set

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
	fun contains(move: Move): Boolean = moveset.contains(move)

	operator fun get(index: Int): Move
	{
		if (index >= count)
			throw IndexOutOfBoundsException()

		return moveset[index] as Move
	}

	override fun equals(other: Any?): Boolean
	{
		if (other !is Moveset)
			return false

		for (move in other as Moveset)
			if (!contains(move))
				return false

		return true
	}

	override fun iterator(): Iterator<Move>
	{
		return MovesetIterator(this)
	}
	class MovesetIterator(private val moveset: Moveset) : Iterator<Move>
	{
		private var index: Int = 0

		override fun hasNext(): Boolean = index < moveset.count
		override fun next(): Move = moveset[index++]
	}
}
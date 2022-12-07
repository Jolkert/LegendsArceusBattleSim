package jolkert.plabattlesim.data.status

class StatusCondition(val data: StatusData, turnTimer: Int)
{
	var turns = turnTimer; private set

	fun tickDown(): Int = --turns

	override fun equals(other: Any?): Boolean = when (other)
	{
		is StatusData -> data == other
		is StatusCondition -> data == other.data
		else -> false
	}

	companion object
	{
		@JvmStatic val None = StatusCondition(StatusData.Healthy, -1)
	}
}
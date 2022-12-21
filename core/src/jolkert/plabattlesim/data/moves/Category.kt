package jolkert.plabattlesim.data.moves

enum class Category
{
	None,
	Physical,
	Special,
	Status;

	fun isDamaging(): Boolean = this != Status
	fun isSpecified(): Boolean = this != None

	companion object
	{
		@JvmStatic fun fromString(str: String): Category
		{
			return when (str.toLowerCase())
			{
				"physical" -> Physical
				"phys" -> Physical
				"special" -> Special
				"spec" -> Special
				"status" -> Status
				"stat" -> Status
				else -> None
			}
		}
	}
}

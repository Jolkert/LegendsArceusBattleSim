package jolkert.plabattlesim.data.pokemon

enum class Position
{
	Target,
	User;

	companion object
	{
		@JvmStatic fun fromString(string: String): Position = when (string.toLowerCase())
		{
			"target" -> Target
			"user" -> User
			else -> throw IllegalArgumentException("Could not convert $string to ${Position::class.qualifiedName}")
		}
	}
}
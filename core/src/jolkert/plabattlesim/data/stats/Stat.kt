package jolkert.plabattlesim.data.stats

enum class Stat
{
	Hp,
	Attack,
	Defense,
	SpecialAttack,
	SpecialDefense,
	Speed;

	companion object
	{
		@JvmStatic fun fromInt(value: Int): Stat = when (value)
		{
			0 -> Hp
			1 -> Attack
			2 -> Defense
			3 -> SpecialAttack
			4 -> SpecialDefense
			5 -> Speed
			else -> throw IllegalArgumentException("Cannot convert $value to stat!")
		}
	}
}
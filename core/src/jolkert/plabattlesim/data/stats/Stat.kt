package jolkert.plabattlesim.data.stats

enum class Stat
{
	Hp,
	Attack,
	Defense,
	SpecialAttack,
	SpecialDefense,
	Speed;

	fun toString(shortened: Boolean): String
	{
		return if (!shortened)
			toString()
		else
			when (this)
			{
				Hp -> "HP"
				Attack -> "Atk"
				Defense -> "Def"
				SpecialAttack -> "SpA"
				SpecialDefense -> "SpD"
				Speed -> "Spe"
			}
	}

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

		@JvmStatic fun fromString(string: String): Stat = when (string.trim().toLowerCase())
		{
			"hp" -> Hp
			"attack" -> Attack
			"atk" -> Attack
			"defense" -> Defense
			"def" -> Defense
			"special attack" -> SpecialAttack
			"spa" -> SpecialAttack
			"spatk" -> SpecialAttack
			"special defense" -> SpecialDefense
			"spd" -> SpecialDefense
			"spdef" -> SpecialDefense
			"speed" -> Speed
			"spe" -> Speed
			else -> throw IllegalArgumentException("Cannot parse stat from \"$string\"")
		}
	}
}
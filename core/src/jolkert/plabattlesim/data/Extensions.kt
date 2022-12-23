package jolkert.plabattlesim.data

import com.badlogic.gdx.utils.JsonValue
import jolkert.plabattlesim.data.moves.StyleTriad

fun Pair<Type, Type>.damageMultiplierFrom(type: Type): Float
{
	if (first.isImmuneTo(type) || second.isImmuneTo(type))
		return 0f

	var weakCount = 0
	if (first.isWeakTo(type))
		weakCount++
	else if (first.resists(type))
		weakCount--

	if (second.isWeakTo(type))
		weakCount++
	else if (second.resists(type))
		weakCount--

	return when (weakCount)
	{
		-2 -> 0.4f
		-1 -> 0.5f
		 0 -> 1.0f
		+1 -> 2.0f
		+2 -> 2.5f
		else -> throw IllegalStateException("Absolute value of weakCount should never be greater than 2, but value was $weakCount!")
	}
}

fun JsonValue.getOrNull(key: String): JsonValue? = if (has(key)) this[key] else null
fun <T> JsonValue.getOrNull(key: String, transform: (JsonValue) -> T): T? = getOrNull(key)?.let(transform)
fun JsonValue.asStyleTriad(): StyleTriad<Int> =
	StyleTriad(this["regular"].asInt(), this["agile"].asInt(), this["strong"].asInt())

fun <T> JsonValue.getStringTransform(key: String, transform: (String) -> T) = transform(getString(key))
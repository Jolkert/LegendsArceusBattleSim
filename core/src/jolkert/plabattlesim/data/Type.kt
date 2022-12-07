package jolkert.plabattlesim.data

class Type(val name: String,
		   val weaknesses: Array<String>,
		   val resistances: Array<String>,
		   val immunities: Array<String>)
{
	constructor(name: String): this(name, emptyArray(), emptyArray(), emptyArray())

	fun isWeakTo(type: Type): Boolean = weaknesses.contains(type.name)
	fun resists(type: Type): Boolean = resistances.contains(type.name)
	fun isImmuneTo(type: Type): Boolean = immunities.contains(type.name)

	companion object
	{
		@JvmStatic val None: Type = Type("none")
	}
}
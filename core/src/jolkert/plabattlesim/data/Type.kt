package jolkert.plabattlesim.data

import jolkert.plabattlesim.registry.Registry

class Type(val name: String,
		   val weaknesses: Array<String>,
		   val resistances: Array<String>,
		   val immunities: Array<String>)
{
	constructor(name: String): this(name, emptyArray(), emptyArray(), emptyArray())

	fun isWeakTo(type: Type): Boolean = weaknesses.contains(type.name)
	fun resists(type: Type): Boolean = resistances.contains(type.name)
	fun isImmuneTo(type: Type): Boolean = immunities.contains(type.name)

	override fun toString(): String
	{
		return "$name:\n" +
				"\tWeak: ${weaknesses.joinToString(" / ") }\n" +
				"\tResist: ${resistances.joinToString(" / ") }\n" +
				"\tImmune: ${immunities.joinToString(" / ") }"
	}

	companion object
	{
		@JvmStatic val Registry = Registry(Type::class.java)
		@JvmStatic val None: Type = Type("none")
	}
}
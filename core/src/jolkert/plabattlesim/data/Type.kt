package jolkert.plabattlesim.data

class Type(val name: String,
		   val weaknesses: Array<String>,
		   val resistances: Array<String>,
		   val immunities: Array<String>)
{
	fun isWeakTo(type: Type): Boolean = weaknesses.contains(type.name)
	fun resists(type: Type): Boolean = resistances.contains(type.name)
	fun isImmuneTo(type: Type): Boolean = immunities.contains(type.name)
}
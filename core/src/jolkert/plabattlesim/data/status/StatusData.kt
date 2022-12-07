package jolkert.plabattlesim.data.status

import jolkert.plabattlesim.data.Type

data class StatusData(val name: String, val isPrimary: Boolean)
{
	val immuneTypes: List<Type> = emptyList()
	val effects: List<Effect> = emptyList()

	constructor(name: String, isPrimary: Boolean, immuneTypes: List<Type>, effects: List<Effect>): this (name, isPrimary)
	{
		for (type in immuneTypes)
			(this.immuneTypes as MutableList).add(type)

		for (effect in effects)
			(this.effects as MutableList).add(effect)
	}

	companion object
	{
		@JvmStatic val Healthy = StatusData("none", true)
	}
}
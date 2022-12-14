package jolkert.plabattlesim.registry

class Registry<T>(val type: Class<T>)
{
	private val map: HashMap<String, T> = HashMap()

	fun register(id: String, value: T): Boolean
	{
		if (map.containsKey(id))
			return false

		map[id] = value
		return true
	}

	operator fun set(key: String, value: T)
	{
		map[key] = value
	}
	operator fun get(key: String): T = map.getOrElse(key) { throw IndexOutOfBoundsException("Unable to find $key in ${type.name} registry") }

	operator fun iterator(): MutableIterator<MutableMap.MutableEntry<String, T>> = map.iterator()
}
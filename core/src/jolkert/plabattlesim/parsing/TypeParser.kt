package jolkert.plabattlesim.parsing

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import jolkert.plabattlesim.data.Type

object TypeParser
{
	fun deserialize(file: FileHandle) = deserialize(file.nameWithoutExtension(), file.readString())

	fun deserialize(name: String, data: String): Type
	{
		val json = JsonReader().parse(data)

		return Type(name,
			json.extractStringArray("weaknesses"),
			json.extractStringArray("resistances"),
			json.extractStringArray("immunities"))
	}

	private fun JsonValue.extractStringArray(arrayName: String): Array<String>
	{
		if (!has(arrayName))
			return emptyArray()

		this[arrayName].let {
			if (!it.isArray)
				throw JsonValueNotFoundException("$arrayName was not an array!")
			else
				return it.asStringArray()
		}
	}
}
package jolkert.plabattlesim.parsing

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import jolkert.plabattlesim.data.Nature
import jolkert.plabattlesim.data.stats.Stat

object NatureParser
{
	fun deserialize(file: FileHandle) = deserialize(file.readString())

	fun deserialize(data: String): Array<Nature>
	{
		val json: JsonValue = JsonReader().parse(data)

		val natures: JsonValue = json["natures"]
		val naturesArray = Array(natures.size) { Nature("", Stat.Speed, Stat.Speed) }

		var index = 0
		for (item: JsonValue in natures)
		{
			val name = item["name"].asString()


			val boostedStat: Stat; val loweredStat: Stat;
			item["value"]. let {
				boostedStat = Stat.fromString(it["boosted"].asString())
				loweredStat = Stat.fromString(it["lowered"].asString())
			}

			naturesArray[index++] = Nature(name, boostedStat, loweredStat)
		}

		return naturesArray
	}
}
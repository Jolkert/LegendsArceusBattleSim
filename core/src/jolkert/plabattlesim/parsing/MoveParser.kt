package jolkert.plabattlesim.parsing

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import jolkert.plabattlesim.data.Type
import jolkert.plabattlesim.data.asStyleTriad
import jolkert.plabattlesim.data.getStringTransform
import jolkert.plabattlesim.data.moves.*
import jolkert.plabattlesim.data.pokemon.Position

object MoveParser
{
	fun deserialize(file: FileHandle) = deserialize(file.nameWithoutExtension(), file.readString())

	fun deserialize(name: String, data: String): Move
	{
		val json = JsonReader().parse(data)
		val move = Move(
			name = name,
			type = json.getStringTransform("type") { Type.Registry.get(it) },
			category = json.getStringTransform("category") { Category.fromString(it) },
			pp = json.getInt("pp"),
			power = json["power"].asStyleTriad(),
			accuracy = json["accuracy"].asStyleTriad(),
			userActionTime = json["userActionTime"].asStyleTriad(),
			targetActionTime = json["targetActionTime"].asStyleTriad(),
			critStage = json["critRate"].asStyleTriad()
		)
		val effectList = move.effects as MutableList<MoveEffect>

		for (effect: JsonValue in json["effects"])
			effectList.add(MoveEffect.fromJson(effect))

		return move
	}
}
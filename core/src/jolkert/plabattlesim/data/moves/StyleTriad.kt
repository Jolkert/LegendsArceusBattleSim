package jolkert.plabattlesim.data.moves

data class StyleTriad<T>(val normal: T, val agile: T, val strong: T)
{
	constructor(value: T): this(value, value, value)

	operator fun get(style: Style): T = when (style)
	{
		Style.Normal -> normal
		Style.Agile -> agile
		Style.Strong -> strong
	}
}
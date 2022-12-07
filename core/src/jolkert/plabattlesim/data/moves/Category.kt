package jolkert.plabattlesim.data.moves

enum class Category
{
	None,
	Physical,
	Special,
	Status;

	fun isDamaging(): Boolean = this != Status
	fun isSpecified(): Boolean = this != None
}

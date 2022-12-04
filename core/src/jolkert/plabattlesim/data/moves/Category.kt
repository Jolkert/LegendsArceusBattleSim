package jolkert.plabattlesim.data.moves

enum class Category
{
	Physical,
	Special,
	Status;

	fun isDamaging(): Boolean = this != Status
}

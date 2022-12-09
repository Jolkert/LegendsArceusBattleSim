package jolkert.plabattlesim.data.status

import jolkert.plabattlesim.data.moves.Category
import jolkert.plabattlesim.data.moves.Move
import jolkert.plabattlesim.data.pokemon.Position
import jolkert.plabattlesim.data.stats.Stat

abstract class Effect
{
	data class StatModifier(val stat: Stat, val multiplier: Float): Effect()
	data class DamageMultiplier(val position: Position, val multiplier: Float, val category: Category): Effect()
	data class CancelTurn(val chance: Int): Effect()
	data class TurnEndDamageFraction(val fraction: Int): Effect()
	data class TurnEndDamageMove(val move: Move): Effect()
	data class EvasionModifier(val multiplier: Float): Effect()
}
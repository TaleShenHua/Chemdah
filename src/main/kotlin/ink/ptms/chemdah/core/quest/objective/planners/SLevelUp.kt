package ink.ptms.chemdah.core.quest.objective.planners

import com.bh.planners.api.event.PlayerLevelChangeEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objiective.skillapi.SLevelUp
 *
 * @author Peng_Lx
 * @since 2021/5/29 7:59 下午
 */
@Dependency("Planners")
object SLevelUp : ObjectiveCountableI<PlayerLevelChangeEvent>() {

    override val name = "skillapi levelup"
    override val event = PlayerLevelChangeEvent::class.java

    init {
        handler {
            it.player
        }
        addSimpleCondition("position") { data, it ->
            data.toPosition().inside(it.player.location)
        }
        addSimpleCondition("level") { data, it ->
            data.toInt() <= it.to
        }
        addConditionVariable("level") {
            it.to
        }
    }
}

package ink.ptms.chemdah.core.quest.objective.planners

import com.bh.planners.api.event.PlayerSelectedJobEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objiective.skillapi.SClassChange
 *
 * @author GalaxyVN
 * @since 2021/7/18 2:05 下午
 */

@Dependency("Planners")
object SClassChange : ObjectiveCountableI<PlayerSelectedJobEvent>() {

    override val name = "skillapi class change"
    override val event = PlayerSelectedJobEvent::class.java

    init {
        handler {
            it.profile.getPlayer()
        }
        addSimpleCondition("position") { data, it ->
            data.toPosition().inside(it.profile.getPlayer()!!.location)
        }
        addSimpleCondition("class") { data, it ->
            data.toString().equals(it.profile.getCurrentJob()?.jobKey ?: "", true)
        }
        addConditionVariable("class") {
            it.profile.getCurrentJob()?.jobKey ?: ""
        }
    }
}

package ink.ptms.chemdah.core.quest.objective.planners

import com.bh.planners.api.event.PlayerSkillUpgradeEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objiective.skillapi.SSkillUpgrade
 *
 * @author Peng_Lx
 * @since 2021/5/29 7:59 下午
 */
@Dependency("Planners")
object SSkillUpgrade : ObjectiveCountableI<PlayerSkillUpgradeEvent>() {

    override val name = "skillapi skill upgrade"
    override val event = PlayerSkillUpgradeEvent::class.java

    init {
        handler {
            it.player
        }
        addSimpleCondition("position") { data, it ->
            data.toPosition().inside(it.player.location)
        }
        addSimpleCondition("skill") { data, it ->
            data.toString().equals(it.skillData.key, true)
        }
        addConditionVariable("skill") {
            it.skillData.key
        }
    }
}

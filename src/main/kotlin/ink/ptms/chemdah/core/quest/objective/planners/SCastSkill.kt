package ink.ptms.chemdah.core.quest.objective.planners

import com.bh.planners.api.event.EntitySkillEvents
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import org.bukkit.entity.Player

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objiective.skillapi.SCastSkill
 *
 * @author Peng_Lx
 * @since 2021/5/29 7:59 下午
 */
@Dependency("Planners")
object SCastSkill : ObjectiveCountableI<EntitySkillEvents.CastPre>() {

    override val name = "skillapi castskill"
    override val event = EntitySkillEvents.CastPre::class.java

    init {
        handler {
            it.entity as? Player
        }
        addSimpleCondition("position") { data, it ->
            data.toPosition().inside(it.entity.location)
        }
        addSimpleCondition("skill") { data, it ->
            data.toString().equals(it.skill.key, true)
        }
    }
}

package ink.ptms.chemdah.core.quest.objective.planners

import com.bh.planners.api.event.DamageByEntity
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import org.bukkit.entity.Player

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objiective.skillapi.SSkillDamage
 *
 * @author Peng_Lx
 * @since 2021/5/29 7:59 下午
 */
@Dependency("Planners")
object SSkillDamage : ObjectiveCountableI<DamageByEntity.Post>() {

    override val name = "skillapi skill damage"
    override val event = DamageByEntity.Post::class.java

    init {
        handler {
            it.damager as? Player
        }
        addSimpleCondition("position") { data, it ->
            data.toPosition().inside(it.damager.location)
        }
        addSimpleCondition("damage") { data, it ->
            data.toDouble() <= it.getDamage()
        }
        addConditionVariable("damage") {
            it.getDamage()
        }
        addConditionVariable("target") {
            it.entity
        }
    }
}

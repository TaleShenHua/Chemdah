package ink.ptms.chemdah.core.quest.objective.mythicmobs

import com.bh.planners.api.event.KillMobEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import org.bukkit.entity.Player
import taboolib.common.platform.event.EventPriority

@Dependency("MythicMobs")
object MMobDeath : ObjectiveCountableI<KillMobEvent>() {

    override val name = "mythicmobs kill"
    override val event = KillMobEvent::class.java
    override val priority: EventPriority
        get() = EventPriority.NORMAL

    init {
        handler {
            it.source as? Player
        }
        addSimpleCondition("position") { data, e ->
            data.toPosition().inside(e.source.location)
        }
        addSimpleCondition("name") { data, e ->
            data.asList().any { it.equals(e.mob.mobType, true) }
        }
        addSimpleCondition("level") { data, e ->
            data.toDouble() == e.mob.level
        }
        addSimpleCondition("min-level") { data, e ->
            data.toDouble() <= e.mob.level
        }
        addConditionVariable("name") {
            it.mob.mobType
        }
    }
}

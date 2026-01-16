package ink.ptms.chemdah.core.quest.objective.bukkit

import com.hitable.hipeequip.event.PlayerDropItemEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objective.bukkit.IItemDrop
 *
 * @author sky
 * @since 2021/3/2 5:09 下午
 */
@Dependency("minecraft")
object IItemDrop : ObjectiveCountableI<PlayerDropItemEvent>() {

    override val name = "drop item"
    override val event = PlayerDropItemEvent::class.java

    init {
        handler {
            it.player
        }
        addSimpleCondition("position") { data, e ->
            data.toPosition().inside(e.player.location)
        }
        addSimpleCondition("item") { data, e ->
            e.itemData?.itemKey == data.toString()
        }
    }
}
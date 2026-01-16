package ink.ptms.chemdah.core.quest.objective.bukkit

import com.hitable.hipeequip.event.PlayerBackpackSetItemEvent
import com.hitable.hipeequip.event.PlayerLootItemEvent
import com.hitable.hipeequip.manager.BackpackManager.backpack
import ink.ptms.chemdah.core.PlayerProfile
import ink.ptms.chemdah.core.quest.Task
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objective.bukkit.IItemPick
 *
 * @author sky
 * @since 2021/3/2 5:09 下午
 */
@Dependency("minecraft")
object IItemPick : ObjectiveCountableI<PlayerLootItemEvent>() {

    override val name = "pickup item"
    override val event = PlayerLootItemEvent::class.java

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
        addSimpleCondition("amount") { data, e ->
            data.toInt() <= (e.itemData?.amount ?: 0)
        }
        addConditionVariable("amount") {
            it.itemData?.amount ?: 0
        }
    }

    override fun getCount(profile: PlayerProfile, task: Task, event: PlayerLootItemEvent): Int {
        return event.itemData?.amount ?: 0
    }
}
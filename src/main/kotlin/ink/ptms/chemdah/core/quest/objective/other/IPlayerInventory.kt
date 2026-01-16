package ink.ptms.chemdah.core.quest.objective.other

import com.hitable.hipeequip.bean.backpack.ItemData
import com.hitable.hipeequip.enums.BackpackType
import com.hitable.hipeequip.enums.ItemType
import com.hitable.hipeequip.manager.BackpackManager.backpack
import com.hitable.hipeequip.manager.ItemManager
import ink.ptms.chemdah.core.PlayerProfile
import ink.ptms.chemdah.core.quest.Task
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import ink.ptms.chemdah.core.quest.objective.Progress
import ink.ptms.chemdah.core.quest.objective.Progress.Companion.toProgress
import org.bukkit.event.Event
import kotlin.math.min

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objective.other.IPlayerInventory
 *
 * @author sky
 * @since 2021/3/2 5:09 下午
 */
object IPlayerInventory : ObjectiveCountableI<Event>() {

    override val name = "player inventory"
    override val event = Event::class.java
    override val isListener = false
    override val isTickable = true

    init {
        addFullCondition("item") { profile, task, _ ->
            val item = task.condition["item"]!!.toString()
            val amount = task.condition["amount"]?.toInt() ?: 1
            val consume = task.condition["consume"]?.toBoolean() ?: false
            val itemData = ItemData(item, amount)
            val inventory = if(itemData.getItemType() == ItemType.QUEST) {
                profile.player.backpack(BackpackType.TASK)
            } else {
                profile.player.backpack(BackpackType.ITEM)
            }
            
            val hasItem = inventory.hasItem(itemData)
            if (consume && hasItem) {
                inventory.takeItem(itemData)
            }
            hasItem
        }
    }

    override fun getProgress(profile: PlayerProfile, task: Task): Progress {
        val consume = task.condition["consume"]?.toBoolean() ?: false
        if (!consume) {
            val item = task.condition["item"]!!.toString()
            val amount = task.condition["amount"]?.toInt() ?: 1
            val target = task.goal["amount", 1].toInt()
            val itemData = ItemData(item)
            val inventory = if(itemData.getItemType() == ItemType.QUEST) {
                profile.player.backpack(BackpackType.TASK)
            } else {
                profile.player.backpack(BackpackType.ITEM)
            }
            val value = min(inventory.countItem(itemData), amount * target)
            return value.toProgress(amount * target)
        }
        return super.getProgress(profile, task)
    }
}
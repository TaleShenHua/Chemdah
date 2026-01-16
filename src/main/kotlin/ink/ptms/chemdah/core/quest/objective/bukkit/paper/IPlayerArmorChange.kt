package ink.ptms.chemdah.core.quest.objective.bukkit.paper

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("minecraft")
object IPlayerArmorChange : ObjectiveCountableI<PlayerArmorChangeEvent>() {

    override val name = "armor change"
    override val event = PlayerArmorChangeEvent::class.java

    init {
        handler {
            it.player
        }
        addSimpleCondition("chest") { data, e ->
            data.toInferItem().isItem(e.newItem!!)
        }
        addSimpleCondition("feet") { data, e ->
            data.toInferItem().isItem(e.newItem!!)
        }
        addSimpleCondition("head") { data, e ->
            data.toInferItem().isItem(e.newItem!!)
        }
        addSimpleCondition("legs") { data, e ->
            data.toInferItem().isItem(e.newItem!!)
        }
        addSimpleCondition("slot") { data, e ->
            e.slotType.name == data.toString()
        }
    }
}

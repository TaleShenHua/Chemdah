package ink.ptms.chemdah.core.conversation.trigger

import ink.ptms.chemdah.api.ChemdahAPI.conversationSession
import ink.ptms.chemdah.api.event.collect.ConversationEvents
import ink.ptms.chemdah.core.conversation.ConversationManager
import ink.ptms.chemdah.core.conversation.Source
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.mobs.ActiveMob
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.util.unsafeLazy
import taboolib.module.ai.controllerLookAt

/**
 * Chemdah
 * ink.ptms.chemdah.core.conversation.trigger.TriggerMythicMobs
 *
 * @author sky
 * @since 2021/2/9 8:46 下午
 */
internal object TriggerMythicMobs {

    val isMythicMobsHooked by unsafeLazy { Bukkit.getPluginManager().isPluginEnabled("MythicMobs") }

    @SubscribeEvent
    fun onBegin(e: ConversationEvents.Begin) {
        if (!isMythicMobsHooked) {
            return
        }
        val npc = e.session.source.entity
        if (npc is ActiveMob && npc.entity is LivingEntity && e.conversation.hasFlag("LOOK_PLAYER")) {
            (npc.entity as LivingEntity).controllerLookAt(e.session.player)
        }
    }

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onInteract(e: PlayerInteractAtEntityEvent) {
        if (!isMythicMobsHooked) {
            return
        }
        if (e.hand == EquipmentSlot.HAND && e.rightClicked is LivingEntity && e.player.conversationSession == null) {
            val mob = MythicMobs.inst().mobManager.getMythicMobInstance(e.rightClicked) ?: return
            val conversation = ConversationManager.getConversation(e.player, "mythicmobs", mob, mob.type.internalName)
            if (conversation != null) {
                e.isCancelled = true
                // 打开对话
                conversation.open(e.player, object : Source<ActiveMob>(mob.displayName, mob) {

                    override fun transfer(player: Player, newId: String): Boolean {
                        val entities = e.rightClicked.getNearbyEntities(10.0, 10.0, 10.0)
                        val nearby = entities.mapNotNull { MythicMobs.inst().mobManager.getMythicMobInstance(it) }
                            .firstOrNull { it.type.internalName == newId } ?: return false
                        update(nearby.displayName, nearby)
                        return true
                    }

                    override fun getOriginLocation(entity: ActiveMob): Location {
                        val be = entity.entity
                        return BukkitAdapter.adapt(be.location.add(0.0, be.eyeHeight, 0.0))
                    }
                })
            }
        }
    }
}
package ink.ptms.chemdah.module.party

import ink.ptms.chemdah.api.event.PartyHookEvent
import org.bukkit.entity.Player
import org.serverct.ersha.dungeon.DungeonPlus
import org.serverct.ersha.dungeon.common.team.type.PlayerStateType
import taboolib.common.platform.event.SubscribeEvent

/**
 * Chemdah
 * ink.ptms.chemdah.module.party.PartyHok
 *
 * @author sky
 * @since 2021/4/24 5:30 下午
 */
object PartyHook {

    @SubscribeEvent
    private fun onPartyHook(e: PartyHookEvent) {
        e.party = when (e.plugin) {
            "DungeonPlus" -> DungeonPlusHook
            else -> return
        }
    }

    object DungeonPlusHook : Party {

        override fun getParty(player: Player): Party.PartyInfo? {
            // 1.1.3
            return try {
                val team = DungeonPlus.teamManager.getTeam(player) ?: return null
                object : Party.PartyInfo {

                    override fun getLeader(): Player? {
                        return team.getLeader()
                    }

                    override fun getMembers(): List<Player> {
                        return team.getPlayers(PlayerStateType.ALL).filter { it.uniqueId != team.leader }
                    }
                }
            } catch (ex: Error) {
                error("Outdated DungeonPlus (required: >1.1.3)")
            }
        }
    }

}

package ink.ptms.chemdah.module.papi

import ink.ptms.chemdah.api.ChemdahAPI.chemdahProfile
import ink.ptms.chemdah.api.ChemdahAPI.isChemdahProfileLoaded
import ink.ptms.chemdah.api.event.PlaceholderHookEvent
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object PlaceholderForLiteral : PlaceholderExpansion {
    override val identifier: String
        get() = "ch"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        return if (player != null && player.isChemdahProfileLoaded) {
            val name = args.substringBefore('_').lowercase()
            val body = args.substringAfter('_')
            val event = PlaceholderHookEvent(player, player.chemdahProfile, name, body)
            event.call()
            val result = event.result
            result?.let {
                result.toString()
            } ?: "UNSUPPORTED"
        } else "ERROR"
    }

}

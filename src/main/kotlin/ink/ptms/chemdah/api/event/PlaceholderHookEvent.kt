package ink.ptms.chemdah.api.event

import ink.ptms.chemdah.core.PlayerProfile
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

class PlaceholderHookEvent(
    val player: Player,
    val profile: PlayerProfile,
    val identifier: String,
    val parameter: String,
    var result: Any? = null
) : BukkitProxyEvent()

package ink.ptms.chemdah.module.papi

import ink.ptms.chemdah.api.ChemdahAPI.isChemdahProfileLoaded
import ink.ptms.chemdah.util.namespaceQuest
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.printKetherErrorMessage
import taboolib.platform.compat.PlaceholderExpansion

object PlaceholderForKether : PlaceholderExpansion {
    override val identifier: String
        get() = "chemdah"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player == null) return "<NO_PLAYER>"
        return if (player.isChemdahProfileLoaded) {
            try {
                KetherShell.eval(args, ScriptOptions.builder().useCache(false).namespace(namespaceQuest).sender(adaptPlayer(player)).build())
                    .getNow(null).toString()
            } catch (ex: Throwable) {
                ex.printKetherErrorMessage()
                "<ERROR>"
            }
        } else "..."
    }

}

package ink.ptms.chemdah.module.command

import org.bukkit.command.CommandSender
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.chat.impl.DefaultComponent

internal fun space(sender: CommandSender) {
    DefaultComponent().apply { repeat(30) { newLine() } }.sendTo(adaptCommandSender(sender))
}

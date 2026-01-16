package ink.ptms.chemdah.module.command

import ink.ptms.chemdah.api.ChemdahAPI.chemdahProfile
import ink.ptms.chemdah.util.increaseAny
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.platform.util.sendLang

/**
 * Chemdah
 * ink.ptms.chemdah.command.CommandChemdahPlayerData
 *
 * @author sky
 * @since 2021/2/11 7:19 下午
 */
@CommandHeader(name = "ChemdahPlayerData", aliases = ["chpd"], permission = "chemdah.command")
object CommandChemdahPlayerData {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val set = subCommand {
        dynamic(comment ="player") {
            suggestPlayers()
            dynamic(comment ="key") {
                suggestion<CommandSender>(uncheck = true) { _, ctx ->
                    ctx.player("player").cast<Player>().chemdahProfile.persistentDataContainer.keys()
                }
                dynamic(comment ="value") {
                    execute<CommandSender> { sender, ctx, argument ->
                        ctx.player("player").cast<Player>().chemdahProfile.persistentDataContainer[ctx["player"]] = argument
                        if (sender is Player) {
                            sender.sendLang("command-variables-change", "${ctx["player"]} §8= §f${argument}")
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    val add = subCommand {
        dynamic(comment ="player") {
            suggestPlayers()
            dynamic(comment ="key") {
                suggestion<CommandSender>(uncheck = true) { _, ctx ->
                    ctx.player("player").cast<Player>().chemdahProfile.persistentDataContainer.keys()
                }
                dynamic(comment ="value") {
                    execute<CommandSender> { sender, ctx, argument ->
                        val key = ctx["key"]
                        val persistentDataContainer = ctx.player("player").cast<Player>().chemdahProfile.persistentDataContainer
                        persistentDataContainer[key] = persistentDataContainer[key].increaseAny(argument)
                        if (sender is Player) {
                            sender.sendLang("command-variables-change", "$key §8+= §f${argument}")
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    val remove = subCommand {
        dynamic(comment ="player") {
            suggestPlayers()
            dynamic(comment ="key") {
                suggestion<CommandSender>(uncheck = true) { _, ctx ->
                    ctx.player("player").cast<Player>().chemdahProfile.persistentDataContainer.keys()
                }
                execute<CommandSender> { sender, ctx, argument ->
                    val playerExact = ctx.player("player").cast<Player>()
                    if (argument == "*") {
                        playerExact.chemdahProfile.persistentDataContainer.clear()
                        if (sender is Player) {
                            sender.sendLang("command-variables-change", "* §8= §fnull")
                        }
                    } else {
                        playerExact.chemdahProfile.persistentDataContainer.remove(argument)
                        if (sender is Player) {
                            sender.sendLang("command-variables-change", "$argument §8= §fnull")
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    val clear = subCommand {
        dynamic(comment ="player") {
            suggestPlayers()
            execute<CommandSender> { sender, ctx, _ ->
                ctx.player("player").cast<Player>().chemdahProfile.persistentDataContainer.clear()
                if (sender is Player) {
                    sender.sendLang("command-variables-change", "* §8= §fnull")
                }
            }
        }
    }
}

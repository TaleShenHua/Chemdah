package ink.ptms.chemdah.module.papi.impl

import ink.ptms.chemdah.api.event.PlaceholderHookEvent
import ink.ptms.chemdah.module.level.LevelSystem.getLevel
import ink.ptms.chemdah.module.level.LevelSystem.getLevelOption
import ink.ptms.chemdah.module.realms.RealmsSystem.getRealms
import taboolib.common.platform.event.SubscribeEvent

object PDataKt {
    @SubscribeEvent
    fun onPlaceholderData(e: PlaceholderHookEvent) {
        when (e.identifier) {
            "maxexp" -> {
                val option = getLevelOption(e.parameter)
                e.result = if (option != null) option.algorithm.getExp(e.profile.getLevel(option).level).getNow(0)
                else "LEVEL_OPTION_NOT_FOUND"
            }

            "exp" -> {
                val option = getLevelOption(e.parameter)
                e.result = if (option != null) e.profile.getLevel(option).experience else "LEVEL_OPTION_NOT_FOUND"
            }

            "data" -> {
                val key = e.parameter.substringBefore("?:")
                val def = e.parameter.substringAfter("?:").ifEmpty { "null" }
                e.result = e.profile.persistentDataContainer[key]?.toString() ?: def
            }

            "level" -> {
                val option = getLevelOption(e.parameter)
                e.result = if (option != null) e.profile.getLevel(option).level else "LEVEL_OPTION_NOT_FOUND"
            }

            "realm", "realms" -> {
                e.result = e.player.location.getRealms()?.id ?: "null"
            }
        }
    }
}

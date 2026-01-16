package ink.ptms.chemdah.module.papi.impl

import ink.ptms.adyeshach.module.editor.format
import ink.ptms.chemdah.api.ChemdahAPI.getQuestTemplate
import ink.ptms.chemdah.api.event.PlaceholderHookEvent
import ink.ptms.chemdah.core.quest.AcceptResult
import ink.ptms.chemdah.core.quest.QuestContainer
import ink.ptms.chemdah.core.quest.addon.AddonStats.Companion.getProgress
import ink.ptms.chemdah.core.quest.addon.AddonTrack.Companion.trackQuest
import ink.ptms.chemdah.core.quest.objective.Progress
import ink.ptms.chemdah.core.quest.objective.Progress.Companion.ZERO
import taboolib.common.platform.event.SubscribeEvent

object PQuestKt {
    @SubscribeEvent
    fun onPlaceholderData(e: PlaceholderHookEvent) {
        when (e.identifier) {
            "questdata" -> {
                val name = e.parameter.substringBefore(':')
                e.result = e.profile.getQuestById(name, true)?.let {
                    val node = e.parameter.substringAfter(':').substringBefore("?:")
                    val def = e.parameter.substringAfter("?:").ifEmpty { "null" }
                    it.persistentDataContainer[node]?.toString() ?: def
                } ?: "QUEST_NOT_FOUND"
            }

            "accepted", "accept" -> {
                e.result = e.profile.getQuestById(e.parameter, false) != null
            }

            "completed", "complete" -> {
                e.result = e.profile.isQuestCompleted(e.parameter)
            }

            "proceed" -> {
                e.result = e.profile.getQuestById(e.parameter, true) != null
            }

            "track", "tracking" -> {
                e.result = e.profile.trackQuest?.id ?: "null"
            }

            "acceptcheck", "checkaccept" -> {
                e.result = getQuestTemplate(e.parameter)?.checkAccept(e.profile)
                    ?.getNow(AcceptResult(AcceptResult.Type.FAILED)) ?: "QUEST_NOT_FOUND"
            }

            else -> {
                val name = e.parameter.substringBefore(":")
                val quest = e.profile.getQuestById(name, true)
                if (quest != null) {
                    val node = e.parameter.substringAfter(":")

                    val progress = if (node.isEmpty()) {
                        (quest.template as QuestContainer).getProgress(e.profile).getNow(ZERO)
                    } else {
                        quest.getTask(node)?.let {
                            (it as QuestContainer).getProgress(e.profile).getNow(ZERO)
                        } ?: let {
                            e.result = "TASK_NOT_FOUND"
                            null
                        }
                    }
                    if (progress != null) {
                        e.result = when (e.identifier) {
                            "progresstarget" -> {
                                progress.target
                            }

                            "progressvalue" -> {
                                progress.value
                            }

                            "progresspercent" -> {
                                (progress.percent * 100.0).format()
                            }

                            else -> {
                                throw IllegalStateException("out of case")
                            }
                        }
                    }
                } else {
                    e.result = "QUEST_NOT_FOUND"
                }
            }
        }
    }
}

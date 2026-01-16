package ink.ptms.chemdah.core.quest.addon

import com.bh.planners.api.PlannersAPI.plannersProfile
import com.bh.planners.api.addExperience
import com.bh.planners.api.enums.Attribute
import com.bh.planners.core.feature.attributesystem.GameplayAttributeManager.getAttributeSet
import com.hitable.hipecurrency.api.CurrencyAPI
import com.hitable.hipeequip.bean.backpack.ItemData
import com.hitable.hipeequip.enums.BackpackType
import com.hitable.hipeequip.enums.ItemType
import com.hitable.hipeequip.manager.BackpackManager.backpack
import ink.ptms.chemdah.core.PlayerProfile
import ink.ptms.chemdah.core.quest.Id
import ink.ptms.chemdah.core.quest.Option
import ink.ptms.chemdah.core.quest.QuestContainer
import ink.ptms.chemdah.core.quest.Template
import taboolib.common.util.asList
import taboolib.common5.clong
import taboolib.library.configuration.ConfigurationSection

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.addon.AddonUI
 *
 * @author sky
 * @since 2021/3/11 9:05 上午
 */
@Id("ui")
@Option(Option.Type.SECTION)
class AddonUI(root: ConfigurationSection, questContainer: QuestContainer) : Addon(root, questContainer) {

    /**
     * 持续在 UI 中显示
     * 启用后则可能显示为 can-start 或 cannot-start
     */
    val visibleStart = root.getBoolean("visible.start", false)

    /**
     * 任务完成后显示为 complete 已完成状态
     */
    val visibleComplete = root.getBoolean("visible.complete", true)

    /**
     * 显示图标
     */
    val icon: String? = root.getString("icon")

    /**
     * 显示介绍，适配 Chemdah Lab
     */
    val description = root["description"]?.asList()?.flatMap { it.lines() }

    /**
     * 显示奖励
     */
    val rewards: List<Pair<String, Int>>? = if (root.contains("rewards")) root.getStringList("rewards").mapNotNull {
        val split = it.split(" ")
        if (split.size == 2) split[0] to split[1].toInt() else null
    } else null

    companion object {

        /** 获取 UI 组件 */
        fun Template.ui() = addon<AddonUI>("ui")

        fun Template.reward(profile: PlayerProfile) {
            val ui = ui()
            if (ui?.rewards != null) {
                val player = profile.player
                val plannersProfile = player.plannersProfile
                val itemBackpack = player.backpack(BackpackType.ITEM)
                val taskBackpack = player.backpack(BackpackType.TASK)
                ui.rewards.forEach {
                    if (CurrencyAPI.isCurrency(it.first).get()) {
                        CurrencyAPI.addCurrencyAmount(profile.uniqueId, it.first, it.second.clong, "任务奖励")
                    } else if (it.first == "Exp") {
                        var addExp = it.second.toDouble()
                        addExp *= 1 + profile.player.getAttributeSet().getAttribute(Attribute.ExperienceGainBonus)
                        plannersProfile.addExperience(addExp.toInt())
                    } else {
                        val itemData = ItemData(it.first, it.second)
                        if (itemData.getItemType() == ItemType.QUEST) {
                            taskBackpack.lootItem(itemData)
                        } else {
                            itemBackpack.lootItem(itemData)
                        }
                    }
                }
            }
        }

    }
}
package ink.ptms.chemdah.core.quest.selector

import ink.ptms.chemdah.api.event.InferItemHookEvent
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getItemTag

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.selector.item.InferItemCompat
 *
 * @author sky
 * @since 2021/4/23 10:30 下午
 */
@Suppress("SpellCheckingInspection")
internal object InferItemHooks {

    @SubscribeEvent
    private fun onItemHook(e: InferItemHookEvent) {
        when (e.id.lowercase()) {
            "zaphkiel" -> {
                e.itemClass = ItemZaphkiel::class.java
            }

            "itemsystem" -> {
                e.itemClass = ItemSystem::class.java
            }
        }
    }

    class ItemZaphkiel(material: String, flags: List<Flags>, data: List<DataMatch>) :
        InferItem.Item(material, flags, data) {

        fun ItemStack.zaphkielId(): String {
            val itemStream = ZaphkielAPI.read(this)
            return if (itemStream.isExtension()) itemStream.getZaphkielName() else "@vanilla"
        }

        override fun match(item: ItemStack): Boolean {
            return matchType(item.zaphkielId()) && matchMetaData(item)
        }

        override fun matchMetaData(item: ItemStack, itemMeta: ItemMeta?, dataMatch: DataMatch): Boolean {
            return when {
                dataMatch.key.startsWith("config.") -> {
                    val itemStream = ZaphkielAPI.read(item)
                    if (itemStream.isVanilla()) return false
                    val data = itemStream.getZaphkielItem().config[dataMatch.key.substringAfter("data.")]
                    return if (data != null) dataMatch.check(data) else false
                }

                dataMatch.key.startsWith("data.") -> {
                    val itemStream = ZaphkielAPI.read(item)
                    if (itemStream.isVanilla()) return false
                    val data = itemStream.getZaphkielData().getDeep(dataMatch.key.substringAfter("data."))?.unsafeData()
                    return if (data != null) dataMatch.check(data) else false
                }

                else -> {
                    super.matchMetaData(item, itemMeta, dataMatch)
                }
            }
        }
    }

    class ItemSystem(material: String, flags: List<Flags>, data: List<DataMatch>) :
        InferItem.Item(material, flags, data) {

        fun ItemStack.pxId(): String {
            return getItemTag().getDeep("pxrpg.id")?.asString() ?: "@vanilla"
        }

        fun ItemStack.pxName(): String {
            return getItemTag().getDeep("pxrpg.name")?.asString() ?: "@vanilla"
        }

        fun ItemStack.pxAuthor(): String {
            return getItemTag().getDeep("pxrpg.authorName")?.asString() ?: "@vanilla"
        }

        fun ItemStack.pxQuality(): String {
            return getItemTag().getDeep("pxrpg.itemQuality")?.asString() ?: "@vanilla"
        }

        fun ItemStack.pxType(): String {
            return getItemTag().getDeep("pxrpg.itemType")?.asString() ?: "@vanilla"
        }

        fun ItemStack.pxTemplate(): String {
            return getItemTag().getDeep("pxrpg.template")?.asString() ?: "@vanilla"
        }

        fun ItemStack.pxLevel(): Int {
            return getItemTag().getDeep("pxrpg.level")?.asInt() ?: -1
        }

        fun ItemStack.pxBind(): String {
            return getItemTag().getDeep("pxrpg.bind")?.asString() ?: "@vanilla"
        }

        override fun match(item: ItemStack): Boolean {
            return matchType(item.pxId()) && matchMetaData(item)
        }

        override fun matchMetaData(item: ItemStack, itemMeta: ItemMeta?, dataMatch: DataMatch): Boolean {
            return when (dataMatch.key) {
                "name" -> dataMatch.check(item.pxName())
                "author" -> dataMatch.check(item.pxAuthor())
                "quality" -> dataMatch.check(item.pxQuality())
                "type" -> dataMatch.check(item.pxType())
                "template" -> dataMatch.check(item.pxTemplate())
                "level" -> dataMatch.check(item.pxLevel())
                "bind" -> dataMatch.check(item.pxBind())
                else -> super.matchMetaData(item, itemMeta, dataMatch)
            }
        }
    }
}

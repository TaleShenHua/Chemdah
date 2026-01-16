package ink.ptms.chemdah.util

import com.google.common.base.Enums
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common5.Coerce
import taboolib.common5.Demand
import taboolib.common5.Quat
import taboolib.library.xseries.XParticle

open class Effect(val source: String) {

    val demand = Demand(source)
    val particle = Enums.getIfPresent(XParticle::class.java, demand.namespace.uppercase()).or(XParticle.FLAME)!!
    val offsetX = Coerce.toDouble(demand.get(0, "0")!!)
    val offsetY = Coerce.toDouble(demand.get(1, "0")!!)
    val offsetZ = Coerce.toDouble(demand.get(2, "0")!!)
    val posX = Coerce.toDouble(demand.get("posX"))
    val posY = Coerce.toDouble(demand.get("posY"))
    val posZ = Coerce.toDouble(demand.get("posZ"))
    val speed = Coerce.toDouble(demand.get(listOf("speed", "s"), "0")!!)
    val count = Coerce.toInteger(demand.get(listOf("count", "c"), "1")!!)
    var data: Any? = null

    init {
        demand.get("block")?.let {
            data = Material.valueOf(it).createBlockData()
        }
        demand.get("item")?.let {
            data = ItemStack(Material.valueOf(it.split(":")[0]), 1, 0, Coerce.toByte(it.split(":").getOrElse(1) { "0" }))
        }
        demand.get("color")?.let {
            val color = it.split("~")[0].split("-")
            data = Particle.DustOptions(
                Color.fromBGR(
                    Coerce.toInteger(color.getOrElse(0) { "0" }),
                    Coerce.toInteger(color.getOrElse(1) { "1" }),
                    Coerce.toInteger(color.getOrElse(2) { "2" })
                ),
                Coerce.toFloat(it.split("~").getOrElse(1) { "0" })
            )
        }
    }

    fun run(location: Location) {
        val pos = location.clone().add(posX, posY, posZ)
        var quat = Quat.at(pos.x, pos.y, pos.z)
        quat = quat.rotate2D(location.yaw.toDouble(), location.x, location.z)
        location.world!!.getNearbyEntities(location, 100.0, 100.0, 100.0).filterIsInstance<Player>().forEach {
            it.spawnParticle(particle.get()!!, Location(location.world, quat.x(), quat.y(), quat.z()), count, offsetX, offsetY, offsetZ, speed, data)
        }
    }

    fun run(location: Location, player: Player) {
        val pos = location.clone().add(posX, posY, posZ)
        var quat = Quat.at(pos.x, pos.y, pos.z)
        quat = quat.rotate2D(location.yaw.toDouble(), location.x, location.z)
        player.spawnParticle(particle.get()!!, Location(location.world, quat.x(), quat.y(), quat.z()), count, offsetX, offsetY, offsetZ, speed, data)
    }

    override fun toString(): String {
        return "Effect(source='$source', demand=$demand, particle=$particle, offsetX=$offsetX, offsetY=$offsetY, offsetZ=$offsetZ, posX=$posX, posY=$posY, posZ=$posZ, speed=$speed, count=$count, data=$data)"
    }
}
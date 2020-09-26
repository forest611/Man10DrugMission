package red.man10.man10drugmission

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerCommandSendEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class Event(val plugin: Man10DrugMission) :Listener{

    @EventHandler
    fun join(e:PlayerJoinEvent){
        Thread{
            plugin.dunce.load(e.player)
        }.start()
    }

    @EventHandler
    fun quit(e:PlayerQuitEvent){

        val p = e.player

        if (p.world.name==plugin.drugWorld){
            Thread{
                plugin.dunce.addDunceCount(p)
            }.start()
        }
    }

    @EventHandler
    fun worldChange(e:PlayerChangedWorldEvent){

        val p = e.player

        if (p.world.name != plugin.drugWorld)return

        if (plugin.dunce.isDunce(p)){

            p.playSound(p.location, Sound.ENTITY_PARROT_IMITATE_WITCH,1.0F,1.0F)
            p.sendMessage("§c§l負け犬は密売マップに入れない！！")

            p.health = 0.0
            return
        }

    }

    @EventHandler
    fun glideEvent(e:EntityToggleGlideEvent){

        val p = e.entity
        if (p !is Player)return

        if (p.world.name != plugin.drugWorld)return

        if (e.isGliding){
            e.isCancelled = true
        }
    }

    @EventHandler
    fun deathEvent(e:PlayerDeathEvent){
        if (e.entity.world.name == plugin.drugWorld){
            e.deathMessage = null
        }
    }

}
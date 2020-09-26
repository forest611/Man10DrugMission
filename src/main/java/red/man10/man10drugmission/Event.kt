package red.man10.man10drugmission

import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
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


}
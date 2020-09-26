package red.man10.man10drugmission

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
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


}
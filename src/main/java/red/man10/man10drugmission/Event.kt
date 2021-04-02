package red.man10.man10drugmission

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

class Event(private val plugin: Man10DrugMission) :Listener{

    companion object{
        val killCount = HashMap<UUID,MutableList<UUID>>()
    }

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

            p.teleport(plugin.spawnLocation)
            return
        }

        Thread{
            Thread.sleep(1000)

            if (!plugin.start && !p.hasPermission("drug_mission.op")){
                p.sendMessage("§c§l麻薬マップは現在閉まっています")
                Bukkit.getScheduler().runTask(plugin,Runnable{
                    p.teleport(plugin.spawnLocation)
                })
                return@Thread
            }
        }.start()

//        if (!plugin.start){
//            p.sendMessage("§c§l麻薬マップは現在閉まっています")
//            p.teleport(plugin.spawnLocation)
//            return
//        }

    }

    @EventHandler
    fun footBedrock(e:PlayerMoveEvent){
        val p = e.player

        if (p.world.name != plugin.drugWorld)return

        val loc = p.location
        loc.set(loc.x,loc.y-1.0,loc.z)
        if (loc.block.type == Material.BEDROCK){
            p.health = 0.0
            p.sendMessage("§c地雷を踏んでしまった！")
        }
    }

//    @EventHandler
//    fun glideEvent(e:EntityToggleGlideEvent){
//
//        val p = e.entity
//        if (p !is Player)return
//
//        if (p.world.name != plugin.drugWorld)return
//
//        if (e.isGliding){
//            e.isCancelled = true
//        }
//    }

    @EventHandler
    fun deathEvent(e:PlayerDeathEvent){
        val p = e.entity
        val killer = e.entity.killer

        if (p.world.name == plugin.drugWorld){
            e.deathMessage = null

            if (killer !=null){

                val drop = Random.nextInt(plugin.dropMoney).toDouble()

                if (plugin.vault.getBalance(p.uniqueId)>drop){
                    plugin.vault.withdraw(p.uniqueId,drop)
                    plugin.vault.deposit(killer.uniqueId,drop)
                }

                val list = killCount[killer.uniqueId]?: mutableListOf()
                if (!list.contains(p.uniqueId)){
                    list.add(p.uniqueId)
                }
                killCount[killer.uniqueId] = list
            }

        }
    }

}
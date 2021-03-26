package red.man10.man10drugmission

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Man10DrugMission : JavaPlugin() {

    lateinit var dunce:Dunce
    lateinit var event:Event
    lateinit var vault:VaultManager

    var drugWorld = ""
    var dunceCount = 10
    var dropMoney = 0
    var start = false
    var detonateFireworks = true
    lateinit var spawnLocation : Location

    override fun onEnable() {
        // Plugin startup logic
        saveDefaultConfig()

        drugWorld = config.getString("world")!!
        dunceCount = config.getInt("dunce")
        dropMoney = config.getInt("dropmoney")
        start = config.getBoolean("start")
        spawnLocation = config.getLocation("location")?: Location(Bukkit.getWorld("world"),0.0,60.0,0.0)

        dunce = Dunce(this)
        event = Event(this)
        vault = VaultManager(this)

        server.pluginManager.registerEvents(event,this)


    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {


        if (sender !is Player)return false

        if (!sender.hasPermission("drug_mission.op"))return true

        if (args.isEmpty()){

            sender.sendMessage("§dMan10DrugMission 麻薬密売ミッション")
            sender.sendMessage("§d/mission setworld 現在地点を密売マップに指定する")
            sender.sendMessage("§d/mission reload configをリロードする")
            sender.sendMessage("§d/mission dunce <set/reset> <player> 負け犬設定をする")
            sender.sendMessage("§d/mission <on/off> 麻薬ゲームをon offにする")
            sender.sendMessage("§d/mission location 現在地点をキックしたときのロケーションに設定する")

            return true
        }

        if (args[0] == "setworld"){

            val w = sender.world.name
            Thread{

                config.set("world",w)
                saveConfig()

                drugWorld = w

            }.start()

        }

        if (args[0] == "dunce"){

            if (args.size != 3)return false

            if (args[1] == "set"){

                val p = Bukkit.getPlayer(args[2])?:return false

                Thread{
                    dunce.setDunce(p)

                    sender.sendMessage("§dセット完了")
                }.start()

                return true
            }

            if (args[1] == "reset"){

                val p = Bukkit.getPlayer(args[2])?:return false

                Thread{
                    dunce.resetDunce(p)

                    sender.sendMessage("§dリセット完了")
                }.start()

                return true
            }

        }

        if (args[0] == "reload"){

            Thread{
                reloadConfig()

                drugWorld = config.getString("world")!!
                dunceCount = config.getInt("dunce")
                start = config.getBoolean("start")
            }.start()

        }

        if (args[0] == "location"){

            Thread{
                spawnLocation = sender.location
                config.set("location",spawnLocation)
                saveConfig()
            }.start()

        }

        if (args[0] == "on"){
            Thread{
                start = true
                config.set("start",true)
                saveConfig()
            }.start()

            Bukkit.broadcastMessage("§e§l麻薬マップが開きました！")
        }

        if (args[0] == "off"){
            Thread{
                start = false
                config.set("start",false)
                saveConfig()
            }.start()
            for (p in Bukkit.getOnlinePlayers()){
                if (p.world.name == drugWorld){
                    p.teleport(spawnLocation)
                }
            }
            Bukkit.broadcastMessage("§e§l麻薬マップが閉じました！")

            var pair : Pair<UUID?,Int> = Pair(null,0)

            for (data in Event.killCount){

                if (pair.second < data.value.size){
                    pair = Pair(data.key,data.value.size)
                }

            }

            Bukkit.broadcastMessage("KillRankingTop:${Bukkit.getPlayer(pair.first!!)!!.name},${pair.second} player killed")
        }

        if (args[0] == "firework"){
            detonateFireworks = !detonateFireworks
            sender.sendMessage(detonateFireworks.toString())
        }

        return false
    }
}
package red.man10.man10drugmission

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Man10DrugMission : JavaPlugin() {

    lateinit var dunce:Dunce
    lateinit var event:Event
    lateinit var vault:VaultManager

    var drugWorld = ""
    var dunceCount = 10
    var dropMoney = 0

    override fun onEnable() {
        // Plugin startup logic
        saveDefaultConfig()

        drugWorld = config.getString("world")!!
        dunceCount = config.getInt("dunce")
        dropMoney = config.getInt("dropmoney")

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
            }.start()

        }

        return false
    }
}
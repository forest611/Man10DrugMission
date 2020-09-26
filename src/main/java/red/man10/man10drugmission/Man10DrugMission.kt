package red.man10.man10drugmission

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Man10DrugMission : JavaPlugin() {

    lateinit var dunce:Dunce
    lateinit var event:Event

    var drugWorld = ""
    var dunceCount = 10

    override fun onEnable() {
        // Plugin startup logic
        saveDefaultConfig()

        drugWorld = config.getString("world")!!
        dunceCount = config.getInt("dunce")

        dunce = Dunce(this)
        event = Event(this)

        server.pluginManager.registerEvents(event,this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {


        if (sender !is Player)return false

        if (args.isEmpty()){

            sender.sendMessage("§dMan10DrugMission 麻薬密売ミッション")
            sender.sendMessage("§d/mission setworld 現在地点を密売マップに指定する")
            sender.sendMessage("§d/mission reload configをリロードする")
            sender.sendMessage("§d/mission dunce <set/unset> <player> 負け犬設定をする")

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




        return false
    }
}
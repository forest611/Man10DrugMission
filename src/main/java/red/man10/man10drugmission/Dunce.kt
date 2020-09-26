package red.man10.man10drugmission

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

class Dunce(var plugin:Man10DrugMission) {

    val dunceList = mutableListOf<Player>()

    fun load(p:Player){

        val mysql = MySQLManager(plugin,"DrugMission")

        val rs = mysql.query("SELECT * FROM dunce_table WHERE uuid='${p.uniqueId}';")

        if (rs == null ||!rs.next()){
            mysql.execute("INSERT INTO dunce_table (player, uuid, disconnect_count) VALUES (${p.name}, ${p.uniqueId}, DEFAULT);")
            return
        }

        val count = rs.getInt("disconnect_count")

        if (count > plugin.dunceCount){
            dunceList.add(p)

            Bukkit.broadcastMessage("§e負け犬の§d§l${p.name}§eがログインした！")
        }

    }

    fun addDunceCount(p:Player){
        val mysql = MySQLManager(plugin,"DrugMission")
        mysql.execute("UPDATE dunce_table SET disconnect_count=disconnect_count+1 WHERE uuid='${p.uniqueId}';")
    }

    fun isDunce(p:Player):Boolean{
        if (dunceList.contains(p))return true
        return false
    }

    fun resetDunce(p:Player){
        val mysql = MySQLManager(plugin,"DrugMission")
        mysql.execute("UPDATE dunce_table SET disconnect_count=0 WHERE uuid='${p.uniqueId}';")

    }


}
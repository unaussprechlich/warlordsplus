package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer

object DamageHealingAbsorbedEndOfGame : AbstractHudElement(), IChatConsumer {

    var damage: ArrayList<Int> = arrayListOf()
    var healing: ArrayList<Int> = arrayListOf()
    var absorbed: ArrayList<Int> = arrayListOf()
    var minutes: Int = 0
    var highestDamage: Int = 0
    var highestDamageMin: Int = 0
    var averageDamagePerMin: Int = 0

    var highestHealing: Int = 0
    var highestHealingMin: Int = 0
    var averageHealingPerMin: Int = 0

    var highestAbsorbed: Int = 0
    var highestAbsorbedMin: Int = 0
    var averageAbsorbedPerMin: Int = 0

    var lowestDamage: Int = Int.MAX_VALUE
    var lowestDamageMin: Int = 0
    var lowestHealing: Int = Int.MAX_VALUE
    var lowestHealingMin: Int = 0
    var lowestAbsorbed: Int = Int.MAX_VALUE
    var lowestAbsorbedMin: Int = 0

    init {
        EventBus.register<ResetEvent> {
            damage = arrayListOf()
            healing = arrayListOf()
            absorbed = arrayListOf()
            minutes = 0
            highestDamage = 0
            highestDamageMin = 0
            averageDamagePerMin = 0
            highestHealing = 0
            highestHealingMin = 0
            averageHealingPerMin = 0
            highestAbsorbed = 0
            highestAbsorbedMin = 0
            averageAbsorbedPerMin = 0
            lowestDamage = Int.MAX_VALUE
            lowestDamageMin = 0
            lowestHealing = Int.MAX_VALUE
            lowestHealingMin = 0
            lowestAbsorbed = Int.MAX_VALUE
            lowestAbsorbedMin = 0
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()
        return renderStrings.toTypedArray()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        if (e.message.formattedText.contains("Damage:")) {
            //siblings 1 - 3 - 5
            //{id:"minecraft:stone",Count:1b,tag:{display:{Lore:["§fMinute 1: §64,783","§fMinute 2: §610,206","§fMinute 3: §60","§fMinute 4: §60","§fMinute 5: §62,738","§fMinute 6: §60","§fMinute 7: §60","§fMinute 8: §60","§fMinute 9: §60",],Name:"§bStat Breakdown (Damage):",},},Damage:0s,}
            //"§fMinute 11: §60","§fMinute 12: §60","§fMinute 13: §60","§fMinute 14: §60","§fMinute 15: §60","§fMinute 16: §60",],Name:"§bStat Breakdown (Damage):",},},Damage:0s,}§r
            println(e.message.siblings[1].chatStyle.chatHoverEvent.value.formattedText)
            println(e.message.siblings[3].chatStyle.chatHoverEvent.value.formattedText)
            println(e.message.siblings[5].chatStyle.chatHoverEvent.value.formattedText)

            val damageUnformatted = e.message.siblings[1].chatStyle.chatHoverEvent.value.formattedText
            val healingUnformatted = e.message.siblings[3].chatStyle.chatHoverEvent.value.formattedText
            val absorbedUnformatted = e.message.siblings[5].chatStyle.chatHoverEvent.value.formattedText

            val lastMinutePosition = damageUnformatted.lastIndexOf("Minute")
            val minuteString = damageUnformatted.substring(lastMinutePosition)
            minutes = minuteString.substring(minuteString.indexOf("e") + 2, minuteString.indexOf(":")).toInt()

            var damageString = damageUnformatted
            var healingString = healingUnformatted
            var absorbedString = absorbedUnformatted
            for (x in 0 until minutes) {
                val damageMinutePosition = damageString.substring(damageString.indexOf("Minute") + 12)
                val healingMinutePosition = healingString.substring(healingString.indexOf("Minute") + 12)
                val absorbedMinutePosition = absorbedString.substring(absorbedString.indexOf("Minute") + 12)
                var damageAmount: String
                var healingAmount: String
                var absorbedAmount: String
                if (x >= 9) {
                    damageAmount = damageMinutePosition.substring(
                        0,
                        damageString.substring(damageString.indexOf("Minute") + 13).indexOf("\"")
                    )
                    damageString = damageMinutePosition.substring(
                        damageString.substring(damageString.indexOf("Minute") + 13).indexOf("\"")
                    )

                    healingAmount = healingMinutePosition.substring(
                        0,
                        healingString.substring(healingString.indexOf("Minute") + 13).indexOf("\"")
                    )
                    healingString = healingMinutePosition.substring(
                        healingString.substring(healingString.indexOf("Minute") + 13).indexOf("\"")
                    )

                    absorbedAmount = absorbedMinutePosition.substring(
                        0,
                        absorbedString.substring(absorbedString.indexOf("Minute") + 13).indexOf("\"")
                    )
                    absorbedString = absorbedMinutePosition.substring(
                        absorbedString.substring(absorbedString.indexOf("Minute") + 13).indexOf("\"")
                    )
                } else {
                    damageAmount = damageMinutePosition.substring(
                        0,
                        damageString.substring(damageString.indexOf("Minute") + 12).indexOf("\"")
                    )
                    damageString = damageMinutePosition.substring(
                        damageString.substring(damageString.indexOf("Minute") + 12).indexOf("\"")
                    )

                    healingAmount = healingMinutePosition.substring(
                        0,
                        healingString.substring(healingString.indexOf("Minute") + 12).indexOf("\"")
                    )
                    healingString = healingMinutePosition.substring(
                        healingString.substring(healingString.indexOf("Minute") + 12).indexOf("\"")
                    )

                    absorbedAmount = absorbedMinutePosition.substring(
                        0,
                        absorbedString.substring(absorbedString.indexOf("Minute") + 12).indexOf("\"")
                    )
                    absorbedString = absorbedMinutePosition.substring(
                        absorbedString.substring(absorbedString.indexOf("Minute") + 12).indexOf("\"")
                    )
                }
                damage.add(damageAmount.replace(",", "").toInt())
                healing.add(healingAmount.replace(",", "").toInt())
                absorbed.add(absorbedAmount.replace(",", "").toInt())
            }

            var totalDamage = 0
            var totalHealing = 0
            var totalAbsorbed = 0

            damage.forEachIndexed { index, element ->
                totalDamage += element
                if (element > highestDamage) {
                    highestDamage = element
                    highestDamageMin = index + 1
                } else if (element < lowestDamage) {
                    lowestDamage = element
                    lowestDamageMin = index + 1
                }
            }
            healing.forEachIndexed { index, element ->
                totalHealing += element
                if (element > highestHealing) {
                    highestHealing = element
                    highestHealingMin = index + 1
                } else if (element < lowestHealing) {
                    lowestHealing = element
                    lowestHealingMin = index + 1
                }
            }
            absorbed.forEachIndexed { index, element ->
                totalAbsorbed += element
                if (element > highestAbsorbed) {
                    highestAbsorbed = element
                    highestAbsorbedMin = index + 1
                } else if (element < lowestAbsorbed) {
                    lowestAbsorbed = element
                    lowestAbsorbedMin = index + 1
                }
            }

            averageDamagePerMin = totalDamage / minutes
            averageHealingPerMin = totalHealing / minutes
            averageAbsorbedPerMin = totalAbsorbed / minutes

            println(highestDamage)
            println(highestHealing)
            println(highestAbsorbed)
        }
    }

    override fun isVisible(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
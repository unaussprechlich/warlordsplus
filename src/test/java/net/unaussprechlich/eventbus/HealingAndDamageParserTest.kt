package net.unaussprechlich.eventbus

import net.unaussprechlich.warlordsplus.module.modules.DamageAndHealParser
import net.unaussprechlich.warlordsplus.module.modules.DamageTakenEvent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

const val DAMAGE_TAKEN_EXAMPLE = "» You hit sumSmash for 130 melee damage."

class HealingAndDamageParserTest {


    @Test
    fun parseDamageTakenTest(){

        val result = DamageAndHealParser.parseDamageTaken(DAMAGE_TAKEN_EXAMPLE)

        assertEquals(DamageTakenEvent(130, false, "sumSmash"), result)

    }

}
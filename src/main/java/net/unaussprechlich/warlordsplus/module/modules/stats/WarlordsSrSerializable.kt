package net.unaussprechlich.warlordsplus.module.modules.stats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WarlordsSrApiResponse(
    @SerialName("data")
    val `data`: WarlordsSrApiData? = null,
    @SerialName("success")
    val success: Boolean? = false
)

@Serializable
data class WarlordsSrApiData(
    @SerialName("name_history")
    val nameHistory: List<NameHistory>? = listOf(),
    @SerialName("playername")
    val playername: String = "0",
    @SerialName("ranking")
    val ranking: Ranking? = null,
    @SerialName("uuid")
    val uuid: String = "",
    @SerialName("warlords_hypixel")
    val warlordsHypixel: WarlordsHypixel? = null,
    @SerialName("warlords_sr")
    val warlordsSr: WarlordsSr? = null
)

@Serializable
data class NameHistory(
    @SerialName("changedToAt")
    val changedToAt: Long = -1,
    @SerialName("name")
    val name: String? = ""
)

@Serializable
data class Ranking(
    @SerialName("mage")
    val mage: MageRank? = null,
    @SerialName("overall")
    val overall: Int? = 0,
    @SerialName("paladin")
    val paladin: PaladinRank? = null,
    @SerialName("shaman")
    val shaman: ShamanRank? = null,
    @SerialName("warrior")
    val warrior: WarriorRank? = null
)

@Serializable
data class WarlordsHypixel(
    @SerialName("aquamancer_plays")
    val aquamancerPlays: Int? = 0,
    @SerialName("assists")
    val assists: Int? = 0,
    @SerialName("avenger_plays")
    val avengerPlays: Int? = 0,
    @SerialName("berserker_plays")
    val berserkerPlays: Int? = 0,
    @SerialName("crusader_plays")
    val crusaderPlays: Int? = 0,
    @SerialName("cryomancer_plays")
    val cryomancerPlays: Int? = 0,
    @SerialName("damage")
    val damage: Int? = 0,
    @SerialName("damage_aquamancer")
    val damageAquamancer: Int? = 0,
    @SerialName("damage_avenger")
    val damageAvenger: Int? = 0,
    @SerialName("damage_berserker")
    val damageBerserker: Int? = 0,
    @SerialName("damage_crusader")
    val damageCrusader: Int? = 0,
    @SerialName("damage_cryomancer")
    val damageCryomancer: Int? = 0,
    @SerialName("damage_defender")
    val damageDefender: Int? = 0,
    @SerialName("damage_earthwarden")
    val damageEarthwarden: Int? = 0,
    @SerialName("damage_mage")
    val damageMage: Int? = 0,
    @SerialName("damage_paladin")
    val damagePaladin: Int? = 0,
    @SerialName("damage_prevented")
    val damagePrevented: Int? = 0,
    @SerialName("damage_prevented_aquamancer")
    val damagePreventedAquamancer: Int? = 0,
    @SerialName("damage_prevented_avenger")
    val damagePreventedAvenger: Int? = 0,
    @SerialName("damage_prevented_berserker")
    val damagePreventedBerserker: Int? = 0,
    @SerialName("damage_prevented_crusader")
    val damagePreventedCrusader: Int? = 0,
    @SerialName("damage_prevented_cryomancer")
    val damagePreventedCryomancer: Int? = 0,
    @SerialName("damage_prevented_defender")
    val damagePreventedDefender: Int? = 0,
    @SerialName("damage_prevented_earthwarden")
    val damagePreventedEarthwarden: Int? = 0,
    @SerialName("damage_prevented_mage")
    val damagePreventedMage: Int? = 0,
    @SerialName("damage_prevented_paladin")
    val damagePreventedPaladin: Int? = 0,
    @SerialName("damage_prevented_protector")
    val damagePreventedProtector: Int? = 0,
    @SerialName("damage_prevented_pyromancer")
    val damagePreventedPyromancer: Int? = 0,
    @SerialName("damage_prevented_revenant")
    val damagePreventedRevenant: Int? = 0,
    @SerialName("damage_prevented_shaman")
    val damagePreventedShaman: Int? = 0,
    @SerialName("damage_prevented_spiritguard")
    val damagePreventedSpiritguard: Int? = 0,
    @SerialName("damage_prevented_thunderlord")
    val damagePreventedThunderlord: Int? = 0,
    @SerialName("damage_prevented_warrior")
    val damagePreventedWarrior: Int? = 0,
    @SerialName("damage_protector")
    val damageProtector: Int? = 0,
    @SerialName("damage_pyromancer")
    val damagePyromancer: Int? = 0,
    @SerialName("damage_revenant")
    val damageRevenant: Int? = 0,
    @SerialName("damage_shaman")
    val damageShaman: Int? = 0,
    @SerialName("damage_spiritguard")
    val damageSpiritguard: Int? = 0,
    @SerialName("damage_taken")
    val damageTaken: Int? = 0,
    @SerialName("damage_thunderlord")
    val damageThunderlord: Int? = 0,
    @SerialName("damage_warrior")
    val damageWarrior: Int? = 0,
    @SerialName("deaths")
    val deaths: Int? = 0,
    @SerialName("defender_plays")
    val defenderPlays: Int? = 0,
    @SerialName("earthwarden_plays")
    val earthwardenPlays: Int? = 0,
    @SerialName("flag_conquer_self")
    val flagConquerSelf: Int? = 0,
    @SerialName("flag_conquer_team")
    val flagConquerTeam: Int? = 0,
    @SerialName("heal")
    val heal: Int? = 0,
    @SerialName("heal_aquamancer")
    val healAquamancer: Int? = 0,
    @SerialName("heal_avenger")
    val healAvenger: Int? = 0,
    @SerialName("heal_berserker")
    val healBerserker: Int? = 0,
    @SerialName("heal_crusader")
    val healCrusader: Int? = 0,
    @SerialName("heal_cryomancer")
    val healCryomancer: Int? = 0,
    @SerialName("heal_defender")
    val healDefender: Int? = 0,
    @SerialName("heal_earthwarden")
    val healEarthwarden: Int? = 0,
    @SerialName("heal_mage")
    val healMage: Int? = 0,
    @SerialName("heal_paladin")
    val healPaladin: Int? = 0,
    @SerialName("heal_protector")
    val healProtector: Int? = 0,
    @SerialName("heal_pyromancer")
    val healPyromancer: Int? = 0,
    @SerialName("heal_revenant")
    val healRevenant: Int? = 0,
    @SerialName("heal_shaman")
    val healShaman: Int? = 0,
    @SerialName("heal_spiritguard")
    val healSpiritguard: Int? = 0,
    @SerialName("heal_thunderlord")
    val healThunderlord: Int? = 0,
    @SerialName("heal_warrior")
    val healWarrior: Int? = 0,
    @SerialName("kills")
    val kills: Int? = 0,
    @SerialName("life_leeched")
    val lifeLeeched: Int? = 0,
    @SerialName("life_leeched_berserker")
    val lifeLeechedBerserker: Int? = 0,
    @SerialName("losses")
    val losses: Int? = 0,
    @SerialName("losses_aquamancer")
    val lossesAquamancer: Int? = 0,
    @SerialName("losses_avenger")
    val lossesAvenger: Int? = 0,
    @SerialName("losses_berserker")
    val lossesBerserker: Int? = 0,
    @SerialName("losses_crusader")
    val lossesCrusader: Int? = 0,
    @SerialName("losses_cryomancer")
    val lossesCryomancer: Int? = 0,
    @SerialName("losses_defender")
    val lossesDefender: Int? = 0,
    @SerialName("losses_earthwarden")
    val lossesEarthwarden: Int? = 0,
    @SerialName("losses_mage")
    val lossesMage: Int? = 0,
    @SerialName("losses_paladin")
    val lossesPaladin: Int? = 0,
    @SerialName("losses_protector")
    val lossesProtector: Int? = 0,
    @SerialName("losses_pyromancer")
    val lossesPyromancer: Int? = 0,
    @SerialName("losses_revenant")
    val lossesRevenant: Int? = 0,
    @SerialName("losses_shaman")
    val lossesShaman: Int? = 0,
    @SerialName("losses_spiritguard")
    val lossesSpiritguard: Int? = 0,
    @SerialName("losses_thunderlord")
    val lossesThunderlord: Int? = 0,
    @SerialName("losses_warrior")
    val lossesWarrior: Int? = 0,
    @SerialName("mage_cooldown")
    val mageCooldown: Int? = 0,
    @SerialName("mage_critchance")
    val mageCritchance: Int? = 0,
    @SerialName("mage_critmultiplier")
    val mageCritmultiplier: Int? = 0,
    @SerialName("mage_energy")
    val mageEnergy: Int? = 0,
    @SerialName("mage_health")
    val mageHealth: Int? = 0,
    @SerialName("mage_plays")
    val magePlays: Int? = 0,
    @SerialName("mage_skill1")
    val mageSkill1: Int? = 0,
    @SerialName("mage_skill2")
    val mageSkill2: Int? = 0,
    @SerialName("mage_skill3")
    val mageSkill3: Int? = 0,
    @SerialName("mage_skill4")
    val mageSkill4: Int? = 0,
    @SerialName("mage_skill5")
    val mageSkill5: Int? = 0,
    @SerialName("paladin_cooldown")
    val paladinCooldown: Int? = 0,
    @SerialName("paladin_critchance")
    val paladinCritchance: Int? = 0,
    @SerialName("paladin_critmultiplier")
    val paladinCritmultiplier: Int? = 0,
    @SerialName("paladin_energy")
    val paladinEnergy: Int? = 0,
    @SerialName("paladin_health")
    val paladinHealth: Int? = 0,
    @SerialName("paladin_plays")
    val paladinPlays: Int? = 0,
    @SerialName("paladin_skill1")
    val paladinSkill1: Int? = 0,
    @SerialName("paladin_skill2")
    val paladinSkill2: Int? = 0,
    @SerialName("paladin_skill3")
    val paladinSkill3: Int? = 0,
    @SerialName("paladin_skill4")
    val paladinSkill4: Int? = 0,
    @SerialName("paladin_skill5")
    val paladinSkill5: Int? = 0,
    @SerialName("penalty")
    val penalty: Int? = 0,
    @SerialName("protector_plays")
    val protectorPlays: Int? = 0,
    @SerialName("pyromancer_plays")
    val pyromancerPlays: Int? = 0,
    @SerialName("revenant_plays")
    val revenantPlays: Int? = 0,
    @SerialName("shaman_cooldown")
    val shamanCooldown: Int? = 0,
    @SerialName("shaman_critchance")
    val shamanCritchance: Int? = 0,
    @SerialName("shaman_critmultiplier")
    val shamanCritmultiplier: Int? = 0,
    @SerialName("shaman_energy")
    val shamanEnergy: Int? = 0,
    @SerialName("shaman_health")
    val shamanHealth: Int? = 0,
    @SerialName("shaman_plays")
    val shamanPlays: Int? = 0,
    @SerialName("shaman_skill1")
    val shamanSkill1: Int? = 0,
    @SerialName("shaman_skill2")
    val shamanSkill2: Int? = 0,
    @SerialName("shaman_skill3")
    val shamanSkill3: Int? = 0,
    @SerialName("shaman_skill4")
    val shamanSkill4: Int? = 0,
    @SerialName("shaman_skill5")
    val shamanSkill5: Int? = 0,
    @SerialName("spiritguard_plays")
    val spiritguardPlays: Int? = 0,
    @SerialName("thunderlord_plays")
    val thunderlordPlays: Int? = 0,
    @SerialName("warrior_cooldown")
    val warriorCooldown: Int? = 0,
    @SerialName("warrior_critchance")
    val warriorCritchance: Int? = 0,
    @SerialName("warrior_critmultiplier")
    val warriorCritmultiplier: Int? = 0,
    @SerialName("warrior_energy")
    val warriorEnergy: Int? = 0,
    @SerialName("warrior_health")
    val warriorHealth: Int? = 0,
    @SerialName("warrior_plays")
    val warriorPlays: Int? = 0,
    @SerialName("warrior_skill1")
    val warriorSkill1: Int? = 0,
    @SerialName("warrior_skill2")
    val warriorSkill2: Int? = 0,
    @SerialName("warrior_skill3")
    val warriorSkill3: Int? = 0,
    @SerialName("warrior_skill4")
    val warriorSkill4: Int? = 0,
    @SerialName("warrior_skill5")
    val warriorSkill5: Int? = 0,
    @SerialName("wins")
    val wins: Int? = 0,
    @SerialName("wins_aquamancer")
    val winsAquamancer: Int? = 0,
    @SerialName("wins_avenger")
    val winsAvenger: Int? = 0,
    @SerialName("wins_berserker")
    val winsBerserker: Int? = 0,
    @SerialName("wins_blu")
    val winsBlu: Int? = 0,
    @SerialName("wins_capturetheflag")
    val winsCapturetheflag: Int? = 0,
    @SerialName("wins_capturetheflag_a")
    val winsCapturetheflagA: Int? = 0,
    @SerialName("wins_capturetheflag_b")
    val winsCapturetheflagB: Int? = 0,
    @SerialName("wins_capturetheflag_blu")
    val winsCapturetheflagBlu: Int? = 0,
    @SerialName("wins_capturetheflag_red")
    val winsCapturetheflagRed: Int? = 0,
    @SerialName("wins_crusader")
    val winsCrusader: Int? = 0,
    @SerialName("wins_cryomancer")
    val winsCryomancer: Int? = 0,
    @SerialName("wins_defender")
    val winsDefender: Int? = 0,
    @SerialName("wins_domination")
    val winsDomination: Int? = 0,
    @SerialName("wins_domination_a")
    val winsDominationA: Int? = 0,
    @SerialName("wins_domination_b")
    val winsDominationB: Int? = 0,
    @SerialName("wins_domination_blu")
    val winsDominationBlu: Int? = 0,
    @SerialName("wins_domination_red")
    val winsDominationRed: Int? = 0,
    @SerialName("wins_earthwarden")
    val winsEarthwarden: Int? = 0,
    @SerialName("wins_mage")
    val winsMage: Int? = 0,
    @SerialName("wins_paladin")
    val winsPaladin: Int? = 0,
    @SerialName("wins_protector")
    val winsProtector: Int? = 0,
    @SerialName("wins_pyromancer")
    val winsPyromancer: Int? = 0,
    @SerialName("wins_red")
    val winsRed: Int? = 0,
    @SerialName("wins_revenant")
    val winsRevenant: Int? = 0,
    @SerialName("wins_shaman")
    val winsShaman: Int? = 0,
    @SerialName("wins_spiritguard")
    val winsSpiritguard: Int? = 0,
    @SerialName("wins_teamdeathmatch")
    val winsTeamdeathmatch: Int? = 0,
    @SerialName("wins_teamdeathmatch_a")
    val winsTeamdeathmatchA: Int? = 0,
    @SerialName("wins_teamdeathmatch_b")
    val winsTeamdeathmatchB: Int? = 0,
    @SerialName("wins_teamdeathmatch_blu")
    val winsTeamdeathmatchBlu: Int? = 0,
    @SerialName("wins_teamdeathmatch_red")
    val winsTeamdeathmatchRed: Int? = 0,
    @SerialName("wins_thunderlord")
    val winsThunderlord: Int? = 0,
    @SerialName("wins_warrior")
    val winsWarrior: Int? = 0
)

@Serializable
data class WarlordsSr(
    @SerialName("DHP")
    val dHP: Int? = 0,
    @SerialName("KD")
    val kD: Double? = 0.0,
    @SerialName("KDA")
    val kDA: Double? = 0.0,
    @SerialName("mage")
    val mage: Mage? = null,
    @SerialName("paladin")
    val paladin: Paladin? = null,
    @SerialName("plays")
    val plays: Int? = 0,
    @SerialName("SR")
    val sR: Int? = 0,
    @SerialName("shaman")
    val shaman: Shaman? = null,
    @SerialName("WL")
    val wL: Double? = 0.0,
    @SerialName("warrior")
    val warrior: Warrior? = null
)

@Serializable
data class MageRank(
    @SerialName("aquamancer")
    val aquamancer: Int? = 0,
    @SerialName("cryomancer")
    val cryomancer: Int? = 0,
    @SerialName("overall")
    val overall: Int? = 0,
    @SerialName("pyromancer")
    val pyromancer: Int? = 0
)

@Serializable
data class PaladinRank(
    @SerialName("avenger")
    val avenger: Int? = 0,
    @SerialName("crusader")
    val crusader: Int? = 0,
    @SerialName("overall")
    val overall: Int? = 0,
    @SerialName("protector")
    val protector: Int? = 0
)

@Serializable
data class ShamanRank(
    @SerialName("earthwarden")
    val earthwarden: Int? = 0,
    @SerialName("overall")
    val overall: Int? = 0,
    @SerialName("spiritguard")
    val spiritguard: Int? = 0,
    @SerialName("thunderlord")
    val thunderlord: Int? = 0
)

@Serializable
data class WarriorRank(
    @SerialName("berserker")
    val berserker: Int? = 0,
    @SerialName("defender")
    val defender: Int? = 0,
    @SerialName("overall")
    val overall: Int? = 0,
    @SerialName("revenant")
    val revenant: Int? = 0
)

@Serializable
data class Mage(
    @SerialName("aquamancer")
    val aquamancer: Spec? = null,
    @SerialName("cryomancer")
    val cryomancer: Spec? = null,
    @SerialName("pyromancer")
    val pyromancer: Spec? = null,
    @SerialName("LEVEL")
    val LEVEL: Int? = 0,
    @SerialName("DHP")
    val DHP: Int? = 0,
    @SerialName("SR")
    val SR: Int? = 0,
    @SerialName("WINS")
    val WINS: Int? = 0,
    @SerialName("WL")
    val WL: Double? = 0.0
)

@Serializable
data class Paladin(
    @SerialName("avenger")
    val avenger: Spec? = null,
    @SerialName("crusader")
    val crusader: Spec? = null,
    @SerialName("protector")
    val protector: Spec? = null,
    @SerialName("LEVEL")
    val LEVEL: Int? = 0,
    @SerialName("DHP")
    val DHP: Int? = 0,
    @SerialName("SR")
    val SR: Int? = 0,
    @SerialName("WINS")
    val WINS: Int? = 0,
    @SerialName("WL")
    val WL: Double? = 0.0
)

@Serializable
data class Shaman(
    @SerialName("earthwarden")
    val earthwarden: Spec? = null,
    @SerialName("spiritguard")
    val spiritguard: Spec? = null,
    @SerialName("thunderlord")
    val thunderlord: Spec? = null,
    @SerialName("LEVEL")
    val LEVEL: Int? = 0,
    @SerialName("DHP")
    val DHP: Int? = 0,
    @SerialName("SR")
    val SR: Int? = 0,
    @SerialName("WINS")
    val WINS: Int? = 0,
    @SerialName("WL")
    val WL: Double? = 0.0
)

@Serializable
data class Warrior(
    @SerialName("berserker")
    val berserker: Spec? = null,
    @SerialName("revenant")
    val revenant: Spec? = null,
    @SerialName("defender")
    val defender: Spec? = null,
    @SerialName("LEVEL")
    val LEVEL: Int? = 0,
    @SerialName("DHP")
    val DHP: Int? = 0,
    @SerialName("SR")
    val SR: Int? = 0,
    @SerialName("WINS")
    val WINS: Int? = 0,
    @SerialName("WL")
    val WL: Double? = 0.0
)


@Serializable
data class Spec(
    @SerialName("DHP")
    val DHP: Int? = 0,
    @SerialName("SR")
    val SR: Int? = 0,
    @SerialName("WINS")
    val WINS: Int? = 0,
    @SerialName("WL")
    val WL: Double? = 0.0
)

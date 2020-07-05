package net.unaussprechlich.warlordsplus.stats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WarlordsSrApiResponse(
    @SerialName("data")
    val `data`: WarlordsSrApiData,
    @SerialName("success")
    val success: Boolean
)

@Serializable
data class WarlordsSrApiData(
    @SerialName("name_history")
    val nameHistory: List<NameHistory>?,
    @SerialName("playername")
    val playername: String,
    @SerialName("ranking")
    val ranking: Ranking,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("warlords_hypixel")
    val warlordsHypixel: WarlordsHypixel,
    @SerialName("warlords_sr")
    val warlordsSr: WarlordsSr
)

@Serializable
data class NameHistory(
    @SerialName("changedToAt")
    val changedToAt: Long = -1,
    @SerialName("name")
    val name: String?
)

@Serializable
data class Ranking(
    @SerialName("mage")
    val mage: MageRank,
    @SerialName("overall")
    val overall: Int?,
    @SerialName("paladin")
    val paladin: PaladinRank,
    @SerialName("shaman")
    val shaman: ShamanRank,
    @SerialName("warrior")
    val warrior: WarriorRank
)

@Serializable
data class WarlordsHypixel(
    @SerialName("aquamancer_plays")
    val aquamancerPlays: Int?,
    @SerialName("assists")
    val assists: Int?,
    @SerialName("avenger_plays")
    val avengerPlays: Int?,
    @SerialName("berserker_plays")
    val berserkerPlays: Int?,
    @SerialName("crusader_plays")
    val crusaderPlays: Int?,
    @SerialName("cryomancer_plays")
    val cryomancerPlays: Int?,
    @SerialName("damage")
    val damage: Int?,
    @SerialName("damage_aquamancer")
    val damageAquamancer: Int?,
    @SerialName("damage_avenger")
    val damageAvenger: Int?,
    @SerialName("damage_berserker")
    val damageBerserker: Int?,
    @SerialName("damage_crusader")
    val damageCrusader: Int?,
    @SerialName("damage_cryomancer")
    val damageCryomancer: Int?,
    @SerialName("damage_defender")
    val damageDefender: Int?,
    @SerialName("damage_earthwarden")
    val damageEarthwarden: Int?,
    @SerialName("damage_mage")
    val damageMage: Int?,
    @SerialName("damage_paladin")
    val damagePaladin: Int?,
    @SerialName("damage_prevented")
    val damagePrevented: Int?,
    @SerialName("damage_prevented_aquamancer")
    val damagePreventedAquamancer: Int?,
    @SerialName("damage_prevented_avenger")
    val damagePreventedAvenger: Int?,
    @SerialName("damage_prevented_berserker")
    val damagePreventedBerserker: Int?,
    @SerialName("damage_prevented_crusader")
    val damagePreventedCrusader: Int?,
    @SerialName("damage_prevented_cryomancer")
    val damagePreventedCryomancer: Int?,
    @SerialName("damage_prevented_defender")
    val damagePreventedDefender: Int?,
    @SerialName("damage_prevented_earthwarden")
    val damagePreventedEarthwarden: Int?,
    @SerialName("damage_prevented_mage")
    val damagePreventedMage: Int?,
    @SerialName("damage_prevented_paladin")
    val damagePreventedPaladin: Int?,
    @SerialName("damage_prevented_protector")
    val damagePreventedProtector: Int?,
    @SerialName("damage_prevented_pyromancer")
    val damagePreventedPyromancer: Int?,
    @SerialName("damage_prevented_revenant")
    val damagePreventedRevenant: Int?,
    @SerialName("damage_prevented_shaman")
    val damagePreventedShaman: Int?,
    @SerialName("damage_prevented_spiritguard")
    val damagePreventedSpiritguard: Int?,
    @SerialName("damage_prevented_thunderlord")
    val damagePreventedThunderlord: Int?,
    @SerialName("damage_prevented_warrior")
    val damagePreventedWarrior: Int?,
    @SerialName("damage_protector")
    val damageProtector: Int?,
    @SerialName("damage_pyromancer")
    val damagePyromancer: Int?,
    @SerialName("damage_revenant")
    val damageRevenant: Int?,
    @SerialName("damage_shaman")
    val damageShaman: Int?,
    @SerialName("damage_spiritguard")
    val damageSpiritguard: Int?,
    @SerialName("damage_taken")
    val damageTaken: Int?,
    @SerialName("damage_thunderlord")
    val damageThunderlord: Int?,
    @SerialName("damage_warrior")
    val damageWarrior: Int?,
    @SerialName("deaths")
    val deaths: Int?,
    @SerialName("defender_plays")
    val defenderPlays: Int?,
    @SerialName("earthwarden_plays")
    val earthwardenPlays: Int?,
    @SerialName("flag_conquer_self")
    val flagConquerSelf: Int?,
    @SerialName("flag_conquer_team")
    val flagConquerTeam: Int?,
    @SerialName("heal")
    val heal: Int?,
    @SerialName("heal_aquamancer")
    val healAquamancer: Int?,
    @SerialName("heal_avenger")
    val healAvenger: Int?,
    @SerialName("heal_berserker")
    val healBerserker: Int?,
    @SerialName("heal_crusader")
    val healCrusader: Int?,
    @SerialName("heal_cryomancer")
    val healCryomancer: Int?,
    @SerialName("heal_defender")
    val healDefender: Int?,
    @SerialName("heal_earthwarden")
    val healEarthwarden: Int?,
    @SerialName("heal_mage")
    val healMage: Int?,
    @SerialName("heal_paladin")
    val healPaladin: Int?,
    @SerialName("heal_protector")
    val healProtector: Int?,
    @SerialName("heal_pyromancer")
    val healPyromancer: Int?,
    @SerialName("heal_revenant")
    val healRevenant: Int?,
    @SerialName("heal_shaman")
    val healShaman: Int?,
    @SerialName("heal_spiritguard")
    val healSpiritguard: Int?,
    @SerialName("heal_thunderlord")
    val healThunderlord: Int?,
    @SerialName("heal_warrior")
    val healWarrior: Int?,
    @SerialName("kills")
    val kills: Int?,
    @SerialName("life_leeched")
    val lifeLeeched: Int?,
    @SerialName("life_leeched_berserker")
    val lifeLeechedBerserker: Int?,
    @SerialName("losses")
    val losses: Int?,
    @SerialName("losses_aquamancer")
    val lossesAquamancer: Int?,
    @SerialName("losses_avenger")
    val lossesAvenger: Int?,
    @SerialName("losses_berserker")
    val lossesBerserker: Int?,
    @SerialName("losses_crusader")
    val lossesCrusader: Int?,
    @SerialName("losses_cryomancer")
    val lossesCryomancer: Int?,
    @SerialName("losses_defender")
    val lossesDefender: Int?,
    @SerialName("losses_earthwarden")
    val lossesEarthwarden: Int?,
    @SerialName("losses_mage")
    val lossesMage: Int?,
    @SerialName("losses_paladin")
    val lossesPaladin: Int?,
    @SerialName("losses_protector")
    val lossesProtector: Int?,
    @SerialName("losses_pyromancer")
    val lossesPyromancer: Int?,
    @SerialName("losses_revenant")
    val lossesRevenant: Int?,
    @SerialName("losses_shaman")
    val lossesShaman: Int?,
    @SerialName("losses_spiritguard")
    val lossesSpiritguard: Int?,
    @SerialName("losses_thunderlord")
    val lossesThunderlord: Int?,
    @SerialName("losses_warrior")
    val lossesWarrior: Int?,
    @SerialName("mage_cooldown")
    val mageCooldown: Int?,
    @SerialName("mage_critchance")
    val mageCritchance: Int?,
    @SerialName("mage_critmultiplier")
    val mageCritmultiplier: Int?,
    @SerialName("mage_energy")
    val mageEnergy: Int?,
    @SerialName("mage_health")
    val mageHealth: Int?,
    @SerialName("mage_plays")
    val magePlays: Int?,
    @SerialName("mage_skill1")
    val mageSkill1: Int?,
    @SerialName("mage_skill2")
    val mageSkill2: Int?,
    @SerialName("mage_skill3")
    val mageSkill3: Int?,
    @SerialName("mage_skill4")
    val mageSkill4: Int?,
    @SerialName("mage_skill5")
    val mageSkill5: Int?,
    @SerialName("paladin_cooldown")
    val paladinCooldown: Int?,
    @SerialName("paladin_critchance")
    val paladinCritchance: Int?,
    @SerialName("paladin_critmultiplier")
    val paladinCritmultiplier: Int?,
    @SerialName("paladin_energy")
    val paladinEnergy: Int?,
    @SerialName("paladin_health")
    val paladinHealth: Int?,
    @SerialName("paladin_plays")
    val paladinPlays: Int?,
    @SerialName("paladin_skill1")
    val paladinSkill1: Int?,
    @SerialName("paladin_skill2")
    val paladinSkill2: Int?,
    @SerialName("paladin_skill3")
    val paladinSkill3: Int?,
    @SerialName("paladin_skill4")
    val paladinSkill4: Int?,
    @SerialName("paladin_skill5")
    val paladinSkill5: Int?,
    @SerialName("penalty")
    val penalty: Int?,
    @SerialName("protector_plays")
    val protectorPlays: Int?,
    @SerialName("pyromancer_plays")
    val pyromancerPlays: Int?,
    @SerialName("revenant_plays")
    val revenantPlays: Int?,
    @SerialName("shaman_cooldown")
    val shamanCooldown: Int?,
    @SerialName("shaman_critchance")
    val shamanCritchance: Int?,
    @SerialName("shaman_critmultiplier")
    val shamanCritmultiplier: Int?,
    @SerialName("shaman_energy")
    val shamanEnergy: Int?,
    @SerialName("shaman_health")
    val shamanHealth: Int?,
    @SerialName("shaman_plays")
    val shamanPlays: Int?,
    @SerialName("shaman_skill1")
    val shamanSkill1: Int?,
    @SerialName("shaman_skill2")
    val shamanSkill2: Int?,
    @SerialName("shaman_skill3")
    val shamanSkill3: Int?,
    @SerialName("shaman_skill4")
    val shamanSkill4: Int?,
    @SerialName("shaman_skill5")
    val shamanSkill5: Int?,
    @SerialName("spiritguard_plays")
    val spiritguardPlays: Int?,
    @SerialName("thunderlord_plays")
    val thunderlordPlays: Int?,
    @SerialName("warrior_cooldown")
    val warriorCooldown: Int?,
    @SerialName("warrior_critchance")
    val warriorCritchance: Int?,
    @SerialName("warrior_critmultiplier")
    val warriorCritmultiplier: Int?,
    @SerialName("warrior_energy")
    val warriorEnergy: Int?,
    @SerialName("warrior_health")
    val warriorHealth: Int?,
    @SerialName("warrior_plays")
    val warriorPlays: Int?,
    @SerialName("warrior_skill1")
    val warriorSkill1: Int?,
    @SerialName("warrior_skill2")
    val warriorSkill2: Int?,
    @SerialName("warrior_skill3")
    val warriorSkill3: Int?,
    @SerialName("warrior_skill4")
    val warriorSkill4: Int?,
    @SerialName("warrior_skill5")
    val warriorSkill5: Int?,
    @SerialName("wins")
    val wins: Int?,
    @SerialName("wins_aquamancer")
    val winsAquamancer: Int?,
    @SerialName("wins_avenger")
    val winsAvenger: Int?,
    @SerialName("wins_berserker")
    val winsBerserker: Int?,
    @SerialName("wins_blu")
    val winsBlu: Int?,
    @SerialName("wins_capturetheflag")
    val winsCapturetheflag: Int?,
    @SerialName("wins_capturetheflag_a")
    val winsCapturetheflagA: Int?,
    @SerialName("wins_capturetheflag_b")
    val winsCapturetheflagB: Int?,
    @SerialName("wins_capturetheflag_blu")
    val winsCapturetheflagBlu: Int?,
    @SerialName("wins_capturetheflag_red")
    val winsCapturetheflagRed: Int?,
    @SerialName("wins_crusader")
    val winsCrusader: Int?,
    @SerialName("wins_cryomancer")
    val winsCryomancer: Int?,
    @SerialName("wins_defender")
    val winsDefender: Int?,
    @SerialName("wins_domination")
    val winsDomination: Int?,
    @SerialName("wins_domination_a")
    val winsDominationA: Int?,
    @SerialName("wins_domination_b")
    val winsDominationB: Int?,
    @SerialName("wins_domination_blu")
    val winsDominationBlu: Int?,
    @SerialName("wins_domination_red")
    val winsDominationRed: Int?,
    @SerialName("wins_earthwarden")
    val winsEarthwarden: Int?,
    @SerialName("wins_mage")
    val winsMage: Int?,
    @SerialName("wins_paladin")
    val winsPaladin: Int?,
    @SerialName("wins_protector")
    val winsProtector: Int?,
    @SerialName("wins_pyromancer")
    val winsPyromancer: Int?,
    @SerialName("wins_red")
    val winsRed: Int?,
    @SerialName("wins_revenant")
    val winsRevenant: Int?,
    @SerialName("wins_shaman")
    val winsShaman: Int?,
    @SerialName("wins_spiritguard")
    val winsSpiritguard: Int?,
    @SerialName("wins_teamdeathmatch")
    val winsTeamdeathmatch: Int?,
    @SerialName("wins_teamdeathmatch_a")
    val winsTeamdeathmatchA: Int?,
    @SerialName("wins_teamdeathmatch_b")
    val winsTeamdeathmatchB: Int?,
    @SerialName("wins_teamdeathmatch_blu")
    val winsTeamdeathmatchBlu: Int?,
    @SerialName("wins_teamdeathmatch_red")
    val winsTeamdeathmatchRed: Int?,
    @SerialName("wins_thunderlord")
    val winsThunderlord: Int?,
    @SerialName("wins_warrior")
    val winsWarrior: Int?
)

@Serializable
data class WarlordsSr(
    @SerialName("DHP")
    val dHP: Int?,
    @SerialName("KD")
    val kD: Double?,
    @SerialName("KDA")
    val kDA: Double?,
    @SerialName("mage")
    val mage: Mage?,
    @SerialName("paladin")
    val paladin: Paladin?,
    @SerialName("plays")
    val plays: Int?,
    @SerialName("SR")
    val sR: Int?,
    @SerialName("shaman")
    val shaman: Shaman?,
    @SerialName("WL")
    val wL: Double?,
    @SerialName("warrior")
    val warrior: Warrior?
)

@Serializable
data class MageRank(
    @SerialName("aquamancer")
    val aquamancer: Int?,
    @SerialName("cryomancer")
    val cryomancer: Int?,
    @SerialName("overall")
    val overall: Int?,
    @SerialName("pyromancer")
    val pyromancer: Int?
)

@Serializable
data class PaladinRank(
    @SerialName("avenger")
    val avenger: Int?,
    @SerialName("crusader")
    val crusader: Int?,
    @SerialName("overall")
    val overall: Int?,
    @SerialName("protector")
    val protector: Int?
)

@Serializable
data class ShamanRank(
    @SerialName("earthwarden")
    val earthwarden: Int?,
    @SerialName("overall")
    val overall: Int?,
    @SerialName("spiritguard")
    val spiritguard: Int?,
    @SerialName("thunderlord")
    val thunderlord: Int?
)

@Serializable
data class WarriorRank(
    @SerialName("berserker")
    val berserker: Int?,
    @SerialName("defender")
    val defender: Int?,
    @SerialName("overall")
    val overall: Int?,
    @SerialName("revenant")
    val revenant: Int?
)

@Serializable
data class Mage(
    @SerialName("aquamancer")
    val aquamancer: Spec?,
    @SerialName("cryomancer")
    val cryomancer: Spec?,
    @SerialName("pyromancer")
    val pyromancer: Spec?,
    @SerialName("LEVEL")
    val LEVEL: Int?,
    @SerialName("DHP")
    val DHP: Int?,
    @SerialName("SR")
    val SR: Int?,
    @SerialName("WINS")
    val WINS: Int?,
    @SerialName("WL")
    val WL: Double?
)

@Serializable
data class Paladin(
    @SerialName("avenger")
    val avenger: Spec?,
    @SerialName("crusader")
    val crusader: Spec?,
    @SerialName("protector")
    val protector: Spec?,
    @SerialName("LEVEL")
    val LEVEL: Int?,
    @SerialName("DHP")
    val DHP: Int?,
    @SerialName("SR")
    val SR: Int?,
    @SerialName("WINS")
    val WINS: Int?,
    @SerialName("WL")
    val WL: Double?
)

@Serializable
data class Shaman(
    @SerialName("earthwarden")
    val earthwarden: Spec?,
    @SerialName("spiritguard")
    val spiritguard: Spec?,
    @SerialName("thunderlord")
    val thunderlord: Spec?,
    @SerialName("LEVEL")
    val LEVEL: Int?,
    @SerialName("DHP")
    val DHP: Int?,
    @SerialName("SR")
    val SR: Int?,
    @SerialName("WINS")
    val WINS: Int?,
    @SerialName("WL")
    val WL: Double?
)

@Serializable
data class Warrior(
    @SerialName("berserker")
    val berserker: Spec?,
    @SerialName("revenant")
    val revenant: Spec?,
    @SerialName("defender")
    val defender: Spec?,
    @SerialName("LEVEL")
    val LEVEL: Int?,
    @SerialName("DHP")
    val DHP: Int?,
    @SerialName("SR")
    val SR: Int?,
    @SerialName("WINS")
    val WINS: Int?,
    @SerialName("WL")
    val WL: Double?
)


@Serializable
data class Spec(
    @SerialName("DHP")
    val DHP: Int?,
    @SerialName("SR")
    val SR: Int?,
    @SerialName("WINS")
    val WINS: Int?,
    @SerialName("WL")
    val WL: Double?
)

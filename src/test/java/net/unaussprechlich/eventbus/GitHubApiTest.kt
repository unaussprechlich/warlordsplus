//package net.unaussprechlich.eventbus
//
//import kotlinx.coroutines.runBlocking
//import net.unaussprechlich.http.HttpModule
//import org.junit.Test
//import kotlin.reflect.jvm.internal.impl.metadata.deserialization.VersionRequirement
//import kotlin.test.assertEquals
//
//
//class GitHubApiTest {
//
//
//
//    @Test
//    fun warlordsSrApiTest() {
//        runBlocking {
//            val SNAPSHOT_PREFIX = "SNAPSHOT_"
//            val RELEASE_PREFIX = "V"
//
//            val MOCK_VERSION = "V1.0.0"
//
//            try {
//                val result = HttpModule.GitHubApi.getReleases("unaussprechlich", "warlordsplus")
//
//                if(MOCK_VERSION.startsWith(SNAPSHOT_PREFIX)){
//                    val snapshots = result.filter { it.name.startsWith(SNAPSHOT_PREFIX) }
//                    val latestSnapshotNumber = snapshots.map { it.name.replace(SNAPSHOT_PREFIX, "")}.map { it.toInt() }.maxOrNull()
//
//                    if(MOCK_VERSION != SNAPSHOT_PREFIX + latestSnapshotNumber!! ){
//                        val latestSnapshot = snapshots.firstOrNull { it.name == SNAPSHOT_PREFIX + latestSnapshotNumber }
//                        val latestAsset = latestSnapshot?.assets?.firstOrNull{it.name == "WarlordsPlus-$SNAPSHOT_PREFIX$latestSnapshotNumber.jar"}
//
//                        println("[WarlordsPlus] New Snapshot release available: ${latestSnapshot?.name} | ${latestAsset?.browser_download_url}" )
//                    }
//
//                }else if(MOCK_VERSION.startsWith(RELEASE_PREFIX)){
//                    val releases = result.filter { it.name.startsWith(RELEASE_PREFIX) }
//
//                    data class SemVersion(val version : String){
//
//                        fun toInt(): Int {
//                            return version.split(".").let {
//                                it[0].toInt().shl(16) + it[1].toInt().shl(8) + it[2].toInt()
//                            }
//                        }
//
//                        override fun toString() : String{
//                            return version
//                        }
//
//                    }
//
//                    val latestReleaseNumber = releases
//                        .map { SemVersion(it.name.replace("V", "")) }
//                        .maxByOrNull { it.toInt() }
//
//                    if(MOCK_VERSION != RELEASE_PREFIX + latestReleaseNumber!! ){
//                        val latestRelease = releases.firstOrNull { it.name == RELEASE_PREFIX + latestReleaseNumber }
//                        val latestAsset = latestRelease?.assets?.firstOrNull{it.name == "WarlordsPlus-$RELEASE_PREFIX$latestReleaseNumber.jar"}
//
//                        println("[WarlordsPlus] New Release available: ${latestRelease?.name} | ${latestAsset?.browser_download_url}" )
//                    }
//
//                }
//            }catch (e : Error){
//                System.err.println("WarlordsPlus failed to pull the update information from GitHub.")
//                e.printStackTrace()
//            }
//
//
//
//            assertEquals(true, true)
//        }
//    }
//}
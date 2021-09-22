package net.unaussprechlich.warlordsplus.module.modules

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.ForgeEventProcessor
import net.unaussprechlich.http.HttpModule
import net.unaussprechlich.warlordsplus.WarlordsPlus
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.NotificationManager
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.notifications.ErrorNotification
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.notifications.UpdateModNotification

object UpdateChecker : IModule{

    const val SNAPSHOT_PREFIX = "SNAPSHOT_"
    const val RELEASE_PREFIX = "V"

    init {

        EventBus.register<WarlordsLeaveAndJoinEvent> { event ->
            if (event.isWarlords && !WarlordsPlus.IS_DEBUGGING) {

                CoroutineScope(Dispatchers.IO).launch {

                    val currentVersion =  WarlordsPlus.getModVersion()

                    try {
                        //Get the last 100 releases from GitHub
                        val result = HttpModule.GitHubApi.getReleases("unaussprechlich", "warlordsplus")

                        //If the user is running the snapshot branch we notify him about a new snapshot release
                        if(currentVersion.startsWith(SNAPSHOT_PREFIX)){
                            val snapshots = result.filter { it.name.startsWith(SNAPSHOT_PREFIX) }
                            val latestSnapshotNumber = snapshots.map { it.name.replace(SNAPSHOT_PREFIX, "")}.map { it.toInt() }.maxOrNull()

                            //We just compare if their version is the latest. They should always run the latest version!!!!!!
                            if(currentVersion != SNAPSHOT_PREFIX + latestSnapshotNumber!! ){
                                val latestSnapshot = snapshots.firstOrNull { it.name == SNAPSHOT_PREFIX + latestSnapshotNumber }
                                val latestAsset = latestSnapshot!!.assets.firstOrNull{it.name == "WarlordsPlus-$SNAPSHOT_PREFIX$latestSnapshotNumber.jar"}

                                NotificationManager.add(UpdateModNotification(latestSnapshot.name, latestAsset!!.browser_download_url))

                                println("[WarlordsPlus] New Snapshot release available: ${latestSnapshot.name} | ${latestAsset.browser_download_url}" )
                            }

                        //If the user is running the release branch we notify him about a new release
                        }else if(currentVersion.startsWith(RELEASE_PREFIX)){
                            val releases = result.filter { it.name.startsWith(RELEASE_PREFIX) }

                            data class SemVersion(val version : String){

                                /**
                                 * Use some bit shifting to parse the semversion into one unique number
                                 */
                                fun toInt(): Int {
                                    return version.split(".").let {
                                        it[0].toInt().shl(16) + it[1].toInt().shl(8) + it[2].toInt()
                                    }
                                }

                                override fun toString() : String{
                                    return version
                                }

                            }

                            val latestReleaseNumber = releases
                                .map { SemVersion(it.name.replace("V", "")) }
                                .maxByOrNull { it.toInt() }

                            //We just compare if their version is the latest. They should always run the latest version!!!!!!
                            if(currentVersion != RELEASE_PREFIX + latestReleaseNumber.toString() ){
                                val latestRelease = releases.firstOrNull { it.name == RELEASE_PREFIX + latestReleaseNumber.toString() }
                                val latestAsset = latestRelease!!.assets.firstOrNull{it.name == "WarlordsPlus-${latestRelease.name}.jar"}

                                NotificationManager.add(UpdateModNotification(latestRelease.name, latestAsset!!.browser_download_url))

                                println("[WarlordsPlus] New Release available: ${latestRelease.name} | ${latestAsset.browser_download_url}" )
                            }

                        }
                    }catch (e : Error){
                        System.err.println("WarlordsPlus failed to pull the update information from GitHub.")
                        e.printStackTrace()
                        NotificationManager.add(ErrorNotification("Failed to check for updates",
                            "WarlordsPlus has failed to pull information about the latest release from GitHub. " +
                                    "Please check manually at https://github.com/unaussprechlich/warlordsplus/releases."))
                    }
                }
            }
        }
    }
}
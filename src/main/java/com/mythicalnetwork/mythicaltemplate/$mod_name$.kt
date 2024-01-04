package com.mythicalnetwork.mythicaltemplate

import com.mojang.logging.LogUtils
import net.fabricmc.api.ModInitializer


/**
 * With Kotlin, the Entrypoint can be defined in numerous ways. This is showcased on Fabrics' Github:
 * https://github.com/FabricMC/fabric-language-kotlin#entrypoint-samples
 */
class $mod_name$ : ModInitializer {

    companion object {
        const val MODID = "$mod_id$"
        val LOGGER = LogUtils.getLogger()
        val CONFIG: $mod_name$Config = $mod_name$Config.createAndLoad()
        fun sendDebugMessage(message: String) {
            if (CONFIG.sendDebugLogs()){
                LOGGER.info(message)
            }
        }
    }

    override fun onInitialize() {
    }
}
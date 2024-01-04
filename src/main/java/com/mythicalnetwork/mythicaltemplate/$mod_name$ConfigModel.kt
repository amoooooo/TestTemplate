package com.mythicalnetwork.mythicaltemplate

import io.wispforest.owo.config.annotation.Config
import io.wispforest.owo.config.annotation.Modmenu

@Modmenu(modId = "$mod_id$")
@Config(name = "$mod_id$", wrapperName = "$mod_name$Config")
class $mod_name$ConfigModel {
    @JvmField
    var sendDebugLogs: Boolean = false
}
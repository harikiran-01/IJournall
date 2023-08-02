package com.hk.ijournal.features.search.views

import omni.platform.android.components.android.BaseActivityConfig
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class SearchActivityConfig : ReadOnlyProperty<Any?, BaseActivityConfig> {
    private var config: BaseActivityConfig = BaseActivityConfig()

    override fun getValue(thisRef: Any?, property: KProperty<*>): BaseActivityConfig {
        return config
    }
}
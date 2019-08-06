package com.glureau.poc.view_state.common

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes

// Hide the context behind an interface so we can work with real strings resolution inside the VM
interface StringResolver {
    fun getString(@StringRes res: Int): String // Same signature than context/resources
}

// Hide the context behind an interface so we can work with real color resolution inside the VM
interface ColorResolver {
    fun getColor(@ColorRes res: Int): Int // Same signature than context/resources
}

// Hide the context behind an interface so we can work with real dimension resolution inside the VM
interface DimensionResolver {
    fun getDimension(@DimenRes res: Int): Float // Same signature than context/resources
}

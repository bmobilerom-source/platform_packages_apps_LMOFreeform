package com.libremobileos.sidebar.utils

import android.content.Context
import android.provider.Settings
import android.text.TextUtils

/**
 * Packages listed in [Settings.Secure.HIDE_APPLIST] must not appear in the
 * LMO freeform sidebar (picker, all-apps grid, or predicted apps).
 *
 * Launcher Trust uses the same setting; PackageManager's hide-list filter
 * skips system apps, so sidebar must enforce this itself.
 */
fun Context.getSidebarHiddenPackages(): Set<String> {
    val raw = try {
        Settings.Secure.getString(contentResolver, Settings.Secure.HIDE_APPLIST)
    } catch (_: Exception) {
        null
    }
    if (TextUtils.isEmpty(raw) || raw == ",") {
        return emptySet()
    }
    return raw!!.split(',')
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
}

fun Context.isSidebarPackageHidden(packageName: String?): Boolean {
    if (packageName.isNullOrEmpty()) return false
    return getSidebarHiddenPackages().contains(packageName)
}

package com.shawnstudio.shawncut

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

internal object AppLocale {
    private const val PREFS = "app_settings"
    private const val LANGUAGE = "language"

    fun wrap(context: Context): Context {
        if (Build.VERSION.SDK_INT >= 33) return context
        val language = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(LANGUAGE, "").orEmpty()
        if (language.isEmpty()) return context
        val configuration = Configuration(context.resources.configuration).apply {
            setLocales(LocaleList(Locale.forLanguageTag(language)))
        }
        return context.createConfigurationContext(configuration)
    }

    fun selected(context: Context): String = if (Build.VERSION.SDK_INT >= 33) {
        context.getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
    } else {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(LANGUAGE, "").orEmpty()
    }

    fun text(context: Context, id: Int, vararg args: Any): String = wrap(context).getString(id, *args)

    fun set(context: Context, language: String) {
        if (Build.VERSION.SDK_INT >= 33) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                if (language.isEmpty()) LocaleList.getEmptyLocaleList() else LocaleList.forLanguageTags(language)
        } else {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(LANGUAGE, language).apply()
        }
    }
}

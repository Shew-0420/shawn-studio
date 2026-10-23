package com.shawnstudio.shawncut.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.shawnstudio.shawncut.ui.editor.EditorScreen
import com.shawnstudio.shawncut.ui.export.ExportScreen
import com.shawnstudio.shawncut.ui.home.AboutScreen
import com.shawnstudio.shawncut.ui.home.HomeScreen
import com.shawnstudio.shawncut.ui.home.LicencesScreen
import com.shawnstudio.shawncut.ui.home.SettingsScreen

/** The app: the current screen, and messages along the bottom. */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppRoot(vm: AppViewModel) {
    val snackbars = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        for (m in vm.messages) snackbars.showSnackbar(m)
    }
    // test tags double as resource ids, so on-device checks can find controls by name
    Box(Modifier.fillMaxSize().background(Fc.Black).semantics { testTagsAsResourceId = true }) {
        if (vm.screen in setOf(Screen.Home, Screen.Settings)) {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f).fillMaxWidth()) {
                    when (vm.screen) {
                        Screen.Home -> HomeScreen(vm)
                        Screen.Settings -> SettingsScreen(vm)
                        else -> Unit
                    }
                }
                MainNavigationBar(vm)
            }
        } else {
            val ed = vm.editor
            when (vm.screen) {
                Screen.Editor -> if (ed != null) EditorScreen(ed) else HomeScreen(vm)
                Screen.Export -> ExportScreen(vm)
                Screen.About -> AboutScreen(vm)
                Screen.Licences -> LicencesScreen(vm)
                else -> Unit
            }
        }
        SnackbarHost(snackbars, Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 96.dp)) { data ->
            Snackbar(shape = RoundedCornerShape(8.dp), containerColor = Fc.Raised, contentColor = Fc.Title) {
                Text(data.visuals.message, style = FcType.BodySmall, color = Fc.Title)
            }
        }
    }
}

@Composable
private fun MainNavigationBar(vm: AppViewModel) {
    NavigationBar(containerColor = Fc.Panel, contentColor = Fc.Title) {
        listOf(
            Triple(Screen.Home, "Home", LucideIcons.House.glyph()),
            Triple(Screen.Edit, "Edit", LucideIcons.Scissors.glyph()),
            Triple(Screen.Settings, "Settings", LucideIcons.Settings.glyph()),
        ).forEach { (screen, label, icon) ->
            NavigationBarItem(
                selected = vm.screen == screen,
                onClick = {
                    if (screen == Screen.Edit) vm.edit()
                    else vm.show(screen)
                },
                icon = { GlyphIcon(icon, if (vm.screen == screen) Fc.Accent else Fc.Detail, description = label) },
                label = { Text(label, style = FcType.ToolLabel) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Fc.Accent,
                    selectedTextColor = Fc.Title,
                    indicatorColor = Fc.Accent.copy(alpha = 0.16f),
                    unselectedIconColor = Fc.Detail,
                    unselectedTextColor = Fc.Detail,
                ),
            )
        }
    }
}

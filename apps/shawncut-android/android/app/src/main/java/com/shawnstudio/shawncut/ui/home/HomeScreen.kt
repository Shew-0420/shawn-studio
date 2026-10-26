package com.shawnstudio.shawncut.ui.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shawnstudio.shawncut.R
import com.shawnstudio.shawncut.engine.ProjectEntry
import com.shawnstudio.shawncut.ui.AppViewModel
import com.shawnstudio.shawncut.ui.Fc
import com.shawnstudio.shawncut.ui.FcDialog
import com.shawnstudio.shawncut.ui.FcIconButton
import com.shawnstudio.shawncut.ui.FcType
import com.shawnstudio.shawncut.ui.Glyph
import com.shawnstudio.shawncut.ui.GlyphIcon
import com.shawnstudio.shawncut.ui.LucideIcons
import com.shawnstudio.shawncut.ui.NewProject
import com.shawnstudio.shawncut.ui.Screen
import com.shawnstudio.shawncut.ui.glyph
import com.shawnstudio.shawncut.ui.editor.TPS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Home: ways to start a project, the projects, and the + button. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(vm: AppViewModel) {
    val context = LocalContext.current
    Box(Modifier.fillMaxSize().background(Fc.Black).windowInsetsPadding(WindowInsets.displayCutout.union(WindowInsets.navigationBars).only(WindowInsetsSides.Horizontal)).testTag("screen_home")) {
        Column(Modifier.fillMaxSize().statusBarsPadding().verticalScroll(rememberScrollState())) {
            HomeTopBar(vm)
            FlowRow(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 4,
            ) {
                QuickTile(LucideIcons.Images.glyph(), stringResource(R.string.home_photo_library), "from_your_photo_library") { vm.newProject(NewProject.PhotoLibrary) }
                QuickTile(LucideIcons.FilePlus.glyph(), stringResource(R.string.home_new_from_files), "new_from_files") { vm.newProject(NewProject.Files) }
                QuickTile(LucideIcons.SquarePlus.glyph(), stringResource(R.string.home_new_blank_project), "new_blank_project") { vm.newProject(NewProject.Blank) }
                QuickTile(LucideIcons.AudioLines.glyph(), stringResource(R.string.home_extract_audio), "extract_audio_button") { vm.newProject(NewProject.ExtractAudio) }
            }
            if (vm.projectList.isNotEmpty()) {
                Text(stringResource(R.string.home_your_projects), style = FcType.Header, color = Fc.Title, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp).testTag("home_your_projects_header"))
                LazyRow(
                    Modifier.fillMaxWidth().testTag("home_projects_list"),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(vm.projectList, key = { it.id }) { p -> ProjectCard(vm, p) }
                }
                Spacer(Modifier.height(32.dp))
            } else {
                Text(stringResource(R.string.home_no_projects), style = FcType.Body, color = Fc.Secondary, modifier = Modifier.padding(horizontal = 16.dp).testTag("home_projects_empty"))
            }
            vm.engineProblem?.let {
                Text(stringResource(R.string.home_editor_start_error, it), style = FcType.Body, color = Fc.Negative, modifier = Modifier.padding(16.dp))
            }
            Spacer(Modifier.height(124.dp))
        }
        // the + button
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp)
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Fc.Accent)
                .clickable(role = Role.Button) { vm.newProject(NewProject.PhotoLibrary) }
                .testTag("new_project")
                .semantics { contentDescription = context.getString(R.string.home_new_project) },
            contentAlignment = Alignment.Center,
        ) {
            GlyphIcon(LucideIcons.Plus.glyph(), Color.White, size = 26.dp)
        }
        if (vm.opening) {
            Box(Modifier.fillMaxSize().background(Fc.Scrim), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Fc.Accent) }
        }
    }
    vm.renaming?.let { RenameDialog(vm, it) }
    vm.deleting?.let { p ->
        FcDialog(stringResource(R.string.project_delete_title, p.name), stringResource(R.string.project_delete), { vm.delete(p) }, { vm.deleting = null }, destructive = true) {
            Text(stringResource(R.string.home_project_delete_notice), style = FcType.Body, color = Fc.Body)
        }
    }
}

@Composable
private fun HomeTopBar(vm: AppViewModel) {
    var menu by remember { mutableStateOf(false) }
    Row(Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 4.dp).testTag("home_screen_top_bar"), verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(R.drawable.shawn_cut_brand_mark), contentDescription = stringResource(R.string.accessibility_shawn_cut), modifier = Modifier.width(136.dp).height(64.dp))
        Spacer(Modifier.weight(1f))
        Box {
            FcIconButton(LucideIcons.Ellipsis.glyph(), stringResource(R.string.more_options), { menu = true }, size = 48.dp, modifier = Modifier.testTag("more_options"))
            DropdownMenu(menu, { menu = false }, containerColor = Fc.Panel, shape = RoundedCornerShape(12.dp)) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.settings), style = FcType.Label, color = Fc.Body) },
                    leadingIcon = { GlyphIcon(LucideIcons.Settings.glyph(), Fc.Icon, size = 20.dp) },
                    onClick = { menu = false; vm.show(Screen.Settings) },
                )
            }
        }
    }
}

@Composable
private fun QuickTile(glyph: Glyph, label: String, tag: String, onClick: () -> Unit) {
    Column(
        Modifier
            .width(94.dp)
            .clickable(role = Role.Button, onClick = onClick)
            .testTag(tag),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(16.dp)).background(Fc.Layer1), contentAlignment = Alignment.Center) {
            GlyphIcon(glyph, Fc.Icon)
        }
        Spacer(Modifier.height(8.dp))
        Text(label, style = FcType.ToolLabel, color = Fc.Body, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

/** A project's length on its card: whole seconds rounded up (":07", "1:05", "1:02:03"). */
fun cardDuration(ticks: Long): String {
    val s = (ticks + TPS - 1) / TPS
    val h = s / 3600
    val m = (s / 60) % 60
    val sec = s % 60
    return when {
        h > 0 -> "%d:%02d:%02d".format(h, m, sec)
        m > 0 -> "%d:%02d".format(m, sec)
        else -> ":%02d".format(sec)
    }
}

@Composable
private fun ProjectCard(vm: AppViewModel, p: ProjectEntry) {
    val context = LocalContext.current
    var menu by remember { mutableStateOf(false) }
    val poster by produceState<ImageBitmap?>(null, p.id, p.opened) {
        value = withContext(Dispatchers.IO) { runCatching { BitmapFactory.decodeFile(p.poster.absolutePath)?.asImageBitmap() }.getOrNull() }
    }
    Column(Modifier.width(100.dp)) {
        Box(
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Fc.Raised)
                .border(1.dp, Fc.Hairline, RoundedCornerShape(12.dp))
                .clickable(role = Role.Button) { vm.openProject(p) }
                .testTag("home_project_${p.id}"),
        ) {
            poster?.let { Image(it, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop) }
            Box(
                Modifier.align(Alignment.TopEnd).padding(6.dp).clip(CircleShape).background(Fc.Layer1.copy(alpha = 0.7f)).padding(horizontal = 6.dp, vertical = 1.dp),
            ) {
                Text(cardDuration(p.duration), style = FcType.Badge, color = Fc.Title)
            }
            Box(Modifier.align(Alignment.BottomEnd).padding(4.dp)) {
                Box(
                    Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Fc.Layer1.copy(alpha = 0.7f))
                        .clickable(role = Role.Button) { menu = true }
                        .semantics { contentDescription = context.getString(R.string.more_options) },
                    contentAlignment = Alignment.Center,
                ) {
                    GlyphIcon(LucideIcons.Ellipsis.glyph(), Fc.Title, size = 18.dp)
                }
                DropdownMenu(menu, { menu = false }, containerColor = Fc.Panel, shape = RoundedCornerShape(12.dp)) {
                    DropdownMenuItem(text = { Text(stringResource(R.string.project_rename), style = FcType.Label, color = Fc.Body) }, leadingIcon = { GlyphIcon(LucideIcons.Pencil.glyph(), Fc.Icon, size = 20.dp) }, onClick = { menu = false; vm.renaming = p })
                    DropdownMenuItem(text = { Text(stringResource(R.string.project_duplicate), style = FcType.Label, color = Fc.Body) }, leadingIcon = { GlyphIcon(LucideIcons.CopyPlus.glyph(), Fc.Icon, size = 20.dp) }, onClick = { menu = false; vm.duplicate(p) })
                    DropdownMenuItem(text = { Text(stringResource(R.string.project_delete), style = FcType.Label, color = Fc.Negative) }, leadingIcon = { GlyphIcon(LucideIcons.Trash2.glyph(), Fc.Negative, size = 20.dp) }, onClick = { menu = false; vm.deleting = p })
                }
            }
        }
        Spacer(Modifier.height(5.dp))
        Text(p.name, style = FcType.Caption, color = Fc.Detail, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun RenameDialog(vm: AppViewModel, p: ProjectEntry) {
    var name by remember(p.id) { mutableStateOf(p.name) }
    val ok = name.trim().isNotEmpty() && name.trim() != p.name
    FcDialog(stringResource(R.string.project_rename_title), stringResource(R.string.project_save), { if (ok) vm.rename(p, name) }, { vm.renaming = null }, confirmEnabled = ok) {
        Text(stringResource(R.string.project_name), style = FcType.Caption, color = Fc.Detail)
        Spacer(Modifier.height(8.dp))
        androidx.compose.foundation.text.BasicTextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            textStyle = FcType.Body.copy(color = Fc.Title),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Fc.Title),
            modifier = Modifier.fillMaxWidth().border(1.dp, Fc.Divider, RoundedCornerShape(12.dp)).padding(horizontal = 14.dp, vertical = 12.dp),
        )
    }
}

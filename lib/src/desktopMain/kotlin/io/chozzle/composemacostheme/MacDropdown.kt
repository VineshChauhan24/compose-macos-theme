package io.chozzle.composemacostheme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.chozzle.composemacostheme.modifiedofficial.MacDropdownMenu
import io.chozzle.composemacostheme.modifiedofficial.MacDropdownMenuItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MacDropdownMenu(
    menuItems: List<String>,
    selectedIndex: Int,
    onItemSelected: (selectedIndex: Int) -> Unit,
    toggleModifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier
) {
    val longestItem = menuItems.maxByOrNull { it.length }.orEmpty()
    var showMenu by remember { mutableStateOf(false) }
    val heightOfItem = with(AmbientDensity.current) { FontSize.toDp() + MenuItemPadding * 2 + FontPadding * 2 }
    MacDropdownMenu(
        toggle = {
            DropdownToggle(
                longestItem,
                FontSize,
                menuItems,
                selectedIndex,
                onClick = { showMenu = true }
            )
        },
        toggleModifier = toggleModifier,
        dropdownModifier = dropdownModifier,
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
        dropdownOffset = DpOffset(0.dp, -(heightOfItem * (selectedIndex + 1)))
    ) {
        menuItems.forEachIndexed { index, itemString ->
            var isMouseHovering by remember { mutableStateOf(false) }
            MacDropdownMenuItem(
                onClick = {
                    showMenu = false
                    onItemSelected(index)
                },
                modifier = Modifier
                    .pointerMoveFilter(
                        onEnter = {
                            isMouseHovering = true
                            false
                        },
                        onExit = {
                            isMouseHovering = false
                            false
                        }
                    ).padding(horizontal = 6.dp)
                    .background(
                        if (isMouseHovering) MacTheme.colors.primary.copy(alpha = 0.7f) else Color.Unspecified,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Row(
                    Modifier.fillMaxHeight().padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val checkOrSpace = if (index == selectedIndex) "\uDBC0\uDD85" else "     "

                    Text(
                        text = checkOrSpace,
                        Modifier.padding(end = MenuItemPadding),
                        color = if (isMouseHovering) Color.White else Color.Unspecified,
                        fontSize = FontSize - 3.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Box(contentAlignment = Alignment.CenterStart) {
                        // Invisible text just to calculate potential space taken by longest item
                        Text(
                            text = longestItem,
                            color = Color.Transparent,
                            style = AmbientTextStyle.current.copy(fontSize = FontSize)
                        )

                        Text(
                            text = itemString,
                            color = if (isMouseHovering) Color.White else Color.Unspecified,
                            style = AmbientTextStyle.current.copy(fontSize = FontSize)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DropdownToggle(
    longestItem: String,
    FontSize: TextUnit,
    menuItems: List<String>,
    selectedIndex: Int,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        elevation = 2.dp,
        border = BorderStroke(Dp.Hairline, MacTheme.colors.border)
    ) {
        Row(
            modifier = Modifier.clickable(
                onClick = onClick,
                interactionState = remember { InteractionState() },
                indication = null
            ).padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.padding(horizontal = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    longestItem,
                    fontSize = FontSize,
                    color = Color.Transparent
                )
                Text(
                    menuItems[selectedIndex],
                    fontSize = FontSize
                )
            }
            DisclosureDoubleArrow()
        }
    }
}

@Composable
private fun DisclosureDoubleArrow() {
    Surface(
        modifier = Modifier.size(16.dp),
        color = MaterialTheme.colors.primary,
        shape = MaterialTheme.shapes.small
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Unfortunately lineHeight doesn't seem to respect small enough values so used offsets
            Text(
                "\uDBC0\uDD87",
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = (-3).dp)
            )
            Text(
                "\uDBC0\uDD88",
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = 3.dp)
            )
        }
    }
}

private val FontSize = 13.sp
private val FontPadding = 1.5.dp
private val MenuItemPadding = 3.dp



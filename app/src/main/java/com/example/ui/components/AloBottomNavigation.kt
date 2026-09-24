package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AloTab
import com.example.ui.theme.PremiumPurple
import com.example.ui.theme.PremiumPurpleContainer

data class NavItem(
    val tab: AloTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun AloBottomNavigation(
    currentTab: AloTab,
    onTabSelected: (AloTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(
            tab = AloTab.CURRICULUM,
            label = "হোম ও বই",
            selectedIcon = Icons.Filled.AutoStories,
            unselectedIcon = Icons.Filled.MenuBook,
            testTag = "nav_curriculum"
        ),
        NavItem(
            tab = AloTab.CHAT,
            label = "শিক্ষক",
            selectedIcon = Icons.Filled.Chat,
            unselectedIcon = Icons.Outlined.Chat,
            testTag = "nav_chat"
        ),
        NavItem(
            tab = AloTab.FORMULAS,
            label = "সূত্র",
            selectedIcon = Icons.Filled.Calculate,
            unselectedIcon = Icons.Outlined.Calculate,
            testTag = "nav_formulas"
        ),
        NavItem(
            tab = AloTab.QUIZ,
            label = "কুইজ",
            selectedIcon = Icons.Filled.Quiz,
            unselectedIcon = Icons.Outlined.Quiz,
            testTag = "nav_quiz"
        ),
        NavItem(
            tab = AloTab.NOTES,
            label = "নোট",
            selectedIcon = Icons.Filled.MenuBook,
            unselectedIcon = Icons.Outlined.MenuBook,
            testTag = "nav_notes"
        )
    )

    NavigationBar(
        windowInsets = WindowInsets.navigationBars,
        tonalElevation = 0.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
                clip = false
            )
            .padding(horizontal = 4.dp)
            .testTag("alo_bottom_navigation")
    ) {
        items.forEach { item ->
            val isSelected = currentTab == item.tab
            NavigationBarItem(
                modifier = Modifier.testTag(item.testTag),
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = if (item.tab == AloTab.CHAT) "আলো শিক্ষক, পূর্ণ পর্দার চ্যাট" else item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        softWrap = false
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PremiumPurple,
                    selectedTextColor = PremiumPurple,
                    indicatorColor = PremiumPurpleContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

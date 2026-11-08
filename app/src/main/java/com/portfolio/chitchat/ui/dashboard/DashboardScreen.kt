package com.portfolio.chitchat.ui.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onChatClicked: (String) -> Unit,
    onStatusClicked: (String) -> Unit,
    onCallClicked: (String) -> Unit,
    onFabClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    val tabs = listOf("CHATS", "STATUS", "CALLS")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "ChitChat", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White) },
            actions = {
                IconButton(onClick = { }) { Icon(Icons.Outlined.PhotoCamera, contentDescription = "Camera", tint = Color.White) }
                IconButton(onClick = { }) { Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White) }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White)
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showMenu = false
                                onSettingsClicked()
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsappTealGreen)
        )

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = WhatsappTealGreen,
            contentColor = Color.White,
            indicator = { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color.White
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (pagerState.currentPage == index) Color.White else Color.White.copy(alpha = 0.7f)
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) { page ->
            when (page) {
                0 -> ChatsTabScreen(onChatClicked = onChatClicked, onFabClicked = onFabClicked)
                1 -> StatusTabScreen(onStatusClicked = onStatusClicked)
                2 -> CallsTabScreen(onCallClicked = onCallClicked)
            }
        }
    }
}

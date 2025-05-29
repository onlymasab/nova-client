package com.paandaaa.nova.android.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.paandaaa.nova.android.domain.model.OnboardingModel
import com.paandaaa.nova.android.ui.navigation.Screen
import com.paandaaa.nova.android.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(navController: NavHostController, onComplete: () -> Unit) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val currentPageIndex by viewModel.currentPage.collectAsState()
    val page = viewModel.pages[currentPageIndex]
    val totalPages = viewModel.totalPages
    val isLastPage = currentPageIndex == totalPages - 1

    Scaffold (
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            // Top content
            OnboardingPage(model = page)

            // Bottom Controls
            Column (
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                    if (!isLastPage) {
                        TextButton(
                            onClick = viewModel::skip,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 24.dp)
                        ) {
                            Text(
                                text = "Skip",
                                style = TextStyle (
                                    color = Color.Black
                                )
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                // Pagination indicators
                Column (
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(totalPages) { index ->
                            val color = if (index == currentPageIndex)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(10.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .background(color)
                            )
                        }
                    }

                    // Navigation Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = viewModel::prevPage,
                            enabled = currentPageIndex > 0
                        ) {
                            Text(
                                text = "Back",
                                style = TextStyle(
                                    color = Color.Black
                                )
                            )
                        }

                        if (isLastPage) {
                            Button(onClick = {
                                navController.navigate(Screen.Auth.route) {
                                    popUpTo(Screen.Onboarding.route) {
                                        inclusive = true
                                    }
                                }
                            }) {
                                Text("Done")
                            }
                        } else {
                            Button(onClick = viewModel::nextPage) {
                                Text("Next")
                            }
                        }
                    }
                }


            }
        }
    }
}

@Composable
fun OnboardingPage(model: OnboardingModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),

    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = model.title,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = model.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = model.imageRes),
            contentDescription = "Onboarding Cover Image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
        )

    }
}
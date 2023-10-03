package com.example.football.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.view.composable.FixtureStatisticsScreen

class FixtureStatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FootballTheme {
                    FixtureStatisticsScreen()
                }
            }
        }
    }
}
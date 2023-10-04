package com.example.football.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.view.composable.FixtureStatisticsScreen

class FixtureStatisticsFragment : Fragment() {

    val navArgs by lazy {
        FixtureStatisticsFragmentArgs.fromBundle(requireArguments())
    }
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
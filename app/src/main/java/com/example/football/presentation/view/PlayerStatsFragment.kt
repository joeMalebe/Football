package com.example.football.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.view.composable.PkayerStatsScreen
import com.example.football.presentation.viewmodel.PlayerStatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerStatsFragment : Fragment() {

    private val viewModel: PlayerStatisticsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FootballTheme {
                    PkayerStatsScreen(viewModel, findNavController())
                }
            }
        }
    }
}
package com.example.football.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.view.composable.StandingsScreen
import com.example.football.presentation.viewmodel.StandingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StandingsFragment : Fragment() {

    private val viewModel by viewModels<StandingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FootballTheme {
                    Text(text = "hello Mr")
                    viewModel.getStandings(arguments?.getString("leagueId") ?: "0")
                    StandingsScreen(viewModel)
                }
            }
        }
    }
}
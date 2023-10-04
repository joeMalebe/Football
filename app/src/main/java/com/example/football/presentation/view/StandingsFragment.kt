package com.example.football.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.view.composable.StandingsCombinedScreen
import com.example.football.presentation.viewmodel.StandingsCombinedViewModel
import com.example.football.presentation.viewmodel.StandingsViewModel
import com.example.football.presentation.viewmodel.TopGoalScorersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StandingsFragment : Fragment() {

    private val standingsViewModel by viewModels<StandingsViewModel>()
    private val topGoalScorersViewModel by viewModels<TopGoalScorersViewModel>()
    private val standingsCombinedViewModel by viewModels<StandingsCombinedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FootballTheme {
                    val leagueId = arguments?.getString("leagueId") ?: "0"
                    standingsViewModel.getStandings(leagueId = leagueId)
                    topGoalScorersViewModel.loadTopGoalScorers(
                        leagueId = leagueId,
                        season = "2020"
                    )

                    StandingsCombinedScreen(
                        standingsCombinedViewModel,
                        standingsViewModel,
                        topGoalScorersViewModel,
                        onTeamClicked = { teamId ->
                            val directions =
                                StandingsFragmentDirections.actionStandingsFragmentToFixturesFragment(
                                    teamId.toString()
                                )
                            findNavController().navigate(directions)
                        },
                        onPlayerClicked = { playerId ->
                            val directions =
                                StandingsFragmentDirections.actionStandingsFragmentToPlayerStatsFragment(
                                    playerId.toString()
                                )
                            findNavController().navigate(directions)
                        }
                    )
                }
            }
        }
    }
}

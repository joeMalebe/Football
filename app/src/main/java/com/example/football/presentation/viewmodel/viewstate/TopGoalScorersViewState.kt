package com.example.football.presentation.viewmodel.viewstate

import com.example.football.domain.TopGoalScorerViewData

sealed class TopGoalScorersViewState {
    object Loading : TopGoalScorersViewState()
    data class TopGoalScorersLoaded(val topGoalScorers: List<TopGoalScorerViewData>) :
        TopGoalScorersViewState()

    object Error : TopGoalScorersViewState()
    object Initial : TopGoalScorersViewState()
    object NoInformation : TopGoalScorersViewState()
}

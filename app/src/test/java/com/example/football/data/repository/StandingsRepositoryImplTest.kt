package com.example.football.data.repository

import com.example.football.FootballService
import com.example.football.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class StandingsRepositoryImplTest {

    lateinit var standingsRepository: StandingsRepository

    @Mock
    lateinit var footballService: FootballService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        standingsRepository = StandingsRepositoryImpl(footballService, Dispatchers.Unconfined)
    }

    @Test
    fun `When get standings fails then return failed result`() = runTest {
        whenever(footballService.getStandings("29", "2022")).thenReturn(Result.failure(Throwable()))

        val sut = standingsRepository.getStandings(leagueId = "29", season = "2022")

        assertTrue(sut.isFailure)
        verify(footballService).getStandings("29", "2022")
    }

    @Test
    fun `When get standings is successful then return result success`() = runTest {
        whenever(
            footballService.getStandings(
                "1",
                "2020"
            )
        ).thenReturn(Result.success(TestData.standingsResponse))

        val sut = standingsRepository.getStandings("1", "2020")

        assertTrue(sut.isSuccess)
        assertEquals(TestData.standingsResponse, sut.getOrNull()!!)
    }

    @Test
    fun `When get standings throws runtime exception then return failed result`() = runTest {
        whenever(footballService.getStandings("2", "2003")).thenThrow(
            java.lang.RuntimeException("runtime error")
        )

        val sut = standingsRepository.getStandings("2", "2003")

        assertTrue(sut.isFailure)
        assertEquals("runtime error", sut.exceptionOrNull()!!.message)
    }
}

object TestData {
    val emptyTopGoalScorerResponse: TopScorersResponse = TopScorersResponse(response = emptyList())
    val nullTopGoalScorerResponse: TopScorersResponse = TopScorersResponse(response = null)
    val nullPlayerDataTopGoalScorerResponse: TopScorersResponse = TopScorersResponse(
        response = listOf(PlayerData(null, null))
    )
    val topGoalScorerResponse = TopScorersResponse(
        response = listOf(
            PlayerData(
                player = Player(
                    id = 1,
                    name = "Lionel Messi",
                    photo = "https://media.api-sports.io/football/players/1.png",
                    firstname = "Lionel",
                    lastname = "Messi"
                ),
                statistics = listOf(
                    Statistics(
                        team = Team(
                            id = 1,
                            name = "Barcelona",
                            logo = "https://media.api-sports.io/football/teams/1.png"
                        ),
                        goals = GoalStats(
                            total = 23,
                            conceded = 0,
                            assists = 0,
                            saves = 0
                        ),
                        league = League(
                            id = 1,
                            name = "La Liga",
                            country = "Spain",
                            logo = "https://media.api-sports.io/football/leagues/1.png",
                            flag = "https://media.api-sports.io/flags/es.svg",
                            season = 2020,
                            standings = listOf(
                                listOf(
                                    TeamStandingDto(
                                        rank = 1,
                                        team = Team(
                                            id = 1,
                                            name = "Barcelona",
                                            logo = "https://media.api-sports.io/football/teams/1.png"
                                        ),
                                        points = 74,
                                        goalsDiff = 45,
                                        group = "La Liga",
                                        form = "WWWLW",
                                        status = "same",
                                        description = "Promotion - Champions League (Group Stage)",
                                        all = TeamStatsDto(
                                            played = 31,
                                            win = 23,
                                            draw = 5,
                                            lose = 3,
                                            goals = TeamGoalsDto(
                                                `for` = 66,
                                                against = 21
                                            )
                                        ),
                                        home = TeamStatsDto(
                                            played = 16,
                                            win = 12,
                                            draw = 2,
                                            lose = 2,
                                            goals = TeamGoalsDto(
                                                `for` = 34,
                                                against = 11
                                            )
                                        ),
                                        away = TeamStatsDto(
                                            played = 15,
                                            win = 11,
                                            draw = 3,
                                            lose = 1,
                                            goals = TeamGoalsDto(
                                                `for` = 32,
                                                against = 10
                                            )
                                        ),
                                        update = "2021-04-06T00:00:00+00:00"
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            PlayerData(
                player = Player(
                    id = 2,
                    name = "Cristiano Ronaldo",
                    photo = "https://media.api-sports.io/football/players/2.png",
                    firstname = "Cristiano",
                    lastname = "Ronaldo"
                ),
                statistics = listOf(
                    Statistics(
                        team = Team(
                            id = 1,
                            name = "Barcelona",
                            logo = "https://media.api-sports.io/football/teams/1.png"
                        ),
                        goals = GoalStats(
                            total = 15,
                            conceded = 0,
                            assists = 0,
                            saves = 0
                        ),
                        league = League(
                            id = 1,
                            name = "La Liga",
                            country = "Portugal",
                            logo = "https://media.api-sports.io/football/leagues/1.png",
                            flag = "https://media.api-sports.io/flags/es.svg",
                            season = 2020,
                            standings = listOf(
                                listOf(
                                    TeamStandingDto(
                                        rank = 1,
                                        team = Team(
                                            id = 1,
                                            name = "Barcelona",
                                            logo = "https://media.api-sports.io/football/teams/1.png"
                                        ),
                                        points = 74,
                                        goalsDiff = 45,
                                        group = "La Liga",
                                        form = "WWWLW",
                                        status = "same",
                                        description = "Promotion - Champions League (Group Stage)",
                                        all = TeamStatsDto(
                                            played = 31,
                                            win = 23,
                                            draw = 5,
                                            lose = 3,
                                            goals = TeamGoalsDto(
                                                `for` = 66,
                                                against = 21
                                            )
                                        ),
                                        home = TeamStatsDto(
                                            played = 16,
                                            win = 12,
                                            draw = 2,
                                            lose = 2,
                                            goals = TeamGoalsDto(
                                                `for` = 34,
                                                against = 11
                                            )
                                        ),
                                        away = TeamStatsDto(
                                            played = 15,
                                            win = 11,
                                            draw = 3,
                                            lose = 1,
                                            goals = TeamGoalsDto(
                                                `for` = 32,
                                                against = 10
                                            )
                                        ),
                                        update = "2021-04-06T00:00:00+00:00"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )

    val standingsResponse = StandingsResponse(
        response =
        listOf(
            LeagueInfo(
                league = League(
                    id = 39,
                    name = "Premier League",
                    country = "England",
                    logo = "https://media.api-sports.io/football/leagues/39.png",
                    flag = "https://media.api-sports.io/flags/gb.svg",
                    season = 2020,
                    standings = listOf(
                        listOf(
                            TeamStandingDto(
                                rank = 1,
                                team = Team(
                                    id = 50,
                                    name = "Manchester City",
                                    logo = "https://media.api-sports.io/football/teams/50.png"
                                ),
                                points = 74,
                                goalsDiff = 45,
                                group = "Premier League",
                                form = "WWWLW",
                                status = "same",
                                description = "Promotion - Champions League (Group Stage)",
                                all = TeamStatsDto(
                                    played = 31,
                                    win = 23,
                                    draw = 5,
                                    lose = 3,
                                    goals = TeamGoalsDto(
                                        `for` = 66,
                                        against = 21
                                    )
                                ),
                                home = TeamStatsDto(
                                    played = 16,
                                    win = 12,
                                    draw = 2,
                                    lose = 2,
                                    goals = TeamGoalsDto(
                                        `for` = 36,
                                        against = 13
                                    )
                                ),
                                away = TeamStatsDto(
                                    played = 15,
                                    win = 11,
                                    draw = 3,
                                    lose = 1,
                                    goals = TeamGoalsDto(
                                        `for` = 30,
                                        against = 8
                                    )
                                ),
                                update = "2021-04-05T00:00:00+00:00"
                            ),
                            TeamStandingDto(
                                rank = 2,
                                team = Team(
                                    id = 33,
                                    name = "Manchester United",
                                    logo = "https://media.api-sports.io/football/teams/33.png"
                                ),
                                points = 60,
                                goalsDiff = 25,
                                group = "Premier League",
                                form = "WWWDD",
                                status = "same",
                                description = "Promotion - Champions League (Group Stage)",
                                all = TeamStatsDto(
                                    played = 30,
                                    win = 17,
                                    draw = 9,
                                    lose = 4,
                                    goals = TeamGoalsDto(
                                        `for` = 58,
                                        against = 33
                                    )
                                ),
                                home = TeamStatsDto(
                                    played = 15,
                                    win = 8,
                                    draw = 3,
                                    lose = 4,
                                    goals = TeamGoalsDto(
                                        `for` = 31,
                                        against = 20
                                    )
                                ),
                                away = TeamStatsDto(
                                    played = 15,
                                    win = 9,
                                    draw = 6,
                                    lose = 0,
                                    goals = TeamGoalsDto(
                                        `for` = 27,
                                        against = 13
                                    )
                                ),
                                update = "2021-04-05T00:00:00+00:00"
                            )
                        )
                    )
                )
            )
        )
    )
}

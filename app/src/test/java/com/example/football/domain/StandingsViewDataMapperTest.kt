package com.example.football.domain

import com.example.football.data.repository.TestData
import org.junit.Assert
import org.junit.Test

class StandingsViewDataMapperTest {

    private val standingsViewDataMapper = StandingsViewDataMapperImpl()

    @Test
    fun `When standings response is null the return no standings information result`() {
        val sut = standingsViewDataMapper.mapDtoToStandingsResult(null)

        Assert.assertSame(StandingsResult.NoStandingsInformation, sut)
    }

    @Test
    fun `When standings response has data then return standings loaded result`() {
        val sut = standingsViewDataMapper.mapDtoToStandingsResult(TestData.standingsResponse)

        Assert.assertSame(StandingsResult.StandingsLoaded::class.java, sut::class.java)
        testStandingsViewData(
            1,
            50,
            "https://media.api-sports.io/football/teams/50.png",
            23,
            3,
            5,
            74,
            "Promotion - Champions League (Group Stage)",
            (sut as StandingsResult.StandingsLoaded).standingsViewData[0]
        )
    }

    private fun testStandingsViewData(
        expectedPosition: Int,
        expectedTeamId: Int,
        expectedLogo: String,
        expectedWins: Int,
        expectedLosses: Int,
        expectedDraws: Int,
        expectedPoints: Int,
        expectedPositionDescription: String?,
        actual: StandingsViewData
    ) {
        Assert.assertEquals(expectedPosition, actual.position)
        Assert.assertEquals(expectedTeamId, actual.teamId)
        Assert.assertEquals(expectedLogo, actual.logo)
        Assert.assertEquals(expectedWins, actual.wins)
        Assert.assertEquals(expectedLosses, actual.losses)
        Assert.assertEquals(expectedDraws, actual.draws)
        Assert.assertEquals(expectedPoints, actual.points)
        Assert.assertEquals(expectedPositionDescription, actual.positionDescription)
    }
}

package leopardcat.studio.fcosquad.data.dto

import org.json.JSONArray

data class PlayerPrice (
    val timeMonthlyArray: JSONArray?,
    val valueMonthlyArray: JSONArray?,
    val monthlyMaxValue: String?,
    val monthlyMinValue: String?
)
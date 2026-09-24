package com.example.ui

import androidx.annotation.DrawableRes
import com.example.R

data class LearningRankInfo(
    val title: String,
    val minStars: Int,
    val maxStars: Int?,
    @DrawableRes val imageRes: Int
) {
    val rangeLabel: String
        get() = maxStars?.let { "$minStars–$it ★" } ?: "$minStars+ ★"
}

val learningRanks = listOf(
    LearningRankInfo("Little Hero", 0, 24, R.drawable.rank_little_hero_3d),
    LearningRankInfo("Power Explorer", 25, 59, R.drawable.rank_power_explorer_3d),
    LearningRankInfo("Brain Hero", 60, 119, R.drawable.rank_brain_hero_3d),
    LearningRankInfo("Super Learner", 120, 219, R.drawable.rank_super_learner_3d),
    LearningRankInfo("Aalo Champion", 220, null, R.drawable.rank_aalo_champion_3d)
)

fun learningRankFor(stars: Int): LearningRankInfo =
    learningRanks.lastOrNull { stars >= it.minStars } ?: learningRanks.first()

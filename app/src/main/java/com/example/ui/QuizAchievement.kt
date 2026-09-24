package com.example.ui

import androidx.annotation.DrawableRes
import com.example.R

data class QuizAchievement(val title: String, val message: String, @DrawableRes val imageRes: Int)

fun quizAchievement(score: Int, total: Int): QuizAchievement {
    if (total <= 0) return QuizAchievement("Little Hero", "পরের কুইজে আবার চেষ্টা করো।", R.drawable.rank_little_hero_3d)
    val percent = score.coerceIn(0, total) * 100 / total
    return when {
        percent >= 90 -> QuizAchievement("Aalo Champion", "দারুণ! তুমি প্রায় সব প্রশ্ন ঠিক করেছ।", R.drawable.rank_aalo_champion_3d)
        percent >= 70 -> QuizAchievement("Super Learner", "খুব ভালো! কঠিন প্রশ্নগুলোও আবার দেখো।", R.drawable.rank_super_learner_3d)
        percent >= 40 -> QuizAchievement("Brain Hero", "ভালো শুরু! ব্যাখ্যা পড়ে আরেকবার খেলো।", R.drawable.rank_brain_hero_3d)
        else -> QuizAchievement("Little Hero", "চেষ্টা চালিয়ে যাও; ভুল থেকেই শেখা শুরু।", R.drawable.rank_little_hero_3d)
    }
}

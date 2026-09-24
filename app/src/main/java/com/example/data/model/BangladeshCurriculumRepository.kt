package com.example.data.model

object BangladeshCurriculumRepository {

    val classLevels: List<CurriculumClassLevel> = Nctb2026Catalog.mergeInto(PrimaryCurriculumData.levels)
        .map { level ->
        if (level.levelNumber in 1..2) level.copy(subjects = level.subjects.filter {
            it.id in setOf("c${level.levelNumber}_bangla", "c${level.levelNumber}_english", "c${level.levelNumber}_math")
            }) else level
        }
    val featuredClassLevels: List<CurriculumClassLevel> = MvpFeaturedCurriculum.levels

    fun getClassesForStage(stage: CurriculumStage): List<CurriculumClassLevel> {
        return classLevels.filter { it.levelNumber in stage.minClass..stage.maxClass }
    }

    fun findTopic(topicId: String): CurriculumTopic? {
        for (level in featuredClassLevels) {
            for (subject in level.subjects) {
                subject.topics.firstOrNull { it.id == topicId }?.let { return it }
            }
        }
        for (level in classLevels) {
            for (sub in level.subjects) {
                for (topic in sub.topics) {
                    if (topic.id == topicId) return topic
                }
            }
        }
        return null
    }
}

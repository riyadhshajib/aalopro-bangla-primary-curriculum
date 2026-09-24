package com.example.ai.needle

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AloCommandRouterTest {
    @Test fun routesBanglaCurriculumCommand() {
        val intent = AloCommandRouter.deterministic("পঞ্চম শ্রেণির গণিত খোলো")
        assertEquals("open_curriculum", intent?.function)
        assertEquals("5", intent?.arguments?.get("grade"))
        assertEquals("গণিত", intent?.arguments?.get("subject"))
    }

    @Test fun routesBanglishCurriculumCommand() {
        val intent = AloCommandRouter.deterministic("class 3 English open koro")
        assertEquals("open_curriculum", intent?.function)
        assertEquals("3", intent?.arguments?.get("grade"))
        assertEquals("ইংরেজি", intent?.arguments?.get("subject"))
    }

    @Test fun routesQuizCommand() {
        assertEquals("start_quiz", AloCommandRouter.deterministic("চতুর্থ শ্রেণির বিজ্ঞান কুইজ শুরু করো")?.function)
    }

    @Test fun neverTreatsKnowledgeQuestionAsNavigation() {
        assertNull(AloCommandRouter.deterministic("বাংলাদেশের জাতীয় কবি কে"))
        assertNull(AloCommandRouter.deterministic("ভগ্নাংশ কী"))
    }
}

package com.example
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AdaptivePractice
import com.example.data.model.QuizQuestion
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[35])
class AdaptivePracticeTest {
    private fun question(id:Int,subject:String="math")=QuizQuestion(id,subject,"2+2?",listOf("3","4","5","6"),1,"2+2=4")
    private fun prefs()=ApplicationProvider.getApplicationContext<Context>().getSharedPreferences("adaptive-test",Context.MODE_PRIVATE).also{it.edit().clear().commit()}
    @Test fun profileGradeReloadAndRepeatedAnswersStaySeparate() {
        val prefs=prefs(); val model=AdaptivePractice(prefs); val q=question(1)
        model.record("one",1,q,true,false)
        assertEquals(.5157894736842106,model.state("one",1,"math").probability,1e-8)
        model.record("one",1,q,true,false)
        assertEquals(1,AdaptivePractice(prefs).state("one",1,"math").independent)
        assertEquals(0,model.state("two",1,"math").independent)
        assertEquals(0,model.state("one",2,"math").independent)
    }
    @Test fun assistedAnswersAreNotIndependentEvidence() {
        val model=AdaptivePractice(prefs());model.record("one",1,question(1),true,true)
        assertEquals(0,model.state("one",1,"math").independent)
        model.record("one",1,question(1),true,false)
        assertEquals(0,model.state("one",1,"math").independent)
    }
    @Test fun newQuestionsPreferWeakerSubjectAndNeverInventContent() {
        val model=AdaptivePractice(prefs())
        model.record("one",1,question(1,"math"),false,false)
        model.record("one",1,question(2,"science"),true,false)
        val weak=question(3,"math");val strong=question(4,"science")
        assertEquals(listOf(weak,strong),model.order("one",1,listOf(strong,weak)))
        assertEquals(listOf(strong,weak),model.order("other",1,listOf(strong,weak)))
    }
    @Test fun damagedDataDoesNotCrashAndKeepsRecoveryCopy() {
        val prefs=prefs();prefs.edit().putString("adaptive.v1.one.1","broken").commit()
        val model=AdaptivePractice(prefs);assertEquals(.2,model.state("one",1,"math").probability,0.0)
        assertEquals("broken",prefs.getString("adaptive.v1.one.1.recovery",null))
    }
}

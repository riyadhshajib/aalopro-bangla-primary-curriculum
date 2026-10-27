package com.example
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.*
import com.example.data.profile.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
@RunWith(RobolectricTestRunner::class)
@Config(sdk=[35])
class GradeQuizFlowTest {
 @Test fun allFiveProfilesKeepGradeAndLearningAfterReload() {
  val context=ApplicationProvider.getApplicationContext<Context>()
  context.getSharedPreferences(SharedPreferencesProfileRepository.PREFS_NAME,0).edit().clear().commit()
  val prefs=context.getSharedPreferences("grade-flow",0);prefs.edit().clear().commit()
  val repo=SharedPreferencesProfileRepository(context)
  val model=AdaptivePractice(prefs)
  for(g in 1..5){
   repo.addChild(ChildProfile("child$g","Learner $g",g)).getOrThrow()
   repo.selectChild("child$g").getOrThrow()
   val child=repo.household.value.activeChild!!
   val bank=GradeQuizBank.forGrade(child.classNumber)
   assertTrue(bank.isNotEmpty());assertTrue(bank.all{it.grade==g})
   val q=model.order(child.id,g,bank).first()
   model.record(child.id,g,q,true,false)
   assertEquals(1,model.state(child.id,g,q.subject).independent)
   model.record(child.id,g,q,true,false)
   assertEquals(1,model.state(child.id,g,q.subject).independent)
   val restored=SharedPreferencesProfileRepository(context).household.value.activeChild!!
   assertEquals(child,restored)
   assertEquals(1,AdaptivePractice(prefs).state(restored.id,g,q.subject).independent)
   assertEquals(0,model.state("unseen",g,q.subject).independent)
   model.finish(child.id,g,1);assertEquals(1,AdaptivePractice(prefs).completed(child.id,g))
   assertEquals(0,model.completed("unseen",g))
  }
  for(g in 1..5){repo.selectChild("child$g").getOrThrow();assertEquals(g,repo.household.value.activeChild!!.classNumber)}
 }
 @Test fun bankIntegrityAndNoCrossGradeFallback() {
  val bank=GradeQuizBank.questions
  assertEquals(bank.size,bank.map{it.id}.toSet().size)
  for(q in bank){
   assertTrue(q.grade in 1..5);assertTrue(q.source.isNotBlank())
   assertTrue(q.explanationBangla.isNotBlank());assertTrue(q.correctIndex in q.options.indices)
   assertEquals(q.options.size,q.options.toSet().size)
  }
  assertTrue(GradeQuizBank.forGrade(0).isEmpty())
  assertTrue(GradeQuizBank.forGrade(6).isEmpty())
 }
 @Test fun actualViewModelSwitchResetsSelectionScoreAndHint() {
  val context=ApplicationProvider.getApplicationContext<Context>()
  context.getSharedPreferences(SharedPreferencesProfileRepository.PREFS_NAME,0).edit().clear().commit()
  val repo=SharedPreferencesProfileRepository(context)
  repo.addChild(ChildProfile("a","First",1)).getOrThrow()
  repo.addChild(ChildProfile("b","Second",5)).getOrThrow()
  val db=androidx.room.Room.inMemoryDatabaseBuilder(context,com.example.data.local.AloDatabase::class.java).allowMainThreadQueries().build()
  val vm=com.example.ui.AloViewModel(com.example.data.local.AloRepository(db.aloDao()),context,repo,ProfileSyncCoordinator(context,NoOpProfileSyncGateway()))
  val first=vm.getFilteredQuestions().first()
  vm.nextQuizQuestion();assertEquals(0,vm.currentQuizIndex.value)
  vm.selectQuizOption(first.correctIndex);vm.showQuizHelp();vm.submitQuizAnswer()
  assertEquals(1,vm.quizScore.value);assertTrue(vm.quizAssisted.value)
  repo.selectChild("b").getOrThrow()
  val second=vm.getFilteredQuestions()
  assertTrue(second.all{it.grade==5});assertEquals(0,vm.quizScore.value)
  assertNull(vm.selectedOption.value);assertFalse(vm.quizAssisted.value);assertFalse(vm.isAnswerEvaluated.value)
  vm.selectQuizOption(999);assertNull(vm.selectedOption.value)
  vm.submitQuizAnswer();assertFalse(vm.isAnswerEvaluated.value)
  repo.selectChild("a").getOrThrow();assertTrue(vm.getFilteredQuestions().all{it.grade==1})
  while(!vm.quizFinished.value){val q=vm.getFilteredQuestions()[vm.currentQuizIndex.value];vm.selectQuizOption(q.correctIndex);vm.submitQuizAnswer();vm.nextQuizQuestion()}
  val local=AdaptivePractice(context.getSharedPreferences("aalo_adaptive_local",0))
  assertEquals(1,local.completed("a",1));vm.nextQuizQuestion();assertEquals(1,local.completed("a",1))
  assertEquals(0,local.completed("b",5))
 }}

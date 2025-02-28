package org.oppia.android.domain.classify.rules.numericinput

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.BindsInstance
import dagger.Component
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oppia.android.app.model.WrittenTranslationContext
import org.oppia.android.domain.classify.InteractionObjectTestBuilder
import org.oppia.android.testing.assertThrows
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import javax.inject.Inject
import javax.inject.Singleton

/** Tests for [NumericInputIsInclusivelyBetweenRuleClassifierProvider]. */
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(manifest = Config.NONE)
class NumericInputIsInclusivelyBetweenRuleClassifierProviderTest {

  private val POSITIVE_REAL_VALUE_1_5 =
    InteractionObjectTestBuilder.createReal(value = 1.5)
  private val POSITIVE_REAL_VALUE_2_5 =
    InteractionObjectTestBuilder.createReal(value = 2.5)
  private val POSITIVE_REAL_VALUE_3_5 =
    InteractionObjectTestBuilder.createReal(value = 3.5)
  private val NEGATIVE_REAL_VALUE_1_5 =
    InteractionObjectTestBuilder.createReal(value = -1.5)
  private val NEGATIVE_REAL_VALUE_2_5 =
    InteractionObjectTestBuilder.createReal(value = -2.5)
  private val NEGATIVE_REAL_VALUE_3_5 =
    InteractionObjectTestBuilder.createReal(value = -3.5)
  private val STRING_VALUE_1 =
    InteractionObjectTestBuilder.createString(value = "test1")
  private val STRING_VALUE_2 =
    InteractionObjectTestBuilder.createString(value = "test2")
  private val POSITIVE_INT_VALUE_1 =
    InteractionObjectTestBuilder.createInt(value = 1)
  private val POSITIVE_INT_VALUE_2 =
    InteractionObjectTestBuilder.createInt(value = 2)
  private val POSITIVE_INT_VALUE_3 =
    InteractionObjectTestBuilder.createInt(value = 3)
  private val NEGATIVE_INT_VALUE_1 =
    InteractionObjectTestBuilder.createInt(value = -1)
  private val NEGATIVE_INT_VALUE_2 =
    InteractionObjectTestBuilder.createInt(value = -2)
  private val NEGATIVE_INT_VALUE_3 =
    InteractionObjectTestBuilder.createInt(value = -3)

  @Inject
  internal lateinit var numericInputIsInclusivelyBetweenRuleClassifierProvider:
    NumericInputIsInclusivelyBetweenRuleClassifierProvider

  private val inputIsInclusivelyBetweenRuleClassifier by lazy {
    numericInputIsInclusivelyBetweenRuleClassifierProvider.createRuleClassifier()
  }

  @Before
  fun setUp() {
    setUpTestApplicationComponent()
  }

  @Test
  fun testPositiveRealAnswer_positiveRealInputs_sameExactValues_answerInclusivelyBetween() {
    val inputs = mapOf(
      "a" to POSITIVE_REAL_VALUE_1_5,
      "b" to POSITIVE_REAL_VALUE_1_5
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_REAL_VALUE_1_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isTrue()
  }

  @Test
  fun testNegativeRealAnswer_negativeRealInputs_sameExactValues_answerInclusivelyBetween() {
    val inputs = mapOf(
      "a" to NEGATIVE_REAL_VALUE_3_5,
      "b" to NEGATIVE_REAL_VALUE_3_5
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = NEGATIVE_REAL_VALUE_3_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isTrue()
  }

  @Test
  fun testPositiveRealAnswer_negativeRealInput_positiveRealInput_answerInclusivelyBetween() {
    val inputs = mapOf(
      "a" to NEGATIVE_REAL_VALUE_1_5,
      "b" to POSITIVE_REAL_VALUE_3_5
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_REAL_VALUE_2_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isTrue()
  }

  @Test
  fun testPositiveRealAnswer_positiveRealInput_negativeRealInput_answerInclusivelyBetween() {
    val inputs = mapOf(
      "a" to POSITIVE_REAL_VALUE_3_5,
      "b" to NEGATIVE_REAL_VALUE_3_5
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_REAL_VALUE_2_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isFalse()
  }

  @Test
  fun testNegativeRealAnswer_negativeRealInput_positiveRealInput_answerInclusivelyBetween() {
    val inputs = mapOf(
      "a" to NEGATIVE_REAL_VALUE_3_5,
      "b" to POSITIVE_REAL_VALUE_1_5
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = NEGATIVE_REAL_VALUE_2_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isTrue()
  }

  @Test
  fun testNegativeRealAnswer_positiveRealInput_negativeRealInput_answerInclusivelyBetween() {
    val inputs = mapOf(
      "a" to POSITIVE_REAL_VALUE_3_5,
      "b" to NEGATIVE_REAL_VALUE_3_5
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = NEGATIVE_REAL_VALUE_2_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isFalse()
  }

  @Test
  fun testPositiveIntAnswer_negativeInput_positiveIntInput_answerInclusivelyBetween() {
    val inputs = mapOf(
      "a" to NEGATIVE_INT_VALUE_1,
      "b" to POSITIVE_INT_VALUE_3
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_INT_VALUE_2,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isTrue()
  }

  @Test
  fun testPositiveIntAnswer_positiveIntInput_negativeIntInput_answerNotInclusivelyBetween() {
    val inputs = mapOf(
      "a" to POSITIVE_INT_VALUE_3,
      "b" to NEGATIVE_INT_VALUE_1
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_INT_VALUE_2,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isFalse()
  }

  @Test
  fun testNegativeIntAnswer_negativeIntInput_positiveIntInput_answerInclusivelyBetween() {
    val inputs = mapOf(
      "a" to NEGATIVE_INT_VALUE_3,
      "b" to POSITIVE_INT_VALUE_1
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = NEGATIVE_INT_VALUE_2,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isTrue()
  }

  @Test
  fun testNegativeIntAnswer_positiveIntInput_negativeIntInput_answerNotInclusivelyBetween() {
    val inputs = mapOf(
      "a" to POSITIVE_REAL_VALUE_3_5,
      "b" to NEGATIVE_REAL_VALUE_3_5
    )
    val matches =
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = NEGATIVE_REAL_VALUE_2_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )

    assertThat(matches).isFalse()
  }

  @Test
  fun testRealAnswer_firstMissingInput_throwsException() {
    val inputs = mapOf(
      "c" to NEGATIVE_REAL_VALUE_3_5,
      "b" to POSITIVE_REAL_VALUE_3_5
    )
    val exception = assertThrows(IllegalStateException::class) {
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_REAL_VALUE_1_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )
    }

    assertThat(exception)
      .hasMessageThat()
      .contains("Expected classifier inputs to contain parameter with name 'a' but had: [c, b]")
  }

  @Test
  fun testRealAnswer_secondMissingInput_throwsException() {
    val inputs = mapOf(
      "a" to NEGATIVE_REAL_VALUE_3_5,
      "c" to POSITIVE_REAL_VALUE_1_5
    )
    val exception = assertThrows(IllegalStateException::class) {
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_REAL_VALUE_1_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )
    }

    assertThat(exception)
      .hasMessageThat()
      .contains("Expected classifier inputs to contain parameter with name 'b' but had: [a, c]")
  }

  @Test
  fun testRealAnswer_firstStringInput_throwsException() {
    val inputs = mapOf(
      "a" to STRING_VALUE_1,
      "b" to NEGATIVE_REAL_VALUE_3_5
    )
    val exception = assertThrows(IllegalStateException::class) {
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_REAL_VALUE_1_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )
    }

    assertThat(exception)
      .hasMessageThat()
      .contains("Expected input value to be of type REAL not NORMALIZED_STRING")
  }

  @Test
  fun testRealAnswer_secondStringInput_throwsException() {
    val inputs = mapOf(
      "a" to POSITIVE_REAL_VALUE_1_5,
      "b" to STRING_VALUE_2
    )
    val exception = assertThrows(IllegalStateException::class) {
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_REAL_VALUE_1_5,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )
    }

    assertThat(exception)
      .hasMessageThat()
      .contains("Expected input value to be of type REAL not NORMALIZED_STRING")
  }

  @Test
  fun testIntAnswer_missingFirstInput_throwsException() {
    val inputs = mapOf(
      "c" to POSITIVE_INT_VALUE_1,
      "b" to POSITIVE_INT_VALUE_3
    )
    val exception = assertThrows(IllegalStateException::class) {
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_INT_VALUE_2,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )
    }

    assertThat(exception)
      .hasMessageThat()
      .contains("Expected classifier inputs to contain parameter with name 'a' but had: [c, b]")
  }

  @Test
  fun testIntAnswer_missingSecondInput_throwsException() {
    val inputs = mapOf(
      "a" to POSITIVE_INT_VALUE_1,
      "c" to POSITIVE_INT_VALUE_3
    )
    val exception = assertThrows(IllegalStateException::class) {
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_INT_VALUE_2,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )
    }

    assertThat(exception)
      .hasMessageThat()
      .contains("Expected classifier inputs to contain parameter with name 'b' but had: [a, c]")
  }

  @Test
  fun testIntAnswer_firstStringInput_throwsException() {
    val inputs = mapOf(
      "a" to STRING_VALUE_1,
      "b" to POSITIVE_INT_VALUE_3
    )
    val exception = assertThrows(IllegalStateException::class) {
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_INT_VALUE_1,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )
    }

    assertThat(exception)
      .hasMessageThat()
      .contains("Expected input value to be of type REAL not NORMALIZED_STRING")
  }

  @Test
  fun testIntAnswer_secondStringInput_throwsException() {
    val inputs = mapOf(
      "a" to NEGATIVE_INT_VALUE_3,
      "b" to STRING_VALUE_2
    )
    val exception = assertThrows(IllegalStateException::class) {
      inputIsInclusivelyBetweenRuleClassifier.matches(
        answer = POSITIVE_INT_VALUE_2,
        inputs = inputs,
        writtenTranslationContext = WrittenTranslationContext.getDefaultInstance()
      )
    }

    assertThat(exception)
      .hasMessageThat()
      .contains("Expected input value to be of type REAL not NORMALIZED_STRING")
  }

  private fun setUpTestApplicationComponent() {
    DaggerNumericInputIsInclusivelyBetweenRuleClassifierProviderTest_TestApplicationComponent
      .builder()
      .setApplication(ApplicationProvider.getApplicationContext())
      .build()
      .inject(this)
  }

  // TODO(#89): Move this to a common test application component.
  @Singleton
  @Component(modules = [])
  interface TestApplicationComponent {
    @Component.Builder
    interface Builder {
      @BindsInstance
      fun setApplication(application: Application): Builder

      fun build(): TestApplicationComponent
    }

    fun inject(test: NumericInputIsInclusivelyBetweenRuleClassifierProviderTest)
  }
}

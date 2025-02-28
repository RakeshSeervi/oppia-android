package org.oppia.android.app.story.storyitemviewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.oppia.android.R
import org.oppia.android.app.model.ChapterPlayState
import org.oppia.android.app.model.ChapterSummary
import org.oppia.android.app.model.ExplorationCheckpoint
import org.oppia.android.app.model.LessonThumbnail
import org.oppia.android.app.model.ProfileId
import org.oppia.android.app.story.ExplorationSelectionListener
import org.oppia.android.app.translation.AppLanguageResourceHandler
import org.oppia.android.domain.exploration.lightweightcheckpointing.ExplorationCheckpointController
import org.oppia.android.util.data.AsyncResult
import org.oppia.android.util.data.DataProviders.Companion.toLiveData

/** Chapter summary view model for the recycler view in [StoryFragment]. */
class StoryChapterSummaryViewModel(
  val index: Int,
  private val fragment: Fragment,
  private val explorationSelectionListener: ExplorationSelectionListener,
  val explorationCheckpointController: ExplorationCheckpointController,
  val internalProfileId: Int,
  val topicId: String,
  val storyId: String,
  val chapterSummary: ChapterSummary,
  val entityType: String,
  private val resourceHandler: AppLanguageResourceHandler
) : StoryItemViewModel() {

  val explorationId: String = chapterSummary.explorationId
  private val name: String = chapterSummary.name
  val summary: String = chapterSummary.summary
  val chapterThumbnail: LessonThumbnail = chapterSummary.chapterThumbnail
  val missingPrerequisiteChapter: ChapterSummary = chapterSummary.missingPrerequisiteChapter
  val chapterPlayState: ChapterPlayState = chapterSummary.chapterPlayState

  fun onExplorationClicked() {
    val shouldSavePartialProgress =
      when (chapterPlayState) {
        ChapterPlayState.IN_PROGRESS_SAVED, ChapterPlayState.IN_PROGRESS_NOT_SAVED,
        ChapterPlayState.STARTED_NOT_COMPLETED, ChapterPlayState.NOT_STARTED -> true
        else -> false
      }
    if (chapterPlayState == ChapterPlayState.IN_PROGRESS_SAVED) {
      val explorationCheckpointLiveData =
        explorationCheckpointController.retrieveExplorationCheckpoint(
          ProfileId.newBuilder().apply {
            internalId = internalProfileId
          }.build(),
          explorationId
        ).toLiveData()

      explorationCheckpointLiveData.observe(
        fragment,
        object : Observer<AsyncResult<ExplorationCheckpoint>> {
          override fun onChanged(it: AsyncResult<ExplorationCheckpoint>) {
            if (it.isSuccess()) {
              explorationCheckpointLiveData.removeObserver(this)
              explorationSelectionListener.selectExploration(
                internalProfileId,
                topicId,
                storyId,
                explorationId,
                canExplorationBeResumed = true,
                shouldSavePartialProgress,
                backflowId = 1,
                explorationCheckpoint = it.getOrThrow()
              )
            } else if (it.isFailure()) {
              explorationCheckpointLiveData.removeObserver(this)
              explorationSelectionListener.selectExploration(
                internalProfileId,
                topicId,
                storyId,
                explorationId,
                canExplorationBeResumed = false,
                shouldSavePartialProgress,
                backflowId = 1,
                explorationCheckpoint = ExplorationCheckpoint.getDefaultInstance()
              )
            }
          }
        }
      )
    } else {
      explorationSelectionListener.selectExploration(
        internalProfileId,
        topicId,
        storyId,
        explorationId,
        canExplorationBeResumed = false,
        shouldSavePartialProgress,
        backflowId = 1,
        explorationCheckpoint = ExplorationCheckpoint.getDefaultInstance()
      )
    }
  }

  fun computeChapterTitleText(): String {
    return resourceHandler.getStringInLocaleWithWrapping(
      R.string.chapter_name, (index + 1).toString(), name
    )
  }
}

package protein.tracking

import com.bugsnag.Bugsnag
import com.schibsted.spain.retroswagger.lib.RetroswaggerErrorTracking

class BugsnagErrorTracking : RetroswaggerErrorTracking {
  private val bugSnag = Bugsnag("a33c387e14e810eace96242d7382737d")

  override fun logException(throwable: Throwable) {
    bugSnag.notify(throwable)
  }
}

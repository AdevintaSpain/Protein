package protein.tracking

import com.bugsnag.Bugsnag

class BugsnagErrorTracking : ErrorTracking {
  private val bugSnag = Bugsnag("a33c387e14e810eace96242d7382737d")

  override fun logException(throwable: Throwable) {
    bugSnag.notify(throwable)
  }
}

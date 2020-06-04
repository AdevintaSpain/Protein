package tracking

import com.schibsted.spain.retroswagger.lib.RetroswaggerErrorTracking

class ConsoleLogTracking : RetroswaggerErrorTracking {
  override fun logException(throwable: Throwable) {
    System.out.print(throwable.message)
  }
}

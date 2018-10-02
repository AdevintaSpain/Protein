package tracking

import protein.tracking.ErrorTracking

class ConsoleLogTracking : ErrorTracking {
  override fun logException(throwable: Throwable) {
    System.out.print(throwable.message)
  }
}
package protein.tracking

interface ErrorTracking {
  fun logException(throwable: Throwable)
}

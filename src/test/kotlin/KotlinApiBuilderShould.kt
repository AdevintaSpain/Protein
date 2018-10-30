import config.ConfigurationForTests
import junit.framework.TestCase.assertEquals
import mocks.OPENSTF_INTERFACE_MOCK
import mocks.PET_STORE_INTERFACE_MOCK
import org.junit.Test
import protein.kotlinbuilders.KotlinApiBuilder
import protein.kotlinbuilders.ProteinApiConfiguration
import tracking.ConsoleLogTracking

class KotlinApiBuilderShould {

  private val favoritesSchemaProteinConfig = ProteinApiConfiguration(
      ConfigurationForTests.SERVICE_ENDPOINT,
      "",
      ConfigurationForTests.PACKAGE_NAME,
      ConfigurationForTests.COMPONENT_NAME,
      ConfigurationForTests.MODULE_NAME,
      ConfigurationForTests.SWAGGER_FILE_FAVORITES
  )

  private val openStfSchemaProteinConfig = ProteinApiConfiguration(
      ConfigurationForTests.SERVICE_ENDPOINT,
      "",
      ConfigurationForTests.PACKAGE_NAME,
      ConfigurationForTests.COMPONENT_NAME,
      ConfigurationForTests.MODULE_NAME,
      ConfigurationForTests.SWAGGER_FILE_OPENSTF
  )

  private val petStoreSchemaProteinConfig = ProteinApiConfiguration(
      ConfigurationForTests.SERVICE_ENDPOINT,
      "",
      ConfigurationForTests.PACKAGE_NAME,
      ConfigurationForTests.COMPONENT_NAME,
      ConfigurationForTests.MODULE_NAME,
      ConfigurationForTests.SWAGGER_FILE_PET_STORE
  )

  private val consoleLogTracking = ConsoleLogTracking()

  @Test
  fun createInterfaceIfFavoritesSchemaFileProvided() {
    val kotlinApiBuilder = KotlinApiBuilder(favoritesSchemaProteinConfig, consoleLogTracking)
    kotlinApiBuilder.build()
    assertEquals("",
        "package com.mycompany.mylibrary\n" +
            "\n" +
            "import io.reactivex.Completable\n" +
            "import io.reactivex.Single\n" +
            "import retrofit2.http.DELETE\n" +
            "import retrofit2.http.GET\n" +
            "import retrofit2.http.PUT\n" +
            "import retrofit2.http.Path\n" +
            "\n" +
            "interface componentNameApiInterface {\n" +
            "    @GET(\"/favorites\")\n" +
            "    fun getFavorites(): Single<GetFavoritesResponse>\n" +
            "\n" +
            "    @PUT(\"/favorites/{adId}\")\n" +
            "    fun saveFavorite(@Path(\"adId\") adId: String): Completable\n" +
            "\n" +
            "    @DELETE(\"/favorites/{adId}\")\n" +
            "    fun deleteFavorite(@Path(\"adId\") adId: String): Completable\n" +
            "}\n",
        kotlinApiBuilder.getGeneratedApiInterfaceString())
  }

  @Test
  fun createInterfaceIfOpenStfSchemaFileProvided() {
    val kotlinApiBuilder = KotlinApiBuilder(openStfSchemaProteinConfig, consoleLogTracking)
    kotlinApiBuilder.build()
    assertEquals("",
        OPENSTF_INTERFACE_MOCK,
        kotlinApiBuilder.getGeneratedApiInterfaceString())
  }

  @Test
  fun createInterfaceIfPetStoreSchemaFileProvided() {
    val kotlinApiBuilder = KotlinApiBuilder(petStoreSchemaProteinConfig, consoleLogTracking)
    kotlinApiBuilder.build()
    assertEquals("",
        PET_STORE_INTERFACE_MOCK,
        kotlinApiBuilder.getGeneratedApiInterfaceString())
  }
}
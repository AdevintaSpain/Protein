import com.schibsted.spain.retroswagger.lib.RetroswaggerApiBuilder
import com.schibsted.spain.retroswagger.lib.RetroswaggerApiConfiguration
import com.squareup.kotlinpoet.TypeSpec
import config.ConfigurationForTests
import junit.framework.TestCase.assertEquals
import mocks.OPENSTF_INTERFACE_MOCK
import mocks.PET_STORE_INTERFACE_MOCK
import org.junit.Test
import protein.common.StorageUtils
import tracking.ConsoleLogTracking

class KotlinApiBuilderShould {

  private val favoritesSchemaProteinConfig = RetroswaggerApiConfiguration(
      ConfigurationForTests.SERVICE_ENDPOINT,
      "",
      ConfigurationForTests.PACKAGE_NAME,
      ConfigurationForTests.COMPONENT_NAME,
      ConfigurationForTests.MODULE_NAME,
      ConfigurationForTests.SWAGGER_FILE_FAVORITES,
      true
  )

  private val openStfSchemaProteinConfig = RetroswaggerApiConfiguration(
      ConfigurationForTests.SERVICE_ENDPOINT,
      "",
      ConfigurationForTests.PACKAGE_NAME,
      ConfigurationForTests.COMPONENT_NAME,
      ConfigurationForTests.MODULE_NAME,
      ConfigurationForTests.SWAGGER_FILE_OPENSTF,
      true
  )

  private val petStoreSchemaProteinConfig = RetroswaggerApiConfiguration(
      ConfigurationForTests.SERVICE_ENDPOINT,
      "",
      ConfigurationForTests.PACKAGE_NAME,
      ConfigurationForTests.COMPONENT_NAME,
      ConfigurationForTests.MODULE_NAME,
      ConfigurationForTests.SWAGGER_FILE_PET_STORE,
      true
  )

  private val consoleLogTracking = ConsoleLogTracking()

  @Test
  fun createInterfaceIfFavoritesSchemaFileProvided() {
    val kotlinApiBuilder = RetroswaggerApiBuilder(favoritesSchemaProteinConfig, consoleLogTracking)
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
          "  /**\n" +
          "   * Get all favorites for this user\n" +
          "   *\n" +
          "   * @param Authorization Authorization\n" +
          "   */\n" +
          "  @GET(\"favorites\")\n" +
          "  fun getFavorites(): Single<GetFavoritesResponse>\n" +
          "\n" +
          "  /**\n" +
          "   * Save favorite\n" +
          "   *\n" +
          "   * @param adId adId\n" +
          "   * @param Authorization Authorization\n" +
          "   */\n" +
          "  @PUT(\"favorites/{adId}\")\n" +
          "  fun saveFavorite(@Path(\"adId\") adId: String): Completable\n" +
          "\n" +
          "  /**\n" +
          "   * Delete favorite\n" +
          "   *\n" +
          "   * @param adId adId\n" +
          "   * @param Authorization Authorization\n" +
          "   */\n" +
          "  @DELETE(\"favorites/{adId}\")\n" +
          "  fun deleteFavorite(@Path(\"adId\") adId: String): Completable\n" +
          "}\n",
      getGeneratedApiInterfaceString(kotlinApiBuilder.getGeneratedApiInterfaceTypeSpec()))
  }

  @Test
  fun createInterfaceIfOpenStfSchemaFileProvided() {
    val kotlinApiBuilder = RetroswaggerApiBuilder(openStfSchemaProteinConfig, consoleLogTracking)
    kotlinApiBuilder.build()
    assertEquals("",
        OPENSTF_INTERFACE_MOCK,
      getGeneratedApiInterfaceString(kotlinApiBuilder.getGeneratedApiInterfaceTypeSpec()))
  }

  @Test
  fun createInterfaceIfPetStoreSchemaFileProvided() {
    val kotlinApiBuilder = RetroswaggerApiBuilder(petStoreSchemaProteinConfig, consoleLogTracking)
    kotlinApiBuilder.build()
    assertEquals("",
        PET_STORE_INTERFACE_MOCK,
        getGeneratedApiInterfaceString(kotlinApiBuilder.getGeneratedApiInterfaceTypeSpec()))
  }

  private fun getGeneratedApiInterfaceString(apiInterfaceTypeSpec: TypeSpec): String {
    return StorageUtils.generateString(petStoreSchemaProteinConfig.packageName, apiInterfaceTypeSpec)
  }
}

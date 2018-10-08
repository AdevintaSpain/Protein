package mocks

const val PET_STORE_INTERFACE_MOCK = "package com.mycompany.mylibrary\n" +
  "\n" +
  "import io.reactivex.Completable\n" +
  "import io.reactivex.Single\n" +
  "import kotlin.collections.List\n" +
  "import retrofit2.http.Body\n" +
  "import retrofit2.http.DELETE\n" +
  "import retrofit2.http.GET\n" +
  "import retrofit2.http.POST\n" +
  "import retrofit2.http.PUT\n" +
  "import retrofit2.http.Path\n" +
  "\n" +
  "interface componentNameApiInterface {\n" +
  "    @PUT(\"/pet\")\n" +
  "    fun updatePet(@Body body: Pet): Completable\n" +
  "\n" +
  "    @POST(\"/pet\")\n" +
  "    fun addPet(@Body body: Pet): Completable\n" +
  "\n" +
  "    @GET(\"/pet/findByStatus\")\n" +
  "    fun findPetsByStatus(): Single<List<Pet>>\n" +
  "\n" +
  "    @GET(\"/pet/findByTags\")\n" +
  "    fun findPetsByTags(): Single<List<Pet>>\n" +
  "\n" +
  "    @GET(\"/pet/{petId}\")\n" +
  "    fun getPetById(@Path(\"petId\") petId: Int): Single<Pet>\n" +
  "\n" +
  "    @POST(\"/pet/{petId}\")\n" +
  "    fun updatePetWithForm(@Path(\"petId\") petId: Int): Completable\n" +
  "\n" +
  "    @DELETE(\"/pet/{petId}\")\n" +
  "    fun deletePet(@Path(\"petId\") petId: Int): Completable\n" +
  "\n" +
  "    @POST(\"/pet/{petId}/uploadImage\")\n" +
  "    fun uploadFile(@Path(\"petId\") petId: Int): Single<ApiResponse>\n" +
  "\n" +
  "    @GET(\"/store/inventory\")\n" +
  "    fun getInventory(): Completable\n" +
  "\n" +
  "    @POST(\"/store/order\")\n" +
  "    fun placeOrder(@Body body: Order): Single<Order>\n" +
  "\n" +
  "    @GET(\"/store/order/{orderId}\")\n" +
  "    fun getOrderById(@Path(\"orderId\") orderId: Int): Single<Order>\n" +
  "\n" +
  "    @DELETE(\"/store/order/{orderId}\")\n" +
  "    fun deleteOrder(@Path(\"orderId\") orderId: Int): Completable\n" +
  "\n" +
  "    @POST(\"/user\")\n" +
  "    fun createUser(@Body body: User): Completable\n" +
  "\n" +
  "    @POST(\"/user/createWithArray\")\n" +
  "    fun createUsersWithArrayInput(@Body body: Body): Completable\n" +
  "\n" +
  "    @POST(\"/user/createWithList\")\n" +
  "    fun createUsersWithListInput(@Body body: Body): Completable\n" +
  "\n" +
  "    @GET(\"/user/login\")\n" +
  "    fun loginUser(): Completable\n" +
  "\n" +
  "    @GET(\"/user/logout\")\n" +
  "    fun logoutUser(): Completable\n" +
  "\n" +
  "    @GET(\"/user/{username}\")\n" +
  "    fun getUserByName(@Path(\"username\") username: String): Single<User>\n" +
  "\n" +
  "    @PUT(\"/user/{username}\")\n" +
  "    fun updateUser(@Path(\"username\") username: String, @Body body: User): Completable\n" +
  "\n" +
  "    @DELETE(\"/user/{username}\")\n" +
  "    fun deleteUser(@Path(\"username\") username: String): Completable\n" +
  "}\n"

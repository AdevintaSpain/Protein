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
  "import retrofit2.http.Query\n" +
  "\n" +
  "interface componentNameApiInterface {\n" +
  "    /**\n" +
  "     * Update an existing pet\n" +
  "     *\n" +
  "     * @param body Pet object that needs to be added to the store\n" +
  "     */\n" +
  "    @PUT(\"/pet\")\n" +
  "    fun updatePet(@Body body: Pet): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Add a new pet to the store\n" +
  "     *\n" +
  "     * @param body Pet object that needs to be added to the store\n" +
  "     */\n" +
  "    @POST(\"/pet\")\n" +
  "    fun addPet(@Body body: Pet): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Finds Pets by status\n" +
  "     *\n" +
  "     * @param status Status values that need to be considered for filter\n" +
  "     */\n" +
  "    @GET(\"/pet/findByStatus\")\n" +
  "    fun findPetsByStatus(@Query(\"status\") status: List<String>): Single<List<Pet>>\n" +
  "\n" +
  "    /**\n" +
  "     * Finds Pets by tags\n" +
  "     *\n" +
  "     * @param tags Tags to filter by\n" +
  "     */\n" +
  "    @GET(\"/pet/findByTags\")\n" +
  "    fun findPetsByTags(@Query(\"tags\") tags: List<String>): Single<List<Pet>>\n" +
  "\n" +
  "    /**\n" +
  "     * Find pet by ID\n" +
  "     *\n" +
  "     * @param petId ID of pet to return\n" +
  "     */\n" +
  "    @GET(\"/pet/{petId}\")\n" +
  "    fun getPetById(@Path(\"petId\") petId: Long): Single<Pet>\n" +
  "\n" +
  "    /**\n" +
  "     * Updates a pet in the store with form data\n" +
  "     *\n" +
  "     * @param petId ID of pet that needs to be updated\n" +
  "     * @param name Updated name of the pet\n" +
  "     * @param status Updated status of the pet\n" +
  "     */\n" +
  "    @POST(\"/pet/{petId}\")\n" +
  "    fun updatePetWithForm(@Path(\"petId\") petId: Long): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Deletes a pet\n" +
  "     *\n" +
  "     * @param petId Pet id to delete\n" +
  "     */\n" +
  "    @DELETE(\"/pet/{petId}\")\n" +
  "    fun deletePet(@Path(\"petId\") petId: Long): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * uploads an image\n" +
  "     *\n" +
  "     * @param petId ID of pet to update\n" +
  "     * @param additionalMetadata Additional data to pass to server\n" +
  "     * @param file file to upload\n" +
  "     */\n" +
  "    @POST(\"/pet/{petId}/uploadImage\")\n" +
  "    fun uploadFile(@Path(\"petId\") petId: Long): Single<ApiResponse>\n" +
  "\n" +
  "    /**\n" +
  "     * Returns pet inventories by status\n" +
  "     */\n" +
  "    @GET(\"/store/inventory\")\n" +
  "    fun getInventory(): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Place an order for a pet\n" +
  "     *\n" +
  "     * @param body order placed for purchasing the pet\n" +
  "     */\n" +
  "    @POST(\"/store/order\")\n" +
  "    fun placeOrder(@Body body: Order): Single<Order>\n" +
  "\n" +
  "    /**\n" +
  "     * Find purchase order by ID\n" +
  "     *\n" +
  "     * @param orderId ID of pet that needs to be fetched\n" +
  "     */\n" +
  "    @GET(\"/store/order/{orderId}\")\n" +
  "    fun getOrderById(@Path(\"orderId\") orderId: Long): Single<Order>\n" +
  "\n" +
  "    /**\n" +
  "     * Delete purchase order by ID\n" +
  "     *\n" +
  "     * @param orderId ID of the order that needs to be deleted\n" +
  "     */\n" +
  "    @DELETE(\"/store/order/{orderId}\")\n" +
  "    fun deleteOrder(@Path(\"orderId\") orderId: Long): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Create user\n" +
  "     *\n" +
  "     * @param body Created user object\n" +
  "     */\n" +
  "    @POST(\"/user\")\n" +
  "    fun createUser(@Body body: User): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Creates list of users with given input array\n" +
  "     *\n" +
  "     * @param body List of user object\n" +
  "     */\n" +
  "    @POST(\"/user/createWithArray\")\n" +
  "    fun createUsersWithArrayInput(@Body body: List<User>): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Creates list of users with given input array\n" +
  "     *\n" +
  "     * @param body List of user object\n" +
  "     */\n" +
  "    @POST(\"/user/createWithList\")\n" +
  "    fun createUsersWithListInput(@Body body: List<User>): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Logs user into the system\n" +
  "     *\n" +
  "     * @param username The user name for login\n" +
  "     * @param password The password for login in clear text\n" +
  "     */\n" +
  "    @GET(\"/user/login\")\n" +
  "    fun loginUser(@Query(\"username\") username: String, @Query(\"password\") password: String): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Logs out current logged in user session\n" +
  "     */\n" +
  "    @GET(\"/user/logout\")\n" +
  "    fun logoutUser(): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Get user by user name\n" +
  "     *\n" +
  "     * @param username The name that needs to be fetched. Use user1 for testing.\n" +
  "     */\n" +
  "    @GET(\"/user/{username}\")\n" +
  "    fun getUserByName(@Path(\"username\") username: String): Single<User>\n" +
  "\n" +
  "    /**\n" +
  "     * Updated user\n" +
  "     *\n" +
  "     * @param username name that need to be updated\n" +
  "     * @param body Updated user object\n" +
  "     */\n" +
  "    @PUT(\"/user/{username}\")\n" +
  "    fun updateUser(@Path(\"username\") username: String, @Body body: User): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Delete user\n" +
  "     *\n" +
  "     * @param username The name that needs to be deleted\n" +
  "     */\n" +
  "    @DELETE(\"/user/{username}\")\n" +
  "    fun deleteUser(@Path(\"username\") username: String): Completable\n" +
  "}\n"

package mocks

const val OPENSTF_INTERFACE_MOCK = "package com.mycompany.mylibrary\n" +
  "\n" +
  "import io.reactivex.Completable\n" +
  "import io.reactivex.Single\n" +
  "import retrofit2.http.Body\n" +
  "import retrofit2.http.DELETE\n" +
  "import retrofit2.http.GET\n" +
  "import retrofit2.http.POST\n" +
  "import retrofit2.http.Path\n" +
  "import retrofit2.http.Query\n" +
  "\n" +
  "interface componentNameApiInterface {\n" +
  "    /**\n" +
  "     * User Profile\n" +
  "     */\n" +
  "    @GET(\"/user\")\n" +
  "    fun getUser(): Single<UserResponse>\n" +
  "\n" +
  "    /**\n" +
  "     * User Devices\n" +
  "     *\n" +
  "     * @param fields Fields query parameter takes a comma seperated list of fields. Only listed field will be return in response\n" +
  "     */\n" +
  "    @GET(\"/user/devices\")\n" +
  "    fun getUserDevices(@Query(\"fields\") fields: String?): Single<DeviceListResponse>\n" +
  "\n" +
  "    /**\n" +
  "     * Add a device to a user\n" +
  "     *\n" +
  "     * @param device Device to add\n" +
  "     */\n" +
  "    @POST(\"/user/devices\")\n" +
  "    fun addUserDevice(@Body device: AddUserDevicePayload): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * User Device\n" +
  "     *\n" +
  "     * @param serial Device Serial\n" +
  "     * @param fields Fields query parameter takes a comma seperated list of fields. Only listed field will be return in response\n" +
  "     */\n" +
  "    @GET(\"/user/devices/{serial}\")\n" +
  "    fun getUserDeviceBySerial(@Path(\"serial\") serial: String, @Query(\"fields\") fields: String?): Single<DeviceResponse>\n" +
  "\n" +
  "    /**\n" +
  "     * Delete User Device\n" +
  "     *\n" +
  "     * @param serial Device Serial\n" +
  "     */\n" +
  "    @DELETE(\"/user/devices/{serial}\")\n" +
  "    fun deleteUserDeviceBySerial(@Path(\"serial\") serial: String): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Remote Connect\n" +
  "     *\n" +
  "     * @param serial Device Serial\n" +
  "     */\n" +
  "    @POST(\"/user/devices/{serial}/remoteConnect\")\n" +
  "    fun remoteConnectUserDeviceBySerial(@Path(\"serial\") serial: String): Single<RemoteConnectUserDeviceResponse>\n" +
  "\n" +
  "    /**\n" +
  "     * Remote Disconnect\n" +
  "     *\n" +
  "     * @param serial Device Serial\n" +
  "     */\n" +
  "    @DELETE(\"/user/devices/{serial}/remoteConnect\")\n" +
  "    fun remoteDisconnectUserDeviceBySerial(@Path(\"serial\") serial: String): Completable\n" +
  "\n" +
  "    /**\n" +
  "     * Access Tokens\n" +
  "     */\n" +
  "    @GET(\"/user/accessTokens\")\n" +
  "    fun getUserAccessTokens(): Single<AccessTokensResponse>\n" +
  "\n" +
  "    /**\n" +
  "     * Device List\n" +
  "     *\n" +
  "     * @param fields Fields query parameter takes a comma seperated list of fields. Only listed field will be return in response\n" +
  "     */\n" +
  "    @GET(\"/devices\")\n" +
  "    fun getDevices(@Query(\"fields\") fields: String?): Single<DeviceListResponse>\n" +
  "\n" +
  "    /**\n" +
  "     * Device Information\n" +
  "     *\n" +
  "     * @param serial Device Serial\n" +
  "     * @param fields Fields query parameter takes a comma seperated list of fields. Only listed field will be return in response\n" +
  "     */\n" +
  "    @GET(\"/devices/{serial}\")\n" +
  "    fun getDeviceBySerial(@Path(\"serial\") serial: String, @Query(\"fields\") fields: String?): Single<DeviceResponse>\n" +
  "}\n"

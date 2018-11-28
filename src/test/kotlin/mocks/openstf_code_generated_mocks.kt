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
  "    @GET(\"/user\")\n" +
  "    fun getUser(): Single<UserResponse>\n" +
  "\n" +
  "    @GET(\"/user/devices\")\n" +
  "    fun getUserDevices(@Query(\"fields\") fields: String?): Single<DeviceListResponse>\n" +
  "\n" +
  "    @POST(\"/user/devices\")\n" +
  "    fun addUserDevice(@Body device: AddUserDevicePayload): Completable\n" +
  "\n" +
  "    @GET(\"/user/devices/{serial}\")\n" +
  "    fun getUserDeviceBySerial(@Path(\"serial\") serial: String, @Query(\"fields\") fields: String?): Single<DeviceResponse>\n" +
  "\n" +
  "    @DELETE(\"/user/devices/{serial}\")\n" +
  "    fun deleteUserDeviceBySerial(@Path(\"serial\") serial: String): Completable\n" +
  "\n" +
  "    @POST(\"/user/devices/{serial}/remoteConnect\")\n" +
  "    fun remoteConnectUserDeviceBySerial(@Path(\"serial\") serial: String): Single<RemoteConnectUserDeviceResponse>\n" +
  "\n" +
  "    @DELETE(\"/user/devices/{serial}/remoteConnect\")\n" +
  "    fun remoteDisconnectUserDeviceBySerial(@Path(\"serial\") serial: String): Completable\n" +
  "\n" +
  "    @GET(\"/user/accessTokens\")\n" +
  "    fun getUserAccessTokens(): Single<AccessTokensResponse>\n" +
  "\n" +
  "    @GET(\"/devices\")\n" +
  "    fun getDevices(@Query(\"fields\") fields: String?): Single<DeviceListResponse>\n" +
  "\n" +
  "    @GET(\"/devices/{serial}\")\n" +
  "    fun getDeviceBySerial(@Path(\"serial\") serial: String, @Query(\"fields\") fields: String?): Single<DeviceResponse>\n" +
  "}\n"

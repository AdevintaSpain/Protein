package mocks

const val BOT_FRAMEWORK_INTERFACE_MOCK = "package com.mycompany.mylibrary\n" +
  "\n" +
  "import com.schibsted.retroswagger.Activity\n" +
  "import com.schibsted.retroswagger.TokenParameters\n" +
  "import io.reactivex.Completable\n" +
  "import io.reactivex.Single\n" +
  "import retrofit2.http.Body\n" +
  "import retrofit2.http.GET\n" +
  "import retrofit2.http.POST\n" +
  "import retrofit2.http.Path\n" +
  "import retrofit2.http.Query\n" +
  "\n" +
  "interface componentNameApiInterface {\n" +
  "  /**\n" +
  "   * null\n" +
  "   */\n" +
  "  @GET(\"v3/directline/session/getsessionid\")\n" +
  "  fun Session_GetSessionId(): Completable\n" +
  "\n" +
  "  /**\n" +
  "   * Start a new conversation\n" +
  "   */\n" +
  "  @POST(\"v3/directline/conversations\")\n" +
  "  fun Conversations_StartConversation(@Body tokenParameters: TokenParameters?): Single<Conversation>\n" +
  "\n" +
  "  /**\n" +
  "   * Get information about an existing conversation\n" +
  "   */\n" +
  "  @GET(\"v3/directline/conversations/{conversationId}\")\n" +
  "  fun Conversations_ReconnectToConversation(@Path(\"conversationId\") conversationId: String,\n" +
  "      @Query(\"watermark\") watermark: String?): Single<Conversation>\n" +
  "\n" +
  "  /**\n" +
  "   * Get activities in this conversation. This method is paged with the 'watermark' parameter.\n" +
  "   *\n" +
  "   * @param conversationId Conversation ID\n" +
  "   * @param watermark (Optional) only returns activities newer than this watermark\n" +
  "   */\n" +
  "  @GET(\"v3/directline/conversations/{conversationId}/activities\")\n" +
  "  fun Conversations_GetActivities(@Path(\"conversationId\") conversationId: String,\n" +
  "      @Query(\"watermark\") watermark: String?): Single<ActivitySet>\n" +
  "\n" +
  "  /**\n" +
  "   * Send an activity\n" +
  "   *\n" +
  "   * @param conversationId Conversation ID\n" +
  "   * @param activity Activity to send\n" +
  "   */\n" +
  "  @POST(\"v3/directline/conversations/{conversationId}/activities\")\n" +
  "  fun Conversations_PostActivity(@Path(\"conversationId\") conversationId: String, @Body\n" +
  "      activity: Activity): Single<ResourceResponse>\n" +
  "\n" +
  "  /**\n" +
  "   * Upload file(s) and send as attachment(s)\n" +
  "   */\n" +
  "  @POST(\"v3/directline/conversations/{conversationId}/upload\")\n" +
  "  fun Conversations_Upload(@Path(\"conversationId\") conversationId: String, @Query(\"userId\")\n" +
  "      userId: String?): Single<ResourceResponse>\n" +
  "\n" +
  "  /**\n" +
  "   * Refresh a token\n" +
  "   */\n" +
  "  @POST(\"v3/directline/tokens/refresh\")\n" +
  "  fun Tokens_RefreshToken(): Single<Conversation>\n" +
  "\n" +
  "  /**\n" +
  "   * Generate a token for a new conversation\n" +
  "   */\n" +
  "  @POST(\"v3/directline/tokens/generate\")\n" +
  "  fun Tokens_GenerateTokenForNewConversation(@Body tokenParameters: TokenParameters?):\n" +
  "      Single<Conversation>\n" +
  "}\n"
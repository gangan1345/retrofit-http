package com.develop.http.api;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author Angus
 */
public interface CommonApi {
    @GET("{path}")
    Observable<ResponseBody> doGet(@Path(value = "path", encoded = true) String url, @QueryMap Map<String, Object> map);

    @GET("{path}")
    Observable<ResponseBody> doGet(@Path(value = "path", encoded = true) String url, @Body RequestBody map);

    @GET("{path}")
    Observable<ResponseBody> doGet(@Path(value = "path", encoded = true) String url);

    /**
     * 参数含有@Field和@FieldMap的请求必须加@FormUrlEncoded
     * Post请求最好用@Field,@Query也行，只是参数会暴露在Url中
     *
     * @param url
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> doPost(@Path(value = "path", encoded = true) String url, @FieldMap Map<String, Object> map);

    @POST("{path}")
    Observable<ResponseBody> doPost(@Path(value = "path", encoded = true) String url, @Body RequestBody body);

    @FormUrlEncoded
    @PUT("{path}")
    Observable<ResponseBody> doPut(@Path(value = "path", encoded = true) String url, @FieldMap Map<String, Object> map);

    @PUT("{path}")
    Observable<ResponseBody> doPut(@Path(value = "path", encoded = true) String url, @Body RequestBody body);

    @DELETE("{path}")
    Observable<ResponseBody> doDelete(@Path(value = "path", encoded = true) String url);

    @DELETE("{path}")
    Observable<ResponseBody> doDelete(@Path(value = "path", encoded = true) String url, @QueryMap Map<String, Object> maps);

    @HTTP(method = "DELETE", path = "{path}", hasBody = true)
    Observable<ResponseBody> doDelete(@Path(value = "path", encoded = true) String url, @Body RequestBody body);

    /**
     * 完整路径
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResponseBody> doGetFullPath(@Url String url);

    /**
     * 完整路径
     *
     * @param url
     * @param map
     * @return
     */
    @GET
    Observable<ResponseBody> doGetFullPath(@Url String url, @QueryMap Map<String, Object> map);

    /**
     * 参数含有@Field和@FieldMap的请求必须加@FormUrlEncoded
     * Post请求最好用@Field,@Query也行，只是参数会暴露在Url中
     *
     * @param url 完整路径
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> doPostFullPath(@Url String url, @FieldMap Map<String, Object> map);

    @Multipart
    @POST("{path}")
    Observable<ResponseBody> uploadFile(@Path(value = "path", encoded = true) String url,
                                        @QueryMap Map<String, Object> queryMap,
                                        @Part("description") RequestBody description,
                                        @Part MultipartBody.Part file);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFileFullPath(@Url String url,
                                                @QueryMap Map<String, Object> queryMap,
                                                @Part("description") RequestBody description,
                                                @Part MultipartBody.Part file);

    @Multipart
    @POST("{path}")
    Observable<ResponseBody> uploadFiles(
            @Path(value = "path", encoded = true) String url,
            @QueryMap Map<String, Object> queryMap,
            @PartMap Map<String, RequestBody> maps);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFilesFullPath(
            @Url String url,
            @QueryMap Map<String, Object> queryMap,
            @PartMap() Map<String, RequestBody> maps);

    //支持大文件
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String fileUrl);
}

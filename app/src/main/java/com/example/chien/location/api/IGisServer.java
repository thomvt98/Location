package com.example.chien.location.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IGisServer {
    /*@POST("/GisTableManager/Insert1")
    @FormUrlEncoded
    Call<GisTableReponse> listRepos(
            @Field("Name") String name,
            @Field("Parent") String parent,
            @Field("Type") String type,
            @Field("CreateBy") String createBy,
            @Field("List") List<NodeGis> list
    );*/

    @POST("/GisTableManager/Insert")
    @FormUrlEncoded
    Call<GisTableReponse> listCheck(
            @Field("Name") String name,
            @Field("Parent") String parent,
            @Field("Type") String type,
            @Field("CreateBy") String createBy,
            @Field("List1") String list1
    );


}

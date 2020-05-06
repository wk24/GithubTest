package com.pep.core.update.http;

import com.pep.core.update.bean.UpdateBaseData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @author wuk
 * @date 2020/1/9
 */
public interface ApiServiceUpdate {
    @GET
    Call<UpdateBaseData> getUpdateInfo(@Url String url, @QueryMap Map<String, Object> params);
}

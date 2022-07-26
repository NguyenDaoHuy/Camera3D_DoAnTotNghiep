package com.bhsoft.ar3d.data.remote

import com.bhsoft.ar3d.data.model.User
import com.bhsoft.ar3d.data.model.api.UserResponse
import io.reactivex.Observable
import retrofit2.http.GET
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

interface ApiUser {
    @GET("/api/users")
    fun getUsers(
    ): Observable<UserResponse<MutableList<User>>>
}
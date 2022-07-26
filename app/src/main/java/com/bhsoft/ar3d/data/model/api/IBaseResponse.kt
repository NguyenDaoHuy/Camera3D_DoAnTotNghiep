package com.bhsoft.ar3d.data.model.api

interface IBaseResponse {
    fun getErrorCode(): Int
    fun getMsg(): String?
    fun getStatus():Int
}
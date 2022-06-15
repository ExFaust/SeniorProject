package com.exfaust.core_api

import retrofit2.HttpException

class HttpUserVisibleException(
    val errorDescription: String?,
    cause: HttpException
) : RuntimeException(cause)
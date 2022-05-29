package com.exfaust.core_api.di

import com.exfaust.core.rx.RxTransformers
import com.exfaust.core.rx.ThroughObservableSourceTransformer
import com.exfaust.core_api.BuildConfig
import com.exfaust.core_api.ErrorResponse
import com.exfaust.core_api.HttpUserVisibleException
import com.exfaust.core_api.RxCallAdapterFactoryWrapper
import com.exfaust.core_api.serialization.SerializerBodyConverterFactory
import com.exfaust.core_api.serialization.SerializerStringConverterFactory
import com.exfaust.serialization.ComponentSerializer
import com.exfaust.serialization.CompositeSerializer
import com.exfaust.serialization.adapters.BasicTypesParameterAdapter
import io.reactivex.schedulers.Schedulers
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.mutate
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.multiton
import org.kodein.di.singleton
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import ru.mosgorpass.utils.exception.NoConnectivityException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

object ApiSerializationTag

fun apiModule(adapters: PersistentList<ComponentSerializer>) = DI.Module(name = "API") {
    bind<OkHttpClient>() with multiton {
        OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .apply {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)

                addInterceptor {
                    val request: Request = it.request()
                    val url: HttpUrl =
                        request.url.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY)
                            .build()
                    it.proceed(request.newBuilder().url(url).build())
                }
            }
            .build()
    }

    bind<Retrofit>() with singleton {
        Retrofit
            .Builder()
            .client(instance())
            .apply {
                val composer: (Retrofit) -> ThroughObservableSourceTransformer<Any> = {
                    RxTransformers
                        .mapError { exception ->
                            when (exception) {
                                is UnknownHostException,
                                is SocketTimeoutException,
                                is ConnectException -> NoConnectivityException
                                is HttpException -> {
                                    exception.response()?.errorBody()?.let { body ->
                                        val converter: Converter<ResponseBody, ErrorResponse> =
                                            it.responseBodyConverter(
                                                ErrorResponse::class.java,
                                                arrayOfNulls<Annotation>(0)
                                            )

                                        try {
                                            val errorConverted: ErrorResponse? =
                                                converter.convert(body)

                                            HttpUserVisibleException(
                                                errorConverted?.message,
                                                exception
                                            )
                                        } catch (e: Exception) {
                                            Timber.e("Failed to parse error response.")

                                            HttpUserVisibleException(null, exception)
                                        }
                                    } ?: exception
                                }
                                else -> exception
                            }
                        }
                }

                val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
                addCallAdapterFactory(RxCallAdapterFactoryWrapper.wrap(rxAdapter, composer))

                baseUrl(BuildConfig.BASE_URL)

                addConverterFactory(
                    SerializerStringConverterFactory(
                        CompositeSerializer(
                            adapters.mutate {
                                it.add(BasicTypesParameterAdapter)
                            }
                        )
                    )
                )

                addConverterFactory(SerializerBodyConverterFactory(instance(ApiSerializationTag)))
            }
            .build()
    }
}
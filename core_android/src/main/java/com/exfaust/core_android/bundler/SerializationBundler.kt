package com.exfaust.core_android.bundler

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.annotation.RequiresPermission
import com.exfaust.serialization.SerializationProvider
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

/**
 * Представляет методы для упаковки и распаковки данных в объекты Android SDK: [Bundle], [Intent] и другие, котороые основаны на работе [Bundle].
 * Упаковка и распаковка выполняется с помощью сериализации в строку с помощью указанного [SerializationProvider].
 * Безопасность передачи данных между приложениями гарантируется [_applicationId].
 */
class SerializationBundler(
    private val _serializationProvider: SerializationProvider,
    private val _applicationId: String
) : Bundler {
    @CheckReturnValue
    override fun keyOf(type: Type): String {
        return "$_applicationId:$type"
    }

    @RequiresPermission("android.permission.AUTHENTICATE_ACCOUNTS")
    override fun fromUserData(
        type: Type,
        accountManager: AccountManager,
        account: Account
    ): Any? {
        return _serializationProvider.deserializer.deserialize(
            type,
            accountManager.getUserData(account, keyOf(type))
        )
    }

    @RequiresPermission("android.permission.AUTHENTICATE_ACCOUNTS")
    override fun setUserData(
        type: Type,
        params: Any?,
        accountManager: AccountManager,
        account: Account
    ) {
        accountManager.setUserData(
            account, keyOf(type), _serializationProvider.serializer.serialize(
                type,
                params
            )
        )
    }

    @CheckReturnValue
    override fun fromBundle(
        type: Type,
        bundle: Bundle
    ): Any? {
        return bundle.getString(keyOf(type))?.let {
            @Suppress("UNCHECKED_CAST")
            _serializationProvider.deserializer.deserialize(type, it)
        }
    }

    @CheckReturnValue
    override fun intoBundle(
        type: Type,
        params: Any,
        bundle: Bundle
    ): Bundle =
        bundle
            .apply {
                putString(
                    keyOf(type),
                    _serializationProvider.serializer.serialize(type, params)
                )
            }

    @CheckReturnValue
    override fun fromIntent(
        type: Type,
        intent: Intent
    ): Any? {
        return intent.getStringExtra(keyOf(type))?.let {
            @Suppress("UNCHECKED_CAST")
            _serializationProvider.deserializer.deserialize(type, it)
        }
    }

    @CheckReturnValue
    override fun intoIntent(
        type: Type,
        params: Any,
        bundle: Intent
    ): Intent =
        bundle
            .apply {
                putExtra(
                    keyOf(type),
                    _serializationProvider.serializer.serialize(type, params)
                )
            }
}
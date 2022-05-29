package com.exfaust.core_android.bundler

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.annotation.RequiresPermission
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

inline fun <reified T> _bundlerTypeOf(): Type = object : TypeToken<T>() {}.type

@CheckReturnValue
inline fun <reified Params> Bundler.keyOf(): String {
    return keyOf(_bundlerTypeOf<Params>())
}

@RequiresPermission("android.permission.AUTHENTICATE_ACCOUNTS")
@CheckReturnValue
inline fun <reified Params> Bundler.fromUserData(
    accountManager: AccountManager,
    account: Account
): Params? = fromUserData(_bundlerTypeOf<Params>(), accountManager, account) as Params?

@RequiresPermission("android.permission.AUTHENTICATE_ACCOUNTS")
@CheckReturnValue
inline fun <reified Params> Bundler.toUserData(
    accountManager: AccountManager,
    account: Account,
    params: Any?
) = setUserData(_bundlerTypeOf<Params>(), params, accountManager, account)

@CheckReturnValue
inline fun <reified Params> Bundler.fromBundle(
    bundle: Bundle
): Params? = fromBundle(_bundlerTypeOf<Params>(), bundle) as Params?

@CheckReturnValue
inline fun <reified Params> Bundler.intoBundle(
    params: Any,
    bundle: Bundle = Bundle()
): Bundle = intoBundle(_bundlerTypeOf<Params>(), params, bundle)

@CheckReturnValue
inline fun <reified Params> Bundler.fromIntent(
    intent: Intent
): Params? = fromIntent(_bundlerTypeOf<Params>(), intent) as Params?

@CheckReturnValue
inline fun <reified Params> Bundler.intoIntent(
    params: Any,
    bundle: Intent = Intent()
): Intent = intoIntent(_bundlerTypeOf<Params>(), params, bundle)
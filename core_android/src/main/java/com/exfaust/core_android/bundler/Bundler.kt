package com.exfaust.core_android.bundler

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.annotation.RequiresPermission
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

/**
 * Представляет методы для упаковки и распаковки данных в объекты Android SDK: [Bundle], [Intent] и другие, котороые основаны на работе [Bundle].
 * Способ упаковки определяет реализация, но она она должна гарантировать уникальность типа объекта в контейнере, то есть в результате нескольких упаковок
 * в контейнер объекта типа **T**, в контейнере должен быть единственный объект, который при распаковки будет иметь тип **T**.
 *
 * _Это ограничение сделано для простоты использования, так как достаточно редко бывают случаи, когда нужно иметь объекты одних типов в контейнере.
 * Поэтому для идентификации объекта достаточно его типа._
 */
interface Bundler {
    @CheckReturnValue
    fun keyOf(type: Type): String

    @RequiresPermission("android.permission.AUTHENTICATE_ACCOUNTS")
    @CheckReturnValue
    fun fromUserData(
        type: Type,
        accountManager: AccountManager,
        account: Account
    ): Any?

    @RequiresPermission("android.permission.AUTHENTICATE_ACCOUNTS")
    @CheckReturnValue
    fun setUserData(
        type: Type,
        params: Any?,
        accountManager: AccountManager,
        account: Account
    )

    @CheckReturnValue
    fun fromBundle(
        type: Type,
        bundle: Bundle
    ): Any?

    @CheckReturnValue
    fun intoBundle(
        type: Type,
        params: Any,
        bundle: Bundle = Bundle()
    ): Bundle

    @CheckReturnValue
    fun fromIntent(
        type: Type,
        intent: Intent
    ): Any?

    @CheckReturnValue
    fun intoIntent(
        type: Type,
        params: Any,
        bundle: Intent = Intent()
    ): Intent
}
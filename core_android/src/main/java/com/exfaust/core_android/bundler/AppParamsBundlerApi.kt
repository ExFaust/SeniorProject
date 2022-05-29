package com.exfaust.core_android.bundler

import android.content.Intent
import android.os.Bundle
import javax.annotation.CheckReturnValue

@CheckReturnValue
inline fun <reified Params : Any> Params.intoBundle(
    bundler: Bundler,
    bundle: Bundle = Bundle()
): Bundle = bundler.intoBundle<Params>(this, bundle)

@CheckReturnValue
inline fun <reified Params : Any> Bundle.getParams(bundler: Bundler): Params? =
    bundler.fromBundle<Params>(this) as Params?

@CheckReturnValue
inline fun <reified Params : Any> Params.intoIntent(
    bundler: Bundler,
    bundle: Intent = Intent()
): Intent = bundler.intoIntent<Params>(this, bundle)

@CheckReturnValue
inline fun <reified Params : Any> Intent.getParams(bundler: Bundler) =
    bundler.fromIntent<Params>(this)
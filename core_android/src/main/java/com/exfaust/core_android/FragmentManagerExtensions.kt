package com.exfaust.core_android

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.reactivex.Observable
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun FragmentManager.fragmentsIncludeNested(): Sequence<Fragment> {
    return fragments.asSequence()
        .flatMap { fragment -> sequenceOf(fragment) + fragment.childFragmentManager.fragmentsIncludeNested() }
}

@CheckReturnValue
fun FragmentManager.registerCallbacks(callbacks: FragmentManager.FragmentLifecycleCallbacks): AutoCloseable {
    registerFragmentLifecycleCallbacks(callbacks, false)

    return AutoCloseable { unregisterFragmentLifecycleCallbacks(callbacks) }
}

inline fun <reified T : DialogFragment> FragmentManager.dismissAllDialogFragments() {
    fragments.filterIsInstance<T>().forEach { it.dismiss() }
}

sealed class FragmentLifecycleEvent {
    abstract val fragmentManager: FragmentManager
    abstract val fragment: Fragment

    data class ViewCreated(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment,
        val view: View,
        val savedInstanceState: Bundle?
    ) : FragmentLifecycleEvent()

    data class Stopped(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment
    ) : FragmentLifecycleEvent()

    data class Created(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment,
        val savedInstanceState: Bundle?
    ) : FragmentLifecycleEvent()

    data class Resumed(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment
    ) : FragmentLifecycleEvent()

    data class Attached(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment,
        val context: Context
    ) : FragmentLifecycleEvent()

    data class PreAttached(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment,
        val context: Context
    ) : FragmentLifecycleEvent()

    data class Destroyed(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment
    ) : FragmentLifecycleEvent()

    data class SaveInstanceState(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment,
        val outState: Bundle
    ) : FragmentLifecycleEvent()

    data class Started(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment
    ) : FragmentLifecycleEvent()

    data class ViewDestroyed(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment
    ) : FragmentLifecycleEvent()

    data class PreCreated(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment,
        val savedInstanceState: Bundle?
    ) : FragmentLifecycleEvent()

    data class Paused(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment
    ) : FragmentLifecycleEvent()

    data class Detached(
        override val fragmentManager: FragmentManager,
        override val fragment: Fragment
    ) : FragmentLifecycleEvent()
}

@MainThread
@CheckReturnValue
fun FragmentManager.onFragmentLifecycleChanged(
    recursive: Boolean = false
): Observable<FragmentLifecycleEvent> =
    Observable.create { emitter ->
        val callbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                emitter.onNext(
                    FragmentLifecycleEvent.ViewCreated(
                        fm,
                        f,
                        v,
                        savedInstanceState
                    )
                )
            }

            override fun onFragmentStopped(
                fm: FragmentManager,
                f: Fragment
            ) {
                emitter.onNext(FragmentLifecycleEvent.Stopped(fm, f))
            }

            override fun onFragmentCreated(
                fm: FragmentManager,
                f: Fragment,
                savedInstanceState: Bundle?
            ) {
                emitter.onNext(FragmentLifecycleEvent.Created(fm, f, savedInstanceState))
            }

            override fun onFragmentResumed(
                fm: FragmentManager,
                f: Fragment
            ) {
                emitter.onNext(FragmentLifecycleEvent.Resumed(fm, f))
            }

            override fun onFragmentAttached(
                fm: FragmentManager,
                f: Fragment,
                context: Context
            ) {
                emitter.onNext(FragmentLifecycleEvent.Attached(fm, f, context))
            }

            override fun onFragmentPreAttached(
                fm: FragmentManager,
                f: Fragment,
                context: Context
            ) {
                emitter.onNext(FragmentLifecycleEvent.PreAttached(fm, f, context))
            }

            override fun onFragmentDestroyed(
                fm: FragmentManager,
                f: Fragment
            ) {
                emitter.onNext(FragmentLifecycleEvent.Destroyed(fm, f))
            }

            override fun onFragmentSaveInstanceState(
                fm: FragmentManager,
                f: Fragment,
                outState: Bundle
            ) {
                emitter.onNext(FragmentLifecycleEvent.SaveInstanceState(fm, f, outState))
            }

            override fun onFragmentStarted(
                fm: FragmentManager,
                f: Fragment
            ) {
                emitter.onNext(FragmentLifecycleEvent.Started(fm, f))
            }

            override fun onFragmentViewDestroyed(
                fm: FragmentManager,
                f: Fragment
            ) {
                emitter.onNext(FragmentLifecycleEvent.ViewDestroyed(fm, f))
            }

            override fun onFragmentPreCreated(
                fm: FragmentManager,
                f: Fragment,
                savedInstanceState: Bundle?
            ) {
                emitter.onNext(FragmentLifecycleEvent.PreCreated(fm, f, savedInstanceState))
            }

            override fun onFragmentPaused(
                fm: FragmentManager,
                f: Fragment
            ) {
                emitter.onNext(FragmentLifecycleEvent.Paused(fm, f))
            }

            override fun onFragmentDetached(
                fm: FragmentManager,
                f: Fragment
            ) {
                emitter.onNext(FragmentLifecycleEvent.Detached(fm, f))
            }
        }

        registerFragmentLifecycleCallbacks(callbacks, recursive)
        emitter.setCancellable { unregisterFragmentLifecycleCallbacks(callbacks) }
    }
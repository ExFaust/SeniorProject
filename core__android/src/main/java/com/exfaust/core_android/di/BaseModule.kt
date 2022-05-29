package com.exfaust.core_android.di

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.exfaust.core_android.ViewModelLazy
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.bindings.subTypes
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.on
import org.kodein.di.provider
import org.kodein.di.singleton
import org.kodein.di.with
import org.kodein.type.jvmType

fun basicActivityModule(activity: AppCompatActivity) = DI.Module("activity") {
    bind<Context>(overrides = true).subTypes() with { type ->
        when (type.jvmType) {
            Context::class.java -> provider { activity }
            AppCompatActivity::class.java -> provider { activity }
            else -> throw NoSuchElementException()
        }
    }
}

fun basicFragmentModule(fragment: Fragment) = DI.Module("fragment") {
    bind<Fragment>() with singleton { fragment }

    bind<Context>(overrides = true).subTypes() with { type ->
        when (type.jvmType) {
            Context::class.java -> provider { fragment.requireContext() }
            AppCompatActivity::class.java -> provider { fragment.requireActivity() }
            else -> throw NoSuchElementException()
        }
    }
}

inline fun <reified VM : ViewModel, T> T.activityViewModel(): Lazy<VM> where T : DIAware, T : FragmentActivity =
    ViewModelLazy(VM::class, { this.viewModelStore }, { direct.instance() }, lifecycle)

inline fun <reified VM : ViewModel, T> T.activityScopedFragmentViewModel(): Lazy<VM> where T : DIAware, T : Fragment =
    ViewModelLazy(
        VM::class,
        { requireParentFragment().viewModelStore },
        { getFactoryInstance() },
        lifecycle
    )

inline fun <reified VM : ViewModel, T> T.fragmentViewModel(): Lazy<VM> where T : DIAware, T : Fragment =
    ViewModelLazy(VM::class, { this.viewModelStore }, { getFactoryInstance() }, lifecycle)

inline fun <reified VM : ViewModel> DI.Builder.bindViewModel(overrides: Boolean? = null): DI.Builder.TypeBinder<VM> {
    return bind<VM>(VM::class.java.simpleName, overrides)
}

fun <T> T.getFactoryInstance(
): ViewModelProvider.Factory where T : DIAware, T : Fragment {
    val viewModeFactory: ViewModelProvider.Factory by di.on(requireActivity()).instance()
    return viewModeFactory
}

fun DI.Builder.bindNavigation(activity: Activity, @IdRes id: Int) =
    bind<NavController>() with singleton { Navigation.findNavController(activity, id) }

fun DI.Builder.bindNavigationFromContainer(activity: AppCompatActivity, @IdRes id: Int) =
    bind<NavController>() with singleton {
        (activity.supportFragmentManager.findFragmentById(id) as NavHostFragment).navController
    }
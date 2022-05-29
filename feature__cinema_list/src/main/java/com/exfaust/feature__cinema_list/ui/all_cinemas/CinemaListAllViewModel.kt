package com.exfaust.feature__cinema_list.ui.all_cinemas

import com.exfaust.core_android.base.BaseStateViewModel
import com.exfaust.core_android.base.Events
import com.exfaust.core_android.getStateFlowable
import com.exfaust.core_android.setValueFlowable
import com.exfaust.feature__cinema_list.data.repository.CinemaListRepository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.collections.immutable.persistentListOf
import timber.log.Timber

class CinemaListAllViewModel(
    private val _repository: CinemaListRepository
) : BaseStateViewModel<CinemaListAllState, CinemaListAllAction>(CinemaListAllState.Idle) {

    override val actions: Flowable<Unit> = actionsSubject
        .distinctUntilChanged()
        .doOnEach {
            Timber.e("Action:%s", it.value?.javaClass?.simpleName)
        }
        .switchMap { action ->
            when (action) {
                CinemaListAllAction.StartLoading -> {
                    _repository.getCinemaList()
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            eventListener.value = Events.Loading
                        }
                        .onErrorReturn {
                            Timber.e(it)
                            eventListener.value = Events.Error(it)
                            persistentListOf()
                        }
                        .flatMapPublisher {
                            mutableState.setValueFlowable(CinemaListAllState.MainState(it))
                        }
                        .doFinally {
                            eventListener.value = Events.HideLoading
                        }
                }
                is CinemaListAllAction.OnCinemaClick -> {
                    mutableState.getStateFlowable<CinemaListAllState, CinemaListAllState.MainState>()
                        .map {
                            mutableState.value = it.toCinemaInfoState(action.cinemaId)
                        }
                }
            }
        }

    override fun onResume() {
        super.onResume()
        mutableState.value = CinemaListAllState.Start
    }
}
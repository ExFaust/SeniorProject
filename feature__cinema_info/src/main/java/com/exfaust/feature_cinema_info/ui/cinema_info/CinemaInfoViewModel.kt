package com.exfaust.feature_cinema_info.ui.cinema_info

import com.exfaust.core_android.base.BaseStateViewModel
import com.exfaust.core_android.base.Events
import com.exfaust.core_android.setValueFlowable
import com.exfaust.feature_cinema_info.data.repository.CinemaInfoRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CinemaInfoViewModel(
    private val _repository: CinemaInfoRepository
) : BaseStateViewModel<CinemaInfoState, CinemaInfoAction>(CinemaInfoState.Idle) {

    override val actions: Flowable<Unit> = actionsSubject
        .distinctUntilChanged()
        .doOnEach {
            Timber.e("Action:%s", it.value?.javaClass?.simpleName)
        }
        .switchMap { action ->
            when (action) {
                is CinemaInfoAction.StartLoading -> {
                    _repository.getCinemaInfo(action.id)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            eventListener.value = Events.Loading
                        }
                        .onErrorResumeNext {
                            Timber.e(it)
                            eventListener.value = Events.ErrorWithExit(it)
                            Single.never()
                        }
                        .flatMapPublisher {
                            mutableState.setValueFlowable(CinemaInfoState.MainState(it))
                        }
                        .doFinally {
                            eventListener.value = Events.HideLoading
                        }
                }
            }
        }
}
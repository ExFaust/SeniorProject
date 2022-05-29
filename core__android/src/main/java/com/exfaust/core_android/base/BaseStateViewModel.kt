package com.exfaust.core_android.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor

/**
 * Вью модель для более удобной работы с состоянием вью. Всё состояние описывается одной подпиской на [LiveData].
 * Все события вью описываются в одной подписке на [PublishProcessor]
 * Для прослушивания события до onCreate добавлен [BehaviorProcessor]
 */
abstract class BaseStateViewModel<State, Action : BaseAction>(initialState: State) :
    BaseViewModel() {

    protected val mutableState = MutableLiveData(initialState)
    val state: LiveData<State> = mutableState

    private val _actionsSubject: PublishProcessor<Action> = PublishProcessor.create()
    val actionsSubject: Flowable<Action> get() = _actionsSubject.hide().onBackpressureLatest()

    private val _earlyActionsSubject: BehaviorProcessor<Action> = BehaviorProcessor.create()
    val earlyActionsSubject: Flowable<Action> get() = _earlyActionsSubject.hide()

    private var _isInitActions: Boolean = false

    fun dispatchAction(action: Action) {
        _actionsSubject.onNext(action)
    }

    fun dispatchEarlyAction(action: Action) {
        _earlyActionsSubject.onNext(action)
    }

    abstract val actions: Flowable<Unit>

    override fun onCreate() {
        super.onCreate()

        _isInitActions = true
        actions.subscribe().scopedByDestroy()
    }

    override fun onResume() {
        if (!_isInitActions)
            actions.subscribe().scopedByPause()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()

        _isInitActions = false
    }
}
package com.exfaust.core_android.base

import com.exfaust.core.exception.ContractException

interface BaseAction

inline fun <State, reified SubState : State> BaseAction.ensureProperState(state: State?): SubState =
    state as? SubState ?: throw ContractException("Illegal state cast")
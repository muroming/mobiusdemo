package com.example.mobiusdemoapplication.tea

import androidx.lifecycle.LifecycleOwner


interface Feature<Msg, State, Effect> {
    val state: State
    fun subscribe(lifecycleOwner: LifecycleOwner, effectsConsumer: (Effect) -> Unit = {})
    fun unsubscribe(lifecycleOwner: LifecycleOwner)

    fun accept(msg: Msg)
    fun reduce(msg: Msg, state: State): Pair<State, Set<Effect>>
}

interface TeaEffectHandler<Effect, Msg> {
    suspend fun execute(eff: Effect, consumer: (Msg) -> Unit)
}

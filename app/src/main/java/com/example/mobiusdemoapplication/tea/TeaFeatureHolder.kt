package com.example.mobiusdemoapplication.tea

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import kotlinx.coroutines.launch

abstract class TeaFeatureHolder<Msg, State : Any, Effect : Any>(
    initState: State,
    private val initStateProducer: (suspend () -> State)?,
    private val initEffects: Set<Effect> = emptySet(),
    private val effectHandler: TeaEffectHandler<Effect, Msg>
) : ViewModel(), Feature<Msg, State, Effect> {

    constructor(
        initState: State,
        initEffects: Set<Effect> = emptySet(),
        effectHandler: TeaEffectHandler<Effect, Msg>
    ) : this(initState, initStateProducer = null, initEffects, effectHandler)

    private val platformEvents: MutableLiveData<Set<Effect>> = SingleLiveEvent()
    private var currentState by mutableStateOf(initState)

    fun init() {
        viewModelScope.launch {
            initStateProducer?.invoke()?.let {
                currentState = it
            }
            executeEffects(initEffects)
        }
    }

    override val state: State get() = currentState

    override fun subscribe(lifecycleOwner: LifecycleOwner, stateConsumer: (State) -> Unit, effectsConsumer: (Effect) -> Unit) {
        val effectsSetConsumer: (Set<Effect>) -> Unit = { it.forEach(effectsConsumer) }
        platformEvents.observe(lifecycleOwner, Observer(effectsSetConsumer))
    }

    override fun subscribe(lifecycleOwner: LifecycleOwner, effectsConsumer: (Effect) -> Unit) {
        val effectsSetConsumer: (Set<Effect>) -> Unit = { it.forEach(effectsConsumer) }
        platformEvents.observe(lifecycleOwner, Observer(effectsSetConsumer))
    }

    override fun unsubscribe(lifecycleOwner: LifecycleOwner) {
        platformEvents.removeObservers(lifecycleOwner)
    }

    override fun accept(msg: Msg) {
        val (newState, effects) = reduce(msg, currentState)
        currentState = newState

        executeEffects(effects)
    }

    private fun executeEffects(effects: Set<Effect>) {
        platformEvents.value = effects

        effects.forEach { eff ->
            viewModelScope.launch {
                effectHandler.execute(eff, ::accept)
            }
        }
    }
}
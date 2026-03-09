package com.stevestechstuff.cidr.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stevestechstuff.cidr.data.CidrRepository
import com.stevestechstuff.cidr.util.CidrOutput
import com.stevestechstuff.cidr.util.CidrUtils
import com.stevestechstuff.cidr.util.SplitOutput
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CalculatorState(
    val input: String = "",
    val isValid: Boolean = true,
    val output: CidrOutput? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CidrRepository(application)
    
    private val _calculatorState = MutableStateFlow(CalculatorState())
    val calculatorState: StateFlow<CalculatorState> = _calculatorState.asStateFlow()

    private val _splitterState = MutableStateFlow<List<SplitOutput>>(emptyList())
    val splitterState: StateFlow<List<SplitOutput>> = _splitterState.asStateFlow()

    val cheatSheet = CidrUtils.getCheatSheet()

    private var saveJob: Job? = null

    init {
        // Load initial state
        viewModelScope.launch {
            val lastCidr = repository.lastEnteredCidrFlow.first()
            updateCalculatorInput(lastCidr)
        }

        // Reactively update splitter state
        viewModelScope.launch {
            calculatorState.collectLatest { state ->
                if (state.isValid && state.input.isNotEmpty()) {
                    val subnets = CidrUtils.getSiblingSubnets(state.input)
                    if (subnets != null) {
                        _splitterState.value = subnets
                    }
                } else {
                    _splitterState.value = emptyList()
                }
            }
        }
    }

    fun updateCalculatorInput(input: String) {
        val trimmed = input.trim()
        val isValid = CidrUtils.isValidCidr(trimmed)
        val output = if (isValid) CidrUtils.calculateCidrOutput(trimmed) else null
        
        _calculatorState.value = _calculatorState.value.copy(
            input = input,
            isValid = isValid || input.isEmpty(), // don't show error if empty
            output = output
        )

        if (isValid) {
            saveJob?.cancel()
            saveJob = viewModelScope.launch {
                delay(500) // debounce
                repository.saveLastEnteredCidr(trimmed)
            }
        }
    }
}

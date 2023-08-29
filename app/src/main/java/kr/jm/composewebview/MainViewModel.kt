package kr.jm.composewebview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _undoSharedFlow = MutableSharedFlow<Unit>()
    val undoSharedFlow = _undoSharedFlow.asSharedFlow()

    private val _redoSharedFlow = MutableSharedFlow<Unit>()
    val redoSharedFlow = _redoSharedFlow.asSharedFlow()


    private val _backPressedFlow = MutableSharedFlow<Unit>()
    val backPressedFlow = _backPressedFlow.asSharedFlow()

    fun undo() {
        viewModelScope.launch {
            _undoSharedFlow.emit(Unit)
        }
    }

    fun redo() {
        viewModelScope.launch {
            _redoSharedFlow.emit(Unit)
        }
    }

    fun backPressed() {
        viewModelScope.launch {
            _backPressedFlow.emit(Unit)
        }
    }
}
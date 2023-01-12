package com.example.assignm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentViewModel:ViewModel() {
    private val _position = MutableLiveData<String>()
    val grade: LiveData<String>
        get() = _position

    init{
        _position.value = ""
    }

    /**
     * Callback called when the ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
    }

    fun clear(){
        _position.value = ""
    }

    fun setPosition(name: String ){
        _position.value = name
    }
    fun getPosition(): String? {
        return _position.value
    }
}
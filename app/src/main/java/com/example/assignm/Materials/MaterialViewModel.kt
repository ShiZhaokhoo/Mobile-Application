package com.example.assignm.Materials

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MaterialViewModel: ViewModel() {
    //current part number
    private val _partIndex = MutableLiveData<Int>()
    val partIndex: LiveData<Int>
        get() = _partIndex

    private val _serialIndex = MutableLiveData<Int>()
    val serialIndex: LiveData<Int>
        get() = _serialIndex

    init {
        _partIndex.value = 0
        _serialIndex.value = 0
    }

    fun setPartIndex(partIndex: Int ){
        _partIndex.value = partIndex
    }
    fun setSerialIndex(serialIndex: Int){
        _serialIndex.value = serialIndex
    }

    fun getPartIndex(): Int? {
        return _partIndex.value
    }
    fun getSerialIndex(): Int? {
        return _serialIndex.value
    }
}
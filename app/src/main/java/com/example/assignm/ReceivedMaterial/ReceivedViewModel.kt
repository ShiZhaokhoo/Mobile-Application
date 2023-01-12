package com.example.assignm.ReceivedMaterial


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Integer.parseInt
import java.lang.NumberFormatException

class ReceivedViewModel : ViewModel() {

    //current part number
    private val _partNumber = MutableLiveData<String>()
    val partNumber: LiveData<String>
        get() = _partNumber

    //current quantity
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int>
        get() = _quantity

    //current PIC
    private val _pic = MutableLiveData<String>()
    val pic: LiveData<String>
        get() = _pic

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    init{
        _partNumber.value = ""
        _quantity.value = 0
        _pic.value = ""
        _error.value = false
    }

    /**
     * Callback called when the ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
    }

    fun clear(){
        _partNumber.value = ""
        _quantity.value = 0
        _pic.value = ""
        _error.value = false
    }

    fun setPartNumber(partNumber: String ){
        _partNumber.value = partNumber
    }
    fun setQuantity(quantity: String){
        try {
            _quantity.value = parseInt(quantity)
            _error.value = false
        }catch (e: NumberFormatException){
            _error.value = true
        }
    }
    fun setPic(pic: String){
        _pic.value = pic
    }

    fun getPartNumber(): String? {
        return _partNumber.value
    }

    fun getQuantity(): Int? {
        return _quantity.value
    }

    fun getPic(): String?{
        return _pic.value
    }

    fun getError(): Boolean?{
        return _error.value
    }
}
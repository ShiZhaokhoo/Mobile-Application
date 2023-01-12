package com.example.assignm.RestockMaterial

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RestockViewModel : ViewModel() {

    //current serial no
    private val _serialNumber = MutableLiveData<String>()
    val serialNumber: LiveData<String>
        get() = _serialNumber

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

    //current Rack Number
    private val _rackNumber = MutableLiveData<String>()
    val rackNumber: LiveData<String>
        get() = _rackNumber

    init{
        _partNumber.value = ""
        _quantity.value = 0
        _pic.value = ""
        _serialNumber.value = ""
        _rackNumber.value = ""
    }

    fun clear(){
        _partNumber.value = ""
        _quantity.value = 0
        _pic.value = ""
        _serialNumber.value = ""
        _rackNumber.value = ""
    }

    /**
     * Callback called when the ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
    }

    fun setSerialNumber(serialNumber: String){
        _serialNumber.value = serialNumber
    }
    fun setPartNumber(partNumber: String ){
        _partNumber.value = partNumber
    }
    fun setQuantity(quantity: Int){
        _quantity.value = quantity
    }
    fun setPic(pic: String){
        _pic.value = pic
    }
    fun setRackNumber(rackNumber: String){
        _rackNumber.value = rackNumber
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
    fun getSerialNumber(): String?{
        return _serialNumber.value
    }
    fun getRackNumber(): String?{
        return _rackNumber.value
    }

}
package com.example.assignm.IssuedMaterial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IssuedViewModel : ViewModel() {

    //current part number
    private val _partNumber = MutableLiveData<String>()
    val partNumber: LiveData<String>
        get() = _partNumber

    //current serial number
    private val _serialNo = MutableLiveData<String>()
    val serialNo: LiveData<String>
        get() = _serialNo

    //current quantity
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int>
        get() = _quantity

    //left quantity
    private val _restQty = MutableLiveData<Int>()
    val restQty: LiveData<Int>
        get() = _restQty

    //current rack id
    private val _rackId = MutableLiveData<String>()
    val rackId: LiveData<String>
        get() = _rackId

    //current PIC
    private val _pic = MutableLiveData<String>()
    val pic: LiveData<String>
        get() = _pic

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    init {
        _quantity.value = 0
        _restQty.value = 0
        _partNumber.value = ""
        _serialNo.value = ""
        _pic.value = ""
        _rackId.value = ""
        _error.value = false
    }

    fun clear(){
        _quantity.value = 0
        _restQty.value = 0
        _partNumber.value = ""
        _serialNo.value = ""
        _pic.value = ""
        _rackId.value = ""
        _error.value = false
    }

    fun setSerialNumber(serialNumber: String){
        _serialNo.value = serialNumber
    }
    fun setPartNumber(partNumber: String ){
        _partNumber.value = partNumber
    }
    fun setQuantity(quantity: Int){
        _quantity.value = quantity
    }
    fun setRestQty(quantity: Int){
        _restQty.value = quantity
    }
    fun setPic(pic: String){
        _pic.value = pic
    }
    fun setRackNumber(rackNumber: String){
        _rackId.value = rackNumber
    }
    fun setError(error: Boolean){
        _error.value = error
    }

    fun getPartNumber(): String? {
        return _partNumber.value
    }
    fun getQuantity(): Int? {
        return _quantity.value
    }
    fun getRestQty(): Int? {
        return _restQty.value
    }
    fun getPic(): String?{
        return _pic.value
    }
    fun getSerialNumber(): String?{
        return _serialNo.value
    }
    fun getRackNumber(): String?{
        return _rackId.value
    }
    fun getError(): Boolean?{
        return error.value
    }
}
package com.example.assignm.ReturnMaterial

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReturnViewModel : ViewModel() {

    //current part number
    private val _partNumber = MutableLiveData<String>()
    val partNumber: LiveData<String>
        get() = _partNumber

    //current serial number
    private val _serialNo = MutableLiveData<String>()
    val serialNo: LiveData<String>
        get() = _serialNo

    private val _reasonReturn = MutableLiveData<String>()
    val reasonReturn: LiveData<String>
        get() = _reasonReturn

    private val _isOther = MutableLiveData<Boolean>()
    val isOther: LiveData<Boolean>
        get() = _isOther

    //current quantity
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int>
        get() = _quantity

    //left quantity
    private val _restQty = MutableLiveData<Int>()
    val restQty: LiveData<Int>
        get() = _restQty

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
        _reasonReturn.value = ""
        _error.value = false
    }

    fun clear(){
        _quantity.value = 0
        _restQty.value = 0
        _partNumber.value = ""
        _serialNo.value = ""
        _pic.value = ""
        _reasonReturn.value = ""
        _error.value = false
    }

    fun setSerialNumber(serialNumber: String){
        _serialNo.value = serialNumber
    }
    fun setPartNumber(partNumber: String ){
        _partNumber.value = partNumber
    }
    fun setReasonReturn(reasonReturn: String){
        _reasonReturn.value = reasonReturn
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
    fun setError(error: Boolean){
        _error.value = error
    }
    fun setIsOther(isOther: Boolean){
        _isOther.value = isOther
    }

    fun getPartNumber(): String? {
        return _partNumber.value
    }
    fun getPic(): String?{
        return _pic.value
    }
    fun getQuantity(): Int? {
        return _quantity.value
    }
    fun getRestQty(): Int? {
        return _restQty.value
    }
    fun getSerialNumber(): String?{
        return _serialNo.value
    }
    fun getReasonReturn(): String?{
        return _reasonReturn.value
    }
    fun getError(): Boolean?{
        return _error.value
    }
    fun getIsOther(): Boolean?{
        return _isOther.value
    }
}
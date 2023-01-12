package com.example.assignm

import android.content.ContentValues.TAG
import android.util.Log
import android.view.inspector.InspectionCompanion
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt
import java.lang.reflect.Constructor
import java.util.*
import kotlin.math.log

const val RECEIVE_STATUS = 1
const val RESTOCK_STATUS = 2
const val ISSUE_STATUS = 3
const val RETURN_STATUS = 4

class Database {
    companion object{
        val db = Firebase.firestore
        var employees: MutableList<Employee> = mutableListOf()
        var materials: MutableList<Material> = mutableListOf()
        var previousUpdatedMaterialDetail: Material.MaterialDetail = Material().MaterialDetail()

        // receive
        fun addMaterial(tempPartId:String, tempQuantity: Int, tempPic: String){
            var tempMaterial = Material()
            Log.i("Database-1",tempMaterial.getDailyReturn().toString())
            var serialNo = tempMaterial.generateSerialNo()
            var tempMaterialDetail = Material().MaterialDetail(serialNo, tempPic, tempQuantity, RECEIVE_STATUS)

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)+1
            val day = c.get(Calendar.DAY_OF_MONTH)

            val tempCurrentDate = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year.toString()

            tempMaterialDetail.setReceivedDate(tempCurrentDate)

            var newMaterial = true
            var totalQuantity = 0

            for(material in materials){
                if(material.getPartNumber() == tempPartId){
                    totalQuantity = tempMaterialDetail.getQuantity() + material.getTotalQuantity()
                    material.addMaterialDetails(tempMaterialDetail, tempMaterialDetail.getQuantity())
                    material.inflowTotalQuantity(tempMaterialDetail.getQuantity())
                    newMaterial = false
                    Log.i("Database","Not New Material")
                    checkDate()
                    material.setDailyReceive(material.getDailyReceive() + tempQuantity)
                    material.setMonthlyReceive(material.getMonthlyReceive() + tempQuantity)
                    material.setYearlyReceive(material.getYearlyReceive() + tempQuantity)
                    Material.setReceivePerDay(Material.getReceivePerDay() + tempQuantity)
                    tempMaterial = material
                }
            }

            if(newMaterial){
                Log.i("Database0",tempMaterial.getDailyReturn().toString())
                totalQuantity = tempMaterialDetail.getQuantity()
                tempMaterial = Material(tempPartId)
                tempMaterial.addMaterialDetails(tempMaterialDetail, totalQuantity)
                checkDate()
                Material.setReceivePerDay(Material.getReceivePerDay() + tempQuantity)
                tempMaterial.setDailyReceive(tempQuantity)
                tempMaterial.setMonthlyReceive(tempQuantity)
                tempMaterial.setYearlyReceive(tempQuantity)
                materials.add(tempMaterial)
                Log.i("Database1",tempMaterial.getDailyReturn().toString())
            }

            // write in database
            val material = hashMapOf(
                "partId" to tempPartId,
                "totalQuantity" to totalQuantity,
                "dailyReceive" to tempMaterial.getDailyReceive(),
                "dailyIssued" to tempMaterial.getDailyIssued(),
                "dailyReturn" to tempMaterial.getDailyReturn(),
                "monthlyReceive" to tempMaterial.getMonthlyReceive(),
                "monthlyIssued" to tempMaterial.getMonthlyIssued(),
                "monthlyReturn" to tempMaterial.getMonthlyReturn(),
                "yearlyReceive" to tempMaterial.getYearlyReceive(),
                "yearlyReturn" to tempMaterial.getYearlyReturn(),
                "yearlyIssued" to tempMaterial.getYearlyIssued()
            )
            Log.i("Database2",tempMaterial.getDailyReturn().toString())

            val previousData = hashMapOf(
                "previousDate" to materials.get(materials.size - 1).getPreviousDate(),
                "previousSerialNo" to materials.get(materials.size - 1).getPreviousSerialNo(),
                "previousMonth" to Material.previousMonth,
                "previousYear" to Material.previousYear,
                "totalReceiveOfTheDay" to Material.getReceivePerDay(),
                "totalIssuedOfTheDay" to Material.getIssuedPerDay()
            )

            val materialDetail = hashMapOf(
                "serialNumber" to tempMaterialDetail.getSerialNumber(),
                "pic" to tempMaterialDetail.getPic(),
                "quantity" to tempMaterialDetail.getQuantity(),
                "receiveDate" to tempMaterialDetail.getReceivedDate(),
                "rackInDate" to tempMaterialDetail.getRackInDate(),
                "rackOutDate" to tempMaterialDetail.getRackOutDate(),
                "reasonReturn" to tempMaterialDetail.getReasonReturn(),
                "rackId" to tempMaterialDetail.getRackId(),
                "status" to tempMaterialDetail.getStatus(),
                "restQuantity" to tempMaterialDetail.getRestQuantity(),
                "returnQuantity" to tempMaterialDetail.getReturnQuantity(),
                "rackPIC" to tempMaterialDetail.getRackPic(),
                "returnPIC" to tempMaterialDetail.getReturnPic(),
                "issuedPIC" to tempMaterialDetail.getIssuedPic(),
                "returnDate" to tempMaterialDetail.getReturnDate()
            )

            db.collection("previousRecord").document("previousRecordDetail")
                .set(previousData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added previousRecord")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Unable to save the data to firestore", it)
                }

            db.collection("materials").document(tempPartId)
                .set(material)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added material")

                    db.collection("materials").document(tempPartId).collection("materialDetails").document(tempMaterialDetail.getSerialNumber())
                        .set(materialDetail)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot added material detail")
                        }
                        .addOnFailureListener{
                            Log.w(TAG, "Error adding document", it)
                        }
                }
                .addOnFailureListener{
                    Log.w(TAG, "Error adding document", it)
                }

            previousUpdatedMaterialDetail = tempMaterialDetail
        }

        //restock
        fun restockMaterial(partNo: String, serialNo: String, rackId: String, pic: String){
            var tempMaterial = Material()
            var materialDetail = Material().MaterialDetail()

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)+1
            val day = c.get(Calendar.DAY_OF_MONTH)

            val tempCurrentDate = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year.toString()

            var countMaterial = 0
            var countDetail = 0

            for(tempMaterial in materials){
                var tempMaterialDetails: MutableList<Material.MaterialDetail> = tempMaterial.getMaterialDetails()
                if(partNo == tempMaterial.getPartNumber()) {
                    for (tempMaterialDetail in tempMaterialDetails) {
                        if (tempMaterialDetail.getSerialNumber() == serialNo) {
                            materialDetail = tempMaterialDetail
                            materialDetail.setRackId(rackId)
                            materialDetail.setRackInDate(tempCurrentDate)
                            materialDetail.setRackPIC(pic)
                            materialDetail.setStatus(RESTOCK_STATUS)
                            materials[countMaterial].replaceMaterialDetail(
                                materialDetail,
                                countDetail
                            )
                            break
                        }
                        ++countDetail
                    }
                }
                ++countMaterial
            }

            val dataToStore = hashMapOf(
                "serialNumber" to materialDetail.getSerialNumber(),
                "pic" to materialDetail.getPic(),
                "quantity" to materialDetail.getQuantity(),
                "receiveDate" to materialDetail.getReceivedDate(),
                "rackInDate" to materialDetail.getRackInDate(),
                "rackOutDate" to materialDetail.getRackOutDate(),
                "reasonReturn" to materialDetail.getReasonReturn(),
                "rackId" to materialDetail.getRackId(),
                "status" to materialDetail.getStatus(),
                "restQuantity" to materialDetail.getRestQuantity(),
                "returnQuantity" to materialDetail.getReturnQuantity(),
                "rackPIC" to materialDetail.getRackPic(),
                "returnPIC" to materialDetail.getReturnPic(),
                "issuedPIC" to materialDetail.getIssuedPic(),
                "returnDate" to materialDetail.getReturnDate()
            )

            db.collection("materials").document(partNo).collection("materialDetails").document(serialNo)
                .set(dataToStore)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added material detail")
                }
                .addOnFailureListener{
                    Log.w(TAG, "Error adding document", it)
                }
            previousUpdatedMaterialDetail = materialDetail
        }

        fun issueMaterial(partNo: String, serialNo: String, quantity: Int, pic: String){
            var material = Material()
            var materialDetail = Material().MaterialDetail()

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)+1
            val day = c.get(Calendar.DAY_OF_MONTH)

            val tempCurrentDate = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year.toString()

            var countMaterial = 0
            var countDetail = 0

            for(tempMaterial in materials){
                var tempMaterialDetails: MutableList<Material.MaterialDetail> = tempMaterial.getMaterialDetails()
                if(partNo == tempMaterial.getPartNumber()) {
                    for (tempMaterialDetail in tempMaterialDetails) {
                        if (tempMaterialDetail.getSerialNumber() == serialNo) {
                            materialDetail = tempMaterialDetail
                            materialDetail.updateRestQuantity(quantity)
                            materialDetail.setRackOutDate(tempCurrentDate)
                            materialDetail.setIssuedPIC(pic)

                            if(materialDetail.getRestQuantity() == 0){
                                materialDetail.setStatus(ISSUE_STATUS)
                            }
                            tempMaterial.outflowTotalQuantity(quantity)
                            tempMaterial.replaceMaterialDetail(
                                materialDetail,
                                countDetail
                            )
                            checkDate()
                            tempMaterial.setDailyIssued(tempMaterial.getDailyIssued()+quantity)
                            tempMaterial.setMonthlyIssued(tempMaterial.getMonthlyIssued()+quantity)
                            tempMaterial.setYearlyIssued(tempMaterial.getYearlyIssued()+quantity)
                            material = materials[countMaterial]
                            Material.setIssuedPerDay(Material.getIssuedPerDay() + quantity)
                            break
                        }
                        ++countDetail
                    }
                }
                ++countMaterial
            }

            val previousData = hashMapOf(
                "previousDate" to materials.get(materials.size - 1).getPreviousDate(),
                "previousSerialNo" to materials.get(materials.size - 1).getPreviousSerialNo(),
                "previousMonth" to Material.previousMonth,
                "previousYear" to Material.previousYear,
                "totalReceiveOfTheDay" to Material.getReceivePerDay(),
                "totalIssuedOfTheDay" to Material.getIssuedPerDay()
            )

            val dataToStore = hashMapOf(
                "serialNumber" to materialDetail.getSerialNumber(),
                "pic" to materialDetail.getPic(),
                "quantity" to materialDetail.getQuantity(),
                "receiveDate" to materialDetail.getReceivedDate(),
                "rackInDate" to materialDetail.getRackInDate(),
                "rackOutDate" to materialDetail.getRackOutDate(),
                "reasonReturn" to materialDetail.getReasonReturn(),
                "rackId" to materialDetail.getRackId(),
                "status" to materialDetail.getStatus(),
                "restQuantity" to materialDetail.getRestQuantity(),
                "returnQuantity" to materialDetail.getReturnQuantity(),
                "rackPIC" to materialDetail.getRackPic(),
                "returnPIC" to materialDetail.getReturnPic(),
                "issuedPIC" to materialDetail.getIssuedPic(),
                "returnDate" to materialDetail.getReturnDate()
            )

            val materialToStore = hashMapOf(
                "partId" to material.getPartNumber(),
                "totalQuantity" to material.getTotalQuantity(),
                "dailyReceive" to material.getDailyReceive(),
                "dailyIssued" to material.getDailyIssued(),
                "dailyReturn" to material.getDailyReturn(),
                "monthlyReceive" to material.getMonthlyReceive(),
                "monthlyIssued" to material.getMonthlyIssued(),
                "monthlyReturn" to material.getMonthlyReturn(),
                "yearlyReceive" to material.getYearlyReceive(),
                "yearlyReturn" to material.getYearlyReturn(),
                "yearlyIssued" to material.getYearlyIssued()
            )

            db.collection("previousRecord").document("previousRecordDetail")
                .set(previousData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added previousRecord")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Unable to save the data to firestore", it)
                }

            db.collection("materials").document(partNo).collection("materialDetails").document(serialNo)
                .set(dataToStore)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added material detail")
                }
                .addOnFailureListener{
                    Log.w(TAG, "Error adding document", it)
                }

            db.collection("materials").document(partNo)
                .set(materialToStore)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added material detail")
                }
                .addOnFailureListener{
                    Log.w(TAG, "Error adding document", it)
                }
            previousUpdatedMaterialDetail = materialDetail
        }

        fun returnMaterial(partNo: String, serialNo: String, quantity: Int, pic: String, reason: String){
            var material = Material()
            var materialDetail = Material().MaterialDetail()

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)+1
            val day = c.get(Calendar.DAY_OF_MONTH)

            val tempCurrentDate = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year.toString()

            var countMaterial = 0
            var countDetail = 0

            for(tempMaterial in materials){
                var tempMaterialDetails: MutableList<Material.MaterialDetail> = tempMaterial.getMaterialDetails()
                if(partNo == tempMaterial.getPartNumber()) {
                    for (tempMaterialDetail in tempMaterialDetails) {
                        if (tempMaterialDetail.getSerialNumber() == serialNo) {
                            materialDetail = tempMaterialDetail
                            materialDetail.updateReturnQuantity(quantity)
                            materialDetail.setReturnDate(tempCurrentDate)
                            materialDetail.setReturnPIC(pic)
                            materialDetail.setReasonReturn(reason)

                            if(materialDetail.getRestQuantity() == 0){
                                materialDetail.setStatus(RETURN_STATUS)
                            }
                            materials[countMaterial].outflowTotalQuantity(quantity)
                            materials[countMaterial].replaceMaterialDetail(
                                materialDetail,
                                countDetail
                            )
                            checkDate()
                            materials[countDetail].setDailyReturn(materials[countDetail].getDailyReturn()+quantity)
                            materials[countDetail].setMonthlyReturn(materials[countDetail].getMonthlyReturn()+quantity)
                            materials[countDetail].setYearlyReturn(materials[countDetail].getYearlyReturn()+quantity)
                            material = materials[countMaterial]
                            break
                        }
                        ++countDetail
                    }
                }
                ++countMaterial
            }

            val materialToStore = hashMapOf(
                "partId" to material.getPartNumber(),
                "totalQuantity" to material.getTotalQuantity(),
                "dailyReceive" to material.getDailyReceive(),
                "dailyIssued" to material.getDailyIssued(),
                "dailyReturn" to material.getDailyReturn(),
                "monthlyReceive" to material.getMonthlyReceive(),
                "monthlyIssued" to material.getMonthlyIssued(),
                "monthlyReturn" to material.getMonthlyReturn(),
                "yearlyReceive" to material.getYearlyReceive(),
                "yearlyReturn" to material.getYearlyReturn(),
                "yearlyIssued" to material.getYearlyIssued()
            )

            val dataToStore = hashMapOf(
                "serialNumber" to materialDetail.getSerialNumber(),
                "pic" to materialDetail.getPic(),
                "quantity" to materialDetail.getQuantity(),
                "receiveDate" to materialDetail.getReceivedDate(),
                "rackInDate" to materialDetail.getRackInDate(),
                "rackOutDate" to materialDetail.getRackOutDate(),
                "reasonReturn" to materialDetail.getReasonReturn(),
                "rackId" to materialDetail.getRackId(),
                "status" to materialDetail.getStatus(),
                "restQuantity" to materialDetail.getRestQuantity(),
                "returnQuantity" to materialDetail.getReturnQuantity(),
                "rackPIC" to materialDetail.getRackPic(),
                "returnPIC" to materialDetail.getReturnPic(),
                "issuedPIC" to materialDetail.getIssuedPic(),
                "returnDate" to materialDetail.getReturnDate()
            )

            db.collection("materials").document(partNo).collection("materialDetails").document(serialNo)
                .set(dataToStore)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added material detail")
                }
                .addOnFailureListener{
                    Log.w(TAG, "Error adding document", it)
                }

            db.collection("materials").document(partNo)
                .set(materialToStore)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added material detail")
                }
                .addOnFailureListener{
                    Log.w(TAG, "Error adding document", it)
                }
            previousUpdatedMaterialDetail = materialDetail
        }

        fun addEmployee(name:String,telNo:String,email:String,position:String,empId:String,password:String){
            var tempEmployee = Employee(name,telNo,email,position,empId,password)
            employees.add(tempEmployee)

            val dataToStore = hashMapOf(
                "name" to name,
                "telNo" to telNo,
                "email" to email,
                "position" to position,
                "empId" to empId,
                "password" to password
            )

            db.collection("employees").document(empId)
                .set(dataToStore)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added previousRecord")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Unable to save the data to firestore", it)
                }
        }

        fun getDataFromFirestore(){
            db.collection("employees")
                .get()
                .addOnSuccessListener { employeeDatabase ->
                    for(employee in employeeDatabase){
                        val employeeData = Employee(employee.data["name"].toString(),employee.data["telNo"].toString(),employee.data["email"].toString(),
                                        employee.data["position"].toString(),employee.data["empId"].toString(),employee.data["password"].toString())
                        employees.add(employeeData)
                    }
                }
                .addOnFailureListener {
                    Log.w(TAG, "Fail to get the database", it)
                }

            var getPrevious = false
            var tempMaterial: MutableList<Material> = mutableListOf()
            db.collection("materials")
                .get()
                .addOnSuccessListener { materialDatabase ->
                    for(material in materialDatabase){
                        val materialData = Material(material.data["partId"].toString(),
                            parseInt(material.data["totalQuantity"].toString()),
                            parseInt(material.data["dailyReceive"].toString()),
                            parseInt(material.data["dailyIssued"].toString()),
                            parseInt(material.data["dailyReturn"].toString()),
                            parseInt(material.data["monthlyReceive"].toString()),
                            parseInt(material.data["monthlyIssued"].toString()),
                            parseInt(material.data["monthlyReturn"].toString()),
                            parseInt(material.data["yearlyReceive"].toString()),
                            parseInt(material.data["yearlyIssued"].toString()),
                            parseInt(material.data["yearlyReturn"].toString()))

                        Log.w(TAG, getPrevious.toString())
                        if(!getPrevious){
                            db.collection("previousRecord")
                                .get()
                                .addOnSuccessListener { previousRecordData ->
                                    for (previousRecord in previousRecordData){
                                        materialData.setStaticData(previousRecord.data["previousDate"].toString(),
                                            parseInt(previousRecord.data["previousSerialNo"].toString()),
                                            parseInt(previousRecord.data["previousMonth"].toString()),
                                            parseInt(previousRecord.data["previousYear"].toString()),
                                            parseInt(previousRecord.data["totalReceiveOfTheDay"].toString()),
                                            parseInt(previousRecord.data["totalIssuedOfTheDay"].toString()))
                                    }
                                }
                            getPrevious = true
                        }

                        db.collection("materials").document(materialData.getPartNumber()).collection("materialDetails")
                            .get()
                            .addOnSuccessListener { materialDetailDatabase ->
                                for (materialDetail in materialDetailDatabase){
                                    val materialDetailData = Material().MaterialDetail(
                                        materialDetail.data["serialNumber"].toString(),
                                        materialDetail.data["pic"].toString(),
                                        parseInt(materialDetail.data["quantity"].toString()),
                                        materialDetail.data["receiveDate"].toString(),
                                        materialDetail.data["rackInDate"].toString(),
                                        materialDetail.data["rackOutDate"].toString(),
                                        materialDetail.data["rackId"].toString(),
                                        parseInt(materialDetail.data["status"].toString()),
                                        materialDetail.data["reasonReturn"].toString(),
                                        parseInt(materialDetail.data["restQuantity"].toString()),
                                        parseInt(materialDetail.data["returnQuantity"].toString()),
                                        materialDetail.data["rackPIC"].toString(),
                                        materialDetail.data["returnPIC"].toString(),
                                        materialDetail.data["issuedPIC"].toString(),
                                        materialDetail.data["returnDate"].toString()
                                    )
                                    materialData.addMaterialDetail(materialDetailData)
                                }
                            }
                        tempMaterial.add(materialData)
                    }
                    materials = tempMaterial
                    Log.w(TAG, "Data Get Successfully From Firebase")
                }
                .addOnFailureListener {
                    Log.w(TAG, "Fail to get the database", it)
                }

        }

        fun setPreviousRecord(){
            val previousData = hashMapOf(
                "previousDate" to Material.previousDate,
                "previousSerialNo" to Material.previousSerialNo,
                "previousMonth" to Material.previousMonth,
                "previousYear" to Material.previousYear,
                "totalReceiveOfTheDay" to Material.getReceivePerDay(),
                "totalIssuedOfTheDay" to Material.getIssuedPerDay()
            )
            db.collection("previousRecord").document("previousRecordDetail")
                .set(previousData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added previousRecord")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Unable to save the data to firestore", it)
                }

            var tempMaterial: Material
            for(material in materials){
                tempMaterial = material
                val material = hashMapOf(
                    "partId" to material.getPartNumber(),
                    "totalQuantity" to material.getTotalQuantity(),
                    "dailyReceive" to material.getDailyReceive(),
                    "dailyIssued" to material.getDailyIssued(),
                    "dailyReturn" to material.getDailyReturn(),
                    "monthlyReceive" to material.getMonthlyReceive(),
                    "monthlyIssued" to material.getMonthlyIssued(),
                    "monthlyReturn" to material.getMonthlyReturn(),
                    "yearlyReceive" to material.getYearlyReceive(),
                    "yearlyReturn" to material.getYearlyReturn(),
                    "yearlyIssued" to material.getYearlyIssued()
                )

                db.collection("materials").document(tempMaterial.getPartNumber())
                    .set(material)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added material")
                    }
                    .addOnFailureListener{
                        Log.w(TAG, "Error adding document", it)
                    }
            }
        }

        fun getPreviousRecord(){
            db.collection("previousRecord")
                .get()
                .addOnSuccessListener { previousRecordData ->
                    for (previousRecord in previousRecordData){
                        Material().setStaticData(previousRecord.data["previousDate"].toString(),
                            parseInt(previousRecord.data["previousSerialNo"].toString()),
                            parseInt(previousRecord.data["previousMonth"].toString()),
                            parseInt(previousRecord.data["previousYear"].toString()),
                            parseInt(previousRecord.data["totalReceiveOfTheDay"].toString()),
                            parseInt(previousRecord.data["totalIssuedOfTheDay"].toString()))
                    }
                }
        }

        fun checkDate(){
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)+1
            val day = c.get(Calendar.DAY_OF_MONTH)

            val tempCurrentDate = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year.toString()

            var changes = false

            if(tempCurrentDate != Material.previousDate){
                Material.previousDate = tempCurrentDate
                Material.previousSerialNo = 0
                Material.totalReceiveOfTheDay = 0
                Material.totalIssuedOfTheDay = 0
                changes = true

                for(material in materials){
                    material.setDailyReturn(0)
                    material.setDailyIssued(0)
                    material.setDailyReceive(0)
                }
                Log.d(TAG, "Date")
            }

            Log.d(TAG, month.toString() + "," + Material.previousMonth.toString())
            if(month != Material.previousMonth){
                Material.previousMonth = month
                changes = true
                for(material in materials){
                    material.setMonthlyReturn(0)
                    material.setMonthlyIssued(0)
                    material.setMonthlyReceive(0)
                }
                Log.d(TAG, "Month")
            }

            Log.d(TAG, year.toString() + "," + Material.previousYear.toString())
            if(year != Material.previousYear){
                Material.previousYear = year
                changes = true
                for(material in materials){
                    material.setYearlyReturn(0)
                    material.setYearlyIssued(0)
                    material.setYearlyReceive(0)
                }
                Log.d(TAG, "Year")
            }
            if(changes){
                setPreviousRecord()
            }
        }
    }
}

class Employee{
    private var name:String
    private var telNo:String
    private var email:String
    private var position:String
    private var empId:String
    private var password:String

    constructor(){
        name = ""
        telNo = ""
        email = ""
        position = ""
        empId = ""
        password = ""
    }

    constructor(tempName: String, tempTelNo: String, tempEmail: String, tempPosition: String, tempEmpId: String, tempPassword: String){
        name = tempName
        telNo = tempTelNo
        email = tempEmail
        position = tempPosition
        empId = tempEmpId
        password = tempPassword
    }

    fun getName():String{
        return name
    }
    fun getTelNo():String{
        return telNo
    }
    fun getEmail():String{
        return email
    }
    fun getPosition():String{
        return position
    }
    fun getEmpId():String{
        return empId
    }
    fun getPassword():String{
        return password
    }
}

class Material{
    private var materialDetails: MutableList<MaterialDetail>
    private var partNumber: String
    private var totalQuantity: Int
    private var dailyReceiveQuantity: Int
    private var dailyIssuedQuantity: Int
    private var dailyReturnQuantity: Int
    private var monthlyReceiveQuantity: Int
    private var monthlyIssuedQuantity: Int
    private var monthlyReturnQuantity: Int
    private var yearlyReceiveQuantity: Int
    private var yearlyIssuedQuantity: Int
    private var yearlyReturnQuantity: Int

    companion object{
        var previousMonth: Int = 0
        var previousYear: Int = 0
        var previousDate: String = ""
        var previousSerialNo: Int = 0
        var totalReceiveOfTheDay: Int = 0
        var totalIssuedOfTheDay: Int = 0

        fun getReceivePerDay():Int{
            return totalReceiveOfTheDay
        }
        fun getIssuedPerDay():Int{
            return totalIssuedOfTheDay
        }

        fun setReceivePerDay(qty: Int){
            totalReceiveOfTheDay = qty
        }
        fun setIssuedPerDay(qty: Int){
            totalIssuedOfTheDay = qty
        }

    }

    constructor(){
        materialDetails = mutableListOf()
        partNumber = ""
        totalQuantity = 0
        dailyReceiveQuantity = 0
        dailyIssuedQuantity = 0
        dailyReturnQuantity = 0
        monthlyReceiveQuantity = 0
        monthlyIssuedQuantity = 0
        monthlyReturnQuantity = 0
        yearlyReceiveQuantity = 0
        yearlyIssuedQuantity = 0
        yearlyReturnQuantity = 0
    }
    constructor(tempPartNo: String){
        partNumber = tempPartNo
        materialDetails = mutableListOf()
        totalQuantity = 0
        dailyReceiveQuantity = 0
        dailyIssuedQuantity = 0
        dailyReturnQuantity = 0
        monthlyReceiveQuantity = 0
        monthlyIssuedQuantity = 0
        monthlyReturnQuantity = 0
        yearlyReceiveQuantity = 0
        yearlyIssuedQuantity = 0
        yearlyReturnQuantity = 0
    }
    constructor(tempPartNo: String, tempTotalQty: Int,dReceiveQty: Int, dIssuedQty:Int, dReturnQty:Int, mReceiveQty:Int, mIssuedQty:Int, mReturnQty:Int, yReceiveQty:Int, yIssuedQty:Int, yReturnQty:Int){
        partNumber = tempPartNo
        totalQuantity = tempTotalQty
        materialDetails = mutableListOf()
        dailyReceiveQuantity = dReceiveQty
        dailyIssuedQuantity = dIssuedQty
        dailyReturnQuantity = dReturnQty
        monthlyReceiveQuantity = mReceiveQty
        monthlyIssuedQuantity = mIssuedQty
        monthlyReturnQuantity = mReturnQty
        yearlyReceiveQuantity = yReceiveQty
        yearlyIssuedQuantity = yIssuedQty
        yearlyReturnQuantity = yReturnQty
    }

    fun setStaticData(tempPreviousDate: String, tempPreviousSerialNo: Int, tempPreviousMonth: Int, tempPreviousYear: Int, tempTotalReceiveOfTheDay: Int, tempTotalIssuedOfTheDay: Int){
        previousSerialNo = tempPreviousSerialNo
        previousDate = tempPreviousDate
        previousMonth = tempPreviousMonth
        previousYear = tempPreviousYear
        totalIssuedOfTheDay = tempTotalIssuedOfTheDay
        totalReceiveOfTheDay = tempTotalReceiveOfTheDay
    }

    fun getPreviousDate(): String{
        return previousDate
    }
    fun getPreviousSerialNo(): Int{
        return previousSerialNo
    }
    fun getPartNumber(): String {
        return partNumber
    }
    fun getTotalQuantity(): Int {
        return totalQuantity
    }
    fun inflowTotalQuantity(inflowQty : Int){
        totalQuantity += inflowQty
    }
    fun outflowTotalQuantity(outflowQty: Int){
        totalQuantity -= outflowQty
    }

    fun addMaterialDetails(tempMaterialDetail: MaterialDetail, tempTotalQuantity: Int){
        materialDetails.add(tempMaterialDetail)
        totalQuantity += tempTotalQuantity
    }
    fun getMaterialDetails():MutableList<MaterialDetail>{
        return materialDetails
    }
    fun getMaterialDetail(index: Int): MaterialDetail{
        return materialDetails.get(index)
    }

    fun addMaterialDetail(detail: MaterialDetail){
        materialDetails.add(detail)
    }
    fun replaceMaterialDetail(detail: MaterialDetail,index: Int){
        materialDetails[index] = detail
    }

    fun generateSerialNo(): String{
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)+1
        val day = c.get(Calendar.DAY_OF_MONTH)

        val tempCurrentDate = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year.toString()

        if(tempCurrentDate != previousDate){
            previousDate = tempCurrentDate
            previousSerialNo = 0
        }
        return year.toString() + String.format("%02d", month) + String.format("%02d", day) + String.format("%04d", ++previousSerialNo)
    }

    fun getDailyReceive(): Int{
        return dailyReceiveQuantity
    }
    fun getMonthlyReceive(): Int{
        return monthlyReceiveQuantity
    }
    fun getYearlyReceive(): Int{
        return yearlyReceiveQuantity
    }
    fun getDailyIssued(): Int{
        return dailyIssuedQuantity
    }
    fun getMonthlyIssued(): Int{
        return monthlyIssuedQuantity
    }
    fun getYearlyIssued(): Int{
        return yearlyIssuedQuantity
    }
    fun getDailyReturn(): Int{
        return dailyReturnQuantity
    }
    fun getMonthlyReturn(): Int{
        return monthlyReturnQuantity
    }
    fun getYearlyReturn(): Int{
        return yearlyReturnQuantity
    }

    fun setDailyReceive(qty: Int){
        dailyReceiveQuantity = qty
    }
    fun setMonthlyReceive(qty: Int){
        monthlyReceiveQuantity = qty
    }
    fun setYearlyReceive(qty: Int){
        yearlyReceiveQuantity = qty
    }
    fun setDailyIssued(qty: Int){
        dailyIssuedQuantity = qty
    }
    fun setMonthlyIssued(qty: Int){
        monthlyIssuedQuantity = qty
    }
    fun setYearlyIssued(qty: Int){
        yearlyIssuedQuantity = qty
    }
    fun setDailyReturn(qty: Int){
        dailyReturnQuantity = qty
    }
    fun setMonthlyReturn(qty: Int){
        monthlyReturnQuantity = qty
    }
    fun setYearlyReturn(qty: Int) {
        yearlyReturnQuantity = qty
    }

    inner class MaterialDetail{
        private var serialNumber: String
        private var pic: String
        private var quantity: Int
        private var receivedDate: String
        private var rackInDate: String
        private var rackOutDate: String
        private var reasonReturn: String
        private var rackId: String
        private var status: Int //1 is receive but not yet rack//2 is rack//3 is issue//4 is return
        private var restQuantity: Int
        private var returnQuantity: Int
        private var rackPIC: String
        private var returnPIC: String
        private var issuedPIC: String
        private var returnDate: String

        constructor(){
            serialNumber = ""
            partNumber = ""
            quantity = 0
            pic = ""
            receivedDate = ""
            rackInDate = ""
            rackOutDate = ""
            reasonReturn = ""
            rackId = ""
            status = 0
            restQuantity = 0
            returnQuantity = 0
            rackPIC = ""
            returnPIC = ""
            issuedPIC = ""
            returnDate = ""
        }

        constructor(tempSerial: String, tempPic:String, tempQuantity:Int, tempStatus: Int){
            serialNumber = tempSerial
            pic = tempPic
            quantity = tempQuantity
            receivedDate = ""
            rackInDate = ""
            rackOutDate = ""
            rackId = ""
            status = tempStatus
            reasonReturn = ""
            restQuantity = tempQuantity
            returnQuantity = 0
            rackPIC = ""
            returnPIC = ""
            issuedPIC = ""
            returnDate = ""
        }

        constructor(tempSerial: String, tempPic:String, tempQuantity:Int, tempReceivedDate: String, tempRackInDate: String,
                    tempRackOutDate: String, tempRackId: String, tempStatus: Int, tempReasonReturn: String, tempRestQuantity: Int,
                    tempReturnQuantity: Int, tempRackPIC: String, tempReturnPIC: String, tempIssuedPIC: String, tempReturnDate: String){
            serialNumber = tempSerial
            pic = tempPic
            quantity = tempQuantity
            receivedDate = tempReceivedDate
            rackInDate = tempRackInDate
            rackOutDate = tempRackOutDate
            rackId = tempRackId
            status = tempStatus
            reasonReturn = tempReasonReturn
            restQuantity = tempRestQuantity
            returnQuantity = tempReturnQuantity
            rackPIC = tempRackPIC
            returnPIC = tempReturnPIC
            issuedPIC = tempIssuedPIC
            returnDate = tempReturnDate
        }

        fun getSerialNumber(): String {
            return serialNumber
        }
        fun getQuantity(): Int {
            return quantity
        }
        fun getPic(): String {
            return pic
        }
        fun getReceivedDate(): String {
            return receivedDate
        }
        fun getRackInDate(): String {
            return rackInDate
        }
        fun getRackOutDate(): String {
            return rackOutDate
        }
        fun getReasonReturn(): String {
            return reasonReturn
        }
        fun getStatus() : Int{
            return status
        }
        fun getRackId() : String{
            return rackId
        }
        fun getRackPic(): String{
            return rackPIC
        }
        fun getIssuedPic(): String{
            return issuedPIC
        }
        fun getReturnPic(): String{
            return returnPIC
        }
        fun getReturnDate(): String {
            return returnDate
        }

        fun setReceivedDate(tempReceivedDate:String){
            receivedDate = tempReceivedDate
        }
        fun setRackInDate(tempRackInDate:String){
            rackInDate = tempRackInDate
        }
        fun setRackOutDate(tempRackOutDate:String){
            rackOutDate = tempRackOutDate
        }
        fun setReasonReturn(reason : String){
            reasonReturn = reason
        }
        fun setRackId(tempRackId:String){
            rackId = tempRackId
        }
        fun setRackPIC(tempRackPIC: String){
            rackPIC = tempRackPIC
        }
        fun setIssuedPIC(tempIssuedPIC: String){
            issuedPIC = tempIssuedPIC
        }
        fun setReturnPIC(tempReturnPIC: String){
            returnPIC = tempReturnPIC
        }
        fun setReturnDate(tempReturnDate: String){
            returnDate = tempReturnDate
        }
        fun setStatus(tempStatus: Int){
            status = tempStatus
        }

        fun getRestQuantity(): Int{
            return restQuantity
        }
        fun updateRestQuantity(outflowQuantity: Int){
            restQuantity -= outflowQuantity
        }

        fun getReturnQuantity(): Int{
            return returnQuantity
        }
        fun updateReturnQuantity(tempReturnQuantity: Int){
            restQuantity -= tempReturnQuantity
            returnQuantity += tempReturnQuantity
        }
    }
}
package com.example.esm.report

data class ReportDataModel(
    var EvaluationTypeList:ArrayList<EvaluationTypeListData>?=null,
    var EvaluationList:ArrayList<EvaluationListData>?=null,
)


data class EvaluationTypeListData(
    var EvaluationTypeId:String?=null,
    var Name:String?=null,
    var ClassId:String?=null,
){
    override fun toString(): String {
        return Name.toString()
    }
}

data class EvaluationListData(
    var EvaluationId:String?=null,
    var Name:String?=null,
){
    override fun toString(): String {
        return Name.toString()
    }
}
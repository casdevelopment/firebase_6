package com.example.esm.policy

data class PolicyData(
    var Status :String?=null,
    var Data :ListData?=null,
)

data class ListData(
    var PolicyList :ArrayList<PolicyListData>?=null,
)

data class PolicyListData(
    var PolicyId:String?=null,
    var PolicyText:String?=null
)

package com.example.esm.results.models

data class KeyValueModel(
    val Id: String,
    val Item: String
) {
    override fun toString(): String {
        return Item
    }
}
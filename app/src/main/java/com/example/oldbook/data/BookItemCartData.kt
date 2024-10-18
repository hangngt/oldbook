package com.example.oldbook.data

data class BookItemCartData(
    val name: String? =null,
    val author: String? =null,
    val imageUrl: String? = null,
    val price: String? =null,
    val type: String? =null,
    val quantity: Int
){
    constructor() : this(null, null,  null, null, null,1)
}
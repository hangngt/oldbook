package com.example.oldbook.data

import java.io.Serializable

data class OrderData(
    var userId: String? = null,
    var nameuser: String? = null,
    var addressuser: String? = null,
    var phoneuser: String? = null,
    var totalAmount: Double = 0.0,
    var purchasedDate: String = "",
    var count: Int = 0,
    var product: MutableList<BookItemCartData>? = mutableListOf()) : Serializable {

//
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readDouble(),
//        parcel.readLong(),
//        parcel.readInt(),
//        parcel.readString(),
//        parcel.readValue(Double::class.java.classLoader) as? Double,
//        parcel.readString()
//    ) {
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeString(userId)
//        dest.writeString(nameuser)
//        dest.writeString(addressuser)
//        dest.writeString(phoneuser)
//        dest.writeDouble(totalAmount)
//        dest.writeLong(purchasedDate)
//        dest.writeInt(count)
//        dest.writeString(productName)
//        productPrice?.let { dest.writeDouble(it) }
//        dest.writeString(imageUrl)
//    }
//
//    companion object CREATOR : Parcelable.Creator<OrderData> {
//        override fun createFromParcel(parcel: Parcel): OrderData {
//            return OrderData(parcel)
//        }
//
//        override fun newArray(size: Int): Array<OrderData?> {
//            return arrayOfNulls(size)
//        }
//    }
//

}

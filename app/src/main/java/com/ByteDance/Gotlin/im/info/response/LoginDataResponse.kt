package com.ByteDance.Gotlin.im.info

import android.os.Parcel
import android.os.Parcelable

data class LoginDataResponse(
    val `data`: Data,
    val msg: String,
    val status: Int
)
data class Data(
    val success: Boolean,
    val user: User
)

data class User(
    val avatar: String?,
    val email: String?,
    val nickName: String?,
    val online: Boolean,
    val sex: String?,
    val userId: Int,
    val userName: String?
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0;
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(avatar)
        p0.writeString(email)
        p0.writeString(nickName)
        p0.writeByte(if (online) 1 else 0)
        p0.writeString(sex)
        p0.writeInt(userId)
        p0.writeString(userName)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
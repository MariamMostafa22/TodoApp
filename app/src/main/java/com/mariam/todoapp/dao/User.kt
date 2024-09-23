package com.mariam.todoapp.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val username: String,
    val password: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),   // Read id as Int
        parcel.readString() ?: "",  // Read email as String
        parcel.readString() ?: "",  // Read username as String
        parcel.readString() ?: ""   // Read password as String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)             // Write id as Int
        parcel.writeString(email)       // Write email as String
        parcel.writeString(username)    // Write username as String
        parcel.writeString(password)    // Write password as String
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

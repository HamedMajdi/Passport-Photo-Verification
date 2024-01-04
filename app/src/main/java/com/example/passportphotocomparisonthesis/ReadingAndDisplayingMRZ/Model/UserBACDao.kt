package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserBACDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userBAC: UserBAC)

    @Query(value = "SELECT * FROM user_bac WHERE id = :id")
    suspend fun getUser(id: Int): UserBAC?

    @Query("SELECT * FROM user_bac ORDER BY inserted_at DESC")
    suspend fun getAllUsers(): List<UserBAC>

    @Delete
    suspend fun deleteUser(userBAC: UserBAC)

    @Update
    suspend fun updateUser(userBAC: UserBAC)

    @Query("UPDATE user_bac SET " +
            "name_surname = :name, " +
            "gender = :gender, " +
            "nationality_full_name = :nationalityFull, " +
            "nationality_first_2_digits = :nationalityFirst2, " +
            "is_NFC_verified = :isNFCVerified, " +
            "travel_document_type = :documentType, " +
            "user_image = :userPhoto, " +
            "user_image_mime_type = :userPhotoMimeType " +
            "WHERE document_id = :documentId")

    suspend fun updateUserByID(documentId: String,
                               name: String,
                               gender: String,
                               nationalityFull: String,
                               nationalityFirst2: String,
                               documentType: Int,
                               userPhoto: ByteArray,
                               userPhotoMimeType: String,
                               isNFCVerified: Boolean)
}
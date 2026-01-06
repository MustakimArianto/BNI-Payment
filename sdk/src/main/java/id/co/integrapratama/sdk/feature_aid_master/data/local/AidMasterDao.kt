package id.co.integrapratama.sdk.feature_aid_master.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AidMasterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<AidMasterEntity>)

    @Query("SELECT * FROM aid_master")
    suspend fun getAidMasters(): List<AidMasterEntity>

    @Query("DELETE FROM aid_master")
    suspend fun clearAidMaster()
}
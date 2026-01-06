package id.co.integrapratama.sdk.feature_capk_master.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CapkMasterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CapkMasterEntity>)

    @Query("SELECT * FROM capk_master")
    suspend fun getCapkMasters(): List<CapkMasterEntity>

    @Query("DELETE FROM capk_master")
    suspend fun clearCapkMaster()
}
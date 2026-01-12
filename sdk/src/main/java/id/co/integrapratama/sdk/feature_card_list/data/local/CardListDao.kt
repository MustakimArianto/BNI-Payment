package id.co.integrapratama.sdk.feature_card_list.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CardListEntity>)

    @Query("SELECT * FROM card_list")
    suspend fun getCardLists(): List<CardListEntity>

    @Query("""
        SELECT * FROM card_list
        WHERE :pan BETWEEN minValue AND maxValue
        LIMIT 1
    """)
    suspend fun findCardByCardNumber(pan: Long): CardListEntity?

    @Query("DELETE FROM card_list")
    suspend fun clearCardList()

}
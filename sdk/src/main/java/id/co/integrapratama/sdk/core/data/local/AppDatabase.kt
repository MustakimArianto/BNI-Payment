package id.co.integrapratama.sdk.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.co.integrapratama.sdk.feature_aid_master.data.local.AidMasterDao
import id.co.integrapratama.sdk.feature_aid_master.data.local.AidMasterEntity
import id.co.integrapratama.sdk.feature_capk_master.data.local.CapkMasterDao
import id.co.integrapratama.sdk.feature_capk_master.data.local.CapkMasterEntity
import id.co.integrapratama.sdk.feature_card_list.data.local.CardListDao
import id.co.integrapratama.sdk.feature_card_list.data.local.CardListEntity
import id.co.integrapratama.sdk.feature_installment.data.local.InstallmentCardPrevTransactionEntity
import id.co.integrapratama.sdk.feature_installment.data.local.InstallmentCardTransactionDao
import id.co.integrapratama.sdk.feature_installment.data.local.InstallmentCardTransactionEntity
import id.co.integrapratama.sdk.feature_sale.data.local.CardPrevTransactionEntity
import id.co.integrapratama.sdk.feature_sale.data.local.CardTransactionDao
import id.co.integrapratama.sdk.feature_sale.data.local.CardTransactionEntity

@Database(
    entities = [
        CardListEntity::class,
        CardTransactionEntity::class,
        CardPrevTransactionEntity::class,
        AidMasterEntity::class,
        CapkMasterEntity::class,
        InstallmentCardTransactionEntity::class,
        InstallmentCardPrevTransactionEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aidMasterDao(): AidMasterDao

    abstract fun capkMasterDao(): CapkMasterDao

    abstract fun cardListDao(): CardListDao

    abstract fun cardTransactionDao(): CardTransactionDao

    abstract fun installmentCardTransactionDao(): InstallmentCardTransactionDao
}
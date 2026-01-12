package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.Calendar
import kotlin.random.Random

class StanManager(
    private val sharedPrefs: SharedPreferences
) {

    companion object {
        private const val LAST_RESET_DAY_KEY = "LAST_RESET_DAY_KEY"
        private const val STAN_KEY = "STAN_KEY"
    }

    fun getCurrentStan(): Long {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastResetDay = sharedPrefs.getInt(LAST_RESET_DAY_KEY, -1)
        if (currentDay != lastResetDay) {
            resetStan()
            saveLastResetDay(currentDay)
        }
        return sharedPrefs.getLong(STAN_KEY, 0L)
    }

    fun increaseStan() {
        saveStan(sharedPrefs.getLong(STAN_KEY, 0L) + 1)
    }

    fun increaseStanUpRandom() {
        val randomNumber = Random.nextLong(1, 1000)
        saveStan(sharedPrefs.getLong(STAN_KEY, 0L) + randomNumber)
    }

    fun setStanManual(value: Long) {
        saveStan(value)
    }

    private fun resetStan() {
        saveStan(0L)
    }

    fun getGenerateRandomStan(): Long{
        val randomInteger = Random.nextInt(1, 1000)
        saveStan(sharedPrefs.getLong(STAN_KEY, 0L) + randomInteger.toLong())
        return sharedPrefs.getLong(STAN_KEY, 0L)
    }

    private fun saveStan(value: Long) {
        sharedPrefs.edit { putLong(STAN_KEY, value) }
    }

    private fun saveLastResetDay(value: Int) {
        sharedPrefs.edit { putInt(LAST_RESET_DAY_KEY, value) }
    }
}
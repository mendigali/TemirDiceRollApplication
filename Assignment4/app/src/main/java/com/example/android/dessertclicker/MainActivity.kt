package com.example.android.dessertclicker

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.example.android.dessertclicker.databinding.ActivityMainBinding
import timber.log.Timber

const val KEY_REVENUE = "revenue_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"
const val KEY_TIMER_SECONDS = "timer_seconds_key"

class MainActivity : AppCompatActivity() {

    private var revenue = 0
    private var coinsSold = 0
    private lateinit var coinTimer: CoinTimer;

    private lateinit var binding: ActivityMainBinding

    data class Coin(val imageId: Int, val price: Int, val startProductionAmount: Int)

    private val allCoins = listOf(
        Coin(R.drawable.ethereum, 2456, 0),
        Coin(R.drawable.dogecoin, 1, 5),
        Coin(R.drawable.bitcoin, 37149, 10),
        Coin(R.drawable.tether, 1, 15),
        Coin(R.drawable.litecoin, 107, 20),
        Coin(R.drawable.solana, 92, 25),
        Coin(R.drawable.binance, 388, 30)
    )
    private var currentDessert = allCoins[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("onCreate called")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }

        coinTimer = CoinTimer(this.lifecycle)

        if (savedInstanceState != null) {
            revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
            coinsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
            coinTimer.secondsCount =
                savedInstanceState.getInt(KEY_TIMER_SECONDS, 0)
            showCurrentDessert()
        }

        binding.revenue = revenue
        binding.amountSold = coinsSold

        binding.dessertButton.setImageResource(currentDessert.imageId)
    }

    private fun onDessertClicked() {

        revenue += currentDessert.price
        coinsSold++

        binding.revenue = revenue
        binding.amountSold = coinsSold

        if (revenue >= 5_000) {
            findViewById<Button>(R.id.withdrawButton).visibility = View.VISIBLE
            findViewById<Button>(R.id.withdrawButton).setOnClickListener { withdrawCash() }
        }

        showCurrentDessert()
    }

    private fun withdrawCash() {
        Timber.i("withdrawCash called $$revenue")
        revenue = 0
        findViewById<Button>(R.id.withdrawButton).visibility = View.GONE
        binding.revenue = revenue
    }

    private fun showCurrentDessert() {
        var newDessert = allCoins[0]
        for (dessert in allCoins) {
            if (coinsSold >= dessert.startProductionAmount) {
                newDessert = dessert
            } else break
        }

        if (newDessert != currentDessert) {
            currentDessert = newDessert
            binding.dessertButton.setImageResource(newDessert.imageId)
        }
    }

    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(getString(R.string.share_text, coinsSold, revenue))
            .setType("text/plain")
            .intent
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this, getString(R.string.sharing_not_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState Called")
        outState.putInt(KEY_REVENUE, revenue)
        outState.putInt(KEY_DESSERT_SOLD, coinsSold)
        outState.putInt(KEY_TIMER_SECONDS, coinTimer.secondsCount)
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }
}

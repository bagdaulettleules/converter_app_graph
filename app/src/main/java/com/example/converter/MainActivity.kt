package com.example.converter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private val viewModel: ExRateViewModel by lazy {
        ViewModelProvider(this)[ExRateViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        viewModel.viewState.observe(this, { viewState ->
            when (viewState) {
                ExRateViewState.Empty -> Log.d("MyTag", "Empty", null)
                is ExRateViewState.Error -> Log.d("MyTag", viewState.message, null)
                is ExRateViewState.Loaded -> {
                    Log.d("myTag", "eur_brl ${viewState.exRate.getRate("EUR", "BRL")}", null)
                    Log.d("myTag", "brl_eur ${viewState.exRate.getRate("BRL", "EUR")}", null)
                    Log.d("myTag", "eur ${viewState.exRate.neighbors("EUR")}", null)

                    Log.d("myTag", "eur_bsd ${viewState.exRate.getRate("EUR", "BSD")}", null)
                    Log.d("myTag", "bsd_eur ${viewState.exRate.getRate("BSD", "EUR")}", null)
                }
            }
        }
        )
    }

}
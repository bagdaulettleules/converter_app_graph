package com.example.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.converter.model.DatabaseModel
import com.example.converter.model.ExRateAdjacency
import com.example.converter.model.ResponseModel
import com.example.converter.network.ExRateRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


/**
 * Created by Bagdaulet Tleules on 06.12.2021.
 * email: bagdaulettleules@gmail.com
 */
class ExRateViewModel : ViewModel() {
    private val repository = ExRateRepository.get()
    private val disposable = CompositeDisposable()
    private val effectiveDateDB get() = repository.getEffectiveDate()
    val viewState = MutableLiveData<ExRateViewState>()

    init {
        getExRatesFromNetwork()
    }


    private fun getExRatesFromNetwork() {
        disposable.add(
            repository.getExRatesFromNetwork()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this::onExRatesFetched, this::onError)
        )
    }

    private fun onExRatesFetched(responseModel: ResponseModel) {
        if (responseModel.rates.isEmpty()) {
            viewState.value = ExRateViewState.Empty
            return
        }
        val exRateAdjacency = ExRateAdjacency()
        exRateAdjacency.addVertex(responseModel.base)
        responseModel.also { header ->
            exRateAdjacency.addVertex(header.base)
            header.rates.map {
                DatabaseModel(
                    header.base,
                    it.key,
                    it.value,
                    header.date
                ).also { exRate ->
                    exRateAdjacency.apply {
                        addVertex(exRate.currTo)
                        connect(exRate.currFrom, exRate.currTo, exRate.rate)
                    }
                }
            }.also { insertIntoDB(header.date, it) }
        }
        viewState.value = ExRateViewState.Loaded(exRateAdjacency)
    }

    private fun insertIntoDB(date: Date, list: List<DatabaseModel>) {
        if (effectiveDateDB == date) {
            repository.addExRates(list)
        }
    }

    private fun onError(e: Throwable) {
        viewState.value = ExRateViewState.Error(e.message ?: "Something went wrong")
    }

}
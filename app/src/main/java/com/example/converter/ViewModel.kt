package com.example.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.converter.database.ExRateEntity
import com.example.converter.model.Model
import com.example.converter.network.Repository
import com.example.converter.network.ResponseModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


/**
 * Created by Bagdaulet Tleules on 06.12.2021.
 * email: bagdaulettleules@gmail.com
 */
class ViewModel : ViewModel() {
    private val repository = Repository.get()
    private val disposable = CompositeDisposable()
    private val effectiveDateDB get() = repository.getEffectiveDate()
    private val model = Model()
    val viewState = MutableLiveData<ViewState>()

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
            viewState.value = ViewState.Empty
            return
        }
        initGraph(responseModel)
        viewState.value = ViewState.Loaded(model)
    }

    private fun initGraph(responseModel: ResponseModel) {
        model.graph.addVertex(responseModel.base)
        responseModel.also { header ->
            model.graph.addVertex(header.base)
            header.rates.map { lines ->
                ExRateEntity(
                    header.base,
                    lines.key,
                    lines.value,
                    header.date
                ).also { exRate ->
                    model.graph.apply {
                        addVertex(exRate.currTo)
                        connect(exRate.currFrom, exRate.currTo, exRate.rate)
                    }
                }
            }.also { insertIntoDB(header.date, it) }
        }
    }

    private fun insertIntoDB(date: Date, list: List<ExRateEntity>) {
        if (effectiveDateDB == date) {
            repository.addExRates(list)
        }
    }

    private fun onError(e: Throwable) {
        viewState.value = ViewState.Error(e.message ?: "Something went wrong")
    }

    fun onCurrFromEntered(currency: String) {
        model.currFrom = currency
        viewState.value =
            ViewState.ResultReady(model.currFrom, model.currTo, model.rate, model.result)
    }

    fun onCurrToEntered(currency: String) {
        model.currTo = currency
        viewState.value =
            ViewState.ResultReady(model.currFrom, model.currTo, model.rate, model.result)
    }

    fun onAmountEntered(amount: Double) {
        model.amount = amount
        viewState.value =
            ViewState.ResultReady(model.currFrom, model.currTo, model.rate, model.result)
    }

    fun onCurrenciesSwitched() {
        val currency = model.currFrom
        model.apply {
            currFrom = this.currTo
            currTo = currency
        }
        viewState.value =
            ViewState.ResultReady(model.currFrom, model.currTo, model.rate, model.result)
    }
}
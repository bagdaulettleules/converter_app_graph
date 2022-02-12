package com.example.converter

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.main_fragment.*


/**
 * Created by Bagdaulet Tleules on 06.02.2022.
 * email: bagdaulettleules@gmail.com
 */
class MainFragment : Fragment() {

    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this)[ViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initEditText()
        ib_revert.setOnClickListener {
            viewModel.onCurrenciesSwitched()
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(
            viewLifecycleOwner
        ) { onViewStateChanges(it) }
    }

    private fun onViewStateChanges(viewState: ViewState) {
        when (viewState) {
            ViewState.Empty -> onEmptyList()
            is ViewState.Error -> onError(viewState.message)
            is ViewState.Loaded -> onListLoaded(viewState.model.graph.getList())
            is ViewState.ResultReady -> setResults(
                viewState.currFrom,
                viewState.currTo,
                viewState.rate,
                viewState.result
            )
        }
    }

    private fun setResults(currFrom: String, currTo: String, rate: Double, result: Double?) {
        actv_curr_from.text = Editable.Factory().newEditable(currFrom)
        actv_curr_to.text = Editable.Factory().newEditable(currTo)
        tv_rate.text = rate.toString()
        tv_result.text = result?.toString() ?: "0.0"
    }

    private fun onEmptyList() {
        Toast.makeText(context, R.string.empty_list, Toast.LENGTH_SHORT).show()
    }

    private fun onError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initCurrFromInput(currencies: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            currencies
        )
        actv_curr_from.apply {
            threshold = 2
            setAdapter(adapter)
            setOnItemClickListener { adapterView, view, i, l ->
                onItemClicked(adapter, i) {
                    viewModel.onCurrFromEntered(it)
                    til_curr_from.hint = getString(R.string.curr_from)
                }
            }
        }
    }

    private fun initCurrToInput(currencies: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            currencies
        )
        actv_curr_to.apply {
            threshold = 2
            setAdapter(adapter)
            setOnItemClickListener { adapterView, view, i, l ->
                onItemClicked(adapter, i) {
                    viewModel.onCurrToEntered(it)
                    til_curr_to.hint = getString(R.string.curr_to)
                }
            }
        }
    }

    private fun onListLoaded(currencies: List<String>) {
        initCurrFromInput(currencies)
        initCurrToInput(currencies)
    }

    private fun onItemClicked(
        adapter: ArrayAdapter<String>,
        position: Int,
        onItemSelected: (currency: String) -> Unit
    ) {
        val currency = adapter.getItem(position)
        if (currency != null) {
            onItemSelected(currency)
        }
    }

    private fun initEditText() {
        input_amount.afterTextChanged { viewModel.onAmountEntered(it?.toDouble() ?: 0.0) }
    }


    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
package com.example.cryptoapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.databinding.FragmentCoinDetailBinding
import com.squareup.picasso.Picasso

class CoinDetailFragment : Fragment() {
    private var _binding: FragmentCoinDetailBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentCoinDetailBinding in null")
    private val coinViewModel by lazy {
        ViewModelProvider(this)[CoinViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fSym = getSymbol()
        coinViewModel.getDetailInfo(fSym).observe(viewLifecycleOwner) {
            with(binding) {
                Picasso.get().load(it.imageUrl).into(imageView)
                textViewCurrencyDetail.text = it.fromSymbol + " / " + it.toSymbol
                textViewPriceDetail.text = it.price
                textViewMaxDay.text = it.highDay
                textViewMinDay.text = it.lowDay
                textViewLastMarket.text = it.lastMarket
                textViewLastUpdateDetail.text = it.lastUpdate
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSymbol(): String {
        return requireArguments().getString(FROM_SYMBOL, EMPTY_SYMBOL)
    }

    companion object {
        private const val FROM_SYMBOL = "fSym"
        private const val EMPTY_SYMBOL = "fSym"
        fun newInstance(fromSymbols: String): Fragment {
            return CoinDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(FROM_SYMBOL, fromSymbols)
                }
            }
        }
    }
}
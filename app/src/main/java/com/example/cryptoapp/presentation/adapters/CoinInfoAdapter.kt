package com.example.cryptoapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.CoinItemBinding
import com.example.cryptoapp.domain.CoinInfo
import com.squareup.picasso.Picasso

class CoinInfoAdapter(private val context: Context) :
    ListAdapter<CoinInfo, CoinInfoViewHolder>(CoinInfoItemCallback) {

    var onCoinClickListener: OnCoinClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val binding = CoinItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CoinInfoViewHolder(binding)
    }



    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coinPriceInfo = getItem(position)
        with(holder.binding) {
            Picasso.get().load(coinPriceInfo.imageUrl).into(ivLogoCoin)
            tvSymbols.text = String.format(
                context.resources.getString(R.string.symbols),
                coinPriceInfo.fromSymbol, coinPriceInfo.toSymbol
            )
            tvPrice.text = coinPriceInfo.price.toString()
            tvLastUpdate.text = String.format(
                context.resources.getString(R.string.last_update),
                coinPriceInfo.lastUpdate
            )
            root.setOnClickListener {
                onCoinClickListener?.onCoinClick(coinPriceInfo)
            }
        }
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinInfo)
    }
}
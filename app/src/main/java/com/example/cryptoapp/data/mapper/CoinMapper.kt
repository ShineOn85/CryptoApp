package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.model.CoinNamesListDto
import com.example.cryptoapp.domain.CoinInfo
import com.google.gson.Gson
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class CoinMapper @Inject constructor() {
    fun mapDtoToDbModel(dto: CoinInfoDto): CoinInfoDbModel = CoinInfoDbModel(
        fromSymbol = dto.fromSymbol,
        toSymbol = dto.toSymbol,
        price = dto.price,
        lastUpdate = dto.lastUpdate,
        highDay = dto.highDay,
        lowDay = dto.lowDay,
        lastMarket = dto.lastMarket,
        imageUrl = dto.imageUrl,
    )

    fun mapJsonContainerToListCoinInfo(jsonContainer: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val list = mutableListOf<CoinInfoDto>()
        val json = jsonContainer.json ?: return list
        val coinKeySet = json.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = json.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currency in currencyKeySet) {
                val coinPriceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currency),
                    CoinInfoDto::class.java
                )
                list.add(coinPriceInfo)
            }
        }
        return list
    }

    fun mapNamesListToString(namesListDto: CoinNamesListDto): String {
        return namesListDto.names?.map {
            it.coinName?.name
        }?.joinToString(",") ?: ""
    }


    fun mapDbModelToEntity(dbModel: CoinInfoDbModel): CoinInfo {
        return CoinInfo(
            fromSymbol = dbModel.fromSymbol,
            toSymbol = dbModel.toSymbol,
            price = dbModel.price,
            lastUpdate = convertTimestampToTime(dbModel.lastUpdate),
            highDay = dbModel.highDay,
            lowDay = dbModel.lowDay,
            lastMarket = dbModel.lastMarket,
            imageUrl = BASE_IMAGE_URL + dbModel.imageUrl
        )
    }

    private fun convertTimestampToTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp = java.sql.Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = "HH:mm:ss"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    companion object {
        private const val BASE_IMAGE_URL = "https://cryptocompare.com"
    }

}


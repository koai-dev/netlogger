package com.koai.netlogger.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.koai.netlogger.model.NetLogItem

/**
 * Basic interface implementation [INetLogRepository]
 *
 */
class NetLogRepositoryImpl : INetLogRepository {
    private val items = MutableLiveData(mutableListOf<NetLogItem>())

    override fun addItem(item: NetLogItem) {
        val list = items.value ?: mutableListOf()
        list.add(0, item)
        items.postValue(list)
    }

    override fun getItems(): LiveData<MutableList<NetLogItem>> = items

    override fun clear() {
        items.postValue(mutableListOf())
    }

}
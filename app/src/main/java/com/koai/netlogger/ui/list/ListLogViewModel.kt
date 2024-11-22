package com.koai.netlogger.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.koai.base.main.viewmodel.BaseViewModel
import com.koai.netlogger.model.NetLogItem
import com.koai.netlogger.repository.INetLogRepository

class ListLogViewModel(private val repository: INetLogRepository) : BaseViewModel() {
    private val _searchQuery = MutableLiveData("")
    private val searchQuery: LiveData<String> = _searchQuery

    fun setSearchQuery(query: String) {
        launchCoroutine {
            if (_searchQuery.value == query) return@launchCoroutine
            _searchQuery.postValue(query)
        }
    }

    fun clearLog() {
        launchCoroutine {
            repository.clear()
        }
    }

    fun items() = repository.getItems()

    fun getSearchedLogs(): LiveData<List<NetLogItem>> =
        MediatorLiveData<List<NetLogItem>>().apply {

            val onChanged = {
                val query = searchQuery.value ?: ""
                val queryList = query.split(" ")
                val ignoreList = queryList.filter { it.startsWith("-") }
                    .map { if (it.startsWith("-")) it.replace("-", "") else it }
                val searchQueryList = if (ignoreList.isEmpty()) queryList else queryList.filter {
                    !it.contains(getRegex(ignoreList))
                }
                val items = repository.getItems().value ?: mutableListOf()
                value = if (query.isEmpty())
                    items
                else
                    items.filter { itemIsValidSearchQuery(it, searchQueryList, ignoreList) }
            }

            addSource(searchQuery) { onChanged.invoke() }
            addSource(repository.getItems()) { onChanged.invoke() }
        }

    private fun itemIsValidSearchQuery(
        netLogItem: NetLogItem,
        searchQueryList: List<String>,
        ignoreList: List<String>
    ): Boolean {
        val url = netLogItem.url
        val containsInSearch = url.contains(getRegex(searchQueryList)) || searchQueryList.isEmpty()
        val notContainsInIgnore = !url.contains(getRegex(ignoreList)) || ignoreList.isEmpty()
        return containsInSearch && notContainsInIgnore
    }

    private fun getRegex(allQueryList: List<String>) =
        allQueryList.joinToString(separator = "|").toRegex()

}
package com.arboleda.tifloapp.menulibros

import android.widget.Filter
import com.arboleda.tifloapp.adapter.AdapterFileAdmin
import com.arboleda.tifloapp.model.ModelFile

class FilterFileAdmin: Filter {
    var filterList: ArrayList<ModelFile>

    var adapterFileAdmin: AdapterFileAdmin

    constructor(filterList: ArrayList<ModelFile>, adapterFileAdmin: AdapterFileAdmin) {
        this.filterList = filterList
        this.adapterFileAdmin = adapterFileAdmin
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        val result = FilterResults()

        if (constraint !=null && constraint.isNotEmpty()){

            constraint = constraint.toString().toLowerCase()
            var filteredModels = ArrayList<ModelFile>()
            for (i in filterList.indices){
                if (filterList[i].name.toLowerCase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            result.count = filteredModels.size
            result.values = filteredModels
        }
        else{
            result.count = filterList.size
            result.values = filterList
        }
        return result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterFileAdmin.fileArrayList = results.values as ArrayList<ModelFile>

        adapterFileAdmin.notifyDataSetChanged()
    }
}
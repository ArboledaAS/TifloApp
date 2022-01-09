package com.arboleda.tifloapp.menulibros

import android.widget.Filter
import com.arboleda.tifloapp.adapter.AdapterDeleteBook
import com.arboleda.tifloapp.model.ModelDeleteBook


class FilterBook :Filter{

    //Arraylist en la que se quiere buscar
    private var filterList: ArrayList<ModelDeleteBook>

    //Adaptador en el que se quiere filtrar
    private  var adapterDeleteBook: AdapterDeleteBook

    //Constructor
    constructor(
        filterList: ArrayList<ModelDeleteBook>,
        adapterDeleteBook: AdapterDeleteBook
    ) : super() {
        this.filterList = filterList
        this.adapterDeleteBook = adapterDeleteBook
    }


    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val result = FilterResults()

        //el valor no debe ser nulo ni vacío
        if (constraint !=null && constraint.isNotEmpty()){
           // el valor de búsqueda no es nulo o vacío

            //cambiar a mayúsculas o minúsculas para evitar la distinción entre mayúsculas y minúsculas
            constraint = constraint.toString().toUpperCase()
            val  filteredModel:ArrayList<ModelDeleteBook> = ArrayList()
            for (i in 0 until filterList.size){
                //validar
                if (filterList[i].name.toUpperCase().contains(constraint)){
                    // agregar la lista de filtro
                    filteredModel.add(filterList[i])
                }


            }
            result.count = filteredModel.size
            result.values = filteredModel
        }
        else{
            //el valor de búsqueda es nulo o vacío
            result.count = filterList.size
            result.values = filterList
        }
        return  result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filtro
        adapterDeleteBook.categoryArrayList = results.values as ArrayList<ModelDeleteBook>

        adapterDeleteBook.notifyDataSetChanged()
    }
}
package com.arboleda.tifloapp.model

class LibroData {
    /**set Data*/
    var name :String? = null
    var info:String? = null
    var img:String? = null
    var id:String = ""
    constructor(){}

    constructor(
            name:String?,
            info:String?,
            img:String?,
            id: String
    ){
        this.name = name
        this.info = info
        this.img = img
        this.id = id
    }
}
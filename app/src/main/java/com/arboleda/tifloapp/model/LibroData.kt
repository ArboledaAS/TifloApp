package com.arboleda.tifloapp.model

class LibroData {
    /**set Data*/
    var name :String? = null
    var info:String? = null
    var img:String? = null
    constructor(){}

    constructor(
            name:String?,
            info:String?,
            img:String?
    ){
        this.name = name
        this.info = info
        this.img = img
    }
}
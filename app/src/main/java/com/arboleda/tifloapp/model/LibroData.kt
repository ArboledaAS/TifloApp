package com.arboleda.tifloapp.model

class LibroData {
    /**variables, que deben coincidir con firebase */
    var name :String? = null
    var info:String? = null
    var img:String? = null
    var id:String = ""
    var pclave:String? = null

    //Constructor vacio requerido de firebase
    constructor(){}

    //Parametros del constructor
    constructor(
            name:String?,
            info:String?,
            img:String?,
            id: String,
            pclave: String?
    ){
        this.name = name
        this.info = info
        this.img = img
        this.id = id
        this.pclave = pclave
    }
}
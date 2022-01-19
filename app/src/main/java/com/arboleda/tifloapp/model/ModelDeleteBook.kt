package com.arboleda.tifloapp.model

class ModelDeleteBook {
    //variables, que deben coincidir con firebase
    var id:String = ""
    var name:String = ""
    var img:String? = ""
    var info:String = ""


    //Constructor vacio requerido de firebase
    constructor()

    //Parametros del constructor
    constructor(id: String, name: String, img: String, info: String) {
        this.id = id
        this.name = name
        this.img = img
        this.info = info
    }
}
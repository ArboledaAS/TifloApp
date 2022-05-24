package com.arboleda.tifloapp.model

class ModelUniversal {
    //variables, que deben coincidir con firebase
    var id:String = ""
    var name:String = ""
    var librosid:String = ""
    var pclave:String = ""//
    var img:String = ""
    var info:String = ""
    var poesiaid:String = ""
    var url:String = ""
    var tipo:String = ""






    //Constructor vacio requerido de firebase
    constructor()

    //Parametros del constructor
    constructor(id: String, name: String, librosid: String, pclave: String,
                img: String, info: String, poesiaid: String, url: String, tipo: String) {
        this.id = id
        this.name = name
        this.librosid = librosid
        this.pclave = pclave
        this.img = img
        this.info = info
        this.poesiaid = poesiaid
        this.url = url
        this.tipo = tipo
    }
}
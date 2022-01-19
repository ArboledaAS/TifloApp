package com.arboleda.tifloapp.model

class ModelFile {

    //variables
    var id:String = ""
    var name:String = ""
    var librosid:String = ""
    var descripcion:String = ""
    var url:String = ""

    //constructor (De la base de datos)
    constructor()

    //parametros del constructor
    constructor(id: String, name: String, librosid: String, descripcion: String, url: String) {
        this.id = id
        this.name = name
        this.librosid = librosid
        this.descripcion = descripcion
        this.url = url
    }



}
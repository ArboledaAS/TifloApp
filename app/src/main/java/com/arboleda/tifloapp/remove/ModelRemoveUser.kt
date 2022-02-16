package com.arboleda.tifloapp.remove

class ModelRemoveUser {

    var email:String = ""
    var id:String = ""
    var nivel:String = ""
    var name:String = ""

    constructor()

    constructor(email: String, id: String, nivel: String, name: String) {
        this.email = email
        this.id = id
        this.nivel = nivel
        this.name = name

    }
}
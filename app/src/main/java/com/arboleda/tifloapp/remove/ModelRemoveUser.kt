package com.arboleda.tifloapp.remove

class ModelRemoveUser {

    var email:String = ""
    var id:String = ""
    var nivel:String = ""

    constructor()

    constructor(email: String, id: String, nivel: String) {
        this.email = email
        this.id = id
        this.nivel = nivel

    }
}
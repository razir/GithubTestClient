package com.anton.github.data.entity

class Notification {
    var id: String? = null
    var reason: String? = null
    var unread: Boolean = false
    var url: String? = null
    var repository: Repository? = null
    var subject: Subject? = null
}

class Subject {
    val title: String? = null
    val url: String? = null
    val type: String? = null
}

package com.example.myapplication.help

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.dateString(): String {
    val formatter:DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    return this.format(formatter)
}
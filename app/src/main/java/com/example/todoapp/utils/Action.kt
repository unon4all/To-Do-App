package com.example.todoapp.utils

enum class Action {
    ADD, UPDATE, DELETE, DELETE_ALL, UNDO, NO_ACTION,
}

fun String?.toAction(): Action {
    return if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)
}
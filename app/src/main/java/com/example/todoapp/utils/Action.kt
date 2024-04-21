package com.example.todoapp.utils

/**
 * Enum representing various actions that can be performed.
 */
enum class Action {
    /**
     * Add action.
     */
    ADD,

    /**
     * Update action.
     */
    UPDATE,

    /**
     * Delete action.
     */
    DELETE,

    /**
     * Delete all action.
     */
    DELETE_ALL,

    /**
     * Undo action.
     */
    UNDO,

    /**
     * No action.
     */
    NO_ACTION,
}

/**
 * Converts a string representation to the corresponding Action enum value.
 * If the string is null or empty, returns [Action.NO_ACTION].
 *
 * @return The Action enum value corresponding to the given string, or [Action.NO_ACTION] if the string is null or empty.
 */
fun String?.toAction(): Action {
    return if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)
}

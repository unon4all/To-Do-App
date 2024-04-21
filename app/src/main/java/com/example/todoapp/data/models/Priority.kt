package com.example.todoapp.data.models

import androidx.compose.ui.graphics.Color
import com.example.todoapp.ui.theme.HighPriorityColor
import com.example.todoapp.ui.theme.LowPriorityColor
import com.example.todoapp.ui.theme.MediumPriorityColor


/**
 * Enum class representing different priorities with associated colors.
 */
enum class Priority(val color: Color) {
    /**
     * Represents high priority tasks.
     */
    HIGH(HighPriorityColor),

    /**
     * Represents medium priority tasks.
     */
    MEDIUM(MediumPriorityColor),

    /**
     * Represents low priority tasks.
     */
    LOW(LowPriorityColor),

    /**
     * Represents tasks with no specified priority.
     */
    NONE(Color.Transparent),
}

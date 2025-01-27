package org.jetbrains.kotlin.doctor.entity

import kotlinx.serialization.Serializable

@Serializable
data class ToolboxItem(
    val id: String,
    val build: String
)

@Serializable
data class ToolboxHistoryEntry(
    val action: String,
    val item: ToolboxItem,
    val timestamp: String
)

@Serializable
data class ToolboxHistory(
    val history: List<ToolboxHistoryEntry>
)
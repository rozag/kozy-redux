package com.github.rozag.redux.notes.model

data class Note(val id: String, val title: String, val body: String) {

    companion object {
        val EMPTY: Note = Note("", "", "")
    }

    override fun toString(): String = if (this == Note.EMPTY) {
        "EMPTY"
    } else {
        "Note" +
                "(" +
                "title=${titleToString()}, " +
                "body=${bodyToString()}" +
                ")"
    }

    private fun titleToString(): String = if (title.isEmpty()) {
        "EMPTY"
    } else {
        "'$title'"
    }

    private fun bodyToString(): String = if (body.isEmpty()) {
        "EMPTY"
    } else {
        val printableBody = body.replace("\n", " ")
        "'$printableBody'"
    }

}
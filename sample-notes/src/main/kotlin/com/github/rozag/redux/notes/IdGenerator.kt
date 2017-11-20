package com.github.rozag.redux.notes

import java.util.*

class IdGenerator {

    data class State(
            // Timestamp of last push, used to prevent local collisions if you push twice in one ms.
            val lastInstant: Long = -1L,
            // We generate 72-bits of randomness which get turned into 12 characters and appended to the
            // timestamp to prevent collisions with other clients.  We store the last characters we
            // generated because in the event of a collision, we'll use those same characters except
            // "incremented" by one.
            val lastRandChars: IntArray = IntArray(12)) {

        // We don't want to use the array in our equals/hashCode
        override fun equals(other: Any?) = lastInstant == (other as? State)?.lastInstant

        override fun hashCode() = lastInstant.toInt()
    }

    data class Result(val id: String, val nextState: State)

    // Modeled after base64 web-safe chars, but ordered by ASCII.
    private val pushChars = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz"
    private val defaultInstant = { System.currentTimeMillis() }
    private val stateLock = Any()

    private var globalState = State()

    /**
     * Generate a new id using a previous state. This method has no side-effects.
     *
     * @param[previousState] The previous state that will be modified.
     * @param[instant] The instant the id is being generated, otherwise [System.currentTimeMillis]
     *                 will be used.
     */
    fun generateNextId(previousState: State, instant: Long = defaultInstant()): Result {
        val duplicateTime = (instant == previousState.lastInstant)

        // Generate the first 8 characters
        val timeStampChars = CharArray(8).also { arr ->
            var instantLeft = instant
            (7 downTo 0).forEach {
                val module = instantLeft % 64L
                instantLeft /= 64L
                arr[it] = pushChars[module.toInt()]
            }
            if (instantLeft != 0L) {
                throw AssertionError("We should have converted the entire timestamp.")
            }
        }

        // Generate the last 12 characters
        val randChars = when (!duplicateTime) {
            true -> Random().let { r -> IntArray(12) { r.nextInt(64) } }
            else -> previousState.lastRandChars.copyOf()
                    .also { arr ->
                        val lastNot63 = arr.indexOfLast { it != 63 }
                        arr.fill(element = 0, fromIndex = lastNot63 + 1)
                        arr[lastNot63]++
                    }
        }

        val randCharsAsString = randChars.fold(StringBuilder(12)) { str, i -> str.append(pushChars[i]) }

        // Join both characters lists
        val id = String(timeStampChars) + randCharsAsString
        require(id.length == 20) { "Length should be 20." }

        return Result(id = id, nextState = State(lastInstant = instant, lastRandChars = randChars))
    }

    /**
     * Generate a new id using a global state. This method has side-effects.
     *
     * @param[instant] The instant the id is being generated, otherwise [System.currentTimeMillis]
     *                 will be used.
     * @param[threadSafe] Determines if a lock will be used for each execution, making it thread safe ^^
     */
    fun generateId(instant: Long = defaultInstant(), threadSafe: Boolean = false): String {
        val op = {
            generateNextId(globalState, instant)
                    .let { (id, nextState) ->
                        globalState = nextState
                        id
                    }
        }

        return when (threadSafe) {
            true -> synchronized(stateLock) { op() }
            else -> op()
        }
    }

}
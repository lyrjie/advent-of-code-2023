package day02

import getInput
import println
import kotlin.io.path.useLines
import kotlin.math.max

data class Game(
    val id: Int,
    val moves: List<Move>,
) {

    data class Move(
        val redCount: Int,
        val greenCount: Int,
        val blueCount: Int,
    )

}

private val partRegex = "(\\d+) (\\w+)".toRegex()

enum class Color(val id: String) { RED("red"), GREEN("green"), BLUE("blue") }

fun String.parseGame(): Game {
    val (rawTitle, rawMoves) = this.split(": ", limit = 2)
    val gameId = rawTitle.removePrefix("Game ").toInt()
    val moves = rawMoves.split("; ")
        .map { move ->
            val parts = move.split(", ")
                .associate { part ->
                    val match = partRegex.find(part)
                    match?.groupValues?.get(2) to match?.groupValues?.get(1)?.toInt()
                }
            Game.Move(
                redCount = parts[Color.RED.id] ?: 0,
                greenCount = parts[Color.GREEN.id] ?: 0,
                blueCount = parts[Color.BLUE.id] ?: 0,
            )
        }
    return Game(gameId, moves)
}

fun part1(input: Sequence<String>): Int {
    return input
        .map { it.parseGame() }
        .filter { game ->
            game.moves.all { it.redCount <= 12 && it.greenCount <= 13 && it.blueCount <= 14 }
        }
        .sumOf { it.id }
}

fun part2(input: Sequence<String>): Int {
    return input
        .map { it.parseGame() }
        .map { game ->
            val colorCounts = game.moves.fold(
                mutableMapOf(Color.RED to 0, Color.GREEN to 0, Color.BLUE to 0)
            ) { map, move ->
                map[Color.RED] = max(map[Color.RED] ?: 0, move.redCount)
                map[Color.GREEN] = max(map[Color.GREEN] ?: 0, move.greenCount)
                map[Color.BLUE] = max(map[Color.BLUE] ?: 0, move.blueCount)
                map
            }
            colorCounts.values.reduce { acc, i -> acc * i }
        }
        .sum()
}

fun main() {
    val input = getInput("day_2")
    input.useLines { part1(it) }.println()
    input.useLines { part2(it) }.println()
}

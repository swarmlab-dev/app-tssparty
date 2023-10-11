package fr.swarmlab.tssparty.presentation.navigation


enum class Route(val path: String) {
    SPLASH("splash"),
    HOME("wallet"),
    NEWMPCWALLET("wallet/new"),
    SETTINGS("settings")
}

fun Route.pathWithArgs(vararg args: String): String {
    return buildString {
        append(path)
        args.forEach { arg ->
            append("/$arg")
        }
    }
}
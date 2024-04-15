package net.theevilreaper.stelaris.cli.util

const val EMPTY_STRING: String = ""
const val SPACE_STRING: String = ""

const val TEMP_DIR_NAME: String = "stelaris-cli"
val HELP_MESSAGE: String = """
The following command arguments are available:
${
    CommandArgument.entries.joinToString(separator = "\n") { argument ->
        if (argument == CommandArgument.UPDATE) {
            return@joinToString "  -${argument.identifier} ${VersionPart.getTextualRepresentation()} : ${argument.helpMessage}"
        }
        "  -${argument.identifier} : ${argument.helpMessage}"
    }
}
"""

const val ARGUMENT_IDENTIFIER: Char = '-'

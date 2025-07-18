package net.theevilreaper.stelaris.cli.generator.dart.sound

enum class SoundType(val type: String) {

    BLOCK("block"),
    ENTITY("entity"),
    MUSIC("music"),
    ITEM("item"),
    AMBIENT("ambient")
    ;

    companion object {

        @JvmStatic
        fun fromType(type: String): SoundType? {
            return entries.find { it.type == type }
        }
    }
}
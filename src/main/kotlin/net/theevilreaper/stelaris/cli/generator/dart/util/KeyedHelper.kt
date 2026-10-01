package net.theevilreaper.stelaris.cli.generator.dart.util

import net.theevilreaper.dartpoet.DartFileBuilder
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassBuilder
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.STRING

/**
 * The name of the api file which contains the `Keyed` interface.
 */
const val KEYED_FILE: String = "keyed.dart"

/**
 * The `Keyed` interface from the api which is implemented by every generated enum.
 */
val KEYED: ClassName = ClassName("Keyed")

private const val KEY = "key"

/**
 * Lets the enum implement `Keyed` and adds a static `byKey` method to it.
 * The enum needs the properties `displayName` and `key`, and the file needs the lookup map from [keyedLookup].
 * @param className the name of the enum
 * @param implementsKeyed false if the enum already implements an interface which extends `Keyed`
 * @return the given [ClassBuilder]
 */
fun ClassBuilder.keyed(className: String, implementsKeyed: Boolean = true): ClassBuilder = apply {
    if (implementsKeyed) {
        implements(KEYED)
    }
    function(
        FunctionSpec.builder("byKey")
            .doc("Returns the entry with the given [key] or null if there is none.")
            .modifier(DartModifier.STATIC)
            .returns(ClassName(className, true))
            .parameter(ParameterSpec.positional(KEY, String::class).build())
            .addCode("return %L[key];", lookupName(className))
            .build()
    )
}

/**
 * Adds the lookup maps which are used by the `byKey` method of the given enums to the file.
 * @param classNames the names of the enums in the file
 * @param importKeyed false if the file doesn't reference `Keyed` directly
 * @param folderDepth the number of folders between the generation folder and the file, see [apiImport]
 * @return the given [DartFileBuilder]
 */
fun DartFileBuilder.keyedLookup(
    vararg classNames: String,
    importKeyed: Boolean = true,
    folderDepth: Int = 1,
): DartFileBuilder = apply {
    if (importKeyed) {
        directive(apiImport(KEYED_FILE, folderDepth))
    }
    classNames.forEach { className ->
        property(
            PropertySpec.builder(lookupName(className).removePrefix("_"), ClassName("Map").parameterizedBy(STRING, ClassName(className)))
                .modifiers(DartModifier.PRIVATE, DartModifier.FINAL)
                .initWith("{for (final e in %L.values) e.key: e}", className)
                .build()
        )
    }
}

private fun lookupName(className: String): String = "_${className.replaceFirstChar { it.lowercase() }}ByKey"

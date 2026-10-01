package net.theevilreaper.stelaris.cli.generator.dart.material

import net.minestom.server.item.Material
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import net.theevilreaper.stelaris.cli.util.StringHelper

/**
 * Generates the data which is needed to search through all materials without doing any string work at runtime.
 * Every material gets a normalized search key, the single words of its name and a bitmask of its categories.
 * The generated enums implement the interfaces from `api/material_search.dart`, which contains the search itself.
 */
internal object MaterialSearchGenerator {

    private const val KEY = "key"
    private const val SEARCH_KEY = "searchKey"
    private const val TERMS = "terms"
    private const val CATEGORIES = "categories"
    private const val MASK = "mask"
    private val enumModifier = DartModifier.FINAL

    fun mask(subType: MaterialSubType): Int = 1 shl subType.ordinal

    fun generateCategoryEnum(className: String): ClassSpec {
        val entries = MaterialSubType.entries.map {
            EnumEntrySpec.builder(StringHelper.toLowerCamelCase(it.type))
                .parameter(EnumParameterSpec.positional("%L", mask(it)))
                .build()
        }
        return ClassSpec.enumClass(className)
            .implements(ClassName("SearchCategory"))
            .enumProperties(*entries.toTypedArray())
            .properties(PropertySpec.builder(MASK, Int::class).modifier(enumModifier).build())
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(ParameterSpec.positional(MASK).build())
                    .build()
            )
            .build()
    }

    fun generateSearchEnum(
        className: String,
        materials: Collection<Material>,
        categories: (Material) -> Int,
    ): ClassSpec {
        val entries = materials.sortedBy { it.name() }.map {
            val rawName = it.name()
            val nameWithoutPrefix = rawName.replace("minecraft:", EMPTY_STRING)
            val terms = nameWithoutPrefix.split('_').filter(String::isNotEmpty).distinct()
            EnumEntrySpec.builder(StringHelper.toLowerCamelCase(nameWithoutPrefix))
                .parameter(EnumParameterSpec.positional("%C", rawName))
                .parameter(EnumParameterSpec.positional("%C", terms.joinToString(" ")))
                .parameter(EnumParameterSpec.positional("%L", terms.joinToString(prefix = "[", postfix = "]") { term -> "'$term'" }))
                .parameter(EnumParameterSpec.positional("%L", categories(it)))
                .build()
        }
        return ClassSpec.enumClass(className)
            .implements(ClassName("SearchableMaterial"))
            .enumProperties(*entries.toTypedArray())
            .properties(
                PropertySpec.builder(KEY, String::class).modifier(enumModifier).build(),
                PropertySpec.builder(SEARCH_KEY, String::class).modifier(enumModifier).build(),
                PropertySpec.builder(TERMS, ClassName("List").parameterizedBy(STRING)).modifier(enumModifier).build(),
                PropertySpec.builder(CATEGORIES, Int::class).modifier(enumModifier).build()
            )
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(
                        ParameterSpec.positional(KEY).build(),
                        ParameterSpec.positional(SEARCH_KEY).build(),
                        ParameterSpec.positional(TERMS).build(),
                        ParameterSpec.positional(CATEGORIES).build()
                    )
                    .build()
            )
            .build()
    }
}

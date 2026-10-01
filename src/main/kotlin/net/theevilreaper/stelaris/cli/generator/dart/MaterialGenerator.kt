package net.theevilreaper.stelaris.cli.generator.dart

import com.google.auto.service.AutoService
import net.minestom.server.component.DataComponents
import net.minestom.server.item.Material
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.material.MaterialSearchGenerator
import net.theevilreaper.stelaris.cli.generator.dart.material.MaterialSubGenerator
import net.theevilreaper.stelaris.cli.generator.dart.material.MaterialSubType
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

/**
 *
 * @author theEvilReaper
 */
@AutoService(Generator::class)
@CodeGenerator(name = "MaterialGenerator")
class MaterialGenerator : BaseGenerator(
    className = "Materials",
    packageName = "materials",
) {

    private val materialClassName = "Material"
    private val classDocumentation = "The file is generated. Don't change anything here"

    init {
        check(className.trim().isNotEmpty()) { "The class name can't be empty" }
    }

    /**
     * Generates the enum which contains all values for dart.
     * @param outputPath the path to store the content
     */
    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val models = Material.values()
        val enumFiles = mutableListOf<DartFile>()

        MaterialSubType.entries.forEach {
            val className = translateEnumClassName(it)
            val fileName = "${it.type}_materials"
            val enumClass = generateItemEnum(models, className) { mat -> mapTypeToBoolean(it, mat) }
            if (enumClass == null) return@forEach
            val file = DartFile.builder(fileName)
                .type(enumClass)
                .doc(classDocumentation)
                .build()
            enumFiles.add(file)
        }

        enumFiles.add(generateSearchFile(models))

        enumFiles.forEach { it.write(folder, baseDir = outputPath) }
    }

    /**
     * Generates the file which contains all materials in a flat, pre-normalized form for a text based search.
     * Unlike the category enums it also contains materials which don't belong to any category.
     * @param materials the materials to include
     * @return the created [DartFile]
     */
    private fun generateSearchFile(materials: Collection<Material>): DartFile {
        val categoryEnum = MaterialSearchGenerator.generateCategoryEnum("${materialClassName}Category")
        val searchEnum = MaterialSearchGenerator.generateSearchEnum("${materialClassName}SearchEntry", materials) { mat ->
            MaterialSubType.entries
                .filter { mapTypeToBoolean(it, mat) }
                .fold(0) { mask, type -> mask or MaterialSearchGenerator.mask(type) }
        }
        return DartFile.builder("material_search")
            .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../api/material_search.dart"))
            .type(categoryEnum, searchEnum)
            .doc(classDocumentation)
            .build()
    }

    private fun mapTypeToBoolean(subType: MaterialSubType, material: Material): Boolean {
        val prototype = material.prototype()
        return when (subType) {
            MaterialSubType.BLOCK -> material.block() != null
            MaterialSubType.ARMOR -> material.equipmentSlot()?.isArmor == true
            MaterialSubType.TOOL -> prototype.has(DataComponents.TOOL)
            MaterialSubType.WEAPON -> prototype.has(DataComponents.WEAPON)
            MaterialSubType.FOOD -> prototype.has(DataComponents.FOOD)
            MaterialSubType.DYE -> prototype.has(DataComponents.DYE)
            MaterialSubType.SPAWN_EGG -> material.name().endsWith("_spawn_egg")
        }
    }

    private fun translateEnumClassName(materialSubType: MaterialSubType): String {
        val prefix = StringHelper.toLowerCamelCase(materialSubType.type).replaceFirstChar { it.uppercase() }
        return "$prefix$materialClassName"
    }

    private inline fun generateItemEnum(
        materials: Collection<Material>,
        className: String,
        crossinline filter: (Material) -> Boolean,
    ): ClassSpec? {
        if (materials.isEmpty()) return null
        val filteredModels = filterMaterials(materials, filter)
        val enumClass = MaterialSubGenerator.generateBlockMaterialEnum(className, filteredModels)
        return enumClass.build()
    }

    private inline fun filterMaterials(
        materials: Collection<Material>,
        crossinline filter: (Material) -> Boolean,
    ): List<Material> {
        return materials.filter { filter(it) }
    }
}
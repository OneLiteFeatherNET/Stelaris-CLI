package net.theevilreaper.stelaris.cli.generator.dart

import net.minestom.server.item.Material
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.material.MaterialSubGenerator
import net.theevilreaper.stelaris.cli.generator.dart.material.MaterialSubType
import java.nio.file.Path

/**
 *
 * @author theEvilReaper
 */
class MaterialGenerator : BaseGenerator<Material>(
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
     * @param javaPath the path to store the content
     */
    override fun generate(javaPath: Path) {
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

        if (enumFiles.isEmpty()) return
        enumFiles.forEach { it.write(javaPath) }
    }

    private fun mapTypeToBoolean(subType: MaterialSubType, material: Material): Boolean {
        return when (subType) {
            MaterialSubType.BLOCK -> material.isBlock
            MaterialSubType.ARMOR -> material.isArmor
        }
    }

    private fun translateEnumClassName(materialSubType: MaterialSubType): String {
        return "${materialSubType.type.replaceFirstChar { it.uppercase() }}$materialClassName"
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

    /**
     * Returns the name from [MaterialGenerator] which is used as identifier.
     * @return the given name
     */
    override fun getName() = "MaterialGenerator"
}
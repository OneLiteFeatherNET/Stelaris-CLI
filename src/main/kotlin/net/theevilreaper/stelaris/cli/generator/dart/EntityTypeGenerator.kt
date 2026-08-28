package net.theevilreaper.stelaris.cli.generator.dart

import net.minestom.server.entity.EntityType
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.entity.EntitySubGenerator
import net.theevilreaper.stelaris.cli.generator.dart.entity.EntitySubType
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

class EntityTypeGenerator : BaseGenerator(
    className = "Entities",
    packageName = "entities",
) {

    private val entityClassName = "EntityType"
    private val classDocumentation = "The file is generated. Don't change anything here"

    init {
        check(className.trim().isNotEmpty()) { "The class name can't be empty" }
    }

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val models = EntityType.values()
        val enumFiles = mutableListOf<DartFile>()

        EntitySubType.entries.forEach { subType ->
            val className = translateEnumClassName(subType)
            val fileName = "${subType.type}_entities"
            val enumClass = generateEntityEnum(models, className) { subType.matches(it) }
            if (enumClass == null) return@forEach
            val file = DartFile.builder(fileName)
                .type(enumClass)
                .doc(classDocumentation)
                .build()
            enumFiles.add(file)
        }

        if (enumFiles.isEmpty()) return
        enumFiles.forEach { it.write(folder) }
    }

    private fun translateEnumClassName(entitySubType: EntitySubType): String {
        val prefix = StringHelper.toLowerCamelCase(entitySubType.type).replaceFirstChar { it.uppercase() }
        return "$prefix$entityClassName"
    }

    private inline fun generateEntityEnum(
        entities: Collection<EntityType>,
        className: String,
        crossinline filter: (EntityType) -> Boolean,
    ): ClassSpec? {
        if (entities.isEmpty()) return null
        val filteredEntities = entities.filter { filter(it) }
        val enumClass = EntitySubGenerator.generateEntityEnum(className, filteredEntities)
        return enumClass.build()
    }

    override fun getName() = "EntityTypeGenerator"
}
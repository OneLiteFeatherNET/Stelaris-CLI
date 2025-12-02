package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import java.nio.file.Path

class EnchantmentRegistryGenerator : BaseGenerator(
    className = "EnchantmentRegistry",
    packageName = "enchantment"
) {

    override fun generate(javaPath: Path) {
        val enchantmentFolder = javaPath.resolve(packageName)
        checkPackageFolder(javaPath, packageName)

        val registry = ClassSpec.abstractClass(className)
            .property {
                PropertySpec.builder("all", Set::class.parameterizedBy(ClassName("Enchantment")))
                    .modifiers(*setOf(DartModifier.STATIC, DartModifier.FINAL).toTypedArray())
                    .initWith(
                        "%L",
                        buildCodeBlock {
                            addStatement("<%T>{", ClassName("Enchantment"))
                            val groups = EnchantmentGroup.entries
                            indent()
                            for (group in groups) {
                                val className = group.classPart.replaceFirstChar { it.uppercase() }
                                val enchantmentClass = ClassName("${className}Enchantment")
                                addStatement("...%T.values,", enchantmentClass)
                            }
                            unindent()
                            add("}")
                        }
                    )
                    .build()
            }
            .build()
        val enumFile = DartFile.builder("enchantment_registry.dart")
            .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../api/enchantment"))
            .doc("The file is generated. Don't change anything here")
            .type(registry)
            .build()
        enumFile.write(enchantmentFolder)
    }

    override fun getName(): String = "EnchantmentRegistryGenerator"


}
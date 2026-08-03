package net.theevilreaper.stelaris.cli.generator.dart

import net.minestom.testing.Env
import net.minestom.testing.extension.MicrotusExtension
import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MicrotusExtension::class)
class MaterialGeneratorTest : GenerationTestBase() {

    @Test
    fun `test material generation`(env: Env) {
        val generator = MaterialGenerator()
        assertEquals("MaterialGenerator", generator.getName())

        generator.generate(generationPath)

        val materialsFolder = generationPath.resolve("materials").toFile()
        assertTrue(materialsFolder.exists(), "Expected materials package folder to exist")

        val generatedFiles = materialsFolder.listFiles()
        assertNotNull(generatedFiles)
        assertTrue(generatedFiles!!.isNotEmpty(), "Expected generated material files to not be empty")

        val sampleFile = generatedFiles.first()
        assertTrue(sampleFile.name.endsWith(".dart"), "Generated file should be a Dart file")
        val content = sampleFile.readText()
        assertTrue(content.contains("final String displayName;"), "Generated material file should declare displayName property")
        assertTrue(content.contains("final String material;"), "Generated material file should declare material property")
        assertTrue(content.contains("final int maxStackSize;"), "Generated material file should declare maxStackSize property")
    }
}

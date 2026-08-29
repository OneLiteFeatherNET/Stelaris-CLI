package net.theevilreaper.stelaris.cli.generator.dart

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EntityTypeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test entity type generation`() {
        val generator = EntityTypeGenerator()

        generator.generate(generationPath)

        val entitiesFolder = generationPath.resolve("entities").toFile()
        assertTrue(entitiesFolder.exists(), "Expected entities package folder to exist")

        val generatedFiles = entitiesFolder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(6, generatedFiles!!.size, "Expected exactly 6 entity files to be generated")

        val expectedFiles = mapOf(
            "animal_entities.dart" to "enum AnimalEntityType",
            "monster_entities.dart" to "enum MonsterEntityType",
            "water_entities.dart" to "enum WaterEntityType",
            "projectile_entities.dart" to "enum ProjectileEntityType",
            "vehicle_entities.dart" to "enum VehicleEntityType",
            "display_entities.dart" to "enum DisplayEntityType"
        )

        for ((fileName, expectedEnum) in expectedFiles) {
            val file = entitiesFolder.resolve(fileName)
            assertTrue(file.exists(), "Expected $fileName to exist")
            val content = file.readText()
            assertTrue(content.contains(expectedEnum), "Expected $fileName to contain '$expectedEnum'")
            assertTrue(content.contains("final String displayName;"), "$fileName should declare displayName property")
            assertTrue(content.contains("final String type;"), "$fileName should declare type property")
        }
    }
}

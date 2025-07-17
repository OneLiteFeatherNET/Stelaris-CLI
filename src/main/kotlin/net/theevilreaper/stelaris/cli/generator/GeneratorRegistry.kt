package net.theevilreaper.stelaris.cli.generator

import net.theevilreaper.stelaris.cli.generator.dart.*
import net.theevilreaper.stelaris.cli.generator.dart.bossbar.BossBarColorGenerator
import net.theevilreaper.stelaris.cli.generator.dart.bossbar.BossBarFlagGenerator
import net.theevilreaper.stelaris.cli.generator.dart.bossbar.BossBarOverlayGenerator
import net.theevilreaper.stelaris.cli.generator.dart.enchantment.EnchantmentGenerator
import net.theevilreaper.stelaris.cli.generator.dart.sound.SoundEventGenerator
import net.theevilreaper.stelaris.cli.generator.dart.sound.SoundSourceGenerator

/**
 * The [GeneratorRegistry] holds all available generators which can be used to generate dart files.
 * @version 1.0.0
 * @since 1.0.0
 * @property generators the set of all available generators
 * @author theEvilReaper
 */
class GeneratorRegistry {

    private val generators: Set<Generator> = setOf(
        BossBarColorGenerator(),
        BossBarFlagGenerator(),
        BossBarOverlayGenerator(),
        EnchantmentGenerator(),
        EntityTypeGenerator(),
        FrameTypeGenerator(),
        MaterialGenerator(),
        SoundSourceGenerator(),
    )

    /**
     * Returns all available generators from the registry
     * @return a set of all available generators
     */
    fun getGenerators(): Set<Generator> = generators

    /**
     * Returns all available generators which match the given predicate
     * @param predicate the predicate to filter the generators
     * @return a set of all available generators which match the predicate
     */
    fun getGenerators(predicate: (Generator) -> Boolean): Set<Generator> {
        return generators.filter { predicate(it) }.toSet()
    }
}
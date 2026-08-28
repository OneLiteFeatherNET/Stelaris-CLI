package net.theevilreaper.stelaris.cli.generator

import net.theevilreaper.stelaris.cli.generator.dart.*
import net.theevilreaper.stelaris.cli.generator.dart.banner.BannerPatternGenerator
import net.theevilreaper.stelaris.cli.generator.dart.bossbar.BossBarColorGenerator
import net.theevilreaper.stelaris.cli.generator.dart.bossbar.BossBarFlagGenerator
import net.theevilreaper.stelaris.cli.generator.dart.bossbar.BossBarOverlayGenerator
import net.theevilreaper.stelaris.cli.generator.dart.color.DyeColorGenerator
import net.theevilreaper.stelaris.cli.generator.dart.damage.DamageTypeGenerator
import net.theevilreaper.stelaris.cli.generator.dart.enchantment.EnchantmentGenerator
import net.theevilreaper.stelaris.cli.generator.dart.enchantment.EnchantmentGroupGenerator
import net.theevilreaper.stelaris.cli.generator.dart.entity.variant.EntityVariantGenerator
import net.theevilreaper.stelaris.cli.generator.dart.item.*
import net.theevilreaper.stelaris.cli.generator.dart.painting.PaintingVariantGenerator
import net.theevilreaper.stelaris.cli.generator.dart.sound.SoundEventGenerator
import net.theevilreaper.stelaris.cli.generator.dart.sound.SoundSourceGenerator
import net.theevilreaper.stelaris.cli.generator.dart.sound.SoundTypeGenerator
import net.theevilreaper.stelaris.cli.generator.dart.villager.VillagerTypeGenerator

/**
 * The [GeneratorRegistry] holds all available generators which can be used to generate dart files.
 * @version 1.0.0
 * @since 1.0.0
 * @property generators the set of all available generators
 * @author theEvilReaper
 */
class GeneratorRegistry {

    private val generators: Set<Generator> = setOf(
        BannerPatternGenerator(),
        BossBarColorGenerator(),
        BossBarFlagGenerator(),
        BossBarOverlayGenerator(),
        DamageTypeGenerator(),
        DyeColorGenerator(),
        EnchantmentGenerator(),
        EnchantmentGroupGenerator(),
        EntityTypeGenerator(),
        EntityVariantGenerator(),
        FireworkShapeGenerator(),
        FrameTypeGenerator(),
        InstrumentGenerator(),
        ItemRarityGenerator(),
        JukeboxSongGenerator(),
        MaterialGenerator(),
        MapPostProcessingGenerator(),
        PaintingVariantGenerator(),
        SoundSourceGenerator(),
        SoundEventGenerator(),
        SoundTypeGenerator(),
        TrimMaterialGenerator(),
        TrimPatternGenerator(),
        VillagerTypeGenerator()
    )

    /**
     * Returns all available generators from the registry
     * @return a set of all available generators
     */
    fun getGenerators(): Set<Generator> = generators

    /**
     * Returns all available generators that match the given predicate
     * @param predicate the predicate to filter the generators
     * @return a set of all available generators which match the predicate
     */
    fun getGenerators(predicate: (Generator) -> Boolean): Set<Generator> {
        return generators.filter { predicate(it) }.toSet()
    }
}
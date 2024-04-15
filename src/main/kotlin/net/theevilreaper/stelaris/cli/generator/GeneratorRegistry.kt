package net.theevilreaper.stelaris.cli.generator

import net.theevilreaper.stelaris.cli.generator.dart.*

class GeneratorRegistry {

     val generators: Set<Generator> = setOf(
        BossBarGenerator(),
        EffectGenerator(),
        EnchantmentGenerator(),
        EntityTypeGenerator(),
        FrameTypeGenerator(),
        ItemFlagGenerator(),
        MaterialGenerator(),
        SoundTypeGenerator(),
    )

    internal inline fun getGenerators(crossinline predicate: (BaseGenerator<*>) -> Boolean): Set<Generator> =
        generators.filter { predicate(it as BaseGenerator<*>) }.toSet()
}
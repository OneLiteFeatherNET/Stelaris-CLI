package net.theevilreaper.stelaris.cli.generator

import net.theevilreaper.stelaris.cli.generator.dart.*

class GeneratorRegistry {

     val generators: Set<Generator> = setOf(
        BossBarGenerator(),
        EffectGenerator(),
        EnchantmentGenerator(),
        EntityTypeGenerator(),
        FrameTypeGenerator(),
        MaterialGenerator(),
        SoundTypeGenerator(),
    )
}
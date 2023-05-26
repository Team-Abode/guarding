package com.teamabode.guarding.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ParryParticle extends SimpleAnimatedParticle {
    private final RandomSource random;

    public ParryParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, spriteSet, 0.0f);
        this.random = RandomSource.create();
        this.age = 5;
        this.quadSize = 1.0f;
        float tint = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = tint;
        this.gCol = tint;
        this.bCol = tint;
        this.setSpriteFromAge(spriteSet);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParryParticle(level, x, y, z, sprites);
        }
    }
}

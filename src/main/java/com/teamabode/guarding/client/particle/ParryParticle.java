package com.teamabode.guarding.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ParryParticle extends TextureSheetParticle {
    private final RandomSource random;
    private final SpriteSet sprites;

    public ParryParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet sprites) {
        super(clientLevel, x, y, z, 0.0f, 0.0f, 0.0f);
        this.random = RandomSource.create();
        this.sprites = sprites;
        this.lifetime = 6;
        this.quadSize = 1.0f;
        float tint = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = tint;
        this.gCol = tint;
        this.bCol = tint;
        this.setSpriteFromAge(sprites);
    }

    public int getLightColor(float partialTick) {
        return 15728880;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
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

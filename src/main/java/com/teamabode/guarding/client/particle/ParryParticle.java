package com.teamabode.guarding.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class ParryParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public ParryParticle(ClientWorld clientLevel, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientLevel, x, y, z, 0.0f, 0.0f, 0.0f);
        this.spriteProvider = spriteProvider;
        this.maxAge = 6;
        this.scale = 1.0f;
        float tint = this.random.nextFloat() * 0.6f + 0.4f;
        this.red = tint;
        this.green = tint;
        this.blue = tint;
        this.setSpriteForAge(spriteProvider);
    }

    public int getBrightness(float partialTick) {
        return 15728880;
    }

    public void tick() {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Provider(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientWorld level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParryParticle(level, x, y, z, sprites);
        }
    }
}

package com.teamabode.guarding.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class ParryParticle extends SpriteBillboardParticle {
    private final Random random;
    private final SpriteProvider sprites;

    public ParryParticle(ClientWorld clientLevel, double x, double y, double z, SpriteProvider sprites) {
        super(clientLevel, x, y, z, 0.0f, 0.0f, 0.0f);
        this.random = Random.create();
        this.sprites = sprites;
        this.maxAge = 6;
        this.scale = 1.0f;
        float tint = this.random.nextFloat() * 0.6f + 0.4f;
        this.red = tint;
        this.green = tint;
        this.blue = tint;
        this.setSpriteForAge(sprites);
    }

    public int getBrightness(float partialTick) {
        return 15728880;
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.sprites);
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
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

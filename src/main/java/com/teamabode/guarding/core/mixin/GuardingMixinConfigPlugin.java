package com.teamabode.guarding.core.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class GuardingMixinConfigPlugin implements IMixinConfigPlugin {

    public boolean shouldApplyMixin(String target, String mixin) {
        // Compatibility with Fabric Shield Lib
        if (FabricLoader.getInstance().isModLoaded("fabricshieldlib") && mixin.equals("ShieldEnchantabilityMixin")) {
            return false;
        }
        return true;
    }

    public void onLoad(String mixinPackage) {

    }

    public String getRefMapperConfig() {
        return null;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}

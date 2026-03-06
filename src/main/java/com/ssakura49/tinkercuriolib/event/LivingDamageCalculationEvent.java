package com.ssakura49.tinkercuriolib.event;

import javax.annotation.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class LivingDamageCalculationEvent extends Event {
    protected final LivingEntity target;
    protected final DamageSource source;
    protected float damage;

    protected LivingDamageCalculationEvent(LivingEntity target, DamageSource source) {
        this.target = target;
        this.source = source;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Nullable
    public LivingEntity getAttacker() {
        Entity var2 = this.source.getEntity();
        LivingEntity var10000;
        if (var2 instanceof LivingEntity liv) {
            var10000 = liv;
        } else {
            var10000 = null;
        }

        return var10000;
    }

    public static class Armor extends LivingDamageCalculationEvent {
        private final float damageBeforeArmor;
        private float damageAfterArmor;

        public Armor(LivingEntity entity, DamageSource source, float damageBeforeArmor, float damageAfterArmor) {
            super(entity, source);
            this.damageBeforeArmor = damageBeforeArmor;
            this.damageAfterArmor = damageAfterArmor;
        }

        public float getDamageBeforeArmor() {
            return this.damageBeforeArmor;
        }

        public float getDamageAfterArmor() {
            return this.damageAfterArmor;
        }

        public void setDamageAfterArmor(float damageAfterArmor) {
            this.damageAfterArmor = damageAfterArmor;
        }

        public void bypassArmor() {
            this.damageAfterArmor = this.damageBeforeArmor;
        }
    }

    @Cancelable
    public static class Magic extends LivingDamageCalculationEvent {
        private final float damageBeforeMagic;
        private boolean bypass;

        public Magic(LivingEntity entity, DamageSource source, float damageBeforeMagic) {
            super(entity, source);
            this.damageBeforeMagic = damageBeforeMagic;
        }

        public float getDamageBeforeMagic() {
            return this.damageBeforeMagic;
        }

        public void bypassMagic() {
            this.bypass = true;
            this.setCanceled(true);
        }

        public boolean isBypassMagic() {
            return this.bypass;
        }
    }
}

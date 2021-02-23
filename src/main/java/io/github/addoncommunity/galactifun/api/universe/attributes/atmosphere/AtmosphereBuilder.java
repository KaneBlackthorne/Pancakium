package io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * Utility class for making atmospheres
 * 
 * @author Mooy1
 */
public final class AtmosphereBuilder {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private boolean weatherCycle;
    private boolean storming;
    private boolean thundering;
    private boolean flammable;
    @Nonnull
    private World.Environment environment = World.Environment.NORMAL;
    @Nonnull
    private AtmosphericEffect[] effects = new AtmosphericEffect[0];
    @Nonnull
    private final Map<AtmosphericComponent, BigDecimal> composition = new EnumMap<AtmosphericComponent, BigDecimal>(AtmosphericComponent.class);
    
    public AtmosphereBuilder setNether() {
        this.environment = World.Environment.NETHER;
        return this;
    }

    /**
     * Note that adding this method <b>will</b> spawn the {@link EnderDragon}
     *
     * @return this object
     */
    public AtmosphereBuilder setEnd() {
        this.environment = World.Environment.THE_END;
        return this;
    }
    
    public AtmosphereBuilder addEffects(@Nonnull AtmosphericEffect... effects) {
        Validate.notNull(effects);
        this.effects = effects;
        return this;
    }

    public AtmosphereBuilder addComponent(@Nonnull AtmosphericComponent component, double percentage) {
        Validate.isTrue(percentage > 0 && percentage <= 100);
        this.composition.put(component, BigDecimal.valueOf(percentage));
        return this;
    }
    
    public AtmosphereBuilder enableWeather() {
        this.weatherCycle = true;
        return this;
    }

    public AtmosphereBuilder addStorm() {
        this.storming = true;
        return this;
    }

    public AtmosphereBuilder addThunder() {
        this.thundering = true;
        return this;
    }

    public AtmosphereBuilder enableFire() {
        this.flammable = true;
        return this;
    }
    
    @Nonnull
    public Atmosphere build() {
        BigDecimal percent = BigDecimal.ZERO;
        for (BigDecimal decimal : composition.values()) percent = percent.add(decimal);

        Validate.isTrue(percent.compareTo(ONE_HUNDRED) <= 0,
            "Percentage cannot be more than 100%!");
        composition.put(AtmosphericComponent.OTHER_GASES, ONE_HUNDRED.subtract(percent));

        return new Atmosphere(this.weatherCycle, this.storming, this.thundering, this.flammable, this.environment,
            this.composition, this.effects);
    }
    
}

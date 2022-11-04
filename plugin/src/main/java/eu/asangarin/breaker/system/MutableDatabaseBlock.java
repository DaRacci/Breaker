package eu.asangarin.breaker.system;

import eu.asangarin.breaker.api.BreakerState;
import eu.asangarin.breaker.util.TriggerType;
import io.lumine.mythic.bukkit.utils.metadata.Pair;
import io.lumine.mythic.core.skills.variables.VariableScope;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MutableDatabaseBlock {

    @Setter
    private int min;
    @Setter
    private int max;
    @Setter
    private Integer base;
    @Setter
    private boolean vanillaEnabled;
    @Setter
    private boolean tools;
    @Setter
    private boolean efficiency;
    @Setter
    private boolean haste;
    @Setter
    private boolean water;
    @Setter
    private boolean air;
    private List<BreakerState> states = new ArrayList<>();
    private List<Pair<TriggerType, BreakerTrigger.TriggerTrigger>> triggers = new ArrayList<>();
    @Setter
    private String mythicSkill;
    @Setter
    private String mythicVariable;
    @Setter
    private VariableScope mythicScope;

    public DatabaseBlock toImmutable() {
        return new DatabaseBlock(
            min,
            max,
            base != null ? base : max,
            vanillaEnabled,
            tools,
            efficiency,
            haste,
            water,
            air,
            mythicSkill != null ? mythicSkill : "",
            mythicVariable != null ? mythicVariable : "",
            () -> mythicScope != null ? mythicScope : VariableScope.SKILL,
            () -> states,
            () -> triggers,
            true
        );
    }

    public static MutableDatabaseBlock from(DatabaseBlock block) {
        MutableDatabaseBlock mutable = new MutableDatabaseBlock();
        mutable.setMin(block.getMin());
        mutable.setMax(block.getMax());
        mutable.setBase(block.getBase());
        mutable.setVanillaEnabled(block.isVanillaEnabled());
        mutable.setTools(block.isTools());
        mutable.setEfficiency(block.isEfficiency());
        mutable.setHaste(block.isHaste());
        mutable.setWater(block.isWater());
        mutable.setAir(block.isAir());
        mutable.states = block.getStates();
        mutable.triggers = block.getTriggers().entrySet().stream()
            .flatMap(entry -> entry.getValue().stream().map(trigger -> Pair.of(entry.getKey(), trigger)))
            .collect(Collectors.toList());
        mutable.setMythicSkill(block.getMythicSkill());
        mutable.setMythicVariable(block.getMythicVariable());
        mutable.setMythicScope(block.getMythicScope());
        return mutable;
    }

    @Override
    public String toString() {
        return "MutableDatabaseBlock{" +
            "min=" + min +
            ", max=" + max +
            ", base=" + base +
            ", vanillaEnabled=" + vanillaEnabled +
            ", tools=" + tools +
            ", efficiency=" + efficiency +
            ", haste=" + haste +
            ", water=" + water +
            ", air=" + air +
            ", states=" + states +
            ", triggers=" + triggers +
            ", mythicSkill='" + mythicSkill + '\'' +
            ", mythicVariable='" + mythicVariable + '\'' +
            ", mythicScope=" + mythicScope +
            '}';
    }
}
package eu.asangarin.breaker.states;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConditionalState extends BreakerState {
    private final List<BreakerState> requiredStates = new ArrayList<>();
    private final List<BreakerState> states = new ArrayList<>();
    private int staticDeduction = 0;

    @Override
    public int getDeduction(
        Player player,
        Block block
    ) {
        int deduction = staticDeduction;
        for (BreakerState state : states) {
            if (!state.isConditionMet(player, block)) continue;
            deduction += state.getDeduction(player, block);
        }
        return deduction;
    }

    @Override
    public boolean isConditionMet(Player breaker, Block block) {
        for (final var state : requiredStates) {
            if (!state.isConditionMet(breaker, block)) return false;
        }

        return true;
    }

    public boolean setup(
        @NotNull Collection<BreakerState> requiredStates,
        @Nullable Collection<BreakerState> applicableStates,
        int staticDeduction
    ) {

        this.requiredStates.addAll(requiredStates);
        if (applicableStates != null) this.states.addAll(applicableStates);
        this.staticDeduction = staticDeduction;
        return true;
    }

    @Override
    protected boolean setup(LineConfig config) {
        final var requiredStates = config.getString("required");
        final var applicableStates = config.getString("applicable");
        final var staticDeduction = config.getInteger("static-deduction", 0);

        if (requiredStates == null || applicableStates == null) {
            return error("Missing required or applicable states");
        }

        final var requiredStatesArray = requiredStates.trim().split(",");
        final var applicableStatesArray = applicableStates.trim().split(",");

        final var requiredStatesList = new ArrayList<BreakerState>();
        final var applicableStatesList = new ArrayList<BreakerState>();

        for (final var state : requiredStatesArray) {
            Breaker.get().getBreakerStates().fromConfig("Required State", state).ifPresent(requiredStatesList::add);
        }

        for (final var state : applicableStatesArray) {
            Breaker.get().getBreakerStates().fromConfig("Applicable State", state).ifPresent(applicableStatesList::add);
        }

        return setup(requiredStatesList, applicableStatesList, staticDeduction);
    }
}
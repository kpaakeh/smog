package io.github.kpaakeh.smog.test;

import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;

/** Test-only access to JEI's public runtime; excluded from the release JAR. */
public final class TestJeiProbe implements IModPlugin {
    private static volatile IJeiRuntime runtime;
    @Override public Identifier getPluginUid() { return Identifier.fromNamespaceAndPath("smog_tests", "probe"); }
    @Override public void onRuntimeAvailable(IJeiRuntime value) { runtime = value; }
    @Override public void onRuntimeUnavailable() { runtime = null; }
    public static boolean ready() { return runtime != null; }
    public static void showMultiblocks() {
        var type = runtime.getRecipeManager().getRecipeType(Identifier.fromNamespaceAndPath("smog", "multiblocks")).orElseThrow();
        runtime.getRecipesGui().showTypes(List.of(type));
    }
}

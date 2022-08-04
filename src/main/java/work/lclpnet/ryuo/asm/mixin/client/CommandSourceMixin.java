/*
Note: This mixin is a modified version from https://github.com/Safrodev/SuggestionProviderFabric/blob/9bbe456ec068ed4022175613c4f65402dffdb1ad/src/main/java/safro/suggestion/provider/fabric/mixin/CommandSourceMixin.java,
originally created by Safrodev and licensed under the MIT license (https://github.com/Safrodev/SuggestionProviderFabric/blob/9bbe456ec068ed4022175613c4f65402dffdb1ad/LICENSE).
 */

package work.lclpnet.ryuo.asm.mixin.client;

import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraft.command.CommandSource.shouldSuggest;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {

    /**
     * @author Safro, modified by LCLP.
     * @reason Support other namespaces on suggestion provider.
     */
    @Overwrite
    static <T> void forEachMatching(Iterable<T> candidates, String remaining, Function<T, Identifier> identifier,
                                    Consumer<T> action) {
        final boolean withColon = remaining.indexOf(':') > -1;

        for (final T object : candidates) {
            final Identifier id = identifier.apply(object);
            if (withColon) {
                final String idString = id.toString();

                if (shouldSuggest(remaining, idString)) {
                    action.accept(object);
                }
            } else if (shouldSuggest(remaining, id.getNamespace()) || shouldSuggest(remaining, id.getPath())) {
                action.accept(object);
            }
        }
    }
}

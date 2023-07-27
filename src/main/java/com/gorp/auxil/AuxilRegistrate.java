package com.gorp.auxil;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

public class AuxilRegistrate extends AbstractRegistrate<AuxilRegistrate> {
    
    protected AuxilRegistrate(String modid) {
        super(modid);
    }
    
    public static NonNullLazyValue<AuxilRegistrate> lazy(String modid) {
        return new NonNullLazyValue(() -> {
            return (AuxilRegistrate)(new AuxilRegistrate(modid)).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
        });
    }
    
    protected <R extends IForgeRegistryEntry<R>, T extends R> RegistryEntry<T> accept(String name, Class<? super R> type, Builder<R, T, ?, ?> builder, NonNullSupplier<? extends T> creator, NonNullFunction<RegistryObject<T>, ? extends RegistryEntry<T>> entryFactory) {
        RegistryEntry<T> ret = super.accept(name, type, builder, creator, entryFactory);
        return ret;
    }
    
    public <T extends Block> BlockBuilder<T, AuxilRegistrate> baseBlock(String name, NonNullFunction<AbstractBlock.Properties, T> factory, NonNullSupplier<Block> propertiesFrom, boolean TFworldGen) {
        return super.block(name, factory).initialProperties(propertiesFrom).blockstate((c, p) -> {
            String location = "block/palettes/" + c.getName() + "/plain";
            p.simpleBlock((Block)c.get(), p.models().cubeAll(c.getName(), p.modLoc(location)));
        }).simpleItem();
    }
    protected static void onClient(Supplier<Runnable> toRun) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, toRun);
    }
}

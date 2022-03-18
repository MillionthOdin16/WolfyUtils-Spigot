package com.wolfyscript.utilities.spigot;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wolfyscript.utilities.spigot.commands.ChatActionCommand;
import com.wolfyscript.utilities.spigot.commands.InfoCommand;
import com.wolfyscript.utilities.spigot.commands.InputCommand;
import com.wolfyscript.utilities.spigot.commands.SpawnParticleAnimationCommand;
import com.wolfyscript.utilities.spigot.commands.SpawnParticleEffectCommand;
import com.wolfyscript.utilities.spigot.listeners.BlockListener;
import com.wolfyscript.utilities.spigot.listeners.EquipListener;
import com.wolfyscript.utilities.spigot.listeners.GUIInventoryListener;
import com.wolfyscript.utilities.spigot.listeners.PlayerListener;
import com.wolfyscript.utilities.spigot.listeners.custom_item.CustomDurabilityListener;
import com.wolfyscript.utilities.spigot.listeners.custom_item.CustomItemPlayerListener;
import com.wolfyscript.utilities.spigot.listeners.custom_item.CustomParticleListener;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.chat.Chat;
import me.wolfyscript.utilities.api.chat.ChatImpl;
import me.wolfyscript.utilities.api.console.Console;
import me.wolfyscript.utilities.api.inventory.custom_items.CustomItem;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.Action;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.ActionCommand;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.ActionParticleAnimation;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.ActionSound;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.Event;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerConsumeItem;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerInteract;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerInteractAtEntity;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerInteractEntity;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerItemBreak;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerItemDamage;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerItemDrop;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerItemHandSwap;
import me.wolfyscript.utilities.api.inventory.custom_items.actions.EventPlayerItemHeld;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.AttributesModifiersMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.CustomDamageMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.CustomDurabilityMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.CustomItemTagMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.CustomModelDataMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.DamageMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.EnchantMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.FlagsMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.LoreMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.Meta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.NameMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.PlayerHeadMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.PotionMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.RepairCostMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.UnbreakableMeta;
import me.wolfyscript.utilities.api.inventory.custom_items.references.APIReference;
import me.wolfyscript.utilities.api.inventory.custom_items.references.VanillaRef;
import me.wolfyscript.utilities.api.inventory.custom_items.references.WolfyUtilitiesRef;
import me.wolfyscript.utilities.compatibility.CompatibilityManager;
import me.wolfyscript.utilities.main.WUPlugin;
import me.wolfyscript.utilities.main.configs.WUConfig;
import me.wolfyscript.utilities.messages.MessageFactory;
import me.wolfyscript.utilities.messages.MessageHandler;
import me.wolfyscript.utilities.util.entity.PlayerUtils;
import me.wolfyscript.utilities.util.eval.operators.ComparisonOperatorEqual;
import me.wolfyscript.utilities.util.eval.operators.ComparisonOperatorGreater;
import me.wolfyscript.utilities.util.eval.operators.ComparisonOperatorGreaterEqual;
import me.wolfyscript.utilities.util.eval.operators.ComparisonOperatorLess;
import me.wolfyscript.utilities.util.eval.operators.ComparisonOperatorLessEqual;
import me.wolfyscript.utilities.util.eval.operators.ComparisonOperatorNotEqual;
import me.wolfyscript.utilities.util.eval.operators.LogicalOperatorAnd;
import me.wolfyscript.utilities.util.eval.operators.LogicalOperatorNot;
import me.wolfyscript.utilities.util.eval.operators.LogicalOperatorOr;
import me.wolfyscript.utilities.util.eval.operators.Operator;
import me.wolfyscript.utilities.util.eval.value_providers.ValueProvider;
import me.wolfyscript.utilities.util.eval.value_providers.ValueProviderConditioned;
import me.wolfyscript.utilities.util.eval.value_providers.ValueProviderFloatConst;
import me.wolfyscript.utilities.util.eval.value_providers.ValueProviderFloatVar;
import me.wolfyscript.utilities.util.eval.value_providers.ValueProviderIntegerConst;
import me.wolfyscript.utilities.util.eval.value_providers.ValueProviderIntegerVar;
import me.wolfyscript.utilities.util.eval.value_providers.ValueProviderStringConst;
import me.wolfyscript.utilities.util.eval.value_providers.ValueProviderStringVar;
import me.wolfyscript.utilities.util.inventory.CreativeModeTab;
import me.wolfyscript.utilities.util.json.jackson.JacksonUtil;
import me.wolfyscript.utilities.util.json.jackson.KeyedTypeIdResolver;
import me.wolfyscript.utilities.util.json.jackson.annotations.OptionalKeyReference;
import me.wolfyscript.utilities.util.json.jackson.annotations.OptionalValueDeserializer;
import me.wolfyscript.utilities.util.json.jackson.annotations.OptionalValueSerializer;
import me.wolfyscript.utilities.util.json.jackson.serialization.APIReferenceSerialization;
import me.wolfyscript.utilities.util.json.jackson.serialization.ColorSerialization;
import me.wolfyscript.utilities.util.json.jackson.serialization.DustOptionsSerialization;
import me.wolfyscript.utilities.util.json.jackson.serialization.ItemStackSerialization;
import me.wolfyscript.utilities.util.json.jackson.serialization.LocationSerialization;
import me.wolfyscript.utilities.util.json.jackson.serialization.PotionEffectSerialization;
import me.wolfyscript.utilities.util.json.jackson.serialization.PotionEffectTypeSerialization;
import me.wolfyscript.utilities.util.json.jackson.serialization.VectorSerialization;
import me.wolfyscript.utilities.util.particles.animators.Animator;
import me.wolfyscript.utilities.util.particles.animators.AnimatorBasic;
import me.wolfyscript.utilities.util.particles.animators.AnimatorCircle;
import me.wolfyscript.utilities.util.particles.animators.AnimatorShape;
import me.wolfyscript.utilities.util.particles.animators.AnimatorSphere;
import me.wolfyscript.utilities.util.particles.animators.AnimatorVectorPath;
import me.wolfyscript.utilities.util.particles.shapes.Shape;
import me.wolfyscript.utilities.util.particles.shapes.ShapeCircle;
import me.wolfyscript.utilities.util.particles.shapes.ShapeComplexCompound;
import me.wolfyscript.utilities.util.particles.shapes.ShapeComplexRotation;
import me.wolfyscript.utilities.util.particles.shapes.ShapeCube;
import me.wolfyscript.utilities.util.particles.shapes.ShapeIcosahedron;
import me.wolfyscript.utilities.util.particles.shapes.ShapeSphere;
import me.wolfyscript.utilities.util.particles.shapes.ShapeSquare;
import me.wolfyscript.utilities.util.particles.timer.Timer;
import me.wolfyscript.utilities.util.particles.timer.TimerLinear;
import me.wolfyscript.utilities.util.particles.timer.TimerPi;
import me.wolfyscript.utilities.util.particles.timer.TimerRandom;
import me.wolfyscript.utilities.util.version.ServerVersion;
import me.wolfyscript.utilities.util.world.WorldUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class WolfyCoreSpigot extends WUPlugin {

    private final Chat chat;
    private final Console console;
    private Metrics metrics;
    private WUConfig config;
    private final MessageHandler messageHandler;
    private final MessageFactory messageFactory;
    private final CompatibilityManager compatibilityManager;
    private BukkitAudiences adventure;

    /**
     * Constructor invoked by Spigot when the plugin is loaded.
     */
    public WolfyCoreSpigot() {
        super();
        this.chat = api.getChat();
        this.console = api.getConsole();
        chat.setChatPrefix(Component.text("[", NamedTextColor.GRAY).append(Component.text("WU", NamedTextColor.AQUA)).append(Component.text("] ", NamedTextColor.DARK_GRAY)));
        this.messageHandler = new MessageHandler(this);
        this.messageFactory = new MessageFactory(this);
        this.compatibilityManager = new CompatibilityManager(this);
    }

    /**
     * Constructor invoked by MockBukkit to mock the plugin.
     */
    private WolfyCoreSpigot(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        this.chat = api.getChat();
        this.console = api.getConsole();
        chat.setChatPrefix(Component.text("[", NamedTextColor.GRAY).append(Component.text("WU", NamedTextColor.AQUA)).append(Component.text("] ", NamedTextColor.DARK_GRAY)));
        this.messageHandler = new MessageHandler(this);
        this.messageFactory = new MessageFactory(this);
        this.compatibilityManager = new CompatibilityManager(this);
    }

    @Override
    public CompatibilityManager getCompatibilityManager() {
        return compatibilityManager;
    }

    @Override
    @NotNull
    public BukkitAudiences getAdventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public WolfyUtilities getWolfyUtilities() {
        return api;
    }

    @Override
    public void onLoad() {
        //Jackson Serializer
        getLogger().info("Register json serializer/deserializer");
        var module = new SimpleModule();
        ItemStackSerialization.create(module);
        ColorSerialization.create(module);
        DustOptionsSerialization.create(module);
        LocationSerialization.create(module);
        //ParticleContentSerialization.create(module);
        PotionEffectTypeSerialization.create(module);
        PotionEffectSerialization.create(module);
        VectorSerialization.create(module);

        //Reference Deserializer
        APIReferenceSerialization.create(module);
        JacksonUtil.registerModule(module);

        var keyReferenceModule = new SimpleModule();
        keyReferenceModule.setSerializerModifier(new OptionalKeyReference.SerializerModifier());
        keyReferenceModule.setDeserializerModifier(new OptionalKeyReference.DeserializerModifier());
        var valueReferenceModule = new SimpleModule();
        valueReferenceModule.setSerializerModifier(new OptionalValueSerializer.SerializerModifier());
        valueReferenceModule.setDeserializerModifier(new OptionalValueDeserializer.DeserializerModifier());
        JacksonUtil.registerModule(keyReferenceModule);
        JacksonUtil.registerModule(valueReferenceModule);

        //Register custom item data

        //Register meta settings providers
        getLogger().info("Register CustomItem meta checks");
        var nbtChecks = getRegistries().getCustomItemNbtChecks();
        nbtChecks.register(AttributesModifiersMeta.KEY, AttributesModifiersMeta.class);
        nbtChecks.register(CustomDamageMeta.KEY, CustomDamageMeta.class);
        nbtChecks.register(CustomDurabilityMeta.KEY, CustomDurabilityMeta.class);
        nbtChecks.register(CustomItemTagMeta.KEY, CustomItemTagMeta.class);
        nbtChecks.register(CustomModelDataMeta.KEY, CustomModelDataMeta.class);
        nbtChecks.register(DamageMeta.KEY, DamageMeta.class);
        nbtChecks.register(EnchantMeta.KEY, EnchantMeta.class);
        nbtChecks.register(FlagsMeta.KEY, FlagsMeta.class);
        nbtChecks.register(LoreMeta.KEY, LoreMeta.class);
        nbtChecks.register(NameMeta.KEY, NameMeta.class);
        nbtChecks.register(PlayerHeadMeta.KEY, PlayerHeadMeta.class);
        nbtChecks.register(PotionMeta.KEY, PotionMeta.class);
        nbtChecks.register(RepairCostMeta.KEY, RepairCostMeta.class);
        nbtChecks.register(UnbreakableMeta.KEY, UnbreakableMeta.class);

        var particleAnimators = getRegistries().getParticleAnimators();
        particleAnimators.register(AnimatorBasic.KEY, AnimatorBasic.class);
        particleAnimators.register(AnimatorSphere.KEY, AnimatorSphere.class);
        particleAnimators.register(AnimatorCircle.KEY, AnimatorCircle.class);
        particleAnimators.register(AnimatorVectorPath.KEY, AnimatorVectorPath.class);
        particleAnimators.register(AnimatorShape.KEY, AnimatorShape.class);

        var particleShapes = getRegistries().getParticleShapes();
        particleShapes.register(ShapeSquare.KEY, ShapeSquare.class);
        particleShapes.register(ShapeCircle.KEY, ShapeCircle.class);
        particleShapes.register(ShapeSphere.KEY, ShapeSphere.class);
        particleShapes.register(ShapeCube.KEY, ShapeCube.class);
        particleShapes.register(ShapeIcosahedron.KEY, ShapeIcosahedron.class);
        particleShapes.register(ShapeComplexRotation.KEY, ShapeComplexRotation.class);
        particleShapes.register(ShapeComplexCompound.KEY, ShapeComplexCompound.class);

        var particleTimers = getRegistries().getParticleTimer();
        particleTimers.register(TimerLinear.KEY, TimerLinear.class);
        particleTimers.register(TimerRandom.KEY, TimerRandom.class);
        particleTimers.register(TimerPi.KEY, TimerPi.class);

        var customItemActions = getRegistries().getCustomItemActions();
        customItemActions.register(ActionCommand.KEY, ActionCommand.class);
        customItemActions.register(ActionParticleAnimation.KEY, ActionParticleAnimation.class);
        customItemActions.register(ActionSound.KEY, ActionSound.class);

        var customItemEvents = getRegistries().getCustomItemEvents();
        customItemEvents.register(EventPlayerInteract.KEY, EventPlayerInteract.class);
        customItemEvents.register(EventPlayerConsumeItem.KEY, EventPlayerConsumeItem.class);
        customItemEvents.register(EventPlayerInteractEntity.KEY, EventPlayerInteractEntity.class);
        customItemEvents.register(EventPlayerInteractAtEntity.KEY, EventPlayerInteractAtEntity.class);
        customItemEvents.register(EventPlayerItemBreak.KEY, EventPlayerItemBreak.class);
        customItemEvents.register(EventPlayerItemDamage.KEY, EventPlayerItemDamage.class);
        customItemEvents.register(EventPlayerItemDrop.KEY, EventPlayerItemDrop.class);
        customItemEvents.register(EventPlayerItemHandSwap.KEY, EventPlayerItemHandSwap.class);
        customItemEvents.register(EventPlayerItemHeld.KEY, EventPlayerItemHeld.class);

        var operators = getRegistries().getOperators();
        operators.register(ComparisonOperatorEqual.KEY, ComparisonOperatorEqual.class);
        operators.register(ComparisonOperatorNotEqual.KEY, ComparisonOperatorNotEqual.class);
        operators.register(ComparisonOperatorGreater.KEY, ComparisonOperatorGreater.class);
        operators.register(ComparisonOperatorGreaterEqual.KEY, ComparisonOperatorGreaterEqual.class);
        operators.register(ComparisonOperatorLess.KEY, ComparisonOperatorLess.class);
        operators.register(ComparisonOperatorLessEqual.KEY, ComparisonOperatorLessEqual.class);
        operators.register(LogicalOperatorAnd.KEY, LogicalOperatorAnd.class);
        operators.register(LogicalOperatorOr.KEY, LogicalOperatorOr.class);
        operators.register(LogicalOperatorNot.KEY, LogicalOperatorNot.class);

        var valueProviders = getRegistries().getValueProviders();
        valueProviders.register(ValueProviderConditioned.KEY, (Class<ValueProviderConditioned<?>>)(Object) ValueProviderConditioned.class);
        valueProviders.register(ValueProviderIntegerConst.KEY, ValueProviderIntegerConst.class);
        valueProviders.register(ValueProviderIntegerVar.KEY, ValueProviderIntegerVar.class);
        valueProviders.register(ValueProviderFloatConst.KEY, ValueProviderFloatConst.class);
        valueProviders.register(ValueProviderFloatVar.KEY, ValueProviderFloatVar.class);
        valueProviders.register(ValueProviderStringConst.KEY, ValueProviderStringConst.class);
        valueProviders.register(ValueProviderStringVar.KEY, ValueProviderStringVar.class);

        KeyedTypeIdResolver.registerTypeRegistry(Meta.class, nbtChecks);
        KeyedTypeIdResolver.registerTypeRegistry(Animator.class, particleAnimators);
        KeyedTypeIdResolver.registerTypeRegistry(Shape.class, particleShapes);
        KeyedTypeIdResolver.registerTypeRegistry(Timer.class, particleTimers);
        KeyedTypeIdResolver.registerTypeRegistry((Class<Action<?>>)(Object) Action.class, customItemActions);
        KeyedTypeIdResolver.registerTypeRegistry((Class<Event<?>>)(Object) Event.class, customItemEvents);
        KeyedTypeIdResolver.registerTypeRegistry(Operator.class, operators);
        KeyedTypeIdResolver.registerTypeRegistry((Class<ValueProvider<?>>) (Object)ValueProvider.class, valueProviders);
    }

    @Override
    public void onEnable() {
        this.api.initialize();
        console.info("Minecraft version: " + ServerVersion.getVersion().getVersion());
        console.info("WolfyUtilities version: " + ServerVersion.getWUVersion().getVersion());
        console.info("Environment: " + WolfyUtilities.getENVIRONMENT());
        this.adventure = BukkitAudiences.create(this);
        this.config = new WUConfig(api.getConfigAPI(), this);
        compatibilityManager.init();
        // Register ReferenceParser
        console.info("Register API references");
        registerAPIReference(new VanillaRef.Parser());
        registerAPIReference(new WolfyUtilitiesRef.Parser());

        //Load Language
        api.getLanguageAPI().loadLangFile("en_US");

        if (!ServerVersion.isIsJUnitTest()) {
            this.metrics = new Metrics(this, 5114);

            WorldUtils.load();
            PlayerUtils.loadStores();
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, WorldUtils::save, 6000, 6000);
            registerListeners();
            registerCommands();

            CreativeModeTab.init();
            loadParticleEffects();
        } else {
            onJUnitTests();
        }
    }

    /**
     * Handles JUnit test startup
     */
    private void onJUnitTests() {
        WorldUtils.load();
        PlayerUtils.loadStores();

        registerCommands();
    }

    @Override
    public void registerAPIReference(APIReference.Parser<?> parser) {
        if (parser instanceof VanillaRef.Parser || parser instanceof WolfyUtilitiesRef.Parser || config.isAPIReferenceEnabled(parser)) {
            CustomItem.registerAPIReferenceParser(parser);
        }
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        api.getConfigAPI().saveConfigs();
        PlayerUtils.saveStores();
        console.info("Save stored Custom Items");
        WorldUtils.save();
    }

    @Override
    public void loadParticleEffects() {
        console.info("Initiating Particles");
        WorldUtils.getWorldCustomItemStore().initiateMissingBlockEffects();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ChatImpl.ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CustomDurabilityListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CustomParticleListener(), this);
        Bukkit.getPluginManager().registerEvents(new CustomItemPlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new EquipListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new GUIInventoryListener(), this);
    }

    private void registerCommands() {
        Bukkit.getServer().getPluginCommand("wolfyutils").setExecutor(new InfoCommand(this));
        Bukkit.getServer().getPluginCommand("particle_effect").setExecutor(new SpawnParticleEffectCommand(api));
        Bukkit.getServer().getPluginCommand("particle_animation").setExecutor(new SpawnParticleAnimationCommand(api));
        Bukkit.getServer().getPluginCommand("wui").setExecutor(new InputCommand(this));
        Bukkit.getServer().getPluginCommand("wui").setTabCompleter(new InputCommand(this));
        Bukkit.getServer().getPluginCommand("wua").setExecutor(new ChatActionCommand());
    }

    @Override
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    @Override
    public com.wolfyscript.utilities.common.chat.Chat getChat() {
        return chat;
    }
}
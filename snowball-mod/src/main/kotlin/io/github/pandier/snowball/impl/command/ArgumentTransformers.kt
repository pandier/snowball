package io.github.pandier.snowball.impl.command

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import io.github.pandier.snowball.command.type.ArgumentType
import io.github.pandier.snowball.command.type.BooleanArgumentType
import io.github.pandier.snowball.command.type.ComponentArgumentType
import io.github.pandier.snowball.command.type.DoubleArgumentType
import io.github.pandier.snowball.command.type.EntityArgumentType
import io.github.pandier.snowball.command.type.EntityListArgumentType
import io.github.pandier.snowball.command.type.FloatArgumentType
import io.github.pandier.snowball.command.type.GameProfileArgumentType
import io.github.pandier.snowball.command.type.GameProfileListArgumentType
import io.github.pandier.snowball.command.type.GreedyStringArgumentType
import io.github.pandier.snowball.command.type.IntArgumentType
import io.github.pandier.snowball.command.type.LongArgumentType
import io.github.pandier.snowball.command.type.NamedTextColorArgumentType
import io.github.pandier.snowball.command.type.PlayerArgumentType
import io.github.pandier.snowball.command.type.PlayerListArgumentType
import io.github.pandier.snowball.command.type.StringArgumentType
import io.github.pandier.snowball.command.type.TextColorArgumentType
import io.github.pandier.snowball.command.type.Vector2dArgumentType
import io.github.pandier.snowball.command.type.Vector2fArgumentType
import io.github.pandier.snowball.command.type.Vector2iArgumentType
import io.github.pandier.snowball.command.type.Vector3dArgumentType
import io.github.pandier.snowball.command.type.Vector3fArgumentType
import io.github.pandier.snowball.command.type.Vector3iArgumentType
import io.github.pandier.snowball.command.type.WordArgumentType
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.math.Vector2d
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector2i
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.math.Vector3f
import io.github.pandier.snowball.math.Vector3i
import io.github.pandier.snowball.profile.GameProfile
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.PosArgument
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ArgumentTransformers {
    private val registry = mutableMapOf<Class<out ArgumentType<*>>, ArgumentTransformer<*, *, *>>()

    init {
        registerDirect<BooleanArgumentType, Boolean> { _, _ -> BoolArgumentType.bool() }
        registerDirect<IntArgumentType, Int> { _, it -> IntegerArgumentType.integer(it.min, it.max) }
        registerDirect<LongArgumentType, Long> { _, it -> com.mojang.brigadier.arguments.LongArgumentType.longArg(it.min, it.max) }
        registerDirect<FloatArgumentType, Float> { _, it -> com.mojang.brigadier.arguments.FloatArgumentType.floatArg(it.min, it.max) }
        registerDirect<DoubleArgumentType, Double> { _, it -> com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg(it.min, it.max) }
        registerDirect<StringArgumentType, String> { _, _ -> com.mojang.brigadier.arguments.StringArgumentType.string() }
        registerDirect<WordArgumentType, String> { _, _ -> com.mojang.brigadier.arguments.StringArgumentType.word() }
        registerDirect<GreedyStringArgumentType, String> { _, _ -> com.mojang.brigadier.arguments.StringArgumentType.greedyString() }
        register<EntityArgumentType, Entity, EntitySelector>(
            { _, _ -> net.minecraft.command.argument.EntityArgumentType.entity() },
            { ctx, value -> Conversions.snowball(value.getEntity(ctx.source)) }
        )
        register<EntityListArgumentType, List<Entity>, EntitySelector>(
            { _, _ -> net.minecraft.command.argument.EntityArgumentType.entities() },
            { ctx, value -> value.getEntities(ctx.source).map { Conversions.snowball(it) } }
        )
        register<PlayerArgumentType, Player, EntitySelector>(
            { _, _ -> net.minecraft.command.argument.EntityArgumentType.player() },
            { ctx, value -> Conversions.snowball(value.getPlayer(ctx.source)) }
        )
        register<PlayerListArgumentType, List<Player>, EntitySelector>(
            { _, _ -> net.minecraft.command.argument.EntityArgumentType.players() },
            { ctx, value -> value.getPlayers(ctx.source).map { Conversions.snowball(it) } }
        )
        register<ComponentArgumentType, Component, Text>(
            { access, _ -> net.minecraft.command.argument.TextArgumentType.text(access) },
            { ctx, value -> Conversions.Adventure.adventure(value) }
        )
        register<GameProfileArgumentType, GameProfile, net.minecraft.command.argument.GameProfileArgumentType.GameProfileArgument>(
            { _, _ -> net.minecraft.command.argument.GameProfileArgumentType.gameProfile() },
            { ctx, value -> Conversions.snowball(value.getNames(ctx.source).first()) }
        )
        register<GameProfileListArgumentType, List<GameProfile>, net.minecraft.command.argument.GameProfileArgumentType.GameProfileArgument>(
            { _, _ -> net.minecraft.command.argument.GameProfileArgumentType.gameProfile() },
            { ctx, value -> value.getNames(ctx.source).map(Conversions::snowball) }
        )
        register<NamedTextColorArgumentType, NamedTextColor, Formatting>(
            { _, _ -> net.minecraft.command.argument.ColorArgumentType.color() },
            { ctx, value -> Conversions.Adventure.adventure(value) }
        )
        register<TextColorArgumentType, TextColor, Int>(
            { _, _ -> net.minecraft.command.argument.HexColorArgumentType.hexColor() },
            { ctx, value -> TextColor.color(value) }
        )
        register<Vector2dArgumentType, Vector2d, PosArgument>(
            { _, it -> net.minecraft.command.argument.Vec2ArgumentType.vec2(it.center) },
            { ctx, value -> value.getPos(ctx.source).let { Vector2d(it.x, it.z)} }
        )
        register<Vector2fArgumentType, Vector2f, PosArgument>(
            { _, it -> net.minecraft.command.argument.Vec2ArgumentType.vec2(it.center) },
            { ctx, value -> value.getPos(ctx.source).let { Vector2f(it.x.toFloat(), it.z.toFloat()) } }
        )
        register<Vector2iArgumentType, Vector2i, PosArgument>(
            { _, _ -> net.minecraft.command.argument.ColumnPosArgumentType.columnPos() },
            { ctx, value -> value.toAbsoluteBlockPos(ctx.source).let { Vector2i(it.x, it.z) } }
        )
        register<Vector3dArgumentType, Vector3d, PosArgument>(
            { _, it -> net.minecraft.command.argument.Vec3ArgumentType.vec3(it.center) },
            { ctx, value -> value.getPos(ctx.source).let { Vector3d(it.x, it.y, it.z) } }
        )
        register<Vector3fArgumentType, Vector3f, PosArgument>(
            { _, it -> net.minecraft.command.argument.Vec3ArgumentType.vec3(it.center) },
            { ctx, value -> value.getPos(ctx.source).let { Vector3f(it.x.toFloat(), it.y.toFloat(), it.z.toFloat()) } }
        )
        register<Vector3iArgumentType, Vector3i, PosArgument>(
            { _, _ -> net.minecraft.command.argument.BlockPosArgumentType.blockPos() },
            { ctx, value -> value.toAbsoluteBlockPos(ctx.source).let { Vector3i(it.x, it.y, it.z) } }
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, A : ArgumentType<T>> get(type: A): ArgumentTransformer<T, A, *> {
        return (registry[type.javaClass] ?: error("Unrecognizable argument type: $type")) as ArgumentTransformer<T, A, *>
    }

    fun <T, A : ArgumentType<T>> getBrigadierType(registryAccess: CommandRegistryAccess, type: A): com.mojang.brigadier.arguments.ArgumentType<*> {
        return get(type).brigadierType(registryAccess, type)
    }

    private inline fun <reified A : ArgumentType<*>> register(transformer: ArgumentTransformer<*, A, *>) {
        registry[A::class.java] = transformer
    }

    private inline fun <reified A : ArgumentType<T>, T, reified B> register(
        noinline brigadierTypeFactory: (CommandRegistryAccess, A) -> com.mojang.brigadier.arguments.ArgumentType<B>,
        noinline transfomer: (com.mojang.brigadier.context.CommandContext<ServerCommandSource>, B) -> T
    ) {
        register(ArgumentTransformer(B::class.java, brigadierTypeFactory, transfomer))
    }

    private inline fun <reified A : ArgumentType<T>, reified T> registerDirect(
        noinline brigadierTypeFactory: (CommandRegistryAccess, A) -> com.mojang.brigadier.arguments.ArgumentType<T>
    ) {
        register<A, T, T>(brigadierTypeFactory) { _, value -> value }
    }
}
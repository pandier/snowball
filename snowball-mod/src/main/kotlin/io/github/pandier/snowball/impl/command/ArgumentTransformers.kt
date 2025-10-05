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
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.ColorArgument
import net.minecraft.commands.arguments.ComponentArgument
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.commands.arguments.HexColorArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument
import net.minecraft.commands.arguments.coordinates.Coordinates
import net.minecraft.commands.arguments.coordinates.Vec2Argument
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.commands.arguments.selector.EntitySelector

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
            { _, _ -> EntityArgument.entity() },
            { ctx, value -> Conversions.snowball(value.findSingleEntity(ctx.source)) }
        )
        register<EntityListArgumentType, List<Entity>, EntitySelector>(
            { _, _ -> EntityArgument.entities() },
            { ctx, value -> value.findEntities(ctx.source).map { Conversions.snowball(it) } }
        )
        register<PlayerArgumentType, Player, EntitySelector>(
            { _, _ -> EntityArgument.player() },
            { ctx, value -> Conversions.snowball(value.findSinglePlayer(ctx.source)) }
        )
        register<PlayerListArgumentType, List<Player>, EntitySelector>(
            { _, _ -> EntityArgument.players() },
            { ctx, value -> value.findPlayers(ctx.source).map { Conversions.snowball(it) } }
        )
        register<ComponentArgumentType, Component, net.minecraft.network.chat.Component>(
            { access, _ -> ComponentArgument.textComponent(access) },
            { _, value -> Conversions.Adventure.adventure(value) }
        )
        // TODO: Replace GameProfile with PlayerEntry or something
        register<GameProfileArgumentType, GameProfile, GameProfileArgument.Result>(
            { _, _ -> GameProfileArgument.gameProfile() },
            { ctx, value -> value.getNames(ctx.source).first().let { GameProfile(it.id, it.name) } }
        )
        // TODO: Replace GameProfile with PlayerEntry or something
        register<GameProfileListArgumentType, List<GameProfile>, GameProfileArgument.Result>(
            { _, _ -> GameProfileArgument.gameProfile() },
            { ctx, value -> value.getNames(ctx.source).map { GameProfile(it.id, it.name) } }
        )
        register<NamedTextColorArgumentType, NamedTextColor, ChatFormatting>(
            { _, _ -> ColorArgument.color() },
            { _, value -> Conversions.Adventure.adventure(value) }
        )
        register<TextColorArgumentType, TextColor, Int>(
            { _, _ -> HexColorArgument.hexColor() },
            { _, value -> TextColor.color(value) }
        )
        register<Vector2dArgumentType, Vector2d, Coordinates>(
            { _, it -> Vec2Argument.vec2(it.center) },
            { ctx, value -> value.getPosition(ctx.source).let { Vector2d(it.x, it.z)} }
        )
        register<Vector2fArgumentType, Vector2f, Coordinates>(
            { _, it -> Vec2Argument.vec2(it.center) },
            { ctx, value -> value.getPosition(ctx.source).let { Vector2f(it.x.toFloat(), it.z.toFloat()) } }
        )
        register<Vector2iArgumentType, Vector2i, Coordinates>(
            { _, _ -> ColumnPosArgument.columnPos() },
            { ctx, value -> value.getBlockPos(ctx.source).let { Vector2i(it.x, it.z) } }
        )
        register<Vector3dArgumentType, Vector3d, Coordinates>(
            { _, it -> Vec3Argument.vec3(it.center) },
            { ctx, value -> value.getPosition(ctx.source).let { Vector3d(it.x, it.y, it.z) } }
        )
        register<Vector3fArgumentType, Vector3f, Coordinates>(
            { _, it -> Vec3Argument.vec3(it.center) },
            { ctx, value -> value.getPosition(ctx.source).let { Vector3f(it.x.toFloat(), it.y.toFloat(), it.z.toFloat()) } }
        )
        register<Vector3iArgumentType, Vector3i, Coordinates>(
            { _, _ -> BlockPosArgument.blockPos() },
            { ctx, value -> value.getBlockPos(ctx.source).let { Vector3i(it.x, it.y, it.z) } }
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, A : ArgumentType<T>> get(type: A): ArgumentTransformer<T, A, *> {
        return (registry[type.javaClass] ?: error("Unrecognizable argument type: $type")) as ArgumentTransformer<T, A, *>
    }

    fun <T, A : ArgumentType<T>> getBrigadierType(registryAccess: CommandBuildContext, type: A): com.mojang.brigadier.arguments.ArgumentType<*> {
        return get(type).brigadierType(registryAccess, type)
    }

    private inline fun <reified A : ArgumentType<*>> register(transformer: ArgumentTransformer<*, A, *>) {
        registry[A::class.java] = transformer
    }

    private inline fun <reified A : ArgumentType<T>, T, reified B> register(
        noinline brigadierTypeFactory: (CommandBuildContext, A) -> com.mojang.brigadier.arguments.ArgumentType<B>,
        noinline transfomer: (com.mojang.brigadier.context.CommandContext<CommandSourceStack>, B) -> T
    ) {
        register(ArgumentTransformer(B::class.java, brigadierTypeFactory, transfomer))
    }

    private inline fun <reified A : ArgumentType<T>, reified T> registerDirect(
        noinline brigadierTypeFactory: (CommandBuildContext, A) -> com.mojang.brigadier.arguments.ArgumentType<T>
    ) {
        register<A, T, T>(brigadierTypeFactory) { _, value -> value }
    }
}
package com.gabriel.lunala.project.utils.client

import com.gabriel.lunala.project.Lunala
import com.gabriel.lunala.project.event.ReactiveEventManager
import com.gabriel.lunala.project.utils.message.DiscordReply
import com.gabriel.lunala.project.utils.message.LunaReply
import kotlinx.coroutines.*
import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.GenericEvent
import org.koin.core.get
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

suspend fun MessageChannel.sendMessage(reply: LunaReply): Message = this.sendMessage(
    MessageBuilder(reply.format()).setEmbed((reply as? DiscordReply)?.embed?.build()).build()
).submit().await()


inline fun <reified T : GenericEvent> JDA.on(
        scope: CoroutineScope = Lunala.get(),
        noinline callback: suspend T.() -> Unit
): Unit = (eventManager as? ReactiveEventManager)?.on(T::class.java)?.subscribe {
    scope.launch {
        callback.invoke(it)
    }
}.run {
    Unit
}
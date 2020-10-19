package com.gabriel.lunala.project.service

import arrow.Kind
import arrow.core.Tuple3
import arrow.core.toTuple3
import arrow.fx.ForIO
import arrow.fx.IO
import com.gabriel.lunala.project.command.*
import com.gabriel.lunala.project.command.impl.misc.PingCommand
import com.gabriel.lunala.project.command.impl.misc.PlanetCommand
import com.gabriel.lunala.project.command.impl.misc.SayCommand
import com.gabriel.lunala.project.command.impl.utils.SupportCommand
import com.gabriel.lunala.project.platform.LunalaCluster
import com.gabriel.lunala.project.util.PlanetService
import com.gabriel.lunala.project.util.ProfileService
import com.gabriel.lunala.project.util.ServerService
import kotlinx.coroutines.Job

class DefaultCommandService(private val cluster: LunalaCluster, private val services: Tuple3<ProfileService, ServerService, PlanetService>): CommandService {

    private var commandsJob: Job? = null
    override val repository = CommandRepository()

    override fun start(): Kind<ForIO, Unit> = IO {
        register()
        commandsJob = DiscordCommandHandler(cluster, Triple(services.a, services.b, this).toTuple3()).setup()
    }

    private fun register() {
        repository.register(PingCommand().create())
        repository.register(PlanetCommand(services.c, services.a).create())
        repository.register(SayCommand().create())
        repository.register(SupportCommand().create())
    }

    override fun stop(): Kind<ForIO, Unit> = IO {
        commandsJob?.cancel()
    }

}
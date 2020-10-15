package com.gabriel.lunala.project

import arrow.Kind
import arrow.core.toT
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.fx
import com.gabriel.lunala.project.locale.DefaultLocaleRepository
import com.gabriel.lunala.project.platform.LunalaCluster
import com.gabriel.lunala.project.service.DefaultCommandService
import com.gabriel.lunala.project.service.DefaultDatabaseService
import com.gabriel.lunala.project.service.DefaultProfileService
import com.gabriel.lunala.project.service.DefaultServerService
import com.gabriel.lunala.project.util.LunalaDiscordConfig
import mu.KLogger
import mu.toKLogger
import org.slf4j.LoggerFactory

class SingleLunalaService(override val config: LunalaDiscordConfig): LunalaService<ForIO> {

    val logger: KLogger = LoggerFactory.getLogger(LunalaService::class.java).toKLogger()
    val cluster: LunalaCluster = LunalaCluster(config, logger)

    override fun start(): Kind<ForIO, Unit> = IO.fx {
        cluster.prepare().bind()
        val primaryServices = DefaultProfileService() toT DefaultServerService()

        DefaultLocaleRepository.reload().bind()
        DefaultDatabaseService(config).let {
            it.connect().bind()
            it.createTables().bind()
        }

        DefaultCommandService(cluster, primaryServices).start().bind()

        cluster.login().bind()
    }

    override fun stop(): Kind<ForIO, Unit> = IO {

    }
}
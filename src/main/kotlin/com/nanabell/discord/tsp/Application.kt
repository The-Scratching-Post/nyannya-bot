package com.nanabell.discord.tsp

import io.micronaut.runtime.Micronaut
import java.lang.management.ManagementFactory

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        val builder = Micronaut.build()
            .args(*args)
            .packages("com.nanabell.discord.tsp")
            .defaultEnvironments("prod")

        if (isDevEnv())
            builder.environments("dev")

        builder.start()
    }

    private fun isDevEnv(): Boolean {
        for (inputArgument in ManagementFactory.getRuntimeMXBean().inputArguments) {
            if (inputArgument.startsWith("-javaagent")
                || inputArgument.startsWith("-agentpath")
                || inputArgument.startsWith("agentlib"))
                    return true
        }

        return false
    }

}
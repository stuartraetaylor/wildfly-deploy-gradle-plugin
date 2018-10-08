package com.mkring.wildlydeplyplugin

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class DeployWildflyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("DeployWildflyPlugin applied")
    }
}

open class DeployWildflyTask : DefaultTask() {

    @Input
    var file: String? = null

    @Input
    var deploymentName: String? = null
    @Input
    var runtimeName: String? = null

    @Input
    var host: String = "localhost"
    @Input
    var port: Int = 9090
    @Input
    var user: String? = null
    @Input
    var password: String? = null
    @Input
    var reload: Boolean = true
    @Input
    var force: Boolean = true

    @Input
    var awaitReload: Boolean = false

    @Input
    var undeployBeforehand: Boolean = false

    init {
        group = "help"
        description = "Deploys files to a Wildfly und reloads it afterwards"
        dependsOn("build")
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun deployWildfly() {
        if (file == null || user == null || password == null) {
            println("DeployWildflyTask: missing configuration")
            return
        }
        println("deployWildfly: going to deploy $file to $host:$port")
        try {
            FileDeployer(
                file,
                host,
                port,
                user,
                password,
                reload,
                force,
                deploymentName,
                runtimeName,
                awaitReload,
                undeployBeforehand
            ).deploy()
        } catch (e: Exception) {
            println("deployWildfly task failed: ${e.message}")
            e.printStackTrace()
        }
    }
}
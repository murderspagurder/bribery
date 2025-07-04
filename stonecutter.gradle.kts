plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.10-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.95" apply false
    id ("dev.kikugie.postprocess.jsonlang") version "2.1-beta.4" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.8.4" apply false
}

stonecutter active "1.21.6-fabric"

stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('-'), "fabric", "neoforge")
}

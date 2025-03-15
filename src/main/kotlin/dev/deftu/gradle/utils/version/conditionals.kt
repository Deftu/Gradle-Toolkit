package dev.deftu.gradle.utils.version

import dev.deftu.gradle.utils.MCData
import dev.deftu.gradle.utils.ModLoader
import org.gradle.api.Project

object MinecraftVersionConditionals {

    @JvmStatic
    fun whenWithinRange(project: Project, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version in start..end) {
            action()
        }
    }

    @JvmStatic
    fun whenWithinRange(project: Project, loader: ModLoader, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version in start..end) {
            action()
        }
    }

    @JvmStatic
    fun whenWithinRangeExclusive(project: Project, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version > start && mcData.version < end) {
            action()
        }
    }

    @JvmStatic
    fun whenWithinRangeExclusive(project: Project, loader: ModLoader, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version > start && mcData.version < end) {
            action()
        }
    }

    @JvmStatic
    fun whenAbove(project: Project, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version > version) {
            action()
        }
    }

    @JvmStatic
    fun whenAbove(project: Project, loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version > version) {
            action()
        }
    }

    @JvmStatic
    fun whenBelow(project: Project, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version < version) {
            action()
        }
    }

    @JvmStatic
    fun whenBelow(project: Project, loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version < version) {
            action()
        }
    }

    @JvmStatic
    fun whenAtLeast(project: Project, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version >= version) {
            action()
        }
    }

    @JvmStatic
    fun whenAtLeast(project: Project, loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version >= version) {
            action()
        }
    }

    @JvmStatic
    fun whenAtMost(project: Project, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version <= version) {
            action()
        }
    }

    @JvmStatic
    fun whenAtMost(project: Project, loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version <= version) {
            action()
        }
    }

    @JvmStatic
    fun whenEquals(project: Project, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version == version) {
            action()
        }
    }

    @JvmStatic
    fun whenEquals(project: Project, loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version == version) {
            action()
        }
    }

    @JvmStatic
    fun whenNotEquals(project: Project, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version != version) {
            action()
        }
    }

    @JvmStatic
    fun whenNotEquals(project: Project, loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version != version) {
            action()
        }
    }

    @JvmStatic
    fun whenNotWithinRange(project: Project, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.version !in start..end) {
            action()
        }
    }

    @JvmStatic
    fun whenNotWithinRange(project: Project, loader: ModLoader, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
        val mcData = MCData.from(project)
        if (mcData.loader == loader && mcData.version !in start..end) {
            action()
        }
    }

}

fun Project.whenWithinRange(start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenWithinRange(this, start, end, action)
}

fun Project.whenWithinRange(loader: ModLoader, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenWithinRange(this, loader, start, end, action)
}

fun Project.whenWithinRangeExclusive(start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenWithinRangeExclusive(this, start, end, action)
}

fun Project.whenWithinRangeExclusive(loader: ModLoader, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenWithinRangeExclusive(this, loader, start, end, action)
}

fun Project.whenAbove(version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenAbove(this, version, action)
}

fun Project.whenAbove(loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenAbove(this, loader, version, action)
}

fun Project.whenBelow(version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenBelow(this, version, action)
}

fun Project.whenBelow(loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenBelow(this, loader, version, action)
}

fun Project.whenAtLeast(version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenAtLeast(this, version, action)
}

fun Project.whenAtLeast(loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenAtLeast(this, loader, version, action)
}

fun Project.whenAtMost(version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenAtMost(this, version, action)
}

fun Project.whenAtMost(loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenAtMost(this, loader, version, action)
}

fun Project.whenEquals(version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenEquals(this, version, action)
}

fun Project.whenEquals(loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenEquals(this, loader, version, action)
}

fun Project.whenNotEquals(version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenNotEquals(this, version, action)
}

fun Project.whenNotEquals(loader: ModLoader, version: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenNotEquals(this, loader, version, action)
}

fun Project.whenNotWithinRange(start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenNotWithinRange(this, start, end, action)
}

fun Project.whenNotWithinRange(loader: ModLoader, start: MinecraftVersion<*>, end: MinecraftVersion<*>, action: () -> Unit) {
    MinecraftVersionConditionals.whenNotWithinRange(this, loader, start, end, action)
}

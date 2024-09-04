package dev.deftu.gradle.exceptions

import dev.deftu.gradle.utils.ModLoader

class LoaderSpecificException(current: ModLoader? = null, vararg loaders: ModLoader) : Exception(generateMessage(current, loaders)) {

    private companion object {

        private fun generateMessage(current: ModLoader?, loaders: Array<out ModLoader>): String {
            return if (loaders.size == 1) {
                "This resource can only be used with ${loaders[0].name}."
            } else {
                val builder = StringBuilder("This resource can only be used with ")
                for (i in loaders.indices) {
                    builder.append(loaders[i].name)
                    if (i < loaders.size - 2) {
                        builder.append(", ")
                    } else if (i == loaders.size - 2) {
                        builder.append(" and ")
                    }
                }

                builder.toString()
            } + if (current != null) {
                " (current: ${current.name})"
            } else {
                ""
            }
        }

    }

}

package dev.deftu.gradle.exceptions

import dev.deftu.gradle.utils.ModLoader

class LoaderSpecificException(loader: ModLoader) : Exception("This resource can only be used with ${loader.name}.")

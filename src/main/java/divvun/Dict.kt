package divvun

import no.divvun.divvunspell.SpellerArchive
import java.io.File
import java.net.URL
import java.nio.file.Files


class Dict {


    companion object {
        init {
            val libName = "libdivvunspell" // The name of the file in resources/ dir

            val url: URL = Dict::class.java.getResource("/${libName}.dylib")!!
            val tmpDir = Files.createTempDirectory("libdivvun").toFile()
            tmpDir.deleteOnExit()
            val nativeLibTmpFile = File(tmpDir, libName)
            nativeLibTmpFile.deleteOnExit()
            url.openStream().use { input -> Files.copy(input, nativeLibTmpFile.toPath()) }
            System.load(nativeLibTmpFile.absolutePath)
            println(nativeLibTmpFile.absolutePath)
        }
        fun bla() {
            val ar = SpellerArchive.open("/Library/Services/se.bundle/Contents/Resources/se.zhsft")
            println(ar)

            println(ar.speller().suggest("same"))
        }
    }
}
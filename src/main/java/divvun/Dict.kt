package divvun

import no.divvun.divvunspell.SpellerArchive
import java.io.File
import java.net.URL
import java.nio.file.Files


class Dict {
    private val speller = SpellerArchive.open("/Library/Services/se.bundle/Contents/Resources/se.zhfst").speller()

    fun suggest(word: String): List<String> =
        speller.suggest(word)

    fun isCorrect(word: String) = speller.isCorrect(word)

    companion object {
        init {
            val libName = "libdivvunspell" // The name of the file in resources/ dir

            val url: URL = Dict::class.java.getResource("/${libName}.dylib")!!
            val tmpDir = Files.createTempDirectory("libdivvun").toFile()
            tmpDir.deleteOnExit()
            val nativeLibTmpFile = File(tmpDir, libName)
            nativeLibTmpFile.deleteOnExit()
            url.openStream().use { input -> Files.copy(input, nativeLibTmpFile.toPath()) }
            try {
                System.load(nativeLibTmpFile.absolutePath)
            } catch (ex: Throwable) {
                println("aaaaaaaaaaaa $ex")
            }
            println(nativeLibTmpFile.absolutePath)
        }


    }
}
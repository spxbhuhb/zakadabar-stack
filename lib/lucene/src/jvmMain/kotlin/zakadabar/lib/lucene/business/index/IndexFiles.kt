/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zakadabar.lib.lucene.business.index

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.*
import org.apache.lucene.index.*
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.IOUtils
import zakadabar.lib.lucene.business.index.knn.DemoEmbeddings
import zakadabar.lib.lucene.business.index.knn.KnnVectorDict
import zakadabar.lib.lucene.business.index.knn.KnnVectorDict.Companion.build
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.io.path.name
import kotlin.system.exitProcess

class IndexFiles private constructor(
    private val vectorDict: KnnVectorDict?,
    val docsRoot: Path
) : AutoCloseable {

    // Calculates embedding vectors for KnnVector search
    private var demoEmbeddings: DemoEmbeddings? = vectorDict?.let { DemoEmbeddings(vectorDict) }

    /**
     * Indexes the given file using the given writer, or if a directory is given, recurses over files
     * and directories found under the given directory.
     *
     *
     * NOTE: This method indexes one document per input file. This is slow. For good throughput,
     * put multiple documents into your input file(s). An example of this is in the benchmark module,
     * which can create "line doc" files, one document per line, using the [WriteLineDocTask](../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html).
     *
     * @param writer Writer to the index where the given file/dir info will be stored
     * @param path The file to index, or the directory to recurse into to find files to index
     * @throws IOException If there is a low-level I/O error
     */
    @Throws(IOException::class)
    fun indexDocs(writer: IndexWriter, path: Path) {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(
                path,
                object : SimpleFileVisitor<Path>() {
                    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {

                        // Index only Markdown documents
                        if (!file.name.endsWith(".md")) return FileVisitResult.CONTINUE

                        try {
                            indexDoc(writer, file, attrs.lastModifiedTime().toMillis())
                        } catch (ignore: IOException) {
                            ignore.printStackTrace(System.err)
                            // don't index files that can't be read.
                        }
                        return FileVisitResult.CONTINUE
                    }
                })
        } else {
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis())
        }
    }

    /** Indexes a single document  */
    @Throws(IOException::class)
    fun indexDoc(writer: IndexWriter, file: Path, lastModified: Long) {
        val bytes = Files.readAllBytes(file)
        fun ByteArray.reader() = BufferedReader(InputStreamReader(ByteArrayInputStream(this), StandardCharsets.UTF_8))

        val relPath = docsRoot.relativize(file).toString()

        // make a new, empty document
        val doc = Document()

        // Add the path of the file as a field named "path".  Use a
        // field that is indexed (i.e. searchable), but don't tokenize
        // the field into separate words and don't index term frequency
        // or positional information:
        doc.add(StringField("path", relPath, Field.Store.YES))

        // Add the last modified date of the file a field named "modified".
        // Use a LongPoint that is indexed (i.e. efficiently filterable with
        // PointRangeQuery).  This indexes to milli-second resolution, which
        // is often too fine.  You could instead create a number based on
        // year/month/day/hour/minutes/seconds, down the resolution you require.
        // For example the long value 2011021714 would mean
        // February 17, 2011, 2-3 PM.
        doc.add(LongPoint("modified", lastModified))

        // Add the contents of the file to a field named "contents".  Specify a Reader,
        // so that the text of the file is tokenized and indexed, but not stored.
        // Note that FileReader expects the file to be in UTF-8 encoding.
        // If that's not the case searching for special characters will fail.
        doc.add(TextField("contents", bytes.reader()))

        // Let's say that title is the first line.
        // TODO refine title extraction
        doc.add(StringField("title", bytes.reader().readLine(), Field.Store.YES))

        demoEmbeddings?.let {
            val vector = it.computeEmbedding(bytes.reader())
            doc.add(
                KnnVectorField("contents-vector", vector, VectorSimilarityFunction.DOT_PRODUCT)
            )
        }

        if (writer.config.openMode == OpenMode.CREATE) {
            // New index, so we just add the document (no old document can be there):
            println("adding $file")
            writer.addDocument(doc)
        } else {
            // Existing index (an old copy of this document may have been indexed) so
            // we use updateDocument instead to replace the old one matching the exact
            // path, if present:
            println("updating $file")
            writer.updateDocument(Term("path", relPath), doc)
        }
    }

    @Throws(IOException::class)
    override fun close() {
        IOUtils.close(vectorDict)
    }

    companion object {
        const val KNN_DICT = "knn-dict"

        /** Index all text files under a directory.  */
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val usage = """
                 java org.apache.lucene.demo.IndexFiles [-index INDEX_PATH] [-docs DOCS_PATH] [-update] [-knn_dict DICT_PATH]
                 
                 This indexes the documents in DOCS_PATH, creating a Lucene indexin INDEX_PATH that can be searched with SearchFiles
                 IF DICT_PATH contains a KnnVector dictionary, the index will also support KnnVector search
                 """.trimIndent()
            var indexPath = "index"
            var docsPath: String? = null
            var vectorDictSource: String? = null
            var create = true
            var i = 0
            while (i < args.size) {
                when (args[i]) {
                    "-index" -> indexPath = args[++ i]
                    "-docs" -> docsPath = args[++ i]
                    "-knn_dict" -> vectorDictSource = args[++ i]
                    "-update" -> create = false
                    "-create" -> create = true
                    else -> throw IllegalArgumentException("unknown parameter " + args[i])
                }
                i ++
            }
            val docDir = if (docsPath == null) {
                System.err.println("Usage: $usage")
                exitProcess(1)
            } else {
                Paths.get(docsPath)
            }
            if (! Files.isReadable(docDir)) {
                println(
                    "Document directory '"
                            + docDir.toAbsolutePath()
                            + "' does not exist or is not readable, please check the path"
                )
                exitProcess(1)
            }
            val start = Date()
            try {
                println("Indexing to directory '$indexPath'...")
                val dir: Directory = FSDirectory.open(Paths.get(indexPath))
                val analyzer: Analyzer = StandardAnalyzer()
                val iwc = IndexWriterConfig(analyzer)
                if (create) {
                    // Create a new index in the directory, removing any
                    // previously indexed documents:
                    iwc.openMode = OpenMode.CREATE
                } else {
                    // Add new documents to an existing index:
                    iwc.openMode = OpenMode.CREATE_OR_APPEND
                }

                // Optional: for better indexing performance, if you
                // are indexing many documents, increase the RAM
                // buffer.  But if you do this, increase the max heap
                // size to the JVM (eg add -Xmx512m or -Xmx1g):
                //
                // iwc.setRAMBufferSizeMB(256.0);
                var vectorDictInstance: KnnVectorDict? = null
                var vectorDictSize: Long = 0
                if (vectorDictSource != null) {
                    build(Paths.get(vectorDictSource), dir, KNN_DICT)
                    vectorDictInstance = KnnVectorDict(dir, KNN_DICT)
                    vectorDictSize = vectorDictInstance.ramBytesUsed()
                }
                try {
                    IndexWriter(dir, iwc).use { writer -> IndexFiles(vectorDictInstance, docDir).use { indexFiles -> indexFiles.indexDocs(writer, docDir) } }
                } finally {
                    IOUtils.close(vectorDictInstance)
                }
                val end = Date()
                DirectoryReader.open(dir).use { reader ->
                    println(
                        "Indexed "
                                + reader.numDocs()
                                + " documents in "
                                + (end.time - start.time)
                                + " milliseconds"
                    )
                    if (reader.numDocs() > 100 && vectorDictSize < 1000000 && System.getProperty("smoketester") == null) {
                        throw RuntimeException(
                            "Are you (ab)using the toy vector dictionary? See the package javadocs to understand why you got this exception."
                        )
                    }
                }
            } catch (e: IOException) {
                println(
                    """ caught a ${e.javaClass}
 with message: ${e.message}"""
                )
            }
        }
    }
}